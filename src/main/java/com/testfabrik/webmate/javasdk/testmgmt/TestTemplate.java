package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.testmgmt.testtypes.TestType;

import java.util.Collection;

public class TestTemplate {

    private TestTemplateInfo info;
    private TestType testType;
    private Collection<ApplicationModelId> models;
    private Collection<TestParameter> params;

    // For jackson
    public TestTemplate() { }

    public TestTemplate(TestTemplateInfo info, TestType testType, Collection<ApplicationModelId> models, Collection<TestParameter> params) {
        this.info = info;
        this.testType = testType;
        this.models = models;
        this.params = params;
    }

    public TestTemplateInfo getInfo() {
        return info;
    }

    public void setInfo(TestTemplateInfo info) {
        this.info = info;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public Collection<ApplicationModelId> getModels() {
        return models;
    }

    public void setModels(Collection<ApplicationModelId> models) {
        this.models = models;
    }

    public Collection<TestParameter> getParams() {
        return params;
    }

    public void setParams(Collection<TestParameter> params) {
        this.params = params;
    }
}
