package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public class DeviceSlotId {

    private UUID value;

    public DeviceSlotId(UUID value) {
        this.value = value;
    }

    @JsonValue
    public String getValueAsString() {
        return value.toString();
    }

    public static DeviceSlotId FOR_TESTING() {
        return new DeviceSlotId(new UUID(0, 43));
    }

    @JsonCreator
    public static DeviceSlotId fromString(String str) {
        return new DeviceSlotId(UUID.fromString(str));
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
        return obj == this || obj instanceof DeviceSlotId && Objects.equals(value, ((DeviceSlotId) obj).value);
    }
}
