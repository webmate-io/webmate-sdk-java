package com.testfabrik.webmate.javasdk.browsersession;

import com.testfabrik.webmate.javasdk.Browser;

import java.net.URI;
import java.util.List;

/**
 * Helpers for creating Expedition specifications.
 */
public class ExpeditionSpecFactory {

    /**
     * Create a live expedition spec that visits a list of URLs in the given browser.
     */
    public static ExpeditionSpec makeUrlListExpeditionSpec(List<URI> urls, Browser browser) {
        return new LiveExpeditionSpec(new URLListDriverSpecification(urls), new BrowserSpecification(browser));
    }
}
