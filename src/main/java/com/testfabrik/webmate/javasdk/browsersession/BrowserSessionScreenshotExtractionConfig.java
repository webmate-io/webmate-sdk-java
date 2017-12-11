package com.testfabrik.webmate.javasdk.browsersession;

/**
 * A wrapper for correctly configuring the screenshot extraction that is made during the stateextraction. If the parameter is null, the default
 * value is taken.
 */
public class BrowserSessionScreenshotExtractionConfig {

    private boolean fullPage;

    private boolean hideFixedElements;

    public boolean isHideFixedElements() {
        return hideFixedElements;
    }

    public void setHideFixedElements(boolean hideFixedElements) {
        this.hideFixedElements = hideFixedElements;
    }

    public boolean isFullPage() {
        return fullPage;
    }

    public void setFullPage(boolean fullPage) {
        this.fullPage = fullPage;
    }

    /**
     * Creates a new BrowserSession ScreenshotExtractionConfig instance.
     * @param fullPage Whether fullpage screenshots should be taken or not.
     * @param hideFixedElements Whether fixed Elements should be hidden when taking fullpage screenshots or not. Note: has no effect if fullpage is false.
     */
    public BrowserSessionScreenshotExtractionConfig(boolean fullPage, boolean hideFixedElements) {
        this.fullPage = fullPage;
        this.hideFixedElements = hideFixedElements;
    }
}
