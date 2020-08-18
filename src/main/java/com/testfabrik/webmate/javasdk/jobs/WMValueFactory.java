package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;

public class WMValueFactory {

    public static WMValue makeBrickValue(WMDataType dataType, JsonNode value) {
        return new WMValue(dataType, value);
    }

    public static WMValue makeExpeditionId(BrowserSessionId id) {
        return makeBrickValue(WMDataType.ExpeditionId, JsonNodeFactory.instance.textNode(id.toString()));
    }
}
