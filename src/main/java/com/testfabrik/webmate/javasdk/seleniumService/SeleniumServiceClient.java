package com.testfabrik.webmate.javasdk.seleniumService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import com.testfabrik.webmate.javasdk.user.UserId;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Facade to the webmate SeleniumService subsystem.
 */
public class SeleniumServiceClient {
    private SeleniumServiceApiClient apiClient;
    private static final Logger LOG = LoggerFactory.getLogger(SeleniumServiceClient.class);

    private static class SeleniumServiceApiClient extends WebmateApiClient {
        private static final UriTemplate getSeleniumsessionTemplate = new UriTemplate("/seleniumsession/${sessionId}");
        private static final UriTemplate getSeleniumCapabilitiesForProjectTemplate = new UriTemplate("/projects/${projectId}/selenium/capabilities");
        private static final UriTemplate getSeleniumsessionsForProjectTemplate = new UriTemplate("/projects/${projectId}/seleniumsession");
        private static final UriTemplate getSeleniumsessionIdsForProjectTemplate = new UriTemplate("/projects/${projectId}/seleniumsession/id");
        private static final UriTemplate stopSeleniumsessionTemplate = new UriTemplate("/seleniumsession/${sessionId}/stop");

        public SeleniumServiceApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public SeleniumServiceApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
            super(authInfo, environment, httpClientBuilder);
        }


