package com.siteminder.webmail.model.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is copied from sendgrid package
 * @see <a href="https://app.sendgrid.com/guide/integrate/langs/java">sendgrid api guide</a>
 */
@JsonInclude(Include.NON_DEFAULT)
public class Content {
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private String value;

    public Content() {
    }

    public Content(String type, String value) {
        this.setType(type);
        this.setValue(value);
    }

    @JsonProperty("type")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("value")
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

