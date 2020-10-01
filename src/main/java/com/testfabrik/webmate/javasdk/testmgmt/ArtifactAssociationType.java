package com.testfabrik.webmate.javasdk.testmgmt;

public enum ArtifactAssociationType {
    BrowserSession("BrowserSession"),
    TestRun("TestRun"),
    Artifact("Artifact"),
    TestResult("TestResult");

    public final String artifactAssociationType;

    ArtifactAssociationType(String artifactAssociationType) {
        this.artifactAssociationType = artifactAssociationType;
    }
}
