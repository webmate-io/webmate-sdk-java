package com.testfabrik.webmate.javasdk.selenium;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class WebmateSeleniumSessionId {

    private UUID value;

    @JsonCreator
    public WebmateSeleniumSessionId(UUID value) {
        this.value = value;
    }

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static WebmateSeleniumSessionId FOR_TESTING() {
        return new WebmateSeleniumSessionId(new UUID(0, 42));
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
        return obj instanceof WebmateSeleniumSessionId && Objects.equals(value, ((WebmateSeleniumSessionId) obj).value);
    }
}
