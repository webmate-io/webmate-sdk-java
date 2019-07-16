package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

public class DeviceTemplate {
    private DeviceTemplateId id;
    private String providerType;
    private String platform;
    private String name;
    private JsonNode capabilities;
    private JsonNode size;


    // for jackson
    public DeviceTemplate() {}
    public DeviceTemplate(DeviceTemplateId id, String providerType, String platform, String name,
                          JsonNode capabilities, JsonNode size) {
        this.id = id;
        this.providerType = providerType;
        this.platform = platform;
        this.name = name;
        this.capabilities = capabilities;
        this.size = size;
    }

    public DeviceTemplateId getId() {
        return id;
    }

    public String getProviderType() {
        return providerType;
    }

    public String getPlatform() {
        return platform;
    }

    public String getName() {
        return name;
    }

    public JsonNode getCapabilities() {
        return capabilities;
    }

    public JsonNode getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceTemplate that = (DeviceTemplate) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(providerType, that.providerType) &&
                Objects.equals(platform, that.platform) &&
                Objects.equals(name, that.name) &&
                Objects.equals(capabilities, that.capabilities) &&
                Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerType, platform, name, capabilities, size);
    }

    @Override
    public String toString() {
        return "DeviceTemplate{" +
                "id=" + id +
                ", providerType='" + providerType + '\'' +
                ", platform='" + platform + '\'' +
                ", name='" + name + '\'' +
                ", capabilities=" + capabilities +
                ", size=" + size +
                '}';
    }
}
