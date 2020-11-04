package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class TestTemplateInfo {

    @JsonProperty("id")
    private TestId testId;
    private String name;
    private DateTime creationTime;
    private String description;
    private int version;
    @JsonProperty("isBaseTemplate")
    private boolean isBaseTemplate;

    // For jackson
    public TestTemplateInfo() { }

    public TestTemplateInfo(TestId testId, String name, DateTime creationTime, String description, int version,
                            boolean isBaseTemplate) {
        this.testId = testId;
        this.name = name;
        this.creationTime = creationTime;
        this.description = description;
        this.version = version;
        this.isBaseTemplate = isBaseTemplate;
    }

    public TestId getTestId() {
        return testId;
    }

    public void setTestId(TestId testId) {
        this.testId = testId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isBaseTemplate() {
        return isBaseTemplate;
    }

    public void setBaseTemplate(boolean baseTemplate) {
        isBaseTemplate = baseTemplate;
    }
}
