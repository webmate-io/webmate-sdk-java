package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.testfabrik.webmate.javasdk.Browser;
import com.testfabrik.webmate.javasdk.commonutils.Dimension;

import java.util.Objects;
import java.util.Optional;

/**
 * Specification of a Browser as an expedition vehicle. This may be used if you want to
 * create an Expedition where a Browser is controlled to perform a test.
 */
public class BrowserSpecification implements VehicleSpecification {
    public final Browser browser;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public final Optional<Dimension> viewportDimensions;

    public final boolean useProxy = false;

    /**
     * @param browser Specfication of the requested Browser, i.e. browser type, version etc.
     * @param viewportDimensions The browser's view port size.
     */
    public BrowserSpecification(final Browser browser, final Dimension viewportDimensions) {
        this.browser = browser;
        this.viewportDimensions = Optional.ofNullable(viewportDimensions);
    }

    public BrowserSpecification(final Browser browser) {
        this.browser = browser;
        this.viewportDimensions = Optional.empty();
    }

    @Override
    public String toString() {
        return "BrowserSpecification{" +
                "browser=" + browser +
                ", viewportDimensions=" + viewportDimensions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrowserSpecification that = (BrowserSpecification) o;
        return Objects.equals(browser, that.browser) &&
                Objects.equals(viewportDimensions, that.viewportDimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(browser, viewportDimensions);
    }
}
