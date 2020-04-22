package com.testfabrik.webmate.javasdk;

import com.testfabrik.webmate.javasdk.artifacts.ArtifactClient;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionClient;
import com.testfabrik.webmate.javasdk.devices.DeviceClient;
import com.testfabrik.webmate.javasdk.jobs.JobEngine;
import com.testfabrik.webmate.javasdk.mailtest.MailTestClient;
import com.testfabrik.webmate.javasdk.testmgmt.TestMgmtClient;
import org.apache.http.impl.client.HttpClientBuilder;

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
        this.artifact = new ArtifactClient(this);
        this.mailTest = new MailTestClient(this, artifact);
    }
}
