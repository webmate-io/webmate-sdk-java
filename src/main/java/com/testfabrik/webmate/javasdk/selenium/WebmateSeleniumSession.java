package com.testfabrik.webmate.javasdk.selenium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testfabrik.webmate.javasdk.Browser;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.WebmateAPISession;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.testmgmt.TestRun;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunEvaluationStatus;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunId;
import com.testfabrik.webmate.javasdk.user.UserId;

import java.util.UUID;

/**
 * Representation of a Selenium session in webmate.
 */
public class WebmateSeleniumSession {

    private interface SeleniumSessionProxy {
        SeleniumSession getSession();
    }

    private static class FromBrowserSessionProxy implements SeleniumSessionProxy {
        private BrowserSessionId sessionId;
        private WebmateAPISession session;

        public FromBrowserSessionProxy(BrowserSessionId sessionId, WebmateAPISession session) {
            this.sessionId = sessionId;
            this.session = session;
        }

        @Override
        public SeleniumSession getSession() {
            return session.selenium.getSeleniumSessionForBrowserSession(this.sessionId);
        }
    }

    private static class FromWebmateSeleniumSessionProxy implements SeleniumSessionProxy {
        private WebmateSeleniumSessionId sessionId;
        private WebmateAPISession session;

        public FromWebmateSeleniumSessionProxy(WebmateSeleniumSessionId sessionId, WebmateAPISession session) {
            this.sessionId = sessionId;
            this.session = session;
        }

        @Override
        public SeleniumSession getSession() {
            return session.selenium.getSeleniumsession(this.sessionId);
        }
    }


    private final SeleniumSessionProxy proxy;

    private final WebmateAPISession apiSession;

    private SeleniumSession cachedValue;

    /**
     * Factory that creates Selenium Session from its browserSessionId (caveat: there may also be browsersessions
     * that are not associated with a Selenium session. In these cases, null is returned)
     */
    public WebmateSeleniumSession(WebmateAPISession apiSession, BrowserSessionId browserSessionId) {
        this.proxy = new FromBrowserSessionProxy(browserSessionId, apiSession);
        this.apiSession = apiSession;
    }

    public WebmateSeleniumSession(WebmateAPISession apiSession, WebmateSeleniumSessionId seleniumSessionId) {
        this.proxy = new FromWebmateSeleniumSessionProxy(seleniumSessionId, apiSession);
        this.apiSession = apiSession;
    }

    private void fetchSessionData(boolean refresh) {
        if (this.cachedValue == null || refresh) {
            this.cachedValue = this.proxy.getSession();
        }
    }

    private void fetchSessionData() {
        this.fetchSessionData(false);
    }

    /**
     * Internal id of Selenium session in webmate.
     */
    public WebmateSeleniumSessionId getId() {
        this.fetchSessionData();
        return this.cachedValue.getId();
    }

    /**
     * UserId of user who has started the session.
     */
    public UserId getUserId() {
        this.fetchSessionData();
        return this.cachedValue.getUserId();
    }

    /**
     * Browser used in the selenium session.
     */
    public Browser getBrowser() {
        this.fetchSessionData();
        return this.cachedValue.getBrowser();
    }

    /**
     * Was a proxy being used?
     */
    public Boolean usingProxy() {
        this.fetchSessionData();
        return this.cachedValue.usingProxy();
    }

    /**
     * State of the Selenium session. May be one of: created, requestingbusinesstransaction, requestingtickt, requestingsession,
     * waitingforsession, requestinglease, running, done, timeout, failed, canceled, invalid
     */
    public String getState() {
        this.fetchSessionData();
        return this.cachedValue.getState();
    }

    /**
     * Internal representation of the requested Selenium capabilities
     */
    public ObjectNode getSeleniumCapabilities() {
        this.fetchSessionData();
        return this.cachedValue.getSeleniumCapabilities();
    }

    /**
     * Project where the session was started.
     */
    public ProjectId getProjectId() {
        this.fetchSessionData();
        return this.cachedValue.getProjectId();
    }

    /**
     * Id of BrowserSession / Expedition
     */
    public BrowserSessionId getBrowserSessionId() {
        this.fetchSessionData();
        return this.cachedValue.getBrowserSessionId();
    }

    /**
     * If this session has failed, return the error message or null otherwise.
     */
    public String getErrorMessage() {
        this.fetchSessionData();
        return this.cachedValue.getErrorMessage();
    }

    /**
     * Id of webmate TestRun associated with Selenium session.
     */
    public TestRunId getTestRunId() {
        this.fetchSessionData();
        JsonNode testRunIdJson = this.getSeleniumCapabilities().findValue("testRunId");
        if (testRunIdJson != null) {
            String testRunId = testRunIdJson.asText();
            if (testRunId != null) {
               return new TestRunId(UUID.fromString(testRunId));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get TestRun associated with Selenium session or null if none is found.
     */
    public TestRun getTestRun() {
        TestRunId id = getTestRunId();
        if (id != null) {
            return new TestRun(id, this.apiSession);
        } else {
            return null;
        }
    }

    /**
     * Finish TestRun associated with Selenium session.
     *
     * @param msg Short message explaining the result of the test run.
     * @param detail Detailed information, e.g. stack trace.
     */
    public void finishTestRun(TestRunEvaluationStatus status, String msg, String detail) {
        this.getTestRun().finish(status, msg, detail);
    }

    /**
     * Finish TestRun associated with Selenium session.
     *
     * @param msg Short message explaining the result of the test run.
     */
    public void finishTestRun(TestRunEvaluationStatus status, String msg) {
        this.getTestRun().finish(status, msg);
    }

    /**
     * Finish TestRun associated with Selenium session.
     */
    public void finishTestRun(TestRunEvaluationStatus status) {
        this.getTestRun().finish(status);
    }
}
