package com.testfabrik.webmate.javasdk.testmgmt.spec;

public class BrowserSpec {

    private String browserType;
    private String version;
    private Platform platform;

    public BrowserSpec(String browserType, String version, Platform platform) {
        this.browserType = browserType;
        this.version = version;
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "[" + browserType + ", " + version + ", " + platform + "]";
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}
