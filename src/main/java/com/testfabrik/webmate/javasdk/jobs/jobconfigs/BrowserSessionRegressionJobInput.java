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
    public Map<PortName, BrickValue> makeInputValues() {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> compareSessionJson = new ArrayList<>();
        JsonNode sessionNode = mapper.valueToTree(compareSession);
        BrickValue sessionBrickValue = new BrickValue(new BrickDataType("BrowserSessionRef"), sessionNode);
        compareSessionJson.add(mapper.valueToTree(sessionBrickValue));

        return ImmutableMap.of(
                new PortName("referenceSession"), new BrickValue(new BrickDataType("BrowserSessionRef"), jsonNodeFactory.textNode(referenceSession.getValueAsString())),
                new PortName("compareSession"), new BrickValue(new BrickDataType("BrowserSessionRef"), jsonNodeFactory.textNode(compareSession.getValueAsString())),
                new PortName("matchingType"), new BrickValue(new BrickDataType("String"), jsonNodeFactory.textNode("tag")),
                new PortName("enabledynamicelementsfilter"), new BrickValue(new BrickDataType("Boolean"), jsonNodeFactory.booleanNode(true))
        );
    }


}
