package com.testfabrik.webmate.javasdk.seleniumService;

import com.testfabrik.webmate.javasdk.BrowserType;

public class SeleniumCapability {
    private BrowserType browserName;
    private String version;
    private String platform;
    private Boolean supportsProxy;
    private String browserlanguage; // Could be null!

    public SeleniumCapability(BrowserType browser, String version, String platform, Boolean supportsProxy, String browserlanguage) {
        this.browserName = browser;
        this.version = version;
        this.platform = platform;
        this.supportsProxy = supportsProxy;
        if (browserlanguage.equals("")) {
            this.browserlanguage = null;
        } else {
            this.browserlanguage = browserlanguage;
        }
    }

    public SeleniumCapability(BrowserType browser, String version, String platform, Boolean supportsProxy) {
        this.browserName = browser;
        this.version = version;
        this.platform = platform;
        this.supportsProxy = supportsProxy;
        this.browserlanguage = null;
    }

    public BrowserType getBrowserName() {
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
