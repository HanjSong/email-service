package com.siteminder.webmail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.webmail.model.EmailForm;

import java.util.ArrayList;
import java.util.List;

public abstract class TestBase {
    private ObjectMapper mapper = new ObjectMapper();

    protected String json(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
    
    protected EmailForm getTestEmailModel() {
        EmailForm emailModel = new EmailForm();
        
        emailModel.setFrom("fromemail@test.com");
        emailModel.setTo(createEmailList(3, "to"));
        emailModel.setCc(createEmailList(3, "cc"));
        emailModel.setBcc(createEmailList(3, "bcc"));
        emailModel.setSubject("testSubject");
        emailModel.setText("testText");

        return emailModel;
    }
    
    private List<String> createEmailList(int listSize, String prefix) {
        List<String> mailList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            mailList.add(prefix + "TestEmail_" + i + "@test.com");
        }
        return mailList;
    }
}
