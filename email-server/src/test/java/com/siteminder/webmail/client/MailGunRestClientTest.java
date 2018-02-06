package com.siteminder.webmail.client;

import com.siteminder.webmail.model.mailgun.MailGunResponseBody;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MailGunRestClientTest {

    private MailGunRestClient mailGunRestClient;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUpTest() throws Exception {
        mailGunRestClient = new MailGunRestClient(restTemplate, "apikeymailgun");
    }

    @Test
    public void test_mailGunRestClientSetUpProperly() {
        ArgumentCaptor<HttpEntity> argumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        Mockito.when(restTemplate.exchange(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(MailGunResponseBody.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mailGunRestClient.send(new LinkedMultiValueMap<String, String>());

        Mockito.verify(restTemplate).exchange(Mockito.anyString(), Mockito.eq(HttpMethod.POST), argumentCaptor.capture(), Mockito.eq(MailGunResponseBody.class));
        HttpEntity entityArgument = argumentCaptor.getValue();
        HttpHeaders headers = entityArgument.getHeaders();

        Assert.assertTrue("Authorization header does not match",
                headers.get(HttpHeaders.AUTHORIZATION).contains("Basic YXBpOmFwaWtleW1haWxndW4="));
        Assert.assertTrue("Content-Type header does not match",
                headers.get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
    }

}
