package com.siteminder.webmail.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.siteminder.webmail.model.EmailForm;
import com.siteminder.webmail.model.SendMailResponse;
import com.siteminder.webmail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${webmail.api.version}")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * send email
     * @param email
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SendMailResponse> sendEmail(@Valid @RequestBody EmailForm email) throws JsonProcessingException {
        SendMailResponse result = emailService.send(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
