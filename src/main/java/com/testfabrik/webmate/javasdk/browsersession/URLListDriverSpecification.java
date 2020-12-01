package com.testfabrik.webmate.javasdk.browsersession;

import com.google.common.collect.ImmutableList;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An expedition driver that makes the vehicle, i.e. probably a web browser in this case, visit
 * a list of URLs.
 */
public class URLListDriverSpecification implements DriverSpecification {
    public final List<String> urls;

    public URLListDriverSpecification(final List<URI> urls) {
        this.urls = ImmutableList.copyOf(urls.stream().map(URI::toString).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "UrlListDriverSpecification{" +
                "urls=" + urls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URLListDriverSpecification that = (URLListDriverSpecification) o;
        return Objects.equals(urls, that.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urls);
    }
}
