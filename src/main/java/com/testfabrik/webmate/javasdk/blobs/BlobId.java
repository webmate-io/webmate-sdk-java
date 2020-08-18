package com.testfabrik.webmate.javasdk.blobs;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class BlobId {

    private UUID value;


    public BlobId(UUID value) {
        this.value = value;
    }

    public BlobId(String value) { this.value = UUID.fromString(value); }

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static BlobId FOR_TESTING() {
        return new BlobId(new UUID(0, 43));
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
        return obj == this || obj instanceof BlobId && Objects.equals(value, ((BlobId) obj).value);
    }
}
