package com.siteminder.webmail.client;

import com.siteminder.webmail.model.mailgun.MailGunResponseBody;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

@Component
public class MailGunRestClient {
    @Value("${mailgun.baseuri}")
    private String mailGunBaseUri;

    @Value("${mailgun.endpoint}")
    private String mailGunEndPoint;

    private final RestTemplate restTemplate;
    private final HttpHeaders requestHeaders;

    /**
     * Constructor for MailGun provider client
     * @param restTemplate
     */
    @Autowired
    public MailGunRestClient(RestTemplate restTemplate, @Value("${mailgun.apikey}") String mailGunApiKey)
            throws UnsupportedEncodingException {
        this.restTemplate = restTemplate;

        this.requestHeaders = new HttpHeaders();
        final byte[] apiKeyBytes = mailGunApiKey.getBytes(Charset.forName("iso-8859-1"));
        this.requestHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.encode(apiKeyBytes), "ASCII"));
        this.requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    /**
     * Send email request to MailGun provider
     * @param mail
     * @return
     */
    public ResponseEntity<MailGunResponseBody> send(MultiValueMap mail) {
        HttpEntity request = new HttpEntity<>(mail, this.requestHeaders);
        return this.restTemplate.exchange(
                this.mailGunBaseUri + this.mailGunEndPoint,  HttpMethod.POST, request, MailGunResponseBody.class);
    }
}
