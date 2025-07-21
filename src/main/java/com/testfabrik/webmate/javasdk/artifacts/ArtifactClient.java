package com.testfabrik.webmate.javasdk.artifacts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.testfabrik.webmate.javasdk.*;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.testmgmt.*;
import org.apache.commons.io.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Facade to webmate's Artifact subsystem.
 */
public class ArtifactClient {

    private WebmateAPISession session;
    private ArtifactApiClient apiClient;

    private static final Logger LOG = LoggerFactory.getLogger(ArtifactClient.class);

    private static class ArtifactApiClient extends WebmateApiClient {

        private final static UriTemplate queryArtifactsTemplate =
                new UriTemplate("/projects/${projectId}/artifact-infos");

        private final static UriTemplate queryArtifactsForBrowserSessionTemplate =
                new UriTemplate("/browsersession/${browsersessionId}/artifacts");

        private final static UriTemplate getArtifactTemplate =
                new UriTemplate("/artifact/artifacts/${artifactId}");


        public ArtifactApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public ArtifactApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder clientBuilder) {
            super(authInfo, environment, clientBuilder);
        }

        /**
         * Retrieve matching artifacts in a project.
         *
         * @param id Id of project to retrieve artifacts from
         * @param associatedTestRun Id of test run associated with artifacts.
         * @param associatedBrowserSession Id of browser session associated with artifacts
         * @param artifactTypes Types of artifacts to retrieve. If set is empty, artifacts of all types are retrieved.
         * @return list of matching artifact infos
         */
        public Optional<List<ArtifactInfo>> queryArtifacts(ProjectId id, TestRunId associatedTestRun, BrowserSessionId associatedBrowserSession, Set<ArtifactType> artifactTypes) {


            List<NameValuePair> params = Lists.newArrayList();
            if (associatedTestRun != null) params.add(new BasicNameValuePair("testRunId", associatedTestRun.toString()));
            if (associatedBrowserSession != null) params.add(new BasicNameValuePair("browserSessionId", associatedBrowserSession.toString()));

            if (!artifactTypes.isEmpty()) {
                StringBuilder typesParam = new StringBuilder();
                for (ArtifactType artifactType : artifactTypes) {
                    String typeName = artifactType.asSerializedString();
                    typesParam = typesParam.length() > 0 ? typesParam.append(",").append(typeName) : typesParam.append(typeName);
                }
                params.add(new BasicNameValuePair("types", typesParam.toString()));
            }
            Optional<HttpResponse> optHttpResponse = sendGET(queryArtifactsTemplate, ImmutableMap.of("projectId", id.toString()), params).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            ApiDataResult<ArtifactInfo[]> artifactInfos;
            try {
                String testInfosJson = EntityUtils.toString(optHttpResponse.get().getEntity());
                ObjectMapper mapper = JacksonMapper.getInstance();
                mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                artifactInfos = mapper.readValue(testInfosJson, new TypeReference<ApiDataResult<ArtifactInfo[]>>() {});
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading data: " + e.getMessage(), e);
            }
            return Optional.of(Arrays.asList(artifactInfos.data));
        }

        /**
         * Retrieve matching artifacts for a browsersession.
         *
         * @param associatedBrowserSession Id of browser session associated with artifacts
         * @return list of matching artifact infos
         */
        public Optional<List<ArtifactInfo>> queryArtifacts(BrowserSessionId associatedBrowserSession) {

            Optional<HttpResponse> optHttpResponse = sendGET(queryArtifactsForBrowserSessionTemplate, ImmutableMap.of("browsersessionId", associatedBrowserSession.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            ArtifactInfo[] artifactInfos;
            try {
                String testInfosJson = EntityUtils.toString(optHttpResponse.get().getEntity());
                ObjectMapper mapper = JacksonMapper.getInstance();
                artifactInfos = mapper.readValue(testInfosJson, ArtifactInfo[].class);
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading data: " + e.getMessage(), e);
            }
            return Optional.of(Arrays.asList(artifactInfos));
        }

        public Optional<Artifact> getArtifact(ArtifactId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getArtifactTemplate, ImmutableMap.of("artifactId", id.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            Artifact artifact;
            try {
                String artifactJson = EntityUtils.toString(optHttpResponse.get().getEntity());
                artifact = Artifact.fromJsonString(artifactJson);
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading Artifact data: " + e.getMessage(), e);
            }
            return Optional.fromNullable(artifact);
        }
    }

    /**
     * Creates an ArtifactClient based on a WebmateApiSession.
     *
     * @param session The WebmateApiSession used by the ArtifactClient
     */
    public ArtifactClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new ArtifactApiClient(session.authInfo, session.environment);
    }

    /**
     * Creates an ArtifactClient based on a WebmateApiSession and a custom HttpClientBuilder.
     *
     * @param session The WebmateApiSession used by the ArtifactClient
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection.
     */
    public ArtifactClient(WebmateAPISession session, HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new ArtifactApiClient(session.authInfo, session.environment, httpClientBuilder);
    }

    /**
     * Retrieve Artifact infos associated with test run in project.
     *
     * @param projectId project id
     * @param associatedTestRun testRunId associated with artifacts.
     * @param types Types of artifacts to retrieve. If set is empty, artifacts of all types are retrieved.
     * @return artifactInfo list
     */
    public List<ArtifactInfo> queryArtifacts(ProjectId projectId, TestRunId associatedTestRun, Set<ArtifactType> types) {
        return this.apiClient.queryArtifacts(projectId, associatedTestRun, null, types).get();
    }

    /**
     * Retrieve Artifact infos associated with browser session in project.
     *
     * @param projectId project id
     * @param associatedBrowserSession browserSessionId associated with artifacts.
     * @param types Types of artifacts to retrieve. If set is empty, artifacts of all types are retrieved.
     * @return artifactInfo list
     */
    public List<ArtifactInfo> queryArtifacts(ProjectId projectId, BrowserSessionId associatedBrowserSession, Set<ArtifactType> types) {
        return this.apiClient.queryArtifacts(projectId,  null, associatedBrowserSession, types).get();
    }

    /**
     * Retrieve Artifact infos associated with browser session.
     *
     * @param associatedBrowserSession browserSessionId associated with artifacts.
     * @return artifactInfo list
     */
    public List<ArtifactInfo> queryArtifacts(BrowserSessionId associatedBrowserSession) {
        return this.apiClient.queryArtifacts(associatedBrowserSession).get();
    }

    /**
     * Retrieve Artifact infos associated with test run and browser session in project.
     *
     * @param projectId project id
     * @param associatedTestRun testRunId associated with artifacts.
     * @param associatedBrowserSession browserSessionId associated with artifacts.
     * @param types Types of artifacts to retrieve. If set is empty, artifacts of all types are retrieved.
     * @return artifactInfo list
     */
    public List<ArtifactInfo> queryArtifacts(ProjectId projectId, TestRunId associatedTestRun, BrowserSessionId associatedBrowserSession, Set<ArtifactType> types) {
        return this.apiClient.queryArtifacts(projectId, associatedTestRun, associatedBrowserSession, types).get();
    }

    /**
     * Retrieve Artifact with id.
     *
     * @param id Id of Artifact.
     * @return Artifact
     */
    public Optional<Artifact> getArtifact(ArtifactId id) {
        return this.apiClient.getArtifact(id);
    }
}
