package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Identifies TestRun in a Test.
 */
public class TestRunInfo {
    private TestId testId;

    private int index;

    // for jackson
    private TestRunInfo() {}

    public TestRunInfo(final TestId testId, final int index) {
        this.testId = testId;
        this.index = index;
    }

    public TestId getTestId() {
        return testId;
    }

    @JsonProperty("testRunIndex")
    @JsonAlias({"index"})
    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRunInfo that = (TestRunInfo) o;
        return index == that.index &&
                testId.equals(that.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, index);
    }

    @Override
    public String toString() {
        return "TestRunInfo{" +
                "testId=" + testId +
                ", index=" + index +
                '}';
    }
}
