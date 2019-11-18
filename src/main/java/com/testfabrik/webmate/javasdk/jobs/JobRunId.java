package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public class JobRunId {
    private UUID value;

    @JsonCreator
    public JobRunId(UUID value) {
        this.value = value;
    }

    /**
     * Create new JobRunId from String representation.
     * @param jobRunIdStr string representation of job run id
     * @return JobRunId
     */
    public static JobRunId of(String jobRunIdStr) {
        return new JobRunId(UUID.fromString(jobRunIdStr));
    }

    static JobRunId FOR_TESTING() {
        return new JobRunId(new UUID(0, 30 /* TODO */));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobRunId projectId = (JobRunId) o;

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
