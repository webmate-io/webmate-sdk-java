package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;

public class JobRunSummary {

    public JobRunSummary(JobRunState state, String failureMessage, JsonNode summaryInformation) {
        this.state = state;
        this.failureMessage = failureMessage;
        this.summaryInformation = summaryInformation;
    }

    private final JobRunState state;
    private final String failureMessage;
    private final JsonNode summaryInformation;

    public JobRunState getState() {
        return state;
    }

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

    public JsonNode getSummaryInformation() {
        return summaryInformation;
    }
}
