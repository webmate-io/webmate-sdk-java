package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Id of Artifact
 */
public class ArtifactId {
    private final UUID value;

    /**
     * Create new ArtifactId.
     * @param value uuid of id
     */
    public ArtifactId(final UUID value) {
        this.value = value;
    }

    /**
     * Create ArtifactId from UUID represented as a String.
     * @param idStr string representation of id
     * @return artifact id
     */
    @JsonCreator
    static ArtifactId of(final String idStr) {
        return new ArtifactId(UUID.fromString(idStr));
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

        ArtifactId artifactId = (ArtifactId) o;

        return value.equals(artifactId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
