package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a new API client for interacting with the webmate API.
 */
public class WebmateApiClient {

    private final static Logger LOG = LoggerFactory.getLogger(WebmateApiClient.class);

    private final static String WEBMATE_JAVASDK_USERAGENT = "webmate-javasdk";
    private final static String WEBMATE_USER_HEADERKEY = "webmate.user";
    private final static String WEBMATE_APITOKEN_HEADERKEY = "webmate.api-token";

    private HttpClient httpClient;
    private WebmateEnvironment environment;

    /**
     * Constructor for WebmateAPIClient. Uses default HTTP connection strategy.
     * @param authInfo webmate authentication information
     * @param environment webmate environment to be used.
     */
    public WebmateApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
        this(authInfo, environment, HttpClientBuilder.create());
    }

    /**
     * Constructor for WebmateAPIClient that takes an additional client builder if the user needs more
     * influence over the HTTP connection.
     * @param authInfo webmate authentication information
     * @param environment webmate environment to be used.
     * @param httpClientBuilder client builder used to create HTTP connections
     */
    public WebmateApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
        this.httpClient = makeHttpClient(authInfo, environment, httpClientBuilder);
        this.environment = environment;
    }

    /**
     * Creates new HttpClient for interacting with webmate API.
     *
     * @param authInfo    authentication information (email address + token)
     * @param environment API endpoint address
     * @return new HttpClient to be used by Service clients.
     */
    private static HttpClient makeHttpClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {

        httpClientBuilder.setUserAgent(WEBMATE_JAVASDK_USERAGENT);

        List<Header> headers = new ArrayList<>();

        headers.add(new BasicHeader(WEBMATE_USER_HEADERKEY, authInfo.emailAddress));
        headers.add(new BasicHeader(WEBMATE_APITOKEN_HEADERKEY, authInfo.apiKey));
        headers.add(new BasicHeader("Content-Type", "application/json"));

        httpClientBuilder.setDefaultHeaders(headers);
        return httpClientBuilder.build();
    }

    private void checkErrors(HttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            String entityContent;
            try {
                entityContent = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                entityContent = "n/a";
            }
            throw new WebmateApiClientException("An error occured during request: " + httpResponse.getStatusLine().getReasonPhrase() + ": " + entityContent);
        }
    }

    /**
     * Sends a Post to the Uri in schema using params to populate the schema. The body of the request is a Json Node
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @param body The json node that is supposed to be sent in the body
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, JsonNode body) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, body);
        checkErrors(httpResponse);
        return new ApiResponse(httpResponse);
    }
    /**
     * Sends a Post to the Uri in schema using params to populate the schema. The body of the request is empty
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params);
        checkErrors(httpResponse);
        return new ApiResponse(httpResponse);
    }

    /**
     * Sends a Post to the Uri in schema using params to populate the schema. Once the schema is built, a query String will also be appended. The body of the request is empty
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @param query The query String that should be appended
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, String query) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, query);
        checkErrors(httpResponse);
        return new ApiResponse(httpResponse);
    }

    /**
     * Sends a Post to the Uri in schema using params to populate the schema. Once the schema is built, the query Params will be appended. The body of the request is empty
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @param urlParams The params that should be appended to the URI
     * @return The response of the API
     */
    public ApiResponse sendPOST(UriTemplate schema, Map<String, String> params, List<NameValuePair> urlParams) {
        HttpResponse httpResponse = sendPOSTUnchecked(schema, params, urlParams);
        checkErrors(httpResponse);
        return new ApiResponse(httpResponse);
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, JsonNode body) {
        HttpResponse httpResponse;
        try {
            HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params));
            req.setEntity(new StringEntity(body.toString()));
            httpResponse = this.httpClient.execute(req);
            // Buffer the response entity in memory so we can release the connection safely
            HttpEntity old = httpResponse.getEntity();
            EntityUtils.updateEntity(httpResponse, new StringEntity(EntityUtils.toString(old)));
            req.releaseConnection();
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending POST to webmate API", e);
        }
        return httpResponse;
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params) {
        HttpResponse httpResponse;
        try {
            HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params));
            httpResponse = this.httpClient.execute(req);
            // Buffer the response entity in memory so we can release the connection safely
            HttpEntity old = httpResponse.getEntity();
            EntityUtils.updateEntity(httpResponse, new StringEntity(EntityUtils.toString(old)));
            req.releaseConnection();
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending POST to webmate API", e);
        }
        return httpResponse;
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, List<NameValuePair> urlParams) {
        HttpResponse httpResponse;
        try {
            HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params));
            req.setEntity(new UrlEncodedFormEntity(urlParams));
            httpResponse = this.httpClient.execute(req);
            // Buffer the response entity in memory so we can release the connection safely
            HttpEntity old = httpResponse.getEntity();
            EntityUtils.updateEntity(httpResponse, new StringEntity(EntityUtils.toString(old)));
            req.releaseConnection();
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending POST to webmate API", e);
        }
        return httpResponse;
    }

    protected HttpResponse sendPOSTUnchecked(UriTemplate schema, Map<String, String> params, String queryString) {
        HttpResponse httpResponse;
        try {
            HttpPost req = new HttpPost(schema.buildUri(environment.baseURI, params, queryString));
            httpResponse = this.httpClient.execute(req);
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
        HttpResponse httpResponse =  sendGETUnchecked(schema, params);
        checkErrors(httpResponse);
        return new ApiResponse(httpResponse);
    }

    public ApiResponse sendGET(UriTemplate schema, Map<String, String> params, List<NameValuePair> queryParams){
        HttpResponse httpResponse = sendGETUnchecked(schema, params, queryParams);
        checkErrors(httpResponse);
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
            }
            else {
                req = new HttpGet(schema.buildUri(environment.baseURI, params));
            }
            httpResponse = this.httpClient.execute(req);
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
     * Sends a HTTP DELETE to the Uri in schema using params to populate the schema. The body of the request is empty
     * @param schema The Uri schema that will become the target of the Post
     * @param params The params that should be used in the schema
     * @return The response of the API
     */
    public ApiResponse sendDELETE(UriTemplate schema, Map<String, String> params) {
        HttpResponse httpResponse;
        try {
            HttpDelete req = new HttpDelete(schema.buildUri(environment.baseURI, params));
            httpResponse = this.httpClient.execute(req);
            // Buffer the response entity in memory so we can release the connection safely
            HttpEntity old = httpResponse.getEntity();
            EntityUtils.updateEntity(httpResponse, new StringEntity(EntityUtils.toString(old)));
            req.releaseConnection();
        } catch (IOException e) {
            throw new WebmateApiClientException("Error sending DELETE to webmate API", e);
        }
        checkErrors(httpResponse);
        return new ApiResponse(httpResponse);
    }

    /**
     * Template for API URI, e.g. "/browsersessions/${browserSessionId}"
     */
    protected static class UriTemplate {
        public final String schema;
        public final Map<String, String> templateParams;

        public UriTemplate(String schema, Map<String, String> templateParams) {
            this.schema = schema;
            this.templateParams = templateParams;
        }

        public UriTemplate(String schema) {
            this(schema, new HashMap<String, String>());
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
