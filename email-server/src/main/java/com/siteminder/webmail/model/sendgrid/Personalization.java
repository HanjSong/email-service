package com.siteminder.webmail.model.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is copied from sendgrid package
 * Removed unnecessary fields
 * @see <a href="https://app.sendgrid.com/guide/integrate/langs/java">sendgrid api guide</a>
 */
@JsonInclude(Include.NON_DEFAULT)
public class Personalization {
    @JsonProperty("to")
    private List<Email> tos;
    @JsonProperty("cc")
    private List<Email> ccs;
    @JsonProperty("bcc")
    private List<Email> bccs;
    @JsonProperty("subject")
    private String subject;

    public Personalization() {
    }

    @JsonProperty("to")
    public List<Email> getTos() {
        return this.tos;
    }

    public void addTo(Email email) {
        Email newEmail = new Email();
        newEmail.setName(email.getName());
        newEmail.setEmail(email.getEmail());
        if (this.tos == null) {
            this.tos = new ArrayList();
            this.tos.add(newEmail);
        } else {
            this.tos.add(newEmail);
        }

    }

    @JsonProperty("cc")
    public List<Email> getCcs() {
        return this.ccs;
    }

    public void addCc(Email email) {
        Email newEmail = new Email();
        newEmail.setName(email.getName());
        newEmail.setEmail(email.getEmail());
        if (this.ccs == null) {
            this.ccs = new ArrayList();
            this.ccs.add(newEmail);
        } else {
            this.ccs.add(newEmail);
        }

    }

    @JsonProperty("bcc")
    public List<Email> getBccs() {
        return this.bccs;
    }

    public void addBcc(Email email) {
        Email newEmail = new Email();
        newEmail.setName(email.getName());
        newEmail.setEmail(email.getEmail());
        if (this.bccs == null) {
            this.bccs = new ArrayList();
            this.bccs.add(newEmail);
        } else {
            this.bccs.add(newEmail);
        }

    }

    @JsonProperty("subject")
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
