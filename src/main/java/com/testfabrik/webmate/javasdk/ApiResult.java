package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

/**
 * Generic API Result type wrapping value type T.
 */
public class ApiResult<T> {

    @JsonProperty
    public T value;

    @JsonProperty("meta")
    public JsonNode metaInfo;

    // for Jackson
    private ApiResult() {}

    public ApiResult(T value, JsonNode metaInfo) {
        this.value = value;
        this.metaInfo = metaInfo;
    }

}
