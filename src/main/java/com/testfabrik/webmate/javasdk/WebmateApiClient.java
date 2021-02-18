package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Creates a new API client for interacting with the webmate API.
 */
public class WebmateApiClient {

    private final static Logger LOG = LoggerFactory.getLogger(WebmateApiClient.class);

    private final static String WEBMATE_JAVASDK_USERAGENT = "webmate-javasdk";
    private final static String WEBMATE_USER_HEADERKEY = "webmate.user";
    private final static String WEBMATE_APITOKEN_HEADERKEY = "webmate.api-token";

    private final HttpClient httpClient;
    private final HttpClientBuilder httpClientBuilder;
    private final WebmateAuthInfo authInfo;
    private final WebmateEnvironment environment;

    /**
     * Constructor for WebmateAPIClient. Uses default HTTP connection strategy.
     *
     * @param authInfo webmate authentication information
     * @param environment webmate environment to be used.
     */
    public WebmateApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
        this(authInfo, environment, HttpClientBuilder.create());
    }

    /**
     * Constructor for WebmateAPIClient that takes an additional client builder if the user needs more
     * influence over the HTTP connection.
     *
     * @param authInfo webmate authentication information
     * @param environment webmate environment to be used.
     * @param httpClientBuilder Client builder used to create HTTP connections
     */
    public WebmateApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
        this.httpClient = makeHttpClient(authInfo, environment, httpClientBuilder);
        this.httpClientBuilder = httpClientBuilder;
        this.authInfo = authInfo;
        this.environment = environment;
    }

    /**
     * Returns the configured and built HttpClient with default "application/json" content type header.
     *
     * @return Configured and built HttpClient.
     */
    private HttpClient getHttpClient() {
        return this.httpClient;
    }

    /**
     * Creates a new HttpClient with the given content type header as default header.
     * There is no easy mechanism to replace or override a default header. Therefore, we instantiate a
     * new http client with the passed content type header. Another solution would be to create a
     * custom HttpRequestInterceptor that handles http headers.
     *
     * @param contentType Content type header set as default header.
     * @return New HttpClient to be used by Service clients.
     */
    private HttpClient getHttpClientAndOverrideContentHeader(Header contentType) {
        return makeHttpClient(this.authInfo, this.environment, this.httpClientBuilder, contentType);
    }

    /**
     * Creates new HttpClient for interacting with webmate API.
     *
     * @param authInfo Authentication information (email address + token)
     * @param environment API endpoint address
     * @return New HttpClient to be used by Service clients.
     */
    private static HttpClient makeHttpClient(WebmateAuthInfo authInfo, WebmateEnvironment environment,
                                             HttpClientBuilder httpClientBuilder) {
        return makeHttpClient(authInfo, environment, httpClientBuilder, new BasicHeader(HttpHeaders.CONTENT_TYPE,
                "application/json"));
    }

    private static HttpClient makeHttpClient(WebmateAuthInfo authInfo, WebmateEnvironment environment,
                                             HttpClientBuilder httpClientBuilder, Header contentType) {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(WEBMATE_USER_HEADERKEY, authInfo.emailAddress));
        headers.add(new BasicHeader(WEBMATE_APITOKEN_HEADERKEY, authInfo.apiKey));
        headers.add(contentType);

        httpClientBuilder.setUserAgent(WEBMATE_JAVASDK_USERAGENT);
        httpClientBuilder.setDefaultHeaders(headers);
        return httpClientBuilder.build();
    }

    private void checkErrors(HttpResponse httpResponse, Optional<String> endpointName) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            String entityContent;
            try {
                entityContent = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                entityContent = "n/a";
            }
            if (endpointName.isPresent()) {
                throw new WebmateApiClientException("An error occurred during '" + endpointName.get() + "' request: " +
                        httpResponse.getStatusLine().getReasonPhrase() + ": " + entityContent);
            } else {
                throw new WebmateApiClientException("An error occurred during request: " +
                        httpResponse.getStatusLine().getReasonPhrase() + ": " + entityContent);
            }
        }
    }

    protected UUID readUUIDFromResponse(HttpResponse res) throws IOException {
        String json = EntityUtils.toString(res.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        String imageIdStr = mapper.readValue(json, new TypeReference<String>(){});
        return UUID.fromString(imageIdStr);
    }

    /**
     * Sends a Post to the Uri in schema using params to populate the schema. The body of the request is a Json Node.
     *
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @param body The json node that is supposed to be sent in the body
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, JsonNode body) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, body);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }
    /**
     * Sends a Post to the Uri in schema using params to populate the schema. The body of the request is empty.
     *
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    /**
     * Sends a Post to the Uri in schema using params to populate the schema. Once the schema is built, a query String
     * will also be appended. The body of the request is empty.
     *
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @param query The query String that should be appended
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, String query) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, query);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    /**
     * Sends a Post to the Uri in schema using params to populate the schema. The body of the request is a Json Node.
     * After template replacement the query string will be appended.
     *
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @param query The query String that should be appended
     * @param body The json node that is supposed to be sent in the body
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, String query, JsonNode body) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, query, body);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    /**
     * Sends a Post to the Uri in schema using params to populate the schema. The body of the request is a byte array.
     *
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @param byteParam The byte array that is supposed to be sent in the body
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, byte[] byteParam,
                                Optional<String> contentType) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, byteParam, contentType);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, byte[] byteParam,
                                Optional<String> contentType, List<NameValuePair> urlParams) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, byteParam, contentType, urlParams);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, JsonNode body) {
        try {
            HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params));
            req.setEntity(new StringEntity(body.toString()));
            return sendPOSTUnchecked(this.getHttpClient(), req);
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending POST to webmate API", e);
        }
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, String query, JsonNode body) {
        try {
            HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params, query));
            req.setEntity(new StringEntity(body.toString()));
            return sendPOSTUnchecked(this.getHttpClient(), req);
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending POST to webmate API", e);
        }
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params) {
        HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params));
        return sendPOSTUnchecked(this.getHttpClient(), req);
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params,
                                             List<NameValuePair> urlParams) {
        try {
            HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params));
            req.setEntity(new UrlEncodedFormEntity(urlParams));
            return sendPOSTUnchecked(this.getHttpClient(), req);
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending POST to webmate API", e);
        }
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, byte[] byteParam,
                                             Optional<String> contentType) {
        HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params));
        HttpClient httpClient;

        if (contentType.isPresent()) {
            req.setEntity(new ByteArrayEntity(byteParam, ContentType.create(contentType.get())));
            httpClient = this.getHttpClientAndOverrideContentHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, contentType.get()));
        } else {
            req.setEntity(new ByteArrayEntity(byteParam));
            httpClient = this.getHttpClient();
        }

        return sendPOSTUnchecked(httpClient, req);
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, byte[] byteParam,
                                             Optional<String> contentType, List<NameValuePair> urlParams) {
        HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params, urlParams));
        HttpClient httpClient;

        if (contentType.isPresent()) {
            req.setEntity(new ByteArrayEntity(byteParam, ContentType.create(contentType.get())));
            httpClient = this.getHttpClientAndOverrideContentHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, contentType.get()));
        } else {
            req.setEntity(new ByteArrayEntity(byteParam));
            httpClient = this.getHttpClient();
        }

        return sendPOSTUnchecked(httpClient, req);
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, String queryString) {
        HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params, queryString));
        return sendPOSTUnchecked(this.getHttpClient(), req);
    }

    protected HttpResponse sendPOSTUnchecked(HttpClient httpClient, HttpPost req) {
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(req);
            // Buffer the response entity in memory so we can release the connection safely
            HttpEntity old = httpResponse.getEntity();
            EntityUtils.updateEntity(httpResponse, new StringEntity(EntityUtils.toString(old)));
            req.releaseConnection();
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending POST to webmate API", e);
        }
        return httpResponse;
    }

    public ApiResponse sendGET(UriTemplate schema, Map<String, String> params) {
        HttpResponse httpResponse = sendGETUnchecked(schema, params);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    public ApiResponse sendGET(UriTemplate schema, Map<String, String> params, List<NameValuePair> queryParams) {
        HttpResponse httpResponse = sendGETUnchecked(schema, params, queryParams);
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    protected HttpResponse sendGETUnchecked(UriTemplate schema, Map<String, String> params) {
        return sendGETUnchecked(schema, params, null);
    }

    protected HttpResponse sendGETUnchecked(UriTemplate schema, Map<String, String> params, List<NameValuePair> queryParams) {
        HttpResponse httpResponse;
        try {
            HttpGet req;
            if (queryParams != null) {
                req = new HttpGet(schema.buildUri(environment.baseURI, params, queryParams));
            } else {
                req = new HttpGet(schema.buildUri(environment.baseURI, params));
            }
            httpResponse = this.getHttpClient().execute(req);
            // Buffer the response entity in memory so we can release the connection safely
            HttpEntity old = httpResponse.getEntity();
            EntityUtils.updateEntity(httpResponse, new StringEntity(EntityUtils.toString(old)));
            req.releaseConnection();
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending GET to webmate API", e);
        }
        return httpResponse;
    }

    /**
     * Sends a HTTP DELETE to the Uri in schema using params to populate the schema. The body of the request is empty.
     *
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @return The response of the API
     */
    public ApiResponse sendDELETE(UriTemplate schema, Map<String, String> params) {
        HttpResponse httpResponse;
        try {
            HttpDelete req = new HttpDelete(schema.buildUri(environment.baseURI, params));
            httpResponse = this.getHttpClient().execute(req);
            // Buffer the response entity in memory so we can release the connection safely
            HttpEntity old = httpResponse.getEntity();
            EntityUtils.updateEntity(httpResponse, new StringEntity(EntityUtils.toString(old)));
            req.releaseConnection();
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending DELETE to webmate API", e);
        }
        checkErrors(httpResponse, schema.name);
        return new ApiResponse(httpResponse);
    }

    /**
     * Template for API URI, e.g. "/browsersessions/${browserSessionId}"
     */
    protected static class UriTemplate {
        public final Optional<String> name;
        public final String schema;
        public final Map<String, String> templateParams;

        public UriTemplate(String name, String schema, Map<String, String> templateParams) {
            this.name = Optional.fromNullable(name);
            this.schema = schema;
            this.templateParams = templateParams;
        }

        public UriTemplate(String schema, Map<String, String> templateParams) {
            this.name = Optional.absent();
            this.schema = schema;
            this.templateParams = templateParams;
        }

        public UriTemplate(String schema) {
            this(schema, new HashMap<String, String>());
        }

        public UriTemplate(String name, String schema) {
            this(name, schema, new HashMap<String, String>());
        }

        private static String replaceParamsInTemplate(String template, Map<String, String> params) {
            final String paramPrologue = "${";
            final String paramEpilogue = "}";

            for (String paramKey : params.keySet()) {
                template = template.replace(paramPrologue + paramKey + paramEpilogue, params.get(paramKey));
            }

            if (template.contains(paramPrologue)) { // there should be no parameters left
                final String errorMsg = "At least one parameter of [" + params.keySet() + "] could not be matched in schema " + template;
                LOG.error(errorMsg);
                throw new WebmateApiClientException(errorMsg);
            }
            return template;
        }

        URI buildUri(URI baseUri, Map<String, String> params) {
            String schemaAfterReplacements = replaceParamsInTemplate(schema, params);

            URIBuilder builder = new URIBuilder();
            builder.setScheme(baseUri.getScheme());
            builder.setHost(baseUri.getHost());
            builder.setPort(baseUri.getPort());
            String path = baseUri.normalize().getPath() + schemaAfterReplacements;
            builder.setPath(path.replaceAll("//", "/")); // Replace double slashes

            for (String templateParamKey : templateParams.keySet()) {
                String templateParamKeyAfterReplacements = replaceParamsInTemplate(templateParamKey, params);
                String templateParamValueAfterReplacements = replaceParamsInTemplate(templateParams.get(templateParamKey), params);
                builder.setParameter(templateParamKeyAfterReplacements, templateParamValueAfterReplacements);
            }

            URI result;
            try {
                result = builder.build();
            } catch (URISyntaxException e) {
                throw new WebmateApiClientException("Could not build valid API URL", e);
            }
            return result;
        }

        URI buildUri(URI baseUri, Map<String, String> params, String query) {
            String schemaAfterReplacements = replaceParamsInTemplate(schema, params);

            URIBuilder builder = new URIBuilder();
            builder.setScheme(baseUri.getScheme());
            builder.setCustomQuery(query);
            builder.setHost(baseUri.getHost());
            builder.setPort(baseUri.getPort());
            String path = baseUri.normalize().getPath() + schemaAfterReplacements;
            builder.setPath(path.replaceAll("//", "/")); // Replace double slashes

            for (String templateParamKey : templateParams.keySet()) {
                String templateParamKeyAfterReplacements = replaceParamsInTemplate(templateParamKey, params);
                String templateParamValueAfterReplacements = replaceParamsInTemplate(templateParams.get(templateParamKey), params);
                builder.setParameter(templateParamKeyAfterReplacements, templateParamValueAfterReplacements);
            }

            URI result;
            try {
                result = builder.build();
            } catch (URISyntaxException e) {
                throw new WebmateApiClientException("Could not build valid API URL", e);
            }
            return result;
        }

        URI buildUri(URI baseUri, Map<String, String> params, List<NameValuePair> queryParams) {
            String schemaAfterReplacements = replaceParamsInTemplate(schema, params);

            URIBuilder builder = new URIBuilder();
            builder.setScheme(baseUri.getScheme());
            builder.addParameters(queryParams);
            builder.setHost(baseUri.getHost());
            builder.setPort(baseUri.getPort());
            String path = baseUri.normalize().getPath() + schemaAfterReplacements;
            builder.setPath(path.replaceAll("//", "/")); // Replace double slashes

            for (String templateParamKey : templateParams.keySet()) {
                String templateParamKeyAfterReplacements = replaceParamsInTemplate(templateParamKey, params);
                String templateParamValueAfterReplacements = replaceParamsInTemplate(templateParams.get(templateParamKey), params);
                builder.setParameter(templateParamKeyAfterReplacements, templateParamValueAfterReplacements);
            }

            URI result;
            try {
                result = builder.build();
            } catch (URISyntaxException e) {
                throw new WebmateApiClientException("Could not build valid API URL", e);
            }
            return result;
        }
    }

    public static class ApiResponse {
        private Optional<HttpResponse> optHttpResponse;

        public ApiResponse(HttpResponse httpResponse) {
            this.optHttpResponse = Optional.fromNullable(httpResponse);
        }

        public Optional<HttpResponse> getOptHttpResponse() {
            return optHttpResponse;
        }
    }
}
