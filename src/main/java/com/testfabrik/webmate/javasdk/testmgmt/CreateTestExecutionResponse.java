package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.JacksonMapper;

/**
 * Contains TestExecutionId and optionally TestRunId.
 */
public class CreateTestExecutionResponse {
    public final TestExecutionId executionId;

    public final Optional<TestRunId> optTestRunId;

    public CreateTestExecutionResponse(TestExecutionId executionId, Optional<TestRunId> optTestRunId) {
        this.executionId = executionId;
        this.optTestRunId = optTestRunId;
    }

    @JsonCreator()
    public static CreateTestExecutionResponse fromJson(JsonNode json) {
        TestExecutionId executionId = TestExecutionId.of(json.get("testExecutionId").textValue());
        Optional<TestRunId> testRunId = Optional.absent();

        if (json.has("testRunId")) {
            testRunId = Optional.of(TestRunId.of(json.get("testRunId").textValue()));
        }
        return new CreateTestExecutionResponse(executionId, testRunId);
    }
}
