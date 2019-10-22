package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class BrowserSessionId {

    private UUID value;


    @JsonCreator
    public BrowserSessionId(UUID value) {
        this.value = value;
    }

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static BrowserSessionId FOR_TESTING() {
        return new BrowserSessionId(new UUID(0, 42));
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
        return obj instanceof BrowserSessionId && Objects.equals(value, ((BrowserSessionId) obj).value);
    }
}
