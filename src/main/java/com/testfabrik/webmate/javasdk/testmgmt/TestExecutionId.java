package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Id of a TestExecution.
 */
public class TestExecutionId {
    private final UUID value;

    /**
     * Create new TestRunId.
     * @param value uuid of test run id
     */
    @JsonCreator
    public TestExecutionId(final UUID value) {
        this.value = value;
    }

    /**
     * Create TestId from UUID represented as a String.
     */
    @JsonCreator
    static TestExecutionId of(final String idStr) {
        return new TestExecutionId(UUID.fromString(idStr));
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

        TestExecutionId testId = (TestExecutionId) o;

        return value.equals(testId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
