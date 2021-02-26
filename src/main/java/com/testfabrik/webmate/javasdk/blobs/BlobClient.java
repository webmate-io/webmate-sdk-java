package com.testfabrik.webmate.javasdk.blobs;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Facade to webmate's Blob subsystem.
 */
public class BlobClient {

    private WebmateAPISession session;
    private BlobApiClient apiClient;

    private static final Logger LOG = LoggerFactory.getLogger(BlobClient.class);

    private static class BlobApiClient extends WebmateApiClient {

        private final static UriTemplate putBlobTemplate = new UriTemplate("/projects/${projectId}/blobs");

        private final static UriTemplate deleteBlobTemplate = new UriTemplate("/blobs/${blobId}");


        public BlobApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public BlobApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
            super(authInfo, environment, httpClientBuilder);
        }

        public BlobId putBlob(ProjectId projectId, byte[] uploadingBlob, Optional<String> contentType) {
            Optional<HttpResponse> r = sendPOST(putBlobTemplate, ImmutableMap.of("projectId", projectId.toString()), uploadingBlob, contentType).getOptHttpResponse();

            if (!r.isPresent()) {
                throw new WebmateApiClientException("Could not put blob. Got no response");
            }
            try {
                return new BlobId(readUUIDFromResponse(r.get()));
            } catch (IOException e) {
                throw new WebmateApiClientException("Error sending blob data: " + e.getMessage(), e);
            }
        }

        public void deleteBlob(BlobId blobId) {
            sendDELETE(deleteBlobTemplate, ImmutableMap.of("blobId", blobId.toString()));
        }
    }

    /**
     * Creates a BlobClient based on a WebmateApiSession.
     *
     * @param session The WebmateApiSession the BlobClient is supposed to be based on.
     */
    public BlobClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new BlobApiClient(session.authInfo, session.environment);
    }

    /**
     * Creates a BlobClient based on a WebmateApiSession and a custom HttpClientBuilder.
     *
     * @param session The WebmateApiSession the BlobClient is supposed to be based on.
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection.
     */
    public BlobClient(WebmateAPISession session, HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new BlobApiClient(session.authInfo, session.environment, httpClientBuilder);
    }

    /**
     * Uploads a blob to webmate.
     *
     * @param projectId ProjectId of device. Can be found in "Details" dialog of an item in webmate project overview.
     * @param blob the Blob that should be uploaded to webmate
     * @return BlobId of the uploaded blob
     */
    public BlobId putBlob(ProjectId projectId, byte[] blob, Optional<String> contentType) {
        return this.apiClient.putBlob(projectId, blob, contentType);
    }

    /**
     * Deletes the Blob with the given Id.
     *
     * @param blobId The Id of the Blob
     */
    public void deleteBlob(BlobId blobId) {
        this.apiClient.deleteBlob(blobId);
    }

}
