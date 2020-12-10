package com.testfabrik.webmate.javasdk;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.testfabrik.webmate.javasdk.artifacts.ArtifactClient;
import com.testfabrik.webmate.javasdk.blobs.BlobClient;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionClient;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.devices.DeviceClient;
import com.testfabrik.webmate.javasdk.jobs.JobEngine;
import com.testfabrik.webmate.javasdk.mailtest.MailTestClient;
import com.testfabrik.webmate.javasdk.packagemgmt.PackageMgmtClient;
import com.testfabrik.webmate.javasdk.selenium.SeleniumServiceClient;
import com.testfabrik.webmate.javasdk.selenium.WebmateSeleniumSession;
import com.testfabrik.webmate.javasdk.testmgmt.ApplicationModelId;
import com.testfabrik.webmate.javasdk.testmgmt.TestMgmtClient;
import com.testfabrik.webmate.javasdk.testmgmt.TestSessionId;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * WebmateSession
 */
public class WebmateAPISession {

    public final WebmateAuthInfo authInfo;
    public final WebmateEnvironment environment;

    /**
     * Facade to webmate's JobEngine subsystem.
     */
    public final JobEngine jobEngine;

    /**
     * Facade to webmate's BrowserSession subsystem.
     */
    public final BrowserSessionClient browserSession;

    /**
     * Facade to webmate's Device subsystem.
     */
    public final DeviceClient device;

    /**
     * Facade to webmate's TestMgmt subsystem.
     */
    public final TestMgmtClient testMgmt;

    /**
     * Facade to webmate's MailTest subsystem.
     */
    public final MailTestClient mailTest;

    /**
     * Facade to webmate's Artifact subsystem.
     */
    public final ArtifactClient artifact;

    /**
     * Facade to webmate's Selenium subsystem.
     */
    public final SeleniumServiceClient selenium;

    /**
     * Facade to webmate's Blob subsystem.
     */
    public final BlobClient blob;

    /**
     * Facade to webmate's Package Management (e.g. App) subsystem.
     */
    public final PackageMgmtClient packages;


    //////////////////////////////
    /// Mutable Session state
    ///////////////////////////////

    private List<Tag> associatedTags = new ArrayList<>();

    private List<ApplicationModelId> associatedModels = new ArrayList<>();

    private List<BrowserSessionId> associatedExpeditions = new ArrayList<>();

    private List<TestSessionId> associatedTestSessions = new ArrayList<>();

    private Optional<ProjectId> projectId = Optional.absent();

    public List<Tag> getAssociatedTags() {
        return ImmutableList.copyOf(associatedTags);
    }

    public List<ApplicationModelId> getAssociatedModels() {
        return ImmutableList.copyOf(associatedModels);
    }

    public List<BrowserSessionId> getAssociatedExpeditions() {
        return ImmutableList.copyOf(associatedExpeditions);
    }

    /**
     * Check if there is only one associated Expedition / BrowserSession and return it.
     */
    public BrowserSessionId getOnlyAssociatedExpedition() {
        if (associatedExpeditions.size() != 1) {
            throw new WebmateApiClientException("Expected exactly one active Expedition (e.g. BrowserSession) in WebmateSession, but there are " +
                    associatedExpeditions.size());
        }
        return associatedExpeditions.get(0);
    }

    public List<TestSessionId> getAssociatedTestSessions() {
        return ImmutableList.copyOf(associatedTestSessions);
    }

    public Optional<ProjectId> getProjectId() {
        return projectId;
    }

    /**
     * Constructor to create a new WebmateAPISession.
     * The session is used to access all functionality of webmate.
     *
     * @param authInfo an instance of WebmateAuthInfo which contains the users credentials
     * @param environment an instance of WebmateEnvironment which contains the url of webmate
     */
    public WebmateAPISession(WebmateAuthInfo authInfo, WebmateEnvironment environment, ProjectId projectId) {
        this.authInfo = authInfo;
        this.environment = environment;
        this.projectId = Optional.of(projectId);

        this.jobEngine = new JobEngine(this);
        this.browserSession = new BrowserSessionClient(this);
        this.device = new DeviceClient(this);
        this.testMgmt = new TestMgmtClient(this);
        this.artifact = new ArtifactClient(this);
        this.mailTest = new MailTestClient(this, artifact);
        this.selenium = new SeleniumServiceClient(this);
        this.packages = new PackageMgmtClient(this);
        this.blob = new BlobClient(this);
    }

