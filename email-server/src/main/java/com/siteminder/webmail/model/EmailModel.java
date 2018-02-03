package com.siteminder.webmail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmailModel {
    /**
     * Sender Email Addres
     * TODO : Both api accepts "Bob <bob@host.com>" format...
     */
    @NotNull
    @Pattern(regexp = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")
    @JsonProperty("from")
    private String from;

    /**
     * Recipient mail address list
     * TODO : Use List wrapper for validating all elements
     */
    @NotNull
    @JsonProperty("to")
    private List<String> to;

    /**
     * copy recipients
     */
    @JsonProperty("cc")
    private List<String> cc;

    /**
     * Hidden copy recipients
     */
    @JsonProperty("bcc")
    private List<String> bcc;

    /**
     * Email subject
     */
    @JsonProperty("subject")
    private String subject;

    /**
     * Email content (text)
     */
    @NotNull
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
