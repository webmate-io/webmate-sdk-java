package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.databind.JsonNode;

public class FactParams {
    private JsonNode value;

    public FactParams(JsonNode value) {
        this.value = value;
    }

    public JsonNode getValue() {
        return value;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }
}
