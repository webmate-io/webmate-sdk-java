package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testfabrik.webmate.javasdk.JacksonMapper;

import java.util.Map;

public class DeviceRequirements {
    private final Map<DevicePropertyName, Object> deviceProperties;

    public DeviceRequirements(Map<DevicePropertyName, Object> deviceProperties) {
        this.deviceProperties = deviceProperties;
    }

    public Map<DevicePropertyName, Object> getDeviceProperties() {
        return deviceProperties;
    }

    @JsonValue
    JsonNode toJson() {
        ObjectMapper om = JacksonMapper.getInstance();
        ObjectNode root = om.createObjectNode();
        for (Map.Entry<DevicePropertyName, Object> entry : this.deviceProperties.entrySet()) {
            JsonNode jsonNode = om.valueToTree(entry.getValue());
            String s = entry.getValue().toString();
            root.set(entry.getKey().property, jsonNode);
        }

        return root;
    }

}
