package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Identifies TestRun in a Test.
 */
public class TestRunInfo {
    private TestId testId;

    private TestRunId testRunId;

    // for jackson
    private TestRunInfo() {}

    public TestRunInfo(final TestId testId, final TestRunId testRunId) {
        this.testId = testId;
        this.testRunId = testRunId;
    }

    public TestId getTestId() {
        return testId;
    }

    @JsonProperty("testRunId")
    @JsonAlias({"id"})
    public TestRunId getTestRunId() {
        return testRunId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRunInfo that = (TestRunInfo) o;
        return testRunId == that.testRunId &&
                testId.equals(that.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, testRunId);
    }

    @Override
    public String toString() {
        return "TestRunInfo{" +
                "testId=" + testId +
                ", index=" + testRunId +
                '}';
    }
}
