package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.databind.JsonNode;

public class Browser {
    private String browserType;
    private String version;
    private String platform;
    private JsonNode properties; // Could be null!

    public Browser(String browserType, String version, String platform) {
        this.browserType = browserType;
        this.version = version;
        this.platform = platform;
        properties = null;
    }

    public Browser(String browserType, String version, String platform, JsonNode properties) {
        this.browserType = browserType;
        this.version = version;
        this.platform = platform;
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "[" + browserType + ", " + version + ", " + platform + "]";
    }

    public String getBrowserType(){
        return browserType;
    }

    public String getVersion(){
        return version;
    }

    public String getPlatform(){
        return platform;
    }

    public Boolean hasProperties(){
        return properties != null;
    }

    public JsonNode getProperties() {
        return properties;
    }
}
