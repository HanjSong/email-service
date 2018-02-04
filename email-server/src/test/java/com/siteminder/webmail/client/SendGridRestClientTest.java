package com.siteminder.webmail.client;

import com.siteminder.webmail.model.sendgrid.Mail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class SendGridRestClientTest {

    private SendGridRestClient sendGridRestClient;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUpTest() throws Exception {
        sendGridRestClient = new SendGridRestClient(restTemplate, "apikeysendgrid");
    }

    @Test
    public void test_mailGunRestClientSetUpProperly() {
        ArgumentCaptor<HttpEntity> argumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        Mockito.when(restTemplate.exchange(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(String.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        sendGridRestClient.send(new Mail());

        Mockito.verify(restTemplate).exchange(Mockito.anyString(), Mockito.eq(HttpMethod.POST), argumentCaptor.capture(), Mockito.eq(String.class));
        HttpEntity entityArgument = argumentCaptor.getValue();
        HttpHeaders headers = entityArgument.getHeaders();

        Assert.assertTrue("Authorization header does not match",
                headers.get(HttpHeaders.AUTHORIZATION).contains("apikeysendgrid"));
        Assert.assertTrue("Content-Type header does not match",
                headers.get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_UTF8_VALUE));
        Assert.assertTrue("Accept header does not match",
                headers.get(HttpHeaders.ACCEPT).contains(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }
}
