package com.testfabrik.webmate.javasdk.jobs;

import java.util.UUID;

public class JobId {
    private UUID value;

    public JobId(UUID value) {
        this.value = value;
    }

    /**
     * Create JobId from string representation.
     * @param jobIdStr string representation of job id
     * @return JobId
     */
    public static JobId of(String jobIdStr) {
        return new JobId(UUID.fromString(jobIdStr));
    }

    public static JobId FOR_TESTING() {
        return new JobId(new UUID(0, 30 /* TODO */));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobId projectId = (JobId) o;

        return value.equals(projectId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
