package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DevicePropertyName {
    Console("automation.available"),
    Resolution("console.resolution"),
    ChangeResolution("console.changeResolution"),
    SlotId("webmate.slotId"),
    ProviderType("webmate.providerType"),
    Locale("os.locale"),
    Platform("machine.platform"),
    ManufacturerName("machine.manufacturer");

    public final String property;

    DevicePropertyName(String property) {
        this.property = property;
    }

    @JsonValue
    public String toValue() {
        return this.property;
    }

}
