package com.siteminder.webmail.model.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is copied from sendgrid package
 * This represents user email (ex. Bob <bob@email.com>)
 * @see <a href="https://app.sendgrid.com/guide/integrate/langs/java">sendgrid api guide</a>
 */
@JsonInclude(Include.NON_DEFAULT)
public class Email {
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;

    public Email() {
    }

    public Email(String email) {
        this.setEmail(email);
    }

    public Email(String email, String name) {
        this.setEmail(email);
        this.setName(name);
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

