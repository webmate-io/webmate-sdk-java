package com.testfabrik.webmate.javasdk.browsersession;

/**
 * A wrapper for correctly configuring the screenshot extraction that is made during the stateextraction. If the parameter is null, the default
 * value is taken.
 */
public class BrowserSessionScreenshotExtractionConfig {

    private boolean fullPage;

    public boolean isFullPage() {
        return fullPage;
    }

    public void setFullPage(boolean fullPage) {
        this.fullPage = fullPage;
    }

    /**
     * Creates a new BrowserSession ScreenshotExtractionConfig instance.
     * @param fullPage Whether fullpage screenshots should be taken or not.
     */
    public BrowserSessionScreenshotExtractionConfig(boolean fullPage) {
        this.fullPage = fullPage;
    }
}
