package com.testfabrik.webmate.javasdk.testmgmt;

import java.util.List;
import java.util.Map;

public class ExportConfig {
    private final List<String> testSessions;
    private final Map<String, String> testSessionAliases;

    public ExportConfig(List<String> testSessions, Map<String, String> testSessionAliases) {
        this.testSessions = testSessions;
        this.testSessionAliases = testSessionAliases;
    }

    public List<String> getTestSessions() {
        return testSessions;
    }

    public Map<String, String> getTestSessionAliases() {
        return testSessionAliases;
    }
}
