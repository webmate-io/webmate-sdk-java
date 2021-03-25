package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.UserId;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.devices.DeviceId;
import com.testfabrik.webmate.javasdk.testmgmt.testtypes.TestType;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Identifies TestRun in a Test.
 */
public class TestRunInfo {

    private TestRunId testRunId;
    private String name;
    private TestExecutionId testExecutionId;
    private UserId creator;
    private ProjectId projectId;
    private Optional<JsonNode> output;
    private DateTime startTime;
    private Optional<DateTime> endTime;
    private DateTime lastUpdateTime;
    private TestRunEvaluationStatus evaluationStatus;
    private TestRunExecutionStatus executionStatus;
    private TestType testType;
    private JsonNode issueSummary;
    private List<BrowserSessionId> expeditions;
    private JsonNode failure;
    private List<ApplicationModelId> models;
    private List<DeviceId> devices;

    // for jackson
    private TestRunInfo() {}


    @JsonProperty("testRunId")
    @JsonAlias({"id"})
    public TestRunId getTestRunId() {
        return testRunId;
    }

    public String getName() {
        return name;
    }

    public TestExecutionId getTestExecutionId() {
        return testExecutionId;
    }

    public UserId getCreator() {
        return creator;
    }

    public Optional<JsonNode> getOutput() {
        return output;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public Optional<DateTime> getEndTime() {
        return endTime;
    }

    public DateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public TestRunEvaluationStatus getEvaluationStatus() {
        return evaluationStatus;
    }

    public TestRunExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public TestType getTestType() {
        return testType;
    }

    public JsonNode getIssueSummary() {
        return issueSummary;
    }

    public List<BrowserSessionId> getExpeditions() {
        return expeditions;
    }

    public Optional<JsonNode> getFailure() {
        return Optional.ofNullable(failure);
    }

    public List<ApplicationModelId> getModels() {
        return models;
    }

    public List<DeviceId> getDevices() {
        return devices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRunInfo that = (TestRunInfo) o;
        return Objects.equals(testRunId, that.testRunId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(testExecutionId, that.testExecutionId) &&
                Objects.equals(creator, that.creator) &&
                Objects.equals(output, that.output) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime) &&
                evaluationStatus == that.evaluationStatus &&
                executionStatus == that.executionStatus &&
                Objects.equals(testType, that.testType) &&
                Objects.equals(issueSummary, that.issueSummary) &&
                Objects.equals(expeditions, that.expeditions) &&
                Objects.equals(failure, that.failure) &&
                Objects.equals(models, that.models);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testRunId, name, testExecutionId, creator, output, startTime, endTime, lastUpdateTime, evaluationStatus, executionStatus, testType, issueSummary, expeditions, failure, models);
    }

    @Override
    public String toString() {
        return "TestRunInfo{" +
                "testRunId=" + testRunId +
                ", name='" + name + '\'' +
                ", testExecutionId=" + testExecutionId +
                ", creator=" + creator +
                ", output=" + output +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", evaluationStatus=" + evaluationStatus +
                ", executionStatus=" + executionStatus +
                ", testType=" + testType +
                ", issueSummary=" + issueSummary +
                ", expeditions=" + expeditions +
                ", failure=" + failure +
                ", models=" + models +
                ", projectId=" + projectId +
                '}';
    }
}
