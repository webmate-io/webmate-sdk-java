package com.testfabrik.webmate.javasdk.images;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Facade to webmate's Artifact subsystem.
 */
public class ImageClient {

    private static final Logger LOG = LoggerFactory.getLogger(ImageClient.class);
    private final WebmateAPISession session;
    private final ImageApiClient apiClient;

    /**
     * Creates an ArtifactClient based on a WebmateApiSession.
     *
     * @param session The WebmateApiSession used by the ArtifactClient
     */
    public ImageClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new ImageApiClient(session.authInfo, session.environment);
    }

    /**
     * Creates an ArtifactClient based on a WebmateApiSession and a custom HttpClientBuilder.
     *
     * @param session           The WebmateApiSession used by the ArtifactClient
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection.
     */
    public ImageClient(WebmateAPISession session, HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new ImageApiClient(session.authInfo, session.environment, httpClientBuilder);
    }

    /**
     * Retrieve Screenshotmetadata with id.
     *
     * @param id Id of Screenshot.
     * @return ScreenshotMetadata
     */
    public Optional<ScreenshotMetadata> getScreenshotMetadata(ScreenshotId id) {
        return this.apiClient.getScreenshotMetadata(id);
    }

    /**
     * Retrieve Screenshot with id.
     *
     * @param id Id of Screenshot.
     * @return String
     */
    public Optional<byte[]> getScreenshot(ScreenshotId id) {
        return this.apiClient.getScreenshot(id);
    }

    private static class ImageApiClient extends WebmateApiClient {

        private final static UriTemplate getMetadataTemplate =
                new UriTemplate("/images/${screenshotId}/meta");

        private final static UriTemplate getScreenshotTemplate =
                new UriTemplate("/images/${screenshotId}");


        public ImageApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public ImageApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder clientBuilder) {
            super(authInfo, environment, clientBuilder);
        }

        public Optional<ScreenshotMetadata> getScreenshotMetadata(ScreenshotId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getMetadataTemplate, ImmutableMap.of("screenshotId", id.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            ScreenshotMetadata screenshotMetadata;
            try {
                String screenshotMetadataJson = EntityUtils.toString(optHttpResponse.get().getEntity());
                screenshotMetadata = ScreenshotMetadata.fromJsonString(screenshotMetadataJson);
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading screenshotMetadata data: " + e.getMessage(), e);
            }
            return Optional.fromNullable(screenshotMetadata);
        }

        public Optional<byte[]> getScreenshot(ScreenshotId id) {
            Optional<HttpResponse> optHttpResponse = sendGET(getScreenshotTemplate, ImmutableMap.of("screenshotId", id.toString())).getOptHttpResponse();
            if (!optHttpResponse.isPresent()) {
                return Optional.absent();
            }

            byte[] screenshot;
            try {
                screenshot = EntityUtils.toByteArray(optHttpResponse.get().getEntity());
            } catch (IOException e) {
                throw new WebmateApiClientException("Error reading screenshotMetadata data: " + e.getMessage(), e);
            }
            return Optional.fromNullable(screenshot);
        }
    }
}
