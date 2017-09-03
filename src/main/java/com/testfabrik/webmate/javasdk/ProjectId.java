package com.testfabrik.webmate.javasdk;

import java.util.UUID;

public class ProjectId {
    private UUID value;

    public ProjectId(UUID value) {
        this.value = value;
    }

    static ProjectId FOR_TESTING() {
        return new ProjectId(new UUID(0, 30 /* TODO */));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectId projectId = (ProjectId) o;

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
