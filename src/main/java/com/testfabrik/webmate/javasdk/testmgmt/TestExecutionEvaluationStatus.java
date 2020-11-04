package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TestExecutionEvaluationStatus {
    Passed("passed"),
    Failed("failed"),
    Skipped("skipped"),
    PendingPassed("pending_passed"),
    PendingFailed("pending_failed");

    private final String value;

    @JsonCreator
    TestExecutionEvaluationStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
