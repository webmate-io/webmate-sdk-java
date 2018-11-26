package com.testfabrik.webmate.javasdk.seleniumService;

import com.testfabrik.webmate.javasdk.Browser;
import com.testfabrik.webmate.javasdk.user.UserId;

public class SeleniumSession {
    private WebmateSeleniumSessionId id;
    private UserId userId;
    private Browser browser;
    private Boolean usingProxy;
    private String state;

    public SeleniumSession(WebmateSeleniumSessionId id, UserId userId, Browser browser, Boolean usingProxy, String state) {
        this.id = id;
        this.userId = userId;
        this.browser = browser;
        this.usingProxy = usingProxy;
        this.state = state;
    }

    public WebmateSeleniumSessionId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public Browser getBrowser() {
        return browser;
    }

    public Boolean usingProxy() {
        return usingProxy;
    }

    public String getState() {
        return state;
    }
}
