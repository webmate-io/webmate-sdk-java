package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceOffer {

    private final String id;
    private final DeviceProperties deviceProperties;

    @JsonCreator
    public DeviceOffer(@JsonProperty("id") String id,
                       @JsonProperty("deviceProperties") DeviceProperties deviceProperties) {
        this.id = id;
        this.deviceProperties = deviceProperties;
    }

    public String getId() {
        return id;
    }

    public DeviceProperties getDeviceProperties() {
        return deviceProperties;
    }

    @Override
    public String toString() {
        return "DeviceOffer " + id + " \nDeviceProperties: " + deviceProperties;
    }

    public String prettyPrint() {
        return "DeviceOffer " + id + " \nDeviceProperties: " + deviceProperties.prettyPrint();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceOffer that = (DeviceOffer) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(deviceProperties, that.deviceProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceProperties);
    }

}

