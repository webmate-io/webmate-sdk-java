package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import com.testfabrik.webmate.javasdk.UserId;
import com.testfabrik.webmate.javasdk.testmgmt.TestExecutionId;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunId;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a concise summary describing the state of a JobRun.
 */
public class JobRunSummary {

    private JobRunId id;
    private JobRunState state;
    private UserId creator;
    private DateTime creationTime;
    private DateTime startTime;
    private DateTime endTime;
    private DateTime lastUpdateTime;
    private String failureMessage;
    private Map<String, WMValue> inputPorts;
    private Optional<TestExecutionId> testExecutionId;
    private Optional<TestRunId> testRunId;
    private Map<String, JsonNode> summaryInformation;

    public JobRunSummary(final JobRunId id, final JobRunState state, final UserId creator, final DateTime creationTime,
                         final DateTime startTime, final DateTime endTime, final DateTime lastUpdateTime, final String failureMessage,
                         final Map<String, WMValue> inputPorts, final Optional<TestExecutionId> testExecutionId, final Optional<TestRunId> testRunId,
                         final Map<String, JsonNode> summaryInformation) {
        this.id = id;
        this.state = state;
        this.creator = creator;
        this.creationTime = creationTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.testExecutionId = testExecutionId;
        this.testRunId = testRunId;
        this.lastUpdateTime = lastUpdateTime;
        this.failureMessage = failureMessage;
        this.inputPorts = inputPorts;
        this.summaryInformation = summaryInformation;
    }

    // for jackson
    private JobRunSummary() {}

    public JobRunId getId() {
        return id;
    }

    /**
     * Get the JobRunState of this JobRun.
     * @return the JobRunState.
     */
    public JobRunState getState() {
        return state;
    }

    public UserId getCreator() {
        return creator;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    /**
     * @return Time, when the actual processing of the JobRun has been started by the JobEngine. This may be later than the createTime (when execution was delayed)
     */
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * @return Time when the JobRun was finished.
     */
    public DateTime getEndTime() {
        return endTime;
    }

    /**
     * @return Last time when the JobRun was updated by the JobEngine, i.e. when its data has changed.
     */
    public DateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Get a readable failure message if the JobRun failed.
     * @return a String describing the error that occured during the execution of this test. If this test did not fail (yet) an empty String is returned.
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * @return Parameters the JobRun has been started with.
     */
    public Map<String, WMValue> getInputPorts() {
        return inputPorts;
    }

    /**
     * @return Id of TestRun associated with this Job.
     */
    public Optional<TestRunId> getTestRunId() {
        return testRunId;
    }

    /**
     * Get a summary of this JobRuns result data if there is any.
     * @return a JsonNode containing a short summary of this JobRuns result data (for example the number of issues detected per browser or the list of urls which could not be accessed). If this test is still running a JsonNode representing empty result data will be returned.
     */
    public Map<String, JsonNode> getSummaryInformation() {
        return summaryInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobRunSummary that = (JobRunSummary) o;
        return id.equals(that.id) &&
                state == that.state &&
                creator.equals(that.creator) &&
                creationTime.equals(that.creationTime) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime) &&
                Objects.equals(failureMessage, that.failureMessage) &&
                inputPorts.equals(that.inputPorts) &&
                Objects.equals(testRunId, that.testRunId) &&
                summaryInformation.equals(that.summaryInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, creator, creationTime, startTime, endTime, lastUpdateTime, failureMessage, inputPorts, testRunId, summaryInformation);
    }

    @Override
    public String toString() {
        return "JobRunSummary{" +
                "id=" + id +
                ", state=" + state +
                ", creator=" + creator +
                ", creationTime=" + creationTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", failureMessage='" + failureMessage + '\'' +
                ", inputPorts=" + inputPorts +
                ", testRunId=" + testRunId +
                ", summaryInformation=" + summaryInformation +
                '}';
    }
}
