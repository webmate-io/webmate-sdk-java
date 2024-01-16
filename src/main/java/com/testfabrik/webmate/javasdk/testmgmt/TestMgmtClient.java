package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfabrik.webmate.javasdk.commonutils.HttpHelpers;
import com.testfabrik.webmate.javasdk.jobs.WMValue;
import com.testfabrik.webmate.javasdk.testmgmt.spec.TestExecutionSpec;
import com.testfabrik.webmate.javasdk.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Facade to webmate's TestMgmt subsystem.
 */
public class TestMgmtClient {

    private WebmateAPISession session;
    private TestMgmtApiClient apiClient;

    private static final Logger LOG = LoggerFactory.getLogger(TestMgmtClient.class);

    public static class SingleTestRunCreationSpec {

        private Map<String, WMValue> parameterAssignments;

        public SingleTestRunCreationSpec(Map<String, WMValue> parameterAssignments) {
            this.parameterAssignments = parameterAssignments;
        }

        @JsonValue
        public JsonNode asJson() {
            ObjectMapper om = new ObjectMapper();
            ObjectNode paramAssignments = ((ObjectNode)om.valueToTree(parameterAssignments));
            ObjectNode root = om.createObjectNode();
            return root
                    .put("type", "SingleTestRunCreationSpec")
                    .set("assignmentSpec", paramAssignments);
        }
    }

    private static class TestMgmtApiClient extends WebmateApiClient {

        private final static UriTemplate getTestTemplatesTemplate =
                new UriTemplate("GetTestTemplates", "/projects/${projectId}/tests");

        private final static UriTemplate getTestTemplate =
                new UriTemplate("GetTestTemplate", "/testmgmt/tests/${testId}");

        private final static UriTemplate getTestResultsTemplate =
                new UriTemplate("GetTestResults", "/testmgmt/testruns/${testRunId}/results");

        private final static UriTemplate createTestSessionTemplate =
                new UriTemplate("CreateTestSession", "/projects/${projectId}/testsessions");

        private final static UriTemplate createTestExecutionTemplate =
                new UriTemplate("CreateTestExecution", "/projects/${projectId}/testexecutions");

        private final static UriTemplate startTestExecutionTemplate =
                new UriTemplate("StartTestExecution", "/testmgmt/testexecutions/${testExecutionId}");

        private final static UriTemplate getTestExecutionTemplate =
                new UriTemplate("GetTestExecution", "/testmgmt/testexecutions/${testExecutionId}");

        private final static UriTemplate finishTestRunTemplate =
                new UriTemplate("FinishTestRun", "/testmgmt/testruns/${testRunId}/finish");

        private final static UriTemplate setTestRunNameTemplate =
                new UriTemplate("SetTestRunName", "/testmgmt/testruns/${testRunId}/name");

        private final static UriTemplate getTestRunTemplate =
                new UriTemplate("GetTestRun", "/testmgmt/testruns/${testRunId}");

        private final static UriTemplate exportTemplate =
                new UriTemplate("TestMgmtExport", "/projects/${projectId}/testlab/export/${exporter}");

        public TestMgmtApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public TestMgmtApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder clientBuilder) {
            super(authInfo, environment, clientBuilder);
        }

        private CreateTestExecutionResponse handleCreateTestExecutionResponse(Optional<HttpResponse> response) {
            if (!response.isPresent()) {
                throw new WebmateApiClientException("Could not create TestExecution. Got no response");
            }
            return HttpHelpers.getObjectFromJsonEntity(response.get(), CreateTestExecutionResponse.class);
        }

        public CreateTestExecutionResponse createTestExecution(ProjectId projectId, TestExecutionSpec spec) {
            Optional<HttpResponse> optHttpResponse = sendPOST(createTestExecutionTemplate, ImmutableMap.of(
                    "projectId", projectId.toString()), spec.asJson()).getOptHttpResponse();

            return handleCreateTestExecutionResponse(optHttpResponse);
        }

        public CreateTestExecutionResponse createAndStartTestExecution(ProjectId projectId, TestExecutionSpec spec) {
            Optional<HttpResponse> optHttpResponse = sendPOST(createTestExecutionTemplate, ImmutableMap.of(
                    "projectId", projectId.toString()), "start=true", spec.asJson()).getOptHttpResponse();

            return handleCreateTestExecutionResponse(optHttpResponse);
        }

