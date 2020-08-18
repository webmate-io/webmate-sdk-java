package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.Tag;

import java.util.ArrayList;
import java.util.List;

public abstract class TestExecutionSpec {

    protected String executionName;

    protected TestType testType;

    protected String defaultTestTemplateName;

    protected List<Tag> tags;

    protected List<ApplicationModelId> models;

    protected List<TestSessionId> associatedTestSessions;

    public TestExecutionSpec(String executionName, TestType testType, String defaultTestTemplateName, List<Tag> tags,
                             List<ApplicationModelId> models, List<TestSessionId> associatedTestSessions) {
        this.executionName = executionName;
        this.testType = testType;
        this.defaultTestTemplateName = defaultTestTemplateName;
        this.tags = new ArrayList<>(tags);
        this.models = new ArrayList<>(models);
        this.associatedTestSessions = new ArrayList<>(associatedTestSessions);
    }

    public TestType getTestType() {
        return this.testType;
    }

    public abstract TestMgmtClient.SingleTestRunCreationSpec makeTestRunCreationSpec();

    // name of default test template in project
    public String getDefaultTestTemplateName() {
        return this.defaultTestTemplateName;
    }

    @JsonValue
    JsonNode asJson() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("executionName", executionName);

        ObjectNode testTemplateIdOrName = mapper.createObjectNode();
        testTemplateIdOrName.put("name", defaultTestTemplateName);
        rootNode.set("testTemplateIdOrName", testTemplateIdOrName);

        rootNode.set("tags", mapper.valueToTree(tags));
        rootNode.set("models", mapper.valueToTree(models));
        rootNode.set("associatedSessions", mapper.valueToTree(associatedTestSessions));

        rootNode.set("testRunCreationSpec", makeTestRunCreationSpec().asJson());
        return rootNode;
    }
}
