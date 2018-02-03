package com.siteminder.webmail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmailModel {
    /**
     * Sender Email Address
     * TODO : Sendgrid obj uses Email obj which includes 'Name'. Check with mailgun later
     */
    @NotNull
    @Pattern(regexp = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")
    private String from;

    /**
     * Recipient mail address list
     * TODO : Use List wrapper for validating all elements
     */
    @NotNull
    private List<String> to;

    /**
     * copy recipients
     */
    private List<String> cc;

    /**
     * Hidden copy recipients
     */
    private List<String> bcc;

    /**
     * Email subject
     */
    @NotNull
    private String subject;

    /**
     * Email content
     */
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
