package com.testfabrik.webmate.javasdk.devices;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceProperties {

    private final Map<String, Object> deviceProperties;

    @JsonCreator
    public DeviceProperties(Map<String, Object> deviceProperties) {
        this.deviceProperties = deviceProperties;
    }

    public Map<String, Object> getProperties() {
        return deviceProperties;
    }

    public String toString() {
        return deviceProperties.toString();
    }

    public String getProperty(String key) {
        return (String) deviceProperties.get(key);
    }

    public String getSlotId() {
        return (String) deviceProperties.get("webmate.slotId");
    }

    public String getPlatform() {
        return (String) deviceProperties.get("machine.platform");
    }

    public String getOsLanguage() {
        return (String) deviceProperties.get("os.language");
    }

    public String getSerial()  { return (String) deviceProperties.getOrDefault("openstf.serial", "none");}

    public String getModel() {
        return (String) deviceProperties.getOrDefault("machine.model",getPlatform());
    }

    public List<Map<String, Object>> getBrowsers() { return (List<Map<String, Object>>) deviceProperties.getOrDefault("machine.browsers", null);}

    public String prettyPrint() {
        return String.format("%s || %s || %s || %s || %s",
            getModel(),
            getSlotId(),
            getPlatform(),
            getSerial(),
            getOsLanguage());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceProperties that = (DeviceProperties) o;
        return Objects.equals(deviceProperties, that.deviceProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModel(), getPlatform(), getSerial(), getOsLanguage());
    }


}

