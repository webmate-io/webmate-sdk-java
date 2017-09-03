package com.testfabrik.webmate.javasdk.jobs;

import java.util.UUID;

public class JobRunId {
    private UUID value;

    public JobRunId(UUID value) {
        this.value = value;
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
    public String toString() {
        return value.toString();
    }
}
