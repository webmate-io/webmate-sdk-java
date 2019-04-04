package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.databind.JsonNode;
import com.testfabrik.webmate.javasdk.Tag;

import java.util.List;
import java.util.Objects;

/**
 * Result of a Test, e.g. a defect that has been found.
 */
public class TestResult {

    private TestResultId id;
    private TestRunInfo testRunInfo;
    private TestResultType issueType;
    private JsonNode properties;
    private List<ArtifactId> associatedArtifacts;
    private List<Tag> tags;

    // for jackson
    private TestResult() {}

    public TestResult(TestResultId id, TestRunInfo testRunInfo, TestResultType issueType, JsonNode properties, List<ArtifactId> associatedArtifacts, List<Tag> tags) {
        this.id = id;
        this.testRunInfo = testRunInfo;
        this.issueType = issueType;
        this.properties = properties;
        this.associatedArtifacts = associatedArtifacts;
        this.tags = tags;
    }

    /**
     * @return Id of TestResult
     */
    public TestResultId getId() {
        return id;
    }

    /**
     * @return Id of Test and test run index that this TestResult is associated with.
     */
    public TestRunInfo getTestRunInfo() {
        return testRunInfo;
    }

    /**
     * @return Type of Issue.
     */
    public TestResultType getIssueType() {
        return issueType;
    }

    /**
     * @return The actual TestResult content. How a TestResult of a specific type is structured is documented in TODO
     */
    public JsonNode getProperties() {
        return properties;
    }

    /**
     * @return List of Ids of artifacts that are associated with with TestResult. For instance, a Layout.AdditionalElement issue will list the Artifact of the
     * reference Layout data and the comparison Layout data.
     */
    public List<ArtifactId> getAssociatedArtifacts() {
        return associatedArtifacts;
    }

    /**
     * @return Tags that are associated with this TestResult. For instance, this will include the Tag "filtered", which indicates that the TestResult should not be shown to the user or appear in statistics.
     */
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return id.equals(that.id) &&
                testRunInfo.equals(that.testRunInfo) &&
                issueType.equals(that.issueType) &&
                properties.equals(that.properties) &&
                associatedArtifacts.equals(that.associatedArtifacts) &&
                tags.equals(that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testRunInfo, issueType, properties, associatedArtifacts, tags);
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "id=" + id +
                ", testRunInfo=" + testRunInfo +
                ", issueType=" + issueType +
                ", properties=" + properties +
                ", associatedArtifacts=" + associatedArtifacts +
                ", tags=" + tags +
                '}';
    }
}
