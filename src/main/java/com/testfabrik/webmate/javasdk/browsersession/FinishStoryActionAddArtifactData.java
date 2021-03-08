package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunId;

import java.util.ArrayList;
import java.util.List;

public class FinishStoryActionAddArtifactData {

    private Optional<JsonNode> result = Optional.absent();

    private Optional<String> errorMessage = Optional.absent();

    private Optional<JsonNode> errorDetails = Optional.absent();

    private ActionSpanId spanId;

    public static FinishStoryActionAddArtifactData successful(ActionSpanId spanId) {
        ObjectMapper om = JacksonMapper.getInstance();
        return new FinishStoryActionAddArtifactData(spanId, om.createObjectNode().put("success", true));
    }

    public static FinishStoryActionAddArtifactData successful(ActionSpanId spanId, String message) {
        ObjectMapper om = JacksonMapper.getInstance();
        return new FinishStoryActionAddArtifactData(spanId, om.createObjectNode()
                .put("success", true)
                .put("message", message));
    }

    public static FinishStoryActionAddArtifactData failure(ActionSpanId spanId, String errorMsg, Optional<JsonNode> detail) {
        return new FinishStoryActionAddArtifactData(spanId, errorMsg, detail);
    }

    private FinishStoryActionAddArtifactData(ActionSpanId spanId, JsonNode result) {
        this.spanId = spanId;
        this.result = Optional.fromNullable(result);
    }

    private FinishStoryActionAddArtifactData(ActionSpanId spanId, String errorMsg, Optional<JsonNode> detail) {
        this.spanId = spanId;
        this.errorMessage = Optional.fromNullable(errorMsg);
        this.errorDetails = detail;
    }

    @JsonValue
    JsonNode toJson() {
        ObjectMapper om = JacksonMapper.getInstance();
        ObjectNode root = om.createObjectNode();
        root.put("artifactType", "Action.ActionFinish");

        ObjectNode data = om.createObjectNode();
        data.put("spanId", spanId.toString());

        if (result.isPresent()) {
            data.set("result", this.result.get());
        } else {
            ObjectNode errorData = om.createObjectNode();
            errorData.put("errorMessage", this.errorMessage.get());
            if (errorDetails.isPresent()) {
                errorData.set("errorDetails", this.errorDetails.get());
            }
            data.set("error", errorData);
        }
        root.set("data", data);

        return root;
    }

}
