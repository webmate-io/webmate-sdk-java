package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ArtifactAssociationType {
    BrowserSession("BrowserSession"),
    TestRun("TestRun"),
    Artifact("Artifact"),
    TestResult("TestResult"),
    TestMailAccount("TestMailAccount"),
    @JsonEnumDefaultValue Unknown("Unknown");

    public final String artifactAssociationType;

    ArtifactAssociationType(String artifactAssociationType) {
        this.artifactAssociationType = artifactAssociationType;
    }
}
