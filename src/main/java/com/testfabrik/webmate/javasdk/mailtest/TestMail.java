package com.testfabrik.webmate.javasdk.mailtest;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Objects;

public class TestMail {
    private String from;
    private List<String> to;
    private JsonNode emailContent;

    public TestMail(String from, List<String> to, JsonNode emailContent) {
        this.from = from;
        this.to = to;
        this.emailContent = emailContent;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
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
