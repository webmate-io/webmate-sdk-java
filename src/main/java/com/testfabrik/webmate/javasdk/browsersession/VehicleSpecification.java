package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Marker interface for "Expedition Vehicles", e.g. a Browser or a mobile App,
 * that are controlled in an expedition to perform tests. Vehicles are controlled by "Expedition Drivers", e.g.
 * URL lists, a Selenium script, or a crawler.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BrowserSpecification.class),
})
public interface VehicleSpecification {
}
