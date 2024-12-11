package com.testfabrik.webmate.javasdk.packagemgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum PackageInstallationState {
    FINISHED("finished"),
    REQUESTED("requested"),
    FAILED("failed");

    private final String value;
    private static final Map<String, PackageInstallationState> VALUES_MAP = new HashMap<>();

    static {
        for (PackageInstallationState state : PackageInstallationState.values()) {
            VALUES_MAP.put(state.value, state);
        }
    }

    PackageInstallationState(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PackageInstallationState fromValue(String value) {
        PackageInstallationState state = VALUES_MAP.get(value.toLowerCase());
        if (state == null) {
            throw new IllegalArgumentException("Invalid value for PackageInstallationState: " + value);
        }
        return state;
    }
}
