package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public class ProjectId {
    private UUID value;

    @JsonCreator
    public ProjectId(UUID value) {
        this.value = value;
    }

    public static ProjectId of(String idStr) {
        return new ProjectId(UUID.fromString(idStr));
    }

    public static ProjectId FOR_TESTING() {
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
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