        public SeleniumSession getSeleniumsession(WebmateSeleniumSessionId sessionId) {
            ApiResponse response = sendGET(getSeleniumsessionTemplate, ImmutableMap.of("sessionId", sessionId.toString()));
            Optional<HttpResponse> optHttpResponse = response.getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("The requested Seleniumsession could not be found");
            }

            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode sessionJson = om.readTree(EntityUtils.toString(optHttpResponse.get().getEntity()));
                return buildSeleniumSessionFromJson(sessionJson);
            }
            catch (IOException e) {
                throw new WebmateApiClientException("The Seleniumsession could not be retrieved");
            }
        }

        private SeleniumSession buildSeleniumSessionFromJson(JsonNode sessionJson) {
            return new SeleniumSession(
                    new WebmateSeleniumSessionId(UUID.fromString(sessionJson.at("/id").asText())),
                    new UserId(UUID.fromString(sessionJson.at("/userId").asText())),
                    buildBrowserFromJson(sessionJson.at("/browser")),
                    sessionJson.at("/usingProxy").asBoolean(),
                    sessionJson.at("/state").asText()
            );
        }

        private Browser buildBrowserFromJson(JsonNode browserJson) {
            return new Browser(
                    BrowserType.getEnum(browserJson.at("/browserType").asText()),
                    browserJson.at("/version").asText(),
                    browserJson.at("/platform").asText()
            );
        }


        public Collection<SeleniumCapability> getSeleniumCapabilitiesForProject(ProjectId projectId) {
            Map<String, String> params = ImmutableMap.of("projectId", projectId.toString());
            ApiResponse apiResponse = sendGET(getSeleniumCapabilitiesForProjectTemplate, params);

            Optional<HttpResponse> optHttpResponse = apiResponse.getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("There has been an error retrieving the Selenium capabilities for the project");
            }

            ArrayList<SeleniumCapability> capabilities = new ArrayList<>();
            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode capabilityListJson = om.readTree(EntityUtils.toString(optHttpResponse.get().getEntity()));
                Iterator<JsonNode> capabilityListIter = capabilityListJson.iterator();
                while (capabilityListIter.hasNext()) {
                    JsonNode capabilityJson = capabilityListIter.next();
                    capabilities.add(buildSeleniumCapabilityFromJson(capabilityJson));
                }
            } catch (IOException e) {
                throw new WebmateApiClientException("The list of Selenium capabilities could not be retrieved");
            }

            return capabilities;
        }

        private SeleniumCapability buildSeleniumCapabilityFromJson(JsonNode capabilityJson) {
            return new SeleniumCapability(
                    BrowserType.getEnum(capabilityJson.at("/browserName").asText()),
                    capabilityJson.at("/version").asText(),
                    capabilityJson.at("/platform").asText(),
                    capabilityJson.at("/supportsProxy").asBoolean(),
                    capabilityJson.at("/browserlanguage").asText()
            );
        }


        public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, WebmateSeleniumSessionId after, Integer count, String state) {
            ArrayList<NameValuePair> queryParams = new ArrayList<>();
            if (after != null) {
                queryParams.add(new BasicNameValuePair("after", after.toString()));
            }
            if (count != null) {
                queryParams.add(new BasicNameValuePair("count", count.toString()));
            }
            if (state != null) {
                queryParams.add(new BasicNameValuePair("state", state));
            }

            Map<String, String> params = ImmutableMap.of("projectId", projectId.toString());
            ApiResponse apiResponse;
            if (!queryParams.isEmpty()) {
                apiResponse = sendGET(getSeleniumsessionsForProjectTemplate, params, queryParams);
            }
            else {
                apiResponse = sendGET(getSeleniumsessionsForProjectTemplate, params);
            }

            ArrayList<SeleniumSession> sessions = new ArrayList<>();
            Optional<HttpResponse> optHttpResponse = apiResponse.getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("There has been an error retrieving the Selenium sessions for the project");
            }

            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode sessionListJson = om.readTree(EntityUtils.toString(optHttpResponse.get().getEntity()));
                Iterator<JsonNode> sessionListIter = sessionListJson.iterator();
                while (sessionListIter.hasNext()) {
                    JsonNode sessionJson = sessionListIter.next();
                    sessions.add(buildSeleniumSessionFromJson(sessionJson));
                }
            } catch (IOException e) {
                throw new WebmateApiClientException("The List of Selenium sessions could not be retrieved");
            }

            return sessions;
        }


        public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, WebmateSeleniumSessionId after, Integer count, String state) {
            ArrayList<NameValuePair> queryParams = new ArrayList<>();
            if (after != null) {
                queryParams.add(new BasicNameValuePair("after", after.toString()));
            }
            if (count != null) {
                queryParams.add(new BasicNameValuePair("count", count.toString()));
            }
            if (state != null) {
                queryParams.add(new BasicNameValuePair("state", state));
            }

            Map<String, String> params = ImmutableMap.of("projectId", projectId.toString());
            ApiResponse apiResponse;
            if (!queryParams.isEmpty()) {
                apiResponse = sendGET(getSeleniumsessionIdsForProjectTemplate, params, queryParams);
            }
            else {
                apiResponse = sendGET(getSeleniumsessionIdsForProjectTemplate, params);
            }

            ArrayList<WebmateSeleniumSessionId> ids = new ArrayList<>();
            Optional<HttpResponse> optHttpResponse = apiResponse.getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("There has been an error retrieving the Selenium session IDs for the project");
            }

            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode idListJson = om.readTree(EntityUtils.toString(optHttpResponse.get().getEntity()));
                Iterator<JsonNode> idListIter = idListJson.iterator();
                while (idListIter.hasNext()) {
                    JsonNode idJson = idListIter.next();
                    ids.add(new WebmateSeleniumSessionId(UUID.fromString(idJson.asText())));
                }
            } catch (IOException e) {
                throw new WebmateApiClientException("The List of Selenium session ids could not be retrieved");
            }

            return ids;
        }


        public void stopSeleniumsession(WebmateSeleniumSessionId sessionId) {
            Map<String, String> params = ImmutableMap.of("sessionId", sessionId.toString());
            // As the sendPOST method already checks for errors if the return code is not 200, we do not need to do it here.
            sendPOST(stopSeleniumsessionTemplate, params);
        }
    }

    /**
     * Create a SeleniumServiceClient from a WebmateAPISession
     * @param session The WebmateApiSession the DeviceClient is supposed to be based on.
     */
    public SeleniumServiceClient(WebmateAPISession session) {
        this.apiClient = new SeleniumServiceApiClient(session.authInfo, session.environment);
    }

    /**
     * Create a SeleniumServiceClient from a WebmateAPISession and a HttpClientBuilder
     * @param session The WebmateApiSession the DeviceClient is supposed to be based on.
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection.
     */
    public SeleniumServiceClient(WebmateAPISession session, HttpClientBuilder httpClientBuilder) {
        this.apiClient = new SeleniumServiceApiClient(session.authInfo, session.environment, httpClientBuilder);
    }

    /**
     * Get a Selenium session by ID
     * @param sessionId ID of the selenium session to retrieve
     * @return SeleniumSession with the requested session ID
     * @throws WebmateApiClientException if a HTTP error occurred or the session could not be found/retrieved
     */
    public SeleniumSession getSeleniumsession(WebmateSeleniumSessionId sessionId) throws WebmateApiClientException {
        return this.apiClient.getSeleniumsession(sessionId);
    }

    /**
     * Get all Selenium capabilities for a project by project ID
     * @param projectId ID of the project of which the capabilities should be retrieved
     * @return List of all Selenium capabilities in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium capabilities could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumCapability> getSeleniumCapabilitiesForProject(ProjectId projectId) throws WebmateApiClientException {
        return this.apiClient.getSeleniumCapabilitiesForProject(projectId);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given Id.
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, WebmateSeleniumSessionId after, Integer count, String state) throws WebmateApiClientException {
        return this.apiClient.getSeleniumsessionsForProject(projectId, after, count, state);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId) throws WebmateApiClientException {
        return getSeleniumsessionsForProject(projectId, null, null, null);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given Id.
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, WebmateSeleniumSessionId after) throws WebmateApiClientException {
        return getSeleniumsessionsForProject(projectId, after, null, null);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, int count) throws WebmateApiClientException {
        return getSeleniumsessionsForProject(projectId, null, count, null);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, String state) throws WebmateApiClientException {
        return getSeleniumsessionsForProject(projectId, null, null, state);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given Id.
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, WebmateSeleniumSessionId after, int count) throws WebmateApiClientException {
        return getSeleniumsessionsForProject(projectId, after, count, null);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given Id.
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, WebmateSeleniumSessionId after, String state) throws WebmateApiClientException {
        return getSeleniumsessionsForProject(projectId, after, null, state);
    }

    /**
     * Get all Selenium sessions for a project by project ID
     * @param projectId The ID of the project of which Selenium sessions shall be retrieved
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium sessions in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium sessions could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<SeleniumSession> getSeleniumsessionsForProject(ProjectId projectId, int count, String state) throws WebmateApiClientException {
        return getSeleniumsessionsForProject(projectId, null, count, state);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given ID.
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, WebmateSeleniumSessionId after, Integer count, String state) throws WebmateApiClientException {
        return this.apiClient.getSeleniumsessionIdsForProject(projectId, after, count, state);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId) throws WebmateApiClientException {
        return getSeleniumsessionIdsForProject(projectId, null, null, null);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given ID.
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, WebmateSeleniumSessionId after) throws WebmateApiClientException {
        return getSeleniumsessionIdsForProject(projectId, after, null, null);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, int count) throws WebmateApiClientException {
        return getSeleniumsessionIdsForProject(projectId, null, count, null);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, String state) throws WebmateApiClientException {
        return getSeleniumsessionIdsForProject(projectId, null, null, state);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given ID.
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, WebmateSeleniumSessionId after, int count) throws WebmateApiClientException {
        return getSeleniumsessionIdsForProject(projectId, after, count, null);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @param after (optional) An ID of a SeleniumSession, the call (and all of its other query parameters) will only take Sessions into account that were created after the Session with the given ID.
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, WebmateSeleniumSessionId after, String state) throws WebmateApiClientException {
        return getSeleniumsessionIdsForProject(projectId, after, null, state);
    }

    /**
     * Get all Selenium session IDs for a project by project ID
     * @param projectId The ID of the project of which Selenium session IDs shall be retrieved
     * @param count (optional) The length of the output is restricted to the given integer, remaining (aka older) Sessions are not returned. Use of this parameter is highly recommended to avoid a gigantic result that needs to be send over the network.
     * @param state (optional) Only SeleniumSessions in the given state are considered and returned by the call, all other Sessions are filtered out.
     * @return (Filtered) List of Selenium session IDs in the given project (Actual type: ArrayList)
     * @throws WebmateApiClientException if a HTTP error occurred or the Selenium session IDs could not be retrieved (e.g. due to missing permissions)
     */
    public Collection<WebmateSeleniumSessionId> getSeleniumsessionIdsForProject(ProjectId projectId, int count, String state) throws WebmateApiClientException {
        return getSeleniumsessionIdsForProject(projectId, null, count, state);
    }

    /**
     * Stop a Selenium session by ID
     * @param sessionId ID of the Selenium session to be stopped
     * @throws WebmateApiClientException if an HTTP error occured or the Selenium session could not be found (e.g. due to missing permissions, or wrong ID)
     */
    public void stopSeleniumsession(WebmateSeleniumSessionId sessionId) throws WebmateApiClientException{
        this.apiClient.stopSeleniumsession(sessionId);
    }
}
