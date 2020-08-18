package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of Test.
 */
public class ApplicationModelId {
    private final String name;

    private ApplicationModelId(final String name) {
        this.name = name;
    }

    /**
     * Create TestType from name.
     */
    @JsonCreator
    public static ApplicationModelId of(final String name) {
        return new ApplicationModelId(name);
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationModelId otherType = (ApplicationModelId) o;

        return name.equals(otherType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
