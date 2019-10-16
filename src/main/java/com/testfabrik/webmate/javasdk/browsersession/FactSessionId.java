package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

// TODO: to own service / package
public class FactSessionId {

    private UUID value;


    public FactSessionId(UUID value) {
        this.value = value;
    }

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static FactSessionId FOR_TESTING() {
        return new FactSessionId(new UUID(0, 42));
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
        return obj instanceof FactSessionId && Objects.equals(value, ((FactSessionId) obj).value);
    }
}
