package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * Facade that provides access to the webmate JobEngine service.
 */
public class JobEngine {

    private static class JobEngineApiClient extends WebmateApiClient {

        private final static UriTemplate createJobTemplate = new UriTemplate("/projects/${projectId}/job/jobs");
        private final static UriTemplate jobRunsForJobTemplate = new UriTemplate("/job/jobs/${jobId}/jobruns");

        public JobEngineApiClient(WebmateAuthInfo webmateAuthInfo, WebmateEnvironment environment) {
            super(webmateAuthInfo, environment);
        }

        /**
         * Create new Job.
         *
         * @param projectId ProjectId of Project where Job should be started in.
         * @param jobConfigName  Name of new JobConfig.
         * @param nameForJobInstance Name describing the current Job.
         * @param inputValues Input values for Job.
         * @return Id of new Job.
         */
        public JobId createJob(ProjectId projectId, JobConfigName jobConfigName, String nameForJobInstance, Map<PortName, BrickValue> inputValues) {

            ObjectMapper mapper = new ObjectMapper();

            Map<String, JsonNode> simpleInputValues = new HashMap<>();
            for (PortName key : inputValues.keySet()) {
                simpleInputValues.put(key.toString(), mapper.valueToTree(inputValues.get(key)));
            }

            JsonNode inputValuesJson = mapper.valueToTree(simpleInputValues);

            Map<String, JsonNode> createJobDto;
            try {
                createJobDto = ImmutableMap.of(
                        "nameForJobInstance", JsonNodeFactory.instance.textNode(nameForJobInstance),
                        "inputValues", inputValuesJson,
                        "scheduling", mapper.readTree("{ \"jobSchedulingSpec\": { \"DirectlyScheduled\": {} } }"),
                        "jobConfigIdOrName", JsonNodeFactory.instance.textNode(jobConfigName.jobConfigName));
            } catch (IOException e) {
                throw new WebmateApiClientException("Error creating JSON", e);
            }

            ApiResponse response = this.sendPOST(createJobTemplate, ImmutableMap.of("projectId", projectId.toString()), mapper.valueToTree(createJobDto));
            Optional<HttpResponse> optHttpResponse = response.getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not start Job. Got no response");
            }

            String jobId;
            try {
                jobId = EntityUtils.toString(optHttpResponse.get().getEntity());
            } catch (IOException e) {
                throw new WebmateApiClientException("Could not start Job. Got no JobId");
            }
            return new JobId(UUID.fromString(jobId));
        }

        /**
         * Return list of JobRuns for the given JobId.
         */
        public List<JobRunId> getJobRunsForJob(JobId jobId) {
            ApiResponse response = this.sendGET(jobRunsForJobTemplate, ImmutableMap.of("jobId", jobId.toString()));

            Optional<HttpResponse> optHttpResponse = response.getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not get JobRuns for Job " + jobId + ". Got no response");
            }

            if (optHttpResponse.get().getStatusLine().getStatusCode() != 200) {
                throw new WebmateApiClientException("Retrieving JobRuns" + jobId + ". Got no response");
            }

            List<JobRunId> jobRunIds = new ArrayList<>();
            ObjectMapper om = new ObjectMapper();

            try {
                String[] jsonIds = om.readValue(optHttpResponse.get().getEntity().getContent(), String[].class);
                for (String jsonId : jsonIds) {
                    jobRunIds.add(new JobRunId(UUID.fromString(jsonId)));
                }
            } catch (IOException e) {
                throw new WebmateApiClientException("Could not read JobRun ids", e);
            }
            return jobRunIds;
        }
    }

    private JobEngineApiClient apiClient;

    public JobEngine(WebmateAPISession session) {
        this.apiClient = new JobEngineApiClient(session.authInfo, session.environment);
    }

    /**
     * Create a new webmate Job and directly start a new JobRun for this Job.
     *
     * @param jobConfigName  Name of new JobConfig.
     * @param nameForJobInstance Name describing JobRun.
     * @param inputValues Input values for Job.
     * @param projectId ProjectId of Project where Job should be started in.
     * @return Id of new JobRun
     */
    public JobRunId startJob(JobConfigName jobConfigName, String nameForJobInstance, Map<PortName, BrickValue> inputValues, ProjectId projectId) {

        // create Job
        JobId jobId = this.apiClient.createJob(projectId, jobConfigName, nameForJobInstance, inputValues);

        // When the Job is directly scheduled, we just have to wait for the first JobRun to appear.
        Optional<JobRunId> firstJobRun = getFirstJobRunForJob(jobId);
        if (! firstJobRun.isPresent()) {
            throw new WebmateApiClientException("Could not start JobRun");
        }
        return firstJobRun.get();
    }

    private Optional<JobRunId> getFirstJobRunForJob(JobId jobId) {
        for (int i = 0; i < 10; ++i) {
            List<JobRunId> jobRunIds = this.apiClient.getJobRunsForJob(jobId);
            if (jobRunIds.size() > 0) {
                return Optional.of(jobRunIds.get(0));
            }
        }
        return Optional.absent();
    }

    /**
     * Create a new webmate Job and directly start a new JobRun for this Job.
     *
     * @param nameForJobInstance Name describing JobRun.
     * @param config Configuration of one of webmate well-known Jobs, e.g. CrossbrowserLayoutAnalysis.
     * @param projectId ProjectId of Project where Job should be started in.
     * @return Id of new JobRun
     */
    public JobRunId startJob(String nameForJobInstance, WellKnownJobInput config, ProjectId projectId) {
        JobConfigName configName = config.getName();
        Map<PortName, BrickValue> inputValues = config.makeInputValues();

        return startJob(configName, nameForJobInstance, inputValues, projectId);
    }

    /**
     * Return list of JobRunIds for the given JobId.
     * @param jobId Id of Job, for which JobRuns should be retrieved.
     * @return List of JobRun ids
     */
    public List<JobRunId> getJobRunsForJob(JobId jobId) {
        return this.apiClient.getJobRunsForJob(jobId);
    }
}
