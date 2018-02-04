package com.siteminder.webmail.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.webmail.TestBase;
import com.siteminder.webmail.client.MailGunRestClient;
import com.siteminder.webmail.client.SendGridRestClient;
import com.siteminder.webmail.model.ResponseCode;
import com.siteminder.webmail.model.SendMailResponse;
import com.siteminder.webmail.model.mailgun.MailGunResponseBody;
import com.siteminder.webmail.model.sendgrid.Mail;
import com.siteminder.webmail.model.sendgrid.Personalization;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest extends TestBase {

    private EmailService emailService;

    @Mock
    private SendGridRestClient sendGridRestClient;
    @Mock
    private MailGunRestClient mailGunRestClient;

    private ObjectMapper objectMapper;

    @Before
    public void initTest() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        emailService = new EmailService(sendGridRestClient, mailGunRestClient, objectMapper);
    }

    @Test
    public void testEmailService_sendPrimaryMethod() {
        MailGunResponseBody mailGunResponse = new MailGunResponseBody();
        mailGunResponse.setId("mailgun_id");
        mailGunResponse.setMessage("mailgun_success");

        ResponseEntity<MailGunResponseBody> result = new ResponseEntity<>(mailGunResponse, HttpStatus.OK);
        Mockito.when(mailGunRestClient.send(Mockito.any())).thenReturn(result);

        SendMailResponse response = emailService.send(getTestEmailModel());
        Assert.assertEquals("mailgun response message does not match", "mailgun_success", response.getBody());
        Assert.assertEquals("mailgun response code should be SUCCESS", ResponseCode.SUCCESS, response.getResponseCode());

        mailGunResponse.setId(null);
        mailGunResponse.setMessage("mailgun_error");
        result = new ResponseEntity<>(mailGunResponse, HttpStatus.BAD_REQUEST);
        Mockito.when(mailGunRestClient.send(Mockito.any())).thenReturn(result);

        response = emailService.send(getTestEmailModel());
        Assert.assertEquals("mailgun response message does not match", "mailgun_error", response.getBody());
        Assert.assertEquals("mailgun response code should be ERROR", ResponseCode.ERROR, response.getResponseCode());
    }

    @Test
    public void testEmailService_sendFallbackMethod() {
        // Send grid does not return andy body contents
        ResponseEntity<String> result = new ResponseEntity<>("", HttpStatus.OK);
        Mockito.when(sendGridRestClient.send(Mockito.any())).thenReturn(result);

        SendMailResponse response = emailService.fallback(getTestEmailModel());
        Assert.assertEquals("sendgrid response code should be SUCCESS", ResponseCode.SUCCESS, response.getResponseCode());

        result = new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(sendGridRestClient.send(Mockito.any())).thenReturn(result);

        response = emailService.fallback(getTestEmailModel());
        Assert.assertEquals("sendgrid response code should be ERROR", ResponseCode.ERROR, response.getResponseCode());
    }

    @Test
    public void testEmailService_convertToMailGunModel() {
        ArgumentCaptor<MultiValueMap> argCaptor = ArgumentCaptor.forClass(MultiValueMap.class);

        MailGunResponseBody mailGunResponse = new MailGunResponseBody();
        mailGunResponse.setId("mailgun_id");
        mailGunResponse.setMessage("mailgun_success");

        ResponseEntity<MailGunResponseBody> result = new ResponseEntity<>(mailGunResponse, HttpStatus.OK);
        Mockito.when(mailGunRestClient.send(Mockito.any())).thenReturn(result);
        emailService.send(getTestEmailModel());

        Mockito.verify(mailGunRestClient).send(argCaptor.capture());

        MultiValueMap multiValueMap = argCaptor.getValue();
        Assert.assertEquals("Sender Email address does not match", "fromemail@test.com", ((List)multiValueMap.get("from")).get(0));
        Assert.assertEquals("Recipient Email address list should be comma separated",
                "toTestEmail_0@test.com,toTestEmail_1@test.com,toTestEmail_2@test.com", ((List)multiValueMap.get("to")).get(0));
        Assert.assertEquals("Recipient(cc) Email address list should be comma separated",
                "ccTestEmail_0@test.com,ccTestEmail_1@test.com,ccTestEmail_2@test.com", ((List)multiValueMap.get("cc")).get(0));
        Assert.assertEquals("Recipient(bcc) Email address list should be comma separated",
                "bccTestEmail_0@test.com,bccTestEmail_1@test.com,bccTestEmail_2@test.com", ((List)multiValueMap.get("bcc")).get(0));
        Assert.assertEquals("Email subject does not match", "testSubject", ((List)multiValueMap.get("subject")).get(0));
        Assert.assertEquals("Email content does not match", "testText", ((List)multiValueMap.get("text")).get(0));
    }

    @Test
    public void testEmailService_convertToSendGridModel() {
        ArgumentCaptor<Mail> argCaptor = ArgumentCaptor.forClass(Mail.class);

        ResponseEntity<String> result = new ResponseEntity<>("", HttpStatus.OK);
        Mockito.when(sendGridRestClient.send(Mockito.any())).thenReturn(result);

        SendMailResponse response = emailService.fallback(getTestEmailModel());
        Mockito.verify(sendGridRestClient).send(argCaptor.capture());

        Mail sendGridModel = argCaptor.getValue();

        Assert.assertEquals("Email subject does not match", "testSubject", sendGridModel.getSubject());
        Assert.assertEquals("Email content does not match", "testText", sendGridModel.getContent().get(0).getValue());
        Assert.assertEquals("Sender Email address does not match", "fromemail@test.com", sendGridModel.getFrom().getEmail());

        // Current implementation contains only one record of personalization obj
        Personalization personalization = sendGridModel.getPersonalization().get(0);

        String allTos = personalization.getTos().stream().map((email) -> email.getEmail()).collect(Collectors.joining(","));
        String allCcs = personalization.getCcs().stream().map((email) -> email.getEmail()).collect(Collectors.joining(","));
        String allBccs = personalization.getBccs().stream().map((email) -> email.getEmail()).collect(Collectors.joining(","));

        Assert.assertEquals("Recipient Email address list should be comma separated",
                "toTestEmail_0@test.com,toTestEmail_1@test.com,toTestEmail_2@test.com", allTos);
        Assert.assertEquals("Recipient(cc) Email address list should be comma separated",
                "ccTestEmail_0@test.com,ccTestEmail_1@test.com,ccTestEmail_2@test.com", allCcs);
        Assert.assertEquals("Recipient(bcc) Email address list should be comma separated",
                "bccTestEmail_0@test.com,bccTestEmail_1@test.com,bccTestEmail_2@test.com", allBccs);
    }
}
