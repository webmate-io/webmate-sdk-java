package com.testfabrik.webmate.javasdk.seleniumService;

public class SeleniumCapability {
    private String browserName;
    private String version;
    private String platform;
    private Boolean supportsProxy;
    private String browserlanguage; // Could be null!


    public SeleniumCapability(String browserName, String version, String platform, Boolean supportsProxy, String browserlanguage) {
        this.browserName = browserName;
        this.version = version;
        this.platform = platform;
        this.supportsProxy = supportsProxy;
        if (browserlanguage.equals("")) {
            this.browserlanguage = null;
        }
        else {
            this.browserlanguage = browserlanguage;
        }
    }

    public SeleniumCapability(String browserName, String version, String platform, Boolean supportsProxy) {
        this.browserName = browserName;
        this.version = version;
        this.platform = platform;
        this.supportsProxy = supportsProxy;
        this.browserlanguage = null;
    }

    public String getBrowserName() {
        return browserName;
    }

    public String getVersion() {
        return version;
    }

    public String getPlatform() {
        return platform;
    }

    public Boolean supportsProxy() {
        return supportsProxy;
    }

    public Boolean hasBrowserlanguage() {
        return browserlanguage != null;
    }

    public String getBrowserlanguage() {
        return browserlanguage;
    }
}
