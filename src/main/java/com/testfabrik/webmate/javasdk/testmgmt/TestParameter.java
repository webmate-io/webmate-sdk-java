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

    public WMDataType getParameterType() {
        return parameterType;
    }

    public boolean isRequired() {
        return required;
    }

    public Optional<String> getOptDescription() {
        return optDescription;
    }

    public Optional<String> getOptDisplayName() {
        return optDisplayName;
    }

    public Optional<WMValue> getDefaultVal() {
        return defaultVal;
    }
}
