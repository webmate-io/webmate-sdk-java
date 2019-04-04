package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Id of a TestResult.
 */
public class TestResultId {
    private final UUID value;

    /**
     * Create new TestId.
     */
    @JsonCreator
    public TestResultId(final UUID value) {
        this.value = value;
    }

    /**
     * Create TestId from UUID represented as a String.
     */
    static TestResultId of(final String idStr) {
        return new TestResultId(UUID.fromString(idStr));
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

        TestResultId testId = (TestResultId) o;

        return value.equals(testId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
