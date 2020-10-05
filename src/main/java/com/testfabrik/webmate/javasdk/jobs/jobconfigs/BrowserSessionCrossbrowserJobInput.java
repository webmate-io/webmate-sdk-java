package com.testfabrik.webmate.javasdk.jobs.jobconfigs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.ComparisonOptions;
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
    private final ComparisonOptions comparisonOptions;

    public BrowserSessionCrossbrowserJobInput(BrowserSessionId referenceSession,
                                              List<BrowserSessionId> compareSessions) {
        this(referenceSession, compareSessions, new ComparisonOptions());
    }

    public BrowserSessionCrossbrowserJobInput(BrowserSessionId referenceSession,
                                              List<BrowserSessionId> compareSessions,
                                              ComparisonOptions comparisonOptions) {
        this.referenceSession = referenceSession;
        this.compareSessions = compareSessions;
        this.comparisonOptions = comparisonOptions;
    }

    @Override
    public JobConfigName getName() {
        return new JobConfigName("browsersession_crossbrowser_analysis");
    }

    @Override
    public Map<PortName, WMValue> makeInputValues() {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> compareSessionJson = new ArrayList<>();
        for (BrowserSessionId compareSession : compareSessions) {
            JsonNode sessionNode = mapper.valueToTree(compareSession);
            WMValue sessionWMValue = new WMValue(new WMDataType("BrowserSessionRef"), sessionNode);
            compareSessionJson.add(mapper.valueToTree(sessionWMValue));
        }

        return ImmutableMap.of(
                new PortName("referenceSession"), new WMValue(new WMDataType("BrowserSessionRef"), jsonNodeFactory.textNode(referenceSession.toString())),
                new PortName("compareSessions"), new WMValue(new WMDataType("List[BrowserSessionRef]"), mapper.valueToTree(compareSessionJson)),
                new PortName("matchingType"), new WMValue(new WMDataType("String"), jsonNodeFactory.textNode("tag")),
                new PortName("enabledynamicelementsfilter"), new WMValue(new WMDataType("Boolean"), jsonNodeFactory.booleanNode(true)),
                new PortName("comparisonOptions"), new WMValue(new WMDataType("ComparisonOptions"), mapper.valueToTree(comparisonOptions))
        );
    }

}
