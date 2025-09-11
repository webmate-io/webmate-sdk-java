package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testfabrik.webmate.javasdk.JacksonMapper;

public class FactType {
    private String artifactType;
    private String findingType;

    private FactType(final String artifactTypeName, final String findingTypeName) {
        this.artifactType = artifactTypeName;
        this.findingType = findingTypeName;
    }

    public static FactType fromArtifactType(String artifactTypeName) {
        return new FactType(artifactTypeName, null);
    }

    public static FactType fromFindingType(String findingType) {
        return new FactType(null, findingType);
    }

    @JsonValue
    JsonNode toJson() {
        ObjectMapper om = JacksonMapper.getInstance();
        ObjectNode root = om.createObjectNode();
        if (this.artifactType != null) {
            root.put("artifactType", this.artifactType);
        } else if (this.findingType != null) {
            root.put("findingType", this.findingType);
        }
        return root;
    }

    @JsonCreator
    static FactType fromJson(JsonNode node) {
        if (node.has("artifactType")) {
            return FactType.fromArtifactType(node.get("artifactType").asText());
        } else if (node.has("findingType")) {
            return FactType.fromFindingType(node.get("findingType").asText());
        } else {
            throw new IllegalArgumentException("Invalid FactType JSON: " + node.toString());
        }
    }
}
