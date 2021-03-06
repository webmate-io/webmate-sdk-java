package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class BrowserSessionStateId {

    private UUID value;

    public BrowserSessionStateId(UUID value) {
        this.value = value;
    }

    @JsonCreator
    public BrowserSessionStateId(String value) {
        this.value = UUID.fromString(value);
    }

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static BrowserSessionStateId FOR_TESTING() {
        return new BrowserSessionStateId(new UUID(0, 42));
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
        if (obj == this) return true;
        return obj instanceof BrowserSessionStateId && Objects.equals(value, ((BrowserSessionStateId) obj).value);
    }
}
