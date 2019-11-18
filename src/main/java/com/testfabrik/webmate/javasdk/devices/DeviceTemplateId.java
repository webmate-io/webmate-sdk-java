package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public class DeviceTemplateId {
    private UUID value;

    @JsonCreator
    public DeviceTemplateId(UUID value) {
        this.value = value;
    }

    /**
     * Create new DeviceTemplateId from String representation.
     * @param templateIdStr id of template
     * @return created template id
     */
    public static DeviceTemplateId of(String templateIdStr) {
        return new DeviceTemplateId(UUID.fromString(templateIdStr));
    }

    static DeviceTemplateId FOR_TESTING() {
        return new DeviceTemplateId(new UUID(0, 30 /* TODO */));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceTemplateId projectId = (DeviceTemplateId) o;

        return value.equals(projectId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
