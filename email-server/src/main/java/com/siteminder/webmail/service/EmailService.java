package com.siteminder.webmail.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.siteminder.webmail.client.MailGunRestClient;
import com.siteminder.webmail.client.SendGridRestClient;
import com.siteminder.webmail.model.EmailForm;
import com.siteminder.webmail.model.ResponseCode;
import com.siteminder.webmail.model.SendMailResponse;
import com.siteminder.webmail.model.mailgun.MailGunResponseBody;
import com.siteminder.webmail.model.sendgrid.Content;
import com.siteminder.webmail.model.sendgrid.Email;
import com.siteminder.webmail.model.sendgrid.Mail;
import com.siteminder.webmail.model.sendgrid.Personalization;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final SendGridRestClient sendGridRestClient;
    private final MailGunRestClient mailGunRestClient;

    /**
     * Spring ObjectMapper
     */
    private final ObjectMapper jacksonObjectMapper;

    public EmailService(SendGridRestClient sendGridRestClient,
                        MailGunRestClient mailGunRestClient,
                        ObjectMapper jacksonObjectMapper) {
        this.sendGridRestClient = sendGridRestClient;
        this.mailGunRestClient = mailGunRestClient;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * Send email via MailGun provider
     * Fallback method timeout is be set to 2500 milliseconds as mailgun server response can be slow.
     * If mailgun server is not healthy and gets delayed, unfortunately email can be sent twice by fallback call.
     * @param mail
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2500")
    })
    public SendMailResponse send(EmailForm mail) {
        ResponseEntity<MailGunResponseBody> result = this.mailGunRestClient.send(convertToMailGunModel(mail));
        return new SendMailResponse(ResponseCode.getResponseCode(result.getStatusCode()), result.getBody().getMessage());
    }

    /**
     * Fallback method to send email via SendGrid provider
     * @param mail
     * @return
     */
    public SendMailResponse fallback(EmailForm mail) {
        ResponseEntity<String> result = this.sendGridRestClient.send(convertToSendGridModel(mail));
        return new SendMailResponse(ResponseCode.getResponseCode(result.getStatusCode()), result.getBody());
    }

    /**
     * convert to map for mailgun request
     * @param mail
     * @return
     */
    private MultiValueMap convertToMailGunModel(EmailForm mail) {
        Map map = jacksonObjectMapper.convertValue(mail, LinkedHashMap.class);
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        map.forEach((key, value) -> {
            if (value instanceof List) {
                multiValueMap.add(key, StringUtils.join(((List) value).toArray(), ','));
            } else {
                multiValueMap.add(key, value);
            }
        });
        return multiValueMap;
    }

    /**
     * convert to SendGrid request model
     * @param mail
     * @return
     */
    private Mail convertToSendGridModel(EmailForm mail) {
        Mail sendGridMail = new Mail();
        sendGridMail.setFrom(new Email(mail.getFrom()));
        sendGridMail.setSubject(mail.getSubject());
        sendGridMail.addContent(new Content(MediaType.TEXT_PLAIN_VALUE, mail.getText()));

        Personalization personalization = new Personalization();

        mail.getTo().forEach((to) -> personalization.addTo(new Email(to)));

        if (!CollectionUtils.isEmpty(mail.getCc())) {
            mail.getCc().forEach((cc) -> personalization.addCc(new Email(cc)));
        }

        if (!CollectionUtils.isEmpty(mail.getBcc())) {
            mail.getBcc().forEach((bcc) -> personalization.addBcc(new Email(bcc)));
        }

        sendGridMail.addPersonalization(personalization);

        return sendGridMail;
    }
}
