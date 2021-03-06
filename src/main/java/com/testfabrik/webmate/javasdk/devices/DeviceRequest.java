package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testfabrik.webmate.javasdk.JacksonMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceRequest {
    private String name;
    private DeviceRequirements deviceRequirements;

    private DeviceRequest() {}

    public DeviceRequest(String name, DeviceRequirements deviceRequirements) {
        this.name = name;
        this.deviceRequirements = deviceRequirements;
    }

    public String getName() {
        return name;
    }

    public DeviceRequirements getDeviceRequirements() {
        return deviceRequirements;
    }

    @JsonValue
    JsonNode toJson() {
        ObjectMapper om = JacksonMapper.getInstance();
        ObjectNode root = om.createObjectNode();
        root.put("name", this.name);
        root.set("deviceRequirements", om.valueToTree(this.deviceRequirements));

        return root;
    }

}
