package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Input or output value of JobEngine
 */
public class BrickValue {

    @JsonProperty("type")
    public final BrickDataType dataType;

    @JsonProperty("data")
    public final JsonNode value;

    public BrickValue(BrickDataType dataType, JsonNode value) {
        this.dataType = dataType;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrickValue that = (BrickValue) o;

        if (!dataType.equals(that.dataType)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = dataType.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
