package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BrowserType {
    Chrome("CHROME"),
    Firefox("FIREFOX"),
    InternetExplorer("IE"),
    Edge("EDGE"),
    SAFARI("SAFARI"),
    OPERA("OPERA");

    private final String value;

    @JsonCreator
    BrowserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static BrowserType getEnum(String value) {
        for (BrowserType v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
