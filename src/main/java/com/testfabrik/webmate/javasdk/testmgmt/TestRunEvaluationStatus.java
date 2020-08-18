package com.testfabrik.webmate.javasdk.testmgmt;

public enum TestRunEvaluationStatus {
    FAILED("failed"),
    PASSED("passed"),
    SKIPPED("skipped");

    String name;

    private TestRunEvaluationStatus(String name) {
        this.name = name;
    }
}
