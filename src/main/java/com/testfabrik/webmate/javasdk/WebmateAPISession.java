package com.testfabrik.webmate.javasdk;

import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionClient;
import com.testfabrik.webmate.javasdk.jobs.JobEngine;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * WebmateSession
 */
public class WebmateAPISession {

    public final WebmateAuthInfo authInfo;
    public final WebmateEnvironment environment;

    public final JobEngine jobEngine;

    public final BrowserSessionClient browserSession;

    public WebmateAPISession(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
        this.authInfo = authInfo;
        this.environment = environment;

        this.jobEngine = new JobEngine(this);
        this.browserSession = new BrowserSessionClient(this);
    }

    public WebmateAPISession(WebmateAuthInfo authInfo, WebmateEnvironment environment,  HttpClientBuilder httpClientBuilder) {
        this.authInfo = authInfo;
        this.environment = environment;

        this.jobEngine = new JobEngine(this);
        this.browserSession = new BrowserSessionClient(this, httpClientBuilder);
    }
}
