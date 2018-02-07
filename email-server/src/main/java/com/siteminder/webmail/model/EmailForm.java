package com.siteminder.webmail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.siteminder.webmail.model.validator.EmailListConstraint;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmailForm {
    /**
     * Sender Email Address
     */
    @NotEmpty(message = "please input sender's email address")
    @Email(message = "please provide valid email address")
    @JsonProperty("from")
    private String from;

    /**
     * Recipient mail address list
     */
    @NotEmpty(message = "please input at lease one recipient")
    @EmailListConstraint
    @JsonProperty("to")
    private List<String> to;

    /**
     * copy recipients
     */
    @JsonProperty("cc")
    @EmailListConstraint
    private List<String> cc;

    /**
     * Hidden copy recipients
     */
    @JsonProperty("bcc")
    @EmailListConstraint
    private List<String> bcc;

    /**
     * Email subject
     */
    @NotEmpty(message = "please input subject")
    @JsonProperty("subject")
    private String subject;

    /**
     * Email content (text)
     */
    @NotEmpty(message = "please input content")
    @JsonProperty("text")
    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
