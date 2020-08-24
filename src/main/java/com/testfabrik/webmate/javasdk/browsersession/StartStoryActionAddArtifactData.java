package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.testmgmt.ArtifactType;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunId;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StartStoryActionAddArtifactData {

    private String name;

    private ActionSpanId spanId;

    private List<TestRunId> associatedTestRuns = new ArrayList<>();

    public StartStoryActionAddArtifactData(String name, ActionSpanId spanId) {
        this.name = name;
        this.spanId = spanId;
    }

    public StartStoryActionAddArtifactData(String name, ActionSpanId spanId, List<TestRunId> associatedTestRuns) {
        this.name = name;
        this.spanId = spanId;
        this.associatedTestRuns = ImmutableList.copyOf(associatedTestRuns);
    }

    @JsonValue
    JsonNode toJson() {
        ObjectMapper om = JacksonMapper.getInstance();
        ObjectNode root = om.createObjectNode();
        root.put("artifactType", "Action.ActionStart");
        if (!associatedTestRuns.isEmpty()) {
            root.set("associatedTestRuns", om.valueToTree(associatedTestRuns));
        }

        ObjectNode data = om.createObjectNode();
        data.put("name", name);
        data.put("actionType", "story");
        data.put("spanId", spanId.toString());

        root.set("data", data);
        return root;
    }

}
