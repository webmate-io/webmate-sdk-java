package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Type of an Artficact, e.g. "Page.FullpageScreenshot"
 */
public class ArtifactType {
    private ArtifactCategory category;
    private String typeName;

    // for jackson
    private ArtifactType() {}

    public ArtifactType(ArtifactCategory category, String typeName) {
        this.category = category;
        this.typeName = typeName;
    }

    /**
     * Category of type
     * @return Category of Artifact, e.g. Page.
     */
    public ArtifactCategory getCategory() {
        return category;
    }

    /**
     * Name of type
     * @return Type of artifact, e.g. FullpageScreenshot
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @return String representation of ArtifactType.
     */
    @JsonValue
    public String asSerializedString() {
        return category.getName() + "." + typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactType that = (ArtifactType) o;
        return category.equals(that.category) &&
                typeName.equals(that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, typeName);
    }

    /**
     * Create an instance of ArtifactType from its string representation, e.g. "Page.FullpageScreenshot".
     * @param input string representation
     * @return new instance
     */
    @JsonCreator
    public static ArtifactType fromString(String input) {
        String[] categoryAndType = input.split("\\.", 2);
        if (categoryAndType[0] == null || categoryAndType[1] == null) {
            throw new IllegalArgumentException("Invalid Artifact type: [" + input + "]");
        }
        return new ArtifactType(new ArtifactCategory(categoryAndType[0]), categoryAndType[1]);
    }


    @Override
    public String toString() {
        return "ArtifactType{" +
                "category=" + category +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
