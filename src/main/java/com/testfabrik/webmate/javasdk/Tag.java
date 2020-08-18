package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a Tag in webmate. A Tag has a string-valued key and an arbitrary value, which is represented as a Json node.
 */
public class Tag {
    private String name;
    private JsonNode value;

    public Tag(String name, JsonNode value) {
        this.name = name;
        this.value = value;
    }

    public Tag(String name, String value) {
        this.name = name;
        this.value = JsonNodeFactory.instance.textNode(value);
    }

    public Tag(String name) {
        this.name = name;
        this.value = JsonNodeFactory.instance.booleanNode(true);
    }

    // for jackson
    private Tag() {}

    public String getName() {
        return name;
    }

    public JsonNode getValue() {
        return value;
    }

    @JsonValue
    public JsonNode asJson() {
        System.out.println("HERE in asJson: " + getName() + ":" + getValue());
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        result.set(getName(), getValue());
        System.out.println("HERE in asJson2: " + result);
        return result;
    }

    @JsonCreator
    public static Tag fromJson(JsonNode node) {
        Iterator<Map.Entry<String, JsonNode>> fields = ((ObjectNode) node).fields();
        Map.Entry<String, JsonNode> theField = fields.next();
        return new Tag(theField.getKey(), theField.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name) &&
                value.equals(tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
