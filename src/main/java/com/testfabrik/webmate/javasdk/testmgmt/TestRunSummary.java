package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.UserId;
import org.joda.time.DateTime;

import java.util.Objects;


/**
 * Summary information about a Test Run.
 */
public class TestRunSummary {
    private TestId testId;
    private TestRunId testRunId;
    private UserId creator;
    private ProjectId projectId;
    private DateTime startTime;
    private DateTime endTime;
    private DateTime lastUpdateTime;
    private Integer numIssues;
    private Integer numFilteredIssues;


    public TestId getTestId() {
        return testId;
    }

    public TestRunId getTestRunId() {
        return testRunId;
    }

    /**
     * User who started the test run.
     */
    public UserId getCreator() {
        return creator;
    }

    /**
     * Project, where the test run was started in.
     */
    public ProjectId getProjectId() {
        return projectId;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public DateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public Integer getNumIssues() {
        return numIssues;
    }

    public Integer getNumFilteredIssues() {
        return numFilteredIssues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRunSummary that = (TestRunSummary) o;
        return testRunId == that.testRunId &&
                testId.equals(that.testId) &&
                creator.equals(that.creator) &&
                projectId.equals(that.projectId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime) &&
                Objects.equals(numIssues, that.numIssues) &&
                Objects.equals(numFilteredIssues, that.numFilteredIssues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, testRunId, creator, projectId, startTime, endTime, lastUpdateTime, numIssues, numFilteredIssues);
    }

    @Override
    public String toString() {
        return "TestRunSummary{" +
                "testId=" + testId +
                ", testRunId=" + testRunId +
                ", creator=" + creator +
                ", projectId=" + projectId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", numIssues=" + numIssues +
                ", numFilteredIssues=" + numFilteredIssues +
                '}';
    }
}
