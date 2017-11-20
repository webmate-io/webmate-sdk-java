package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents a concise summary describing the state of a JobRun.
 */
public class JobRunSummary {

    public JobRunSummary(JobRunState state, String failureMessage, JsonNode summaryInformation) {
        this.state = state;
        this.failureMessage = failureMessage;
        this.summaryInformation = summaryInformation;
    }

    private final JobRunState state;
    private final String failureMessage;
    private final JsonNode summaryInformation;

    /**
     * Get the JobRunState of this JobRun.
     * @return the JobRunState.
     */
    public JobRunState getState() {
        return state;
    }

    /**
     * Get a readable failure message if the JobRun failed.
     * @return a String describing the error thta occured during the execution of this test. If this test did not fail (yet) an empty String is returned.
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobRunSummary that = (JobRunSummary) o;

        if (state != that.state) return false;
        return failureMessage.equals(that.failureMessage);
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + failureMessage.hashCode();
        return result;
    }

    /**
     * Get a summary of this JobRuns result data if there is any.
     * @return a JsonNode containing a short summary of this JobRuns result data (for example the number of issues detected per browser or the list of urls which could not be accessed). If this test is still running a JsonNode representing empty result data will be returned.
     */
    public JsonNode getSummaryInformation() {
        return summaryInformation;
    }
}
