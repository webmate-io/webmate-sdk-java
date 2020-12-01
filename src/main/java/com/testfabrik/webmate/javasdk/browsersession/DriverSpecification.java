package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Specification of an Expedition "Driver", i.e. logic that controls a "Vehicle", e.g. a Browser or an App,
 * in an expedition.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = URLListDriverSpecification.class),
})
public interface DriverSpecification {
}
