package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import com.testfabrik.webmate.javasdk.UserId;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunInfo;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Objects;

//case class JobRunSummaryDTO(id: JobRunId, state: JobRunState, creator: UserId, creationTime: DateTime, startTime: Option[DateTime],
//                            endTime: Option[DateTime], lastUpdateTime: Option[DateTime], failureMessage: Option[String],
//                            inputPorts: Map[PortName, BrickValue], optTestRunInfo: Option[TestRunInfoDTO], summaryInformation: Map[PortName, JsValue])

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
    private Map<String, BrickValue> inputPorts;
    private TestRunInfo optTestRunInfo;
    private Map<String, JsonNode> summaryInformation;

    public JobRunSummary(JobRunId id, JobRunState state, UserId creator, DateTime creationTime, DateTime startTime, DateTime endTime, DateTime lastUpdateTime, String failureMessage,
                         Map<String, BrickValue> inputPorts, TestRunInfo optTestRunInfo, Map<String, JsonNode> summaryInformation) {
        this.id = id;
        this.state = state;
        this.creator = creator;
        this.creationTime = creationTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastUpdateTime = lastUpdateTime;
        this.failureMessage = failureMessage;
        this.inputPorts = inputPorts;
        this.optTestRunInfo = optTestRunInfo;
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
    public Map<String, BrickValue> getInputPorts() {
        return inputPorts;
    }

    /**
     * @return If this Job was part of a TestRun, this is the associated TestRun information. May be null.
     */
    public TestRunInfo getOptTestRunInfo() {
        return optTestRunInfo;
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
                Objects.equals(optTestRunInfo, that.optTestRunInfo) &&
                summaryInformation.equals(that.summaryInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, creator, creationTime, startTime, endTime, lastUpdateTime, failureMessage, inputPorts, optTestRunInfo, summaryInformation);
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
                ", optTestRunInfo=" + optTestRunInfo +
                ", summaryInformation=" + summaryInformation +
                '}';
    }
}
