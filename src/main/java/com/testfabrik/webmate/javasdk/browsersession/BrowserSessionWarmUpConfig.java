package com.testfabrik.webmate.javasdk.browsersession;

/**
 * A wrapper Class that ensures proper configuration of a BrowsersessionWarmUp config. The different parameters determine how the state extraction
 * behaves during the warmup process. If a parameter is set to null, the default value is taken.
 */
public class BrowserSessionWarmUpConfig {

    private int pageWarmUpScrollBy;
    private int pageWarmUpScrollDelay;
    private int pageWarmUpMaxScrolls;

    public int getPageWarmUpScrollBy() {
        return pageWarmUpScrollBy;
    }

    public void setPageWarmUpScrollBy(int pageWarmUpScrollBy) {
        this.pageWarmUpScrollBy = pageWarmUpScrollBy;
    }

    public int getPageWarmUpScrollDelay() {
        return pageWarmUpScrollDelay;
    }

    public void setPageWarmUpScrollDelay(int pageWarmUpScrollDelay) {
        this.pageWarmUpScrollDelay = pageWarmUpScrollDelay;
    }

    public int getPageWarmUpMaxScrolls() {
        return pageWarmUpMaxScrolls;
    }

    public void setPageWarmUpMaxScrolls(int pageWarmUpMaxScrolls) {
        this.pageWarmUpMaxScrolls = pageWarmUpMaxScrolls;
    }

    /**
     * Creates a new instance of a BrowserSessionWarmupConfig
     * Usually, warmup is performed before the state extraction takes place. The browser is directed to scroll down as far as the config demands, to
     * trigger dynamic content to load. The browser will scroll down until it reaches the end of the page or the limit set by pageWarmUpMaxScrolls is
     * reached.
     *
     * @param pageWarmUpScrollBy How far down the browser should scroll with each scroll command. Unit in pixels
     * @param pageWarmUpScrollDelay How much time the browser should wait between two scroll commands. Used so content can actually load.
     * @param pageWarmUpMaxScrolls How often the browser should scroll at maximum.
     */
    public BrowserSessionWarmUpConfig(int pageWarmUpScrollBy, int pageWarmUpScrollDelay, int pageWarmUpMaxScrolls) {
        this.pageWarmUpScrollBy = pageWarmUpScrollBy;
        this.pageWarmUpScrollDelay = pageWarmUpScrollDelay;
        this.pageWarmUpMaxScrolls = pageWarmUpMaxScrolls;
    }
}
