package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class ApplicationModelId {

    private UUID value;

    public ApplicationModelId(UUID value) {
        this.value = value;
    }

    public ApplicationModelId(String value) { this.value = UUID.fromString(value);}

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof ApplicationModelId && Objects.equals(value, ((ApplicationModelId) obj).value);
    }
}
