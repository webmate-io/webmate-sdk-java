package com.testfabrik.webmate.javasdk.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static JsonNode getJsonFromData(Object data, JsonInclude.Include... jsonInclude) {
        ObjectMapper mapper = new ObjectMapper();
        for (JsonInclude.Include include : jsonInclude) {
            mapper.setSerializationInclusion(include);
        }
        return mapper.valueToTree(data);
    }

}
