package com.siteminder.webmail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.webmail.model.EmailModel;
import com.siteminder.webmail.model.sendgrid.Content;
import com.siteminder.webmail.model.sendgrid.Email;
import com.siteminder.webmail.model.sendgrid.Mail;
import com.siteminder.webmail.model.sendgrid.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    @Value("${sendgrid.baseuri}")
    private String sendGridBaseUri;

    @Value("${sendgrid.endpoint}")
    private String sendGridEndPoint;

    /**
     * Spring used objectmapper
     */
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public EmailService(ObjectMapper jacksonObjectMapper) {
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    private HttpHeaders requestHeaders;

    public ResponseEntity<String> send(EmailModel mail) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        // TODO : do not expose sendgrid apikey
        this.initializeSendGrid("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");

        String jsonString = this.jacksonObjectMapper.writeValueAsString(convertToProviderFormat(mail));

        ResponseEntity<String> resultEntity =
                restTemplate.postForEntity(sendGridBaseUri + sendGridEndPoint, new HttpEntity<>(jsonString, requestHeaders), String.class);
        return resultEntity;
    }

    private void initializeSendGrid(String apiKey) {
        this.requestHeaders = new HttpHeaders();
        this.requestHeaders.add("Authorization", "Bearer " + apiKey);
        this.requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        this.requestHeaders.add("User-agent", "sendgrid/3.0.0;java");
        this.requestHeaders.add("Accept", "application/json;charset=UTF-8");
    }

    private Mail convertToProviderFormat(EmailModel mail) {
        Mail sendGridMail = new Mail();
        sendGridMail.setFrom(new Email(mail.getFrom()));
        sendGridMail.addContent(new Content("text/plain", mail.getContent()));

        Personalization personalization = new Personalization();

        mail.getTo().forEach((to) -> personalization.addTo(new Email(to)));
        personalization.setSubject(mail.getSubject());

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
