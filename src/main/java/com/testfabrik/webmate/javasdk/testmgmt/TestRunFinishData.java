package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * For serialization of Finish data.
 */
class TestRunFinishData {
    private TestRunEvaluationStatus status;

    private String message;

    private String detail;

    public TestRunFinishData(TestRunEvaluationStatus status, String message, String detail) {
        this.status = status;
        this.message = message;
        this.detail = detail;
    }

    public TestRunFinishData(TestRunEvaluationStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public TestRunFinishData(TestRunEvaluationStatus status) {
        this.status = status;
    }

    @JsonValue
    JsonNode asJson() {
        ObjectNode result = JsonNodeFactory.instance.objectNode();

        result.put("status", status.name);

        if (message != null) {
           result.put("message", message);
        }
        if (detail != null) {
            result.put("detail", detail);
        }
        return result;
    }

}
