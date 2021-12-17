package com.testfabrik.webmate.javasdk.images;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Id of Screenshot
 */
public class ScreenshotId {
    private final UUID value;

    /**
     * Create new ScreenshotId.
     *
     * @param value uuid of id
     */
    public ScreenshotId(final UUID value) {
        this.value = value;
    }

    /**
     * Create ScreenshotId from UUID represented as a String.
     *
     * @param idStr string representation of id
     * @return artifact id
     */
    @JsonCreator
    public static ScreenshotId of(final String idStr) {
        return new ScreenshotId(UUID.fromString(idStr));
    }

    @Override
    @JsonValue
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScreenshotId artifactId = (ScreenshotId) o;

        return value.equals(artifactId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
