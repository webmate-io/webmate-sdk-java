package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Facade of TestMgmt subsystem.
 */
public class TestMgmtClient {

    private WebmateAPISession session;
    private TestMgmtApiClient apiClient;

    private static final Logger LOG = LoggerFactory.getLogger(TestMgmtClient.class);

    private static class TestMgmtApiClient extends WebmateApiClient {

        private final static UriTemplate getTestsInProjectTemplate =
                new UriTemplate("/projects/${projectId}/testmgmt/tests");

        private final static UriTemplate getTestTemplate =
                new UriTemplate("/testmgmt/tests/${testId}");

        private final static UriTemplate getTestResultsTemplate =
                new UriTemplate("/testmgmt/testruns/${testRunId}/results");

        public TestMgmtApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public TestMgmtApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder clientBuilder) {
            super(authInfo, environment, clientBuilder);
        }

        public Optional<List<TestInfo>> getTestsInProject(ProjectId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getTestsInProjectTemplate, ImmutableMap.of("projectId", id.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            TestInfo[] testInfos;
            try {
                String testInfosJson = EntityUtils.toString(optHttpResponse.get().getEntity());
                ObjectMapper mapper = JacksonMapper.getInstance();
                testInfos = mapper.readValue(testInfosJson, TestInfo[].class);
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading data: " + e.getMessage(), e);
            }
            return Optional.of(Arrays.asList(testInfos));
        }

        public Optional<Test> getTest(TestId id) {
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
         * @param id Id of TestRun.
         * @return Optional with list of TestResults or Optional.absent if there was no such Test or TestRun. If there were no TestResults, the result is Optional of empty list.
         */
        public Optional<List<TestResult>> getTestResults(TestRunId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getTestResultsTemplate, ImmutableMap.of("testRunId", id.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            List<TestResult> result;
            try {
                String testJson = EntityUtils.toString(optHttpResponse.get().getEntity());

                ObjectMapper mapper = JacksonMapper.getInstance();
                ApiResult<TestResult[]> testResults = mapper.readValue(testJson, new TypeReference<ApiResult<TestResult[]>>() {});
                result = Arrays.asList(testResults.value);
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading TestResult data: " + e.getMessage(), e);
            }
            return Optional.fromNullable(result);
        }
    }

    /**
     * Creates a TestMgmtClient based on a WebmateApiSession
     * @param session The WebmateApiSession used by the TestMgmtClient
     */
    public TestMgmtClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new TestMgmtApiClient(session.authInfo, session.environment);
    }

    /**
     * Creates a TestMgmtClient based on a WebmateApiSession and a custom HttpClientBuilder.
     * @param session The WebmateApiSession used by the TestMgmtClient
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection.
     */
    public TestMgmtClient(WebmateAPISession session,  HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new TestMgmtApiClient(session.authInfo, session.environment, httpClientBuilder);
    }

    /**
     * Retrieve Tests in project with id
     * @param id Id of Project.
     * @return Test
     */
    public Optional<List<TestInfo>> getTestsInProject(ProjectId id) {
        return this.apiClient.getTestsInProject(id);
    }

    /**
     * Retrieve Test with id
     * @param id Id of Test.
     * @return Test
     */
    public Optional<Test> getTest(TestId id) {
        return this.apiClient.getTest(id);
    }

    /**
     * Retrieve list of TestResults for given test and test run.
     * @param id Id of TestRun.
     * @return List of TestResults. Optional.absent if there was no such Test or TestRun.
     */
    public Optional<List<TestResult>> getTestResults(TestRunId id) {
        return this.apiClient.getTestResults(id);
    }


    /**
     * Get Id of TestRun associated with a Selenium session.
     * @param opaqueSeleniumSessionIdString selenium session id
     * @return test run id
     */
    public TestRunId getTestRunIdForSessionId(String opaqueSeleniumSessionIdString) {
        return new TestRunId(UUID.randomUUID());
    }
}
