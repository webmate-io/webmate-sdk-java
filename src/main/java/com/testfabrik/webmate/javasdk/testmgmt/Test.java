package com.testfabrik.webmate.javasdk.testmgmt;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Test in webmate.
 */
public class Test {
    private TestInfo info;
    private List<TestRunSummary> testRuns;

    // for jackson
    private Test() {}

    private Test(TestInfo info, List<TestRunSummary> testRunSummaries) {
        this.testRuns = testRunSummaries;
        this.info = info;
    }

    /**
     * Get general information about the test, e.g. name, creation time, etc.
     * @return TestInfo with meta data about test
     */
    public TestInfo getInfo() {
        return info;
    }

    /**
     * Get information about the test runs of this Test. The list returned is sorted according to the creation time of the test run.
     * @return list of test runs associated with test
     */
    public List<TestRunSummary> getTestRuns() {
        return ImmutableList.copyOf(testRuns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return info.equals(test.info) &&
                testRuns.equals(test.testRuns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(info, testRuns);
    }

    @Override
    public String toString() {
        return "Test{" +
                "info=" + info +
                ", testRuns=" + testRuns +
                '}';
    }
}
