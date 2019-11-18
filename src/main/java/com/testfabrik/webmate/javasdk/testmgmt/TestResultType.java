package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Type of a TestResult, e.g. "LayoutComparison.AdditionalElement"
 */
public class TestResultType {
    private TestResultCategory category;
    private String typeName;

    // for jackson
    private TestResultType() {}

    public TestResultType(TestResultCategory category, String typeName) {
        this.category = category;
        this.typeName = typeName;
    }

    /**
     * Category of type
     * @return category, e.g. LayoutComparison
     */
    public TestResultCategory getCategory() {
        return category;
    }

    /**
     * Name of type
     * @return type name, e.g. AdditionalElement
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @return String representation of TestResultType.
     */
    @JsonValue
    public String asSerializedString() {
        return category.getName() + "." + typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResultType that = (TestResultType) o;
        return category.equals(that.category) &&
                typeName.equals(that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, typeName);
    }

    /**
     * Create an instance of TestResultType from its string representation, e.g. "Layout.AdditionalElement".
     * @param input string representation
     * @return new instance
     */
    @JsonCreator
    public static TestResultType fromString(String input) {
        String[] categoryAndType = input.split("\\.", 2);
        if (categoryAndType[0] == null || categoryAndType[1] == null) {
            throw new IllegalArgumentException("Invalid TestResult type: [" + input + "]");
        }
        return new TestResultType(new TestResultCategory(categoryAndType[0]), categoryAndType[1]);
    }


    @Override
    public String toString() {
        return "TestResultType{" +
                "category=" + category +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
