package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.JacksonMapper;

/**
 * Contains TestExecutionId and optionally TestRunId.
 */
class CreateTestExecutionResponse {
    public final TestExecutionId executionId;

    public final Optional<TestRunId> optTestRunId;

    CreateTestExecutionResponse(TestExecutionId executionId, Optional<TestRunId> optTestRunId) {
        this.executionId = executionId;
        this.optTestRunId = optTestRunId;
    }

    @JsonCreator()
    public static CreateTestExecutionResponse fromJson(JsonNode json) {
        TestExecutionId executionId = TestExecutionId.of(json.get("textExecutionId").asText());
        Optional<TestRunId> testRunId = Optional.absent();

        if (json.has("testRunId")) {
            testRunId = Optional.of(TestRunId.of(json.get("testRunId").asText()));
        }
        return new CreateTestExecutionResponse(executionId, testRunId);
    }
}
