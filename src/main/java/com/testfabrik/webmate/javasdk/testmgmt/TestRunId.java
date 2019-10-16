package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Id of a Test.
 */
public class TestRunId {
    private final UUID value;

    /**
     * Create new TestId.
     */
    public TestRunId(final UUID value) {
        this.value = value;
    }

    /**
     * Create TestId from UUID represented as a String.
     */
    @JsonCreator
    static TestRunId of(final String idStr) {
        return new TestRunId(UUID.fromString(idStr));
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

        TestRunId testId = (TestRunId) o;

        return value.equals(testId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
