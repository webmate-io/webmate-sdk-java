package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.jobs.WMDataType;
import com.testfabrik.webmate.javasdk.jobs.WMValue;

import com.google.common.base.Optional;

public class TestParameter {

    private String name;
    private WMDataType parameterType;
    private boolean required;
    private Optional<String> optDescription;
    private Optional<String> optDisplayName;
    private Optional<WMValue> defaultVal;

    // For jackson
    public TestParameter() { }

    public TestParameter(String name, WMDataType parameterType, boolean required, Optional<String> optDescription,
                         Optional<String> optDisplayName, Optional<WMValue> defaultVal) {
        this.name = name;
        this.parameterType = parameterType;
        this.required = required;
        this.optDescription = optDescription;
        this.optDisplayName = optDisplayName;
        this.defaultVal = defaultVal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WMDataType getParameterType() {
        return parameterType;
    }

    public void setParameterType(WMDataType parameterType) {
        this.parameterType = parameterType;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Optional<String> getOptDescription() {
        return optDescription;
    }

    public void setOptDescription(Optional<String> optDescription) {
        this.optDescription = optDescription;
    }

    public Optional<String> getOptDisplayName() {
        return optDisplayName;
    }

    public void setOptDisplayName(Optional<String> optDisplayName) {
        this.optDisplayName = optDisplayName;
    }

    public Optional<WMValue> getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(Optional<WMValue> defaultVal) {
        this.defaultVal = defaultVal;
    }
}
