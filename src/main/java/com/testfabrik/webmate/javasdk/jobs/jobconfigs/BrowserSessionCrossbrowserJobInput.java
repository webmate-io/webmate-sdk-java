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
 * Helper class for creating BrowserSessionCrossbrowser jobs.
 */
public class BrowserSessionCrossbrowserJobInput implements WellKnownJobInput {

    private final BrowserSessionId referenceSession;
    private final List<BrowserSessionId> compareSessions;

    public BrowserSessionCrossbrowserJobInput(BrowserSessionId referenceSession, List<BrowserSessionId> compareSessions) {
        this.referenceSession = referenceSession;
        this.compareSessions = compareSessions;
    }

    @Override
    public JobConfigName getName() {
        return new JobConfigName("browsersession_crossbrowser_analysis");
    }

    @Override
    public Map<PortName, BrickValue> makeInputValues() {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> compareSessionJson = new ArrayList<>();
        for (BrowserSessionId compareSession : compareSessions) {
            JsonNode sessionNode = mapper.valueToTree(compareSession);
            BrickValue sessionBrickValue = new BrickValue(new BrickDataType("BrowserSessionRef"), sessionNode);
            compareSessionJson.add(mapper.valueToTree(sessionBrickValue));
        }

        return ImmutableMap.of(
                new PortName("referenceSession"), new BrickValue(new BrickDataType("BrowserSessionRef"), jsonNodeFactory.textNode(referenceSession.toString())),
                new PortName("compareSessions"), new BrickValue(new BrickDataType("List[BrowserSessionRef]"), mapper.valueToTree(compareSessionJson)),
                new PortName("matchingType"), new BrickValue(new BrickDataType("String"), jsonNodeFactory.textNode("tag")),
                new PortName("enabledynamicelementsfilter"), new BrickValue(new BrickDataType("Boolean"), jsonNodeFactory.booleanNode(true))
        );
    }


}
