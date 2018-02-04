package com.siteminder.webmail.client;

import com.siteminder.webmail.model.sendgrid.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SendGridRestClient {
    @Value("${sendgrid.baseuri}")
    private String sendGridBaseUri;

    @Value("${sendgrid.endpoint}")
    private String sendGridEndPoint;

    private final RestTemplate restTemplate;
    private final HttpHeaders requestHeaders;

    /**
     * Constructor for SendGrid provider client
     * @param restTemplate
     * @param sendGridApiKey
     */
    @Autowired
    public SendGridRestClient(RestTemplate restTemplate, @Value("${sendgrid.apikey}") String sendGridApiKey) {
        this.restTemplate = restTemplate;

        this.requestHeaders = new HttpHeaders();
        this.requestHeaders.add(HttpHeaders.AUTHORIZATION, sendGridApiKey);
        this.requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        this.requestHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * Send email request to SendGrid provider
     * @param mail
     * @return
     */
    public ResponseEntity<String> send(Mail mail) {
        HttpEntity request = new HttpEntity<>(mail, this.requestHeaders);
        return  this.restTemplate.postForEntity(this.sendGridBaseUri + this.sendGridEndPoint, request, String.class);
    }
}
