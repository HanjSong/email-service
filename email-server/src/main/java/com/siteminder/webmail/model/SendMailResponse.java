package com.siteminder.webmail.model;

/**
 * Response wrapper for responses from email providers
 * body may contain any response message from the provider
 */
public class SendMailResponse {
    private ResponseCode responseCode;
    private String body;

    public SendMailResponse(ResponseCode responseCode, String body) {
        this.responseCode = responseCode;
        this.body = body;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
