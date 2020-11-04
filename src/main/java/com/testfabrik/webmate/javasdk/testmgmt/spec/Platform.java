package com.testfabrik.webmate.javasdk.testmgmt.spec;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Platform {
    private String platformType;
    private String platformVersion;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String architecture;

    public Platform(String platformType, String platformVersion) {
        this.platformType = platformType;
        this.platformVersion = platformVersion;
    }

    public Platform(String platformType, String platformVersion, String architecture) {
        this.platformType = platformType;
        this.platformVersion = platformVersion;
        this.architecture = architecture;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }
}
