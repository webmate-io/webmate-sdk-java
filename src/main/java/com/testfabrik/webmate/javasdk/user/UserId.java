package com.testfabrik.webmate.javasdk.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;

public class UserId {
    private final UUID value;

    @JsonCreator
    public UserId(UUID value) {
        this.value = value;
    }

    static UserId FOR_TESTING() {
        return new UserId(new UUID(0, 30 /* TODO */));
    }

    /**
     * Create UserId from UUID represented as a String.
     */
    @JsonCreator
    static UserId of(final String idStr) {
        return new UserId(UUID.fromString(idStr));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId projectId = (UserId) o;

        return value.equals(projectId.value);
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
