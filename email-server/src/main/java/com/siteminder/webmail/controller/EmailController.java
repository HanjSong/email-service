package com.siteminder.webmail.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendEmail() {
        return "SOME RESPONSE TEST .....";
    }
}
