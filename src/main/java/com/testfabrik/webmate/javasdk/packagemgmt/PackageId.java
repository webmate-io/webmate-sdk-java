package com.testfabrik.webmate.javasdk.packagemgmt;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class PackageId {

    private UUID value;


    public PackageId(UUID value) {
        this.value = value;
    }

    public PackageId(String value) { this.value = UUID.fromString(value);}

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static PackageId FOR_TESTING() {
        return new PackageId(new UUID(0, 43));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof PackageId && Objects.equals(value, ((PackageId) obj).value);
    }
}
