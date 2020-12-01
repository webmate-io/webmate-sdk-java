package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Marker interface for ExpeditionSpecs.
 */
@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   include = JsonTypeInfo.As.PROPERTY,
   property = "type")
 @JsonSubTypes({
     @JsonSubTypes.Type(value = LiveExpeditionSpec.class),
     @JsonSubTypes.Type(value = OfflineExpeditionSpec.class)
 })
public interface ExpeditionSpec {
}
