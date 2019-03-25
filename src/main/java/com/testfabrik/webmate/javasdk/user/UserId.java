package com.testfabrik.webmate.javasdk.user;

import java.util.UUID;

public class UserId {
    private UUID value;

    public UserId(UUID value) {
        this.value = value;
    }

    static UserId FOR_TESTING() {
        return new UserId(new UUID(0, 30 /* TODO */));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId projectId = (UserId) o;

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