    /**
     * Constructor to create a new WebmateAPISession.
     * The session is used to access all functionality of webmate.
     *
     * @param authInfo an instance of WebmateAuthInfo which contains the users credentials
     * @param environment an instance of WebmateEnvironment which contains the url of webmate
     */
    public WebmateAPISession(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
        this.authInfo = authInfo;
        this.environment = environment;

        this.jobEngine = new JobEngine(this);
        this.browserSession = new BrowserSessionClient(this);
        this.device = new DeviceClient(this);
        this.testMgmt = new TestMgmtClient(this);
        this.artifact = new ArtifactClient(this);
        this.mailTest = new MailTestClient(this, artifact);
        this.selenium = new SeleniumServiceClient(this);
        this.packages = new PackageMgmtClient(this);
        this.blob = new BlobClient(this);
    }

    /**
     * Constructor to create a new WebmateAPISession.
     * The session is used to access all functionality of webmate.
     *
     * @param authInfo an instance of WebmateAuthInfo which contains the users credentials
     * @param environment an instance of WebmateEnvironment which contains the url of webmate
     * @param httpClientBuilder client builder used to create HTTP connections
     */
    public WebmateAPISession(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
        this.authInfo = authInfo;
        this.environment = environment;

        this.jobEngine = new JobEngine(this, httpClientBuilder);
        this.browserSession = new BrowserSessionClient(this, httpClientBuilder);
        this.device = new DeviceClient(this, httpClientBuilder);
        this.testMgmt = new TestMgmtClient(this, httpClientBuilder);
        this.artifact = new ArtifactClient(this, httpClientBuilder);
        this.mailTest = new MailTestClient(this, artifact, httpClientBuilder);
        this.selenium = new SeleniumServiceClient(this, httpClientBuilder);
        this.packages = new PackageMgmtClient(this, httpClientBuilder);
        this.blob = new BlobClient(this);
    }

    /**
     * Constructor to create a new WebmateAPISession.
     * The session is used to access all functionality of webmate.
     *
     * @param authInfo an instance of WebmateAuthInfo which contains the users credentials
     * @param environment an instance of WebmateEnvironment which contains the url of webmate
     * @param httpClientBuilder client builder used to create HTTP connections
     */
    public WebmateAPISession(WebmateAuthInfo authInfo, WebmateEnvironment environment, ProjectId projectId,
                             HttpClientBuilder httpClientBuilder) {
        this.authInfo = authInfo;
        this.environment = environment;
        this.projectId = Optional.of(projectId);

        this.jobEngine = new JobEngine(this, httpClientBuilder);
        this.browserSession = new BrowserSessionClient(this, httpClientBuilder);
        this.device = new DeviceClient(this, httpClientBuilder);
        this.testMgmt = new TestMgmtClient(this, httpClientBuilder);
        this.artifact = new ArtifactClient(this, httpClientBuilder);
        this.mailTest = new MailTestClient(this, artifact, httpClientBuilder);
        this.selenium = new SeleniumServiceClient(this, httpClientBuilder);
        this.packages = new PackageMgmtClient(this, httpClientBuilder);
        this.blob = new BlobClient(this);
    }

    /**
     * Associate BrowserSession with API session.
     */
    public void addBrowserSession(BrowserSessionId id) {
        this.associatedExpeditions.add(id);
    }

    /**
     * Associate Selenium session with API session.
     */
    public WebmateSeleniumSession addSeleniumSession(String opaqueSeleniumSessionIdString) {
        // currently the browsersession id is equivalent to the selenium session id (which is scary but comes
        // quite handy)
        BrowserSessionId browserSessionId = new BrowserSessionId(UUID.fromString(opaqueSeleniumSessionIdString));
        addBrowserSession(browserSessionId);
        return new WebmateSeleniumSession(this, browserSessionId);
    }

    /**
     * Associate Tag with API session.
     */
    public void addTag(Tag tag) {
        this.associatedTags.add(tag);
    }

    /**
     * Associate Application Model with API session.
     */
    public void addModel(ApplicationModelId modelId) {
        this.associatedModels.add(modelId);
    }

    /**
     * Associate this API session with a webmate TestSession. All TestRuns started in this API session will be
     * associated with the associated TestSessions. It is possible to associate the API session with
     * multiple TestSessions.
     */
    public void addToTestSession(TestSessionId session) {
        this.associatedTestSessions.add(session);
    }

    /**
     * Start an action of type "story" with the given name.
     */
    public void startAction(String actionName) {
        this.browserSession.startAction(actionName);
    }

    /**
     * Finish the current action with successful result.
     */
    public void finishAction() {
        this.browserSession.finishAction();
    }

    /**
     * Finish the current action with successful result.
     */
    public void finishActionAsFailure(String message) {
        this.browserSession.finishActionAsFailure(message);
    }
}
