package com.testfabrik.webmate.javasdk.testmgmt.spec;

import com.testfabrik.webmate.javasdk.Browser;

public class BrowserSpecification extends VehicleSpecification {

    private Browser browser;
    private boolean useProxy;

    public BrowserSpecification(Browser browser) {
        this.browser = browser;
        this.useProxy = false;
    }

    public BrowserSpecification(Browser browser, boolean useProxy) {
        this.browser = browser;
        this.useProxy = useProxy;
    }

    public Browser getBrowser() {
        return browser;
    }

    public boolean isUseProxy() {
        return useProxy;
    }
}
