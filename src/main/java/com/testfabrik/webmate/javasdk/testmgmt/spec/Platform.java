package com.testfabrik.webmate.javasdk.testmgmt.spec;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Platform {
    private String platformType;
    private String platformVersion;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platformArchitecture;

    public Platform(String platformType, String platformVersion) {
        this.platformType = platformType;
        this.platformVersion = platformVersion;
    }

    public Platform(String platformType, String platformVersion, String platformArchitecture) {
        this.platformType = platformType;
        this.platformVersion = platformVersion;
        this.platformArchitecture = platformArchitecture;
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getPlatformArchitecture() {
        return platformArchitecture;
    }
}
