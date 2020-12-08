package com.testfabrik.webmate.javasdk.selenium;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testfabrik.webmate.javasdk.Browser;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.testmgmt.TestRun;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunId;
import com.testfabrik.webmate.javasdk.user.UserId;

import java.util.UUID;

/**
 * Representation of a Selenium session in webmate.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeleniumSession {
    private WebmateSeleniumSessionId id;
    private UserId userId;
    private Browser browser;
    private Boolean usingProxy;
    private String state;

    private ObjectNode seleniumCapabilities;
    private ProjectId projectId;
    private BrowserSessionId browserSessionId;
    private String errorMessage;

    public SeleniumSession() {}

    public SeleniumSession(WebmateSeleniumSessionId id, UserId userId, Browser browser, Boolean usingProxy,
                           String state, ObjectNode seleniumCapabilities, ProjectId projectId,
                           BrowserSessionId browserSessionId, String errorMessage) {
        this.id = id;
        this.userId = userId;
        this.browser = browser;
        this.usingProxy = usingProxy;
        this.state = state;
        this.seleniumCapabilities = seleniumCapabilities;
        this.projectId = projectId;
        this.browserSessionId = browserSessionId;
        this.errorMessage = errorMessage;
    }

    /**
     * Internal id of Selenium session in webmate.
     */
    public WebmateSeleniumSessionId getId() {
        return id;
    }

    /**
     * UserId of user who has started the session.
     */
    public UserId getUserId() {
        return userId;
    }

    /**
     * Browser used in the selenium session.
     */
    public Browser getBrowser() {
        return browser;
    }

    /**
     * Was a proxy being used?
     */
    public Boolean usingProxy() {
        return usingProxy;
    }

    /**
     * State of the Selenium session. May be one of: created, requestingbusinesstransaction, requestingtickt, requestingsession,
     * waitingforsession, requestinglease, running, done, timeout, failed, canceled, invalid
     */
    public String getState() {
        return state;
    }

    /**
     * Internal representation of the requested Selenium capabilities
     */
    public ObjectNode getSeleniumCapabilities() {
        return seleniumCapabilities;
    }

    /**
     * Project where the session was started.
     */
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Id of BrowserSession / Expedition
     */
    public BrowserSessionId getBrowserSessionId() {
        return browserSessionId;
    }

    /**
     * If this session has failed, return the error message or null otherwise.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
