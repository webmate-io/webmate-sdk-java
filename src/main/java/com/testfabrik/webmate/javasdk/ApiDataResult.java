package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Generic API Result type wrapping value type T.
 */
public class ApiDataResult<T> {

    @JsonProperty("data")
    public T data;

    // for Jackson
    private ApiDataResult() {}

    public ApiDataResult(T value) {
        this.data = value;
    }

}
