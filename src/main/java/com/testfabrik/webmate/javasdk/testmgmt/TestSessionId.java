package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Id of a Test.
 */
public class TestSessionId {
    private final UUID value;

    /**
     * Create new TestRunId.
     * @param value uuid of test run id
     */
    @JsonCreator
    public TestSessionId(final UUID value) {
        this.value = value;
    }

    /**
     * Create TestId from UUID represented as a String.
     */
    @JsonCreator
    static TestSessionId of(final String idStr) {
        return new TestSessionId(UUID.fromString(idStr));
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

        TestSessionId testSessionId = (TestSessionId) o;

        return value.equals(testSessionId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
