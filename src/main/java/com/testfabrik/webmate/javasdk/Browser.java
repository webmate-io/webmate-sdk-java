package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.testfabrik.webmate.javasdk.testmgmt.spec.Platform;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Browser {
    private BrowserType browserType;
    private String version;
    private Platform platform;
    private String platformStr;
    private JsonNode properties; // Could be null!

    public Browser(BrowserType browserType, String version, String platform) {
        this(browserType, version, platform, null);
    }

    public Browser(BrowserType browserType, String version, String platform, JsonNode properties) {
        this.browserType = browserType;
        this.version = version;
        this.platformStr = platform;
        this.properties = properties;
    }

    public Browser(BrowserType browserType, String version, Platform platform) {
        this(browserType, version, platform, null);
    }

    public Browser(BrowserType browserType, String version, Platform platform, JsonNode properties) {
        this.browserType = browserType;
        this.version = version;
        this.platform = platform;
        this.properties = properties;
    }

    @Override
    public String toString() {
        String platformStr = platform == null ? this.platformStr : platform.toString();
        return "[" + browserType.getValue() + ", " + version + ", " + platformStr + "]";
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public String getVersion() {
        return version;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getPlatformStr() {
        return platformStr;
    }

    public Boolean hasProperties() {
        return properties != null;
    }

    public JsonNode getProperties() {
        return properties;
    }
}