        public TestSessionId createTestSession(ProjectId projectId, String name) {
            ObjectMapper om = JacksonMapper.getInstance();
            ObjectNode root = om.createObjectNode();
            root.put("name", name);

            Optional<HttpResponse> optHttpResponse = sendPOST(createTestSessionTemplate, ImmutableMap.of(
                    "projectId", projectId.toString()), root).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not create TestSession. Got no response");
            }

            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), TestSessionId.class);
        }

        public TestRunId startTestExecution(TestExecutionId id) {
            Optional<HttpResponse> optHttpResponse = sendPOST(startTestExecutionTemplate, ImmutableMap.of(
                    "testExecutionId", id.toString())).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not start TestExecution. Got no response");
            }

            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), TestRunId.class);
        }

        public TestExecutionSummary getTestExecution(TestExecutionId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getTestExecutionTemplate, ImmutableMap.of(
                    "testExecutionId", id.toString())).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not get TestExecution. Got no response");
            }

            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), TestExecutionSummary.class);
        }

        public TestRunInfo getTestRun(TestRunId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getTestRunTemplate, ImmutableMap.of(
                    "testRunId", id.toString())).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not get TestRun. Got no response");
            }

            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), TestRunInfo.class);
        }

        public void setTestRunName(TestRunId id, String name) {
            Map<String, Object>  params = ImmutableMap.of("name", name);
            Optional<HttpResponse> optHttpResponse = sendPOST(setTestRunNameTemplate, ImmutableMap.of(
                    "testRunId", id.toString()), JsonUtils.getJsonFromData(params)).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not finish TestRun. Got no response");
            }
        }

        public void finishTestRun(TestRunId id, TestRunFinishData data) {
            JsonNode dataJson = JsonUtils.getJsonFromData(data);

            Optional<HttpResponse> optHttpResponse = sendPOST(finishTestRunTemplate, ImmutableMap.of(
                    "testRunId", id.toString()), dataJson).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not finish TestRun. Got no response");
            }

            waitForTestRunCompletion(id);
        }

        public List<TestTemplate> getTestTemplates(ProjectId projectId) {
            Optional<HttpResponse> optHttpResponse = sendGET(getTestTemplatesTemplate, ImmutableMap.of("projectId", projectId.toString())).getOptHttpResponse();
            List<TestTemplate> testTemplates;
            try {
                if (!optHttpResponse.isPresent()) {
                    testTemplates = new ArrayList<>();
                } else {
                    String testInfosJson = EntityUtils.toString(optHttpResponse.get().getEntity());
                    ObjectMapper mapper = JacksonMapper.getInstance();
                    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


                    ApiDataResult<TestTemplate[]> testResults = mapper.readValue(testInfosJson, new TypeReference<ApiDataResult<TestTemplate[]>>() {});
                    testTemplates = Arrays.asList(testResults.data);
                }
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading data: " + e.getMessage(), e);
            }
            return testTemplates;
        }

        public Optional<Test> getTest(TestTemplateId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getTestTemplate, ImmutableMap.of("testId", id.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            Test test;
            try {
                String testJson = EntityUtils.toString(optHttpResponse.get().getEntity());

                ObjectMapper mapper = JacksonMapper.getInstance();
                test = mapper.readValue(testJson, Test.class);
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading Test data: " + e.getMessage(), e);
            }
            return Optional.fromNullable(test);
        }

        /**
         * Get TestResults of Test with given id and testrun index.
         *
         * @param id Id of TestRun.
         * @return Optional with list of TestResults or Optional.absent if there was no such Test or TestRun. If there
         * were no TestResults, the result is Optional of empty list.
         */
        public Optional<List<TestResult>> getTestResults(TestRunId id) {
            if (id == null) {
                throw new WebmateApiClientException("TestRun id must not be null");
            }
            Optional<HttpResponse> optHttpResponse = sendGET(getTestResultsTemplate, ImmutableMap.of("testRunId", id.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            List<TestResult> result;
            try {
                String testJson = EntityUtils.toString(optHttpResponse.get().getEntity());

                ObjectMapper mapper = JacksonMapper.getInstance();
                ApiDataResult<TestResult[]> testResults = mapper.readValue(testJson, new TypeReference<ApiDataResult<TestResult[]>>() {});
                result = Arrays.asList(testResults.data);
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading TestResult data: " + e.getMessage(), e);
            }
            return Optional.fromNullable(result);
        }

        public void export(ProjectId projectId, String exporter, Map<String, Object> config, String targetFilePath) {
            Optional<HttpResponse> optHttpResponse = sendPOST(exportTemplate, ImmutableMap.of("projectId", projectId.toString(), "exporter", exporter), JsonUtils.getJsonFromData(config)).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Error getting export from API.");
            }
            HttpEntity entity = optHttpResponse.get().getEntity();
            if (entity != null) {
                try {
                    File targetFile = new File(targetFilePath);
                    InputStream inputStream = entity.getContent();
                    OutputStream outputStream = Files.newOutputStream(targetFile.toPath());
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                } catch (Throwable e) {
                    throw new WebmateApiClientException("Error reading getting export data: " + e.getMessage(), e);
                }
            } else {
                throw new WebmateApiClientException("Error getting export from API.");
            }
        }

        public TestRunInfo waitForTestRunCompletion(TestRunId testRunId) {
            return waitForTestRunCompletion(testRunId, MAX_LONG_WAITING_TIME_MILLIS);
        }

        public TestRunInfo waitForTestRunCompletion(TestRunId testRunId, long maxWaitingTimeMillis) {
            long startTime = System.currentTimeMillis();
            TestRunInfo testRunInfo = null;
            try {
                do {
                    Thread.sleep(WAITING_POLLING_INTERVAL_MILLIS);
                    testRunInfo = this.getTestRun(testRunId);
                } while ((testRunInfo.getExecutionStatus() == TestRunExecutionStatus.RUNNING ||
                        testRunInfo.getExecutionStatus() == TestRunExecutionStatus.CREATED ||
                        testRunInfo.getEvaluationStatus() == TestRunEvaluationStatus.PENDING_PASSED ||
                        testRunInfo.getEvaluationStatus() == TestRunEvaluationStatus.PENDING_FAILED) &&
                        System.currentTimeMillis() - startTime < maxWaitingTimeMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return testRunInfo;
        }

        public TestRunInfo waitForTestRunRunning(TestRunId testRunId) {
            return waitForTestRunRunning(testRunId, MAX_LONG_WAITING_TIME_MILLIS);
        }

        public TestRunInfo waitForTestRunRunning(TestRunId testRunId, long maxWaitingTimeInMilliSeconds) {
            long startTime = System.currentTimeMillis();
            TestRunInfo testRunInfo = null;
            try {
                do {
                    Thread.sleep(WAITING_POLLING_INTERVAL_MILLIS);
                    testRunInfo = this.getTestRun(testRunId);
                } while (testRunInfo.getExecutionStatus() == TestRunExecutionStatus.CREATED &&
                        System.currentTimeMillis() - startTime < maxWaitingTimeInMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return testRunInfo;
        }
    }

    /**
     * Creates a TestMgmtClient based on a WebmateApiSession.
     *
     * @param session The WebmateApiSession used by the TestMgmtClient
     */
    public TestMgmtClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new TestMgmtApiClient(session.authInfo, session.environment);
    }

    /**
     * Creates a TestMgmtClient based on a WebmateApiSession and a custom HttpClientBuilder.
     *
     * @param session The WebmateApiSession used by the TestMgmtClient
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection.
     */
    public TestMgmtClient(WebmateAPISession session,  HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new TestMgmtApiClient(session.authInfo, session.environment, httpClientBuilder);
    }

    public List<TestTemplate> getTestTemplates(ProjectId projectId) {
        return this.apiClient.getTestTemplates(projectId);
    }

    /**
     * Retrieve Test with id.
     *
     * @param id Id of Test.
     * @return Test
     */
    public Optional<Test> getTest(TestTemplateId id) {
        return this.apiClient.getTest(id);
    }

    /**
     * Retrieve list of TestResults for given test and test run.
     *
     * @param id Id of TestRun.
     * @return List of TestResults. Optional.absent if there was no such Test or TestRun.
     */
    public Optional<List<TestResult>> getTestResults(TestRunId id) {
        return this.apiClient.getTestResults(id);
    }

    /**
     * Retrieve information about TestRun.
     *
     * @param testRunId Id of TestRun.
     * @return TestRun information
     */
    public TestRunInfo getTestRun(TestRunId testRunId) {
        return this.apiClient.getTestRun(testRunId);
    }

    /**
     * Set the name for a given test run.
     *
     * @param testRunId Id of TestRun.
     * @param name New TestRun name.
     */
    public void setTestRunName(TestRunId testRunId, String name) {
        this.apiClient.setTestRunName(testRunId, name);
    }

    /**
     * Create and start a test execution.
     * This method is blocking:
     * it internally calls a method similar to {@link #waitForTestRunCompletion(TestRunId) waitForTestRunCompletion}
     * to wait until the associated test run is running.
     * If the test run is not running after the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param spec      The specification metadata for the test execution.
     * @param projectId The id of the project the test execution belongs to.
     * @return          The response data, including the test execution id and the id of the associated test run.
     */
    public CreateTestExecutionResponse startExecution(TestExecutionSpec spec, ProjectId projectId) {
        CreateTestExecutionResponse executionAndRun = apiClient.createAndStartTestExecution(projectId, spec);
        if (!executionAndRun.optTestRunId.isPresent()) {
            throw new WebmateApiClientException("Got no testrun id for new execution.");
        }
        apiClient.waitForTestRunRunning(executionAndRun.optTestRunId.get());
        return executionAndRun;
    }

    /**
     * Create and start a test execution.
     * This method is blocking:
     * it internally calls a method similar to {@link #waitForTestRunCompletion(TestRunId) waitForTestRunCompletion}
     * to wait until the associated test run is running.
     * If the test run is not running after the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param specBuilder A builder providing the required information for that test type, e.g. {@code Story}.
     * @return            The test run associated with the test execution.
     */
    public TestRun startExecutionWithBuilder(TestExecutionSpecBuilder specBuilder) {
        if (!session.getProjectId().isPresent()) {
            throw new WebmateApiClientException("A TestExecution must be associated with a project and none is provided or associated with the API session");
        }
        TestExecutionSpec spec = specBuilder.setApiSession(this.session).build();
        CreateTestExecutionResponse createTestExecutionResponse = startExecution(spec, session.getProjectId().get());
        return new TestRun(createTestExecutionResponse.optTestRunId.get(), this.session);
    }

    public TestExecutionSummary getTestExecutionSummary(TestExecutionId testExecutionId) {
        return apiClient.getTestExecution(testExecutionId);
    }

    /**
     * Create new TestSession in current project with given name.
     */
    public TestSession createTestSession(String name) {
        if (!session.getProjectId().isPresent()) {
            throw new WebmateApiClientException("A TestSession must be associated with a project and none is provided or associated with the API session");
        }
        return new TestSession(apiClient.createTestSession(session.getProjectId().get(), name), this.session);
    }

    /**
     * Finish a running test run.
     * This method is blocking:
     * it internally calls the {@link #waitForTestRunCompletion(TestRunId) waitForTestRunCompletion} method
     * to wait until the test run is finished.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param id     The id of the test run to finish.
     * @param status The status to finish the test run with.
     */
    public void finishTestRun(TestRunId id, TestRunEvaluationStatus status) {
       apiClient.finishTestRun(id, new TestRunFinishData(status));
    }

    /**
     * Finish a running test run.
     * This method is blocking:
     * it internally calls the {@link #waitForTestRunCompletion(TestRunId) waitForTestRunCompletion} method
     * to wait until the test run is finished.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param id     The id of the test run to finish.
     * @param status The status to finish the test run with.
     * @param msg    A short message explaining the result of the test run.
     */
    public void finishTestRun(TestRunId id, TestRunEvaluationStatus status, String msg) {
        apiClient.finishTestRun(id, new TestRunFinishData(status, msg));
    }

    /**
     * Finish a running test run.
     * This method is blocking:
     * it internally calls the {@link #waitForTestRunCompletion(TestRunId) waitForTestRunCompletion} method
     * to wait until the test run is finished.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param id     The id of the test run to finish.
     * @param status The status to finish the test run with.
     * @param msg    A short message explaining the result of the test run.
     * @param detail Detailed information, e.g. a stack trace.
     */
    public void finishTestRun(TestRunId id, TestRunEvaluationStatus status, String msg, String detail) {
        apiClient.finishTestRun(id, new TestRunFinishData(status, msg, detail));
    }

    /**
     * Generate an export for the given project using the specified exporter and config
     */
    public void export(ProjectId projectId, String exporter, Map<String, Object> config, String targetFilePath) {
        apiClient.export(projectId, exporter, config, targetFilePath);
    }

    /**
     * Block until the test run goes into a finished state (completed or failed) or timeout occurs.
     * The default timeout is 10 minutes.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the returned test run info manually.
     *
     * @param id The id of the test run to wait for.
     * @return   The test run info of the finished test run.
     */
    public TestRunInfo waitForTestRunCompletion(TestRunId id) {
        return apiClient.waitForTestRunCompletion(id);
    }

    /**
     * Block until the test run goes into a finished state (completed or failed) or timeout occurs.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the returned test run info manually.
     *
     * @param id                   The id of the test run to wait for.
     * @param maxWaitingTimeMillis How long to wait before timeout.
     * @return                     The test run info of the finished test run.
     */
    public TestRunInfo waitForTestRunCompletion(TestRunId id, long maxWaitingTimeMillis) {
        return apiClient.waitForTestRunCompletion(id, maxWaitingTimeMillis);
    }

}
