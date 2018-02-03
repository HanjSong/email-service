package com.siteminder.webmail.model.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is copied from sendgrid package
 * Removed unnecessary fields
 * @see <a href="https://app.sendgrid.com/guide/integrate/langs/java">sendgrid api guide</a>
 */
@JsonInclude(Include.NON_DEFAULT)
public class Mail {
    @JsonProperty("from")
    public Email from;
    @JsonProperty("subject")
    public String subject;
    @JsonProperty("personalizations")
    public List<Personalization> personalization;
    @JsonProperty("content")
    public List<Content> content;

    private static final ObjectMapper SORTED_MAPPER = new ObjectMapper();

    public Mail() {
    }

    public Mail(Email from, String subject, Email to, Content content) {
        this.setFrom(from);
        this.setSubject(subject);
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        this.addPersonalization(personalization);
        this.addContent(content);
    }

    @JsonProperty("from")
    public Email getFrom() {
        return this.from;
    }

    public void setFrom(Email from) {
        this.from = from;
    }

    @JsonProperty("subject")
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("personalizations")
    public List<Personalization> getPersonalization() {
        return this.personalization;
    }

    public void addPersonalization(Personalization personalization) {
        if (this.personalization == null) {
            this.personalization = new ArrayList();
            this.personalization.add(personalization);
        } else {
            this.personalization.add(personalization);
        }

    }

    @JsonProperty("content")
    public List<Content> getContent() {
        return this.content;
    }

    public void addContent(Content content) {
        Content newContent = new Content();
        newContent.setType(content.getType());
        newContent.setValue(content.getValue());
        if (this.content == null) {
            this.content = new ArrayList();
            this.content.add(newContent);
        } else {
            this.content.add(newContent);
        }

    }

    public String build() throws IOException {
        try {
            return SORTED_MAPPER.writeValueAsString(this);
        } catch (IOException var2) {
            throw var2;
        }
    }

    public String buildPretty() throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (IOException var2) {
            throw var2;
        }
    }

    static {
        SORTED_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }
}
