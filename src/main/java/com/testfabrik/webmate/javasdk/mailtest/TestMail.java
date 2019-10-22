package com.testfabrik.webmate.javasdk.mailtest;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Objects;

public class TestMail {
    private JsonNode from;
    private List<JsonNode> to;
    private JsonNode emailContent;

    public TestMail() {}

    public void setFrom(JsonNode from) {
        this.from = from;
    }

    public void setTo(List<JsonNode> to) {
        this.to = to;
    }

    public void setEmailContent(JsonNode emailContent) {
        this.emailContent = emailContent;
    }

    public TestMail(JsonNode from, List<JsonNode> to, JsonNode emailContent) {
        this.from = from;
        this.to = to;
        this.emailContent = emailContent;
    }

    public JsonNode getFrom() {
        return from;
    }

    public List<JsonNode> getTo() {
        return to;
    }

    public JsonNode getEmailContent() {
        return emailContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestMail testMail = (TestMail) o;
        return Objects.equals(from, testMail.from) &&
                Objects.equals(to, testMail.to) &&
                Objects.equals(emailContent, testMail.emailContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, emailContent);
    }

    @Override
    public String toString() {
        return "TestMail{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", emailContent=" + emailContent +
                '}';
    }
}
