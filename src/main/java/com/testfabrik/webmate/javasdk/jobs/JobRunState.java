package com.testfabrik.webmate.javasdk.jobs;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the current state of a JobRun which can either be still running, have failed or finished without error.
 * Note that a JobRun whith state SUCCEEDED does *not* mean that the test did not detect any errors on the website under test but rather that the test finished without external problems like browser-crashes or timeouts.
 */
public enum JobRunState {
    RUNNING,
    FAILED,
    SUCCEEDED;

    @JsonCreator
    public static JobRunState translateApiString(String stateString) {
        switch (stateString.toLowerCase()) {
            case "running":
            case "created":
            case "stopping":
                return RUNNING;
            case "done":
                return SUCCEEDED;
            default:
                return FAILED;
        }
    }

    @JsonValue
    public String toValue() {
        switch (this) {
            case FAILED:
                return "failed";
            case RUNNING:
                return "running";
            case SUCCEEDED:
                return "done";
        }
        return "unknown";
    }
}
