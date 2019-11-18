package com.testfabrik.webmate.javasdk.mailtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.WebmateApiClientException;
import com.testfabrik.webmate.javasdk.testmgmt.Artifact;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TestMail {
    private String from;
    private List<String> to;
    private JsonNode emailContent;

    // for jackson
    private TestMail() {}

    public TestMail(String from, List<String> to, JsonNode emailContent) {
        this.from = from;
        this.to = to;
        this.emailContent = emailContent;
    }

    /**
     * Create TestMail from Artifact instance.
     * @param artifact artifact to be interpreted as a TestMail.
     * @return TestMail
     * @throws com.testfabrik.webmate.javasdk.WebmateApiClientException if TestMail could not be instantiated
     */
    public static TestMail fromArtifact(Artifact artifact) {
        ObjectMapper mapper = JacksonMapper.getInstance();
        try {
            return mapper.readValue(artifact.getData().toString(), TestMail.class);
        } catch (IOException e) {
            throw new WebmateApiClientException("Error parsing TestMail json: " + e.getMessage(), e);
        }
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
