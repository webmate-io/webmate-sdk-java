package com.testfabrik.webmate.javasdk.testmgmt.spec;

public class BrowserSpecification extends VehicleSpecification {

    private BrowserSpec browser;
    private boolean useProxy;

    public BrowserSpecification(BrowserSpec browser) {
        this.browser = browser;
        this.useProxy = false;
    }

    public BrowserSpecification(BrowserSpec browser, boolean useProxy) {
        this.browser = browser;
        this.useProxy = useProxy;
    }

    public BrowserSpec getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserSpec browser) {
        this.browser = browser;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }
}
