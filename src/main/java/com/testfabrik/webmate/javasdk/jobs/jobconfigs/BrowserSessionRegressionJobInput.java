package com.testfabrik.webmate.javasdk.jobs.jobconfigs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.jobs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper class for creating BrowserSessionRegression jobs.
 */
public class BrowserSessionRegressionJobInput implements WellKnownJobInput {

    private final BrowserSessionId referenceSession;
    private final BrowserSessionId compareSession;

    public BrowserSessionRegressionJobInput(BrowserSessionId referenceSession, BrowserSessionId compareSession) {
        this.referenceSession = referenceSession;
        this.compareSession = compareSession;
    }

    @Override
    public JobConfigName getName() {
        return new JobConfigName("browsersession_regression_analysis");
    }

    @Override
    public Map<PortName, WMValue> makeInputValues() {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> compareSessionJson = new ArrayList<>();
        JsonNode sessionNode = mapper.valueToTree(compareSession);
        WMValue sessionWMValue = new WMValue(new WMDataType("BrowserSessionRef"), sessionNode);
        compareSessionJson.add(mapper.valueToTree(sessionWMValue));

        return ImmutableMap.of(
                new PortName("referenceSession"), new WMValue(new WMDataType("BrowserSessionRef"), jsonNodeFactory.textNode(referenceSession.getValueAsString())),
                new PortName("compareSession"), new WMValue(new WMDataType("BrowserSessionRef"), jsonNodeFactory.textNode(compareSession.getValueAsString())),
                new PortName("matchingType"), new WMValue(new WMDataType("String"), jsonNodeFactory.textNode("tag")),
                new PortName("enabledynamicelementsfilter"), new WMValue(new WMDataType("Boolean"), jsonNodeFactory.booleanNode(true))
        );
    }


}
