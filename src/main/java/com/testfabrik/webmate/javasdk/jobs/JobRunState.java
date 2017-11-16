package com.testfabrik.webmate.javasdk.jobs;

public enum JobRunState {
    RUNNING,
    FAILED,
    SUCCEEDED;

    public static JobRunState fromString(String stateString) {
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
}