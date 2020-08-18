package com.testfabrik.webmate.javasdk.commonutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.WebmateApiClientException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpHelpers {

    public static String getStringFromEntity(HttpResponse response) {
        String result;
        try {
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new WebmateApiClientException("Could not extract response data.");
        }
        return result;
    }

    public static <T> JsonNode getJsonNodeFromEntity(HttpResponse response) {
        JsonNode result;
        try {
            String strResult = EntityUtils.toString(response.getEntity());
            ObjectMapper om = JacksonMapper.getInstance();
            result = om.readTree(strResult);
        } catch (IOException e) {
            throw new WebmateApiClientException("Could not extract response data to json.");
        }
        return result;
    }

    public static <T> T getObjectFromJsonEntity(HttpResponse response, Class<T> clazz) {
        ObjectMapper om = JacksonMapper.getInstance();
        try {
            return om.treeToValue(getJsonNodeFromEntity(response), clazz);
        } catch (JsonProcessingException e) {
            throw new WebmateApiClientException("Could not extract response json data to instance of type ." + clazz.getSimpleName());
        }
    }
}
