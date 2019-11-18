package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.testfabrik.webmate.javasdk.UserId;

import java.util.UUID;

/**
 * Id of a Test.
 */
public class TestId {
    private final UUID value;

    /**
     * Create new TestId.
     * @param value uuid of TestId
     */
    public TestId(final UUID value) {
        this.value = value;
    }

    /**
     * Create TestId from UUID represented as a String.
     * @param idStr string representation of TestId
     * @return new TestId
     */
    @JsonCreator
    static TestId of(final String idStr) {
        return new TestId(UUID.fromString(idStr));
    }

    @Override
    @JsonValue
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestId testId = (TestId) o;

        return value.equals(testId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
