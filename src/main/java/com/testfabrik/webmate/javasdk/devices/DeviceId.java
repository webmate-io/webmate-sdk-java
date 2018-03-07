package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class DeviceId {

    private UUID value;


    public DeviceId(UUID value) {
        this.value = value;
    }

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static DeviceId FOR_TESTING() {
        return new DeviceId(new UUID(0, 43));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof DeviceId && Objects.equals(value, ((DeviceId) obj).value);
    }
}
