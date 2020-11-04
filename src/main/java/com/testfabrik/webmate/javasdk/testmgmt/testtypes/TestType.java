package com.testfabrik.webmate.javasdk.testmgmt.testtypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of Test.
 */
public class TestType {
    private final String name;

    private TestType(final String name) {
        this.name = name;
    }

    /**
     * Create TestType from name.
     */
    @JsonCreator
    public static TestType of(final String name) {
        return new TestType(name);
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

        TestType otherType = (TestType) o;

        return name.equals(otherType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
