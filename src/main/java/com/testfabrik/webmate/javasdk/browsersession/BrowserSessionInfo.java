package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.testfabrik.webmate.javasdk.devices.DeviceId;
import org.joda.time.DateTime;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrowserSessionInfo {

    private BrowserSessionId id;
    private Optional<DateTime> optStart;
    private Optional<DateTime> optEnd;
    private Optional<DeviceId> optDeviceId;

    public BrowserSessionInfo() {}

    public BrowserSessionInfo(BrowserSessionId id, Optional<DateTime> optStart, Optional<DateTime> optEnd, Optional<DeviceId> optDeviceId) {
        this.id = id;
        this.optStart = optStart;
        this.optEnd = optEnd;
        this.optDeviceId = optDeviceId;
    }

    public BrowserSessionId getId() {
        return id;
    }

    public Optional<DateTime> getOptStart() {
        return optStart;
    }

    public Optional<DateTime> getOptEnd() {
        return optEnd;
    }

    public Optional<DeviceId> getOptDeviceId() {
        return optDeviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrowserSessionInfo that = (BrowserSessionInfo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
