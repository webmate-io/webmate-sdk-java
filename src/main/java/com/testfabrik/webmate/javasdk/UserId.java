package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * UserId in webmate.
 */
public class UserId {
    private UUID value;

    @JsonCreator
    public UserId(UUID value) {
        this.value = value;
    }

    static UserId FOR_TESTING() {
        return new UserId(new UUID(0, 30 /* TODO */));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId userId = (UserId) o;

        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
