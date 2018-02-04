package com.siteminder.webmail.model;

import org.springframework.http.HttpStatus;

/**
 * Response code derived from provider response
 * ResponseCode will be 'SUCCESS' if HttpStatus code is2xxSuccessful returns true
 */
public enum ResponseCode {
    SUCCESS,
    ERROR;

    public static ResponseCode getResponseCode(HttpStatus status) {
        if (status.is2xxSuccessful()) {
            return ResponseCode.SUCCESS;
        }
        return ResponseCode.ERROR;
    }
}
