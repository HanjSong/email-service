package com.siteminder.webmail.controller;

import com.siteminder.webmail.TestBase;
import com.siteminder.webmail.model.EmailForm;
import com.siteminder.webmail.model.ResponseCode;
import com.siteminder.webmail.model.SendMailResponse;
import com.siteminder.webmail.service.EmailService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.any;

@RunWith(SpringRunner.class)
@WebMvcTest(EmailController.class)
public class EmailControllerTest extends TestBase {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmailService emailService;

    @Test
    public void testEmailApi_MappingUriSuccessful() throws Exception {
        SendMailResponse emailServiceResponse = new SendMailResponse(ResponseCode.SUCCESS, "success msg");
        Mockito.when(emailService.send(any())).thenReturn(emailServiceResponse);

        MvcResult result = mvc.perform(post("/api/v1/send")
                .content(json(getTestEmailModel()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        Assert.assertEquals(json(emailServiceResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void testEmailApi_ControllerAdviceExceptionHandling() throws Exception {
        Mockito.when(emailService.send(any())).thenThrow(new RuntimeException("some error happened"));

        mvc.perform(post("/api/v1/send")
                .content(json(getTestEmailModel()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    @Test
    public void testEmailApi_FieldValidationError() throws Exception {
        SendMailResponse emailServiceResponse = new SendMailResponse(ResponseCode.SUCCESS, "success msg");
        Mockito.when(emailService.send(any())).thenReturn(emailServiceResponse);

        // Invalid email address will throw 400
        EmailForm emailModel = getTestEmailModel();
        emailModel.setFrom("notValidEmailAddress.com");

        mvc.perform(post("/api/v1/send")
                .content(json(emailModel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Empty from email address will throw 400
        emailModel = getTestEmailModel();
        emailModel.setFrom("");

        mvc.perform(post("/api/v1/send")
                .content(json(emailModel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Empty email content will throw 400
        emailModel = getTestEmailModel();
        emailModel.setText("");

        mvc.perform(post("/api/v1/send")
                .content(json(emailModel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Empty recipients will throw 500 from validator
        emailModel = getTestEmailModel();
        emailModel.setTo(Collections.EMPTY_LIST);

        mvc.perform(post("/api/v1/send")
                .content(json(emailModel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}
