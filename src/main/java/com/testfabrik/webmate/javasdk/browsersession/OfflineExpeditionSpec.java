package com.testfabrik.webmate.javasdk.browsersession;

import java.util.Objects;

/**
 * Specification that bases on an already finished expedition and its artifacts.
 */
public class OfflineExpeditionSpec implements ExpeditionSpec {
    // note: "expeditionId" has replaced "browserSessionId" within webmate. Types will be renamed soon.
    public final BrowserSessionId expeditionId;

    public OfflineExpeditionSpec(final BrowserSessionId expeditionId) {
        this.expeditionId = expeditionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfflineExpeditionSpec that = (OfflineExpeditionSpec) o;
        return Objects.equals(expeditionId, that.expeditionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expeditionId);
    }
}
