package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Browser {
    private BrowserType browserType;
    private String version;
    private Platform platform;
    private JsonNode properties; // Could be null!

    private Browser() {} // Jackson

    public Browser(BrowserType browserType, String version, String platform) {
        this(browserType, version, platform, null);
    }

    public Browser(BrowserType browserType, String version, String platform, JsonNode properties) {
        this.browserType = browserType;
        this.version = version;
        this.platform = Platform.fromString(platform);
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
        return "Browser{" +
                "browserType=" + browserType +
                ", version='" + version + '\'' +
                ", platform=" + platform +
                ", properties=" + properties +
                '}';
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

    public Boolean hasProperties() {
        return properties != null;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public JsonNode getProperties() {
        return properties;
    }
}
