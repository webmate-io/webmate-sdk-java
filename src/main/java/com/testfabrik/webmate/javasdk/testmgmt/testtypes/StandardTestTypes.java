package com.testfabrik.webmate.javasdk.testmgmt.testtypes;

public enum StandardTestTypes {
    AdHoc("adhoc"),
    Selenium("selenium"),
    LegacyCrossbrowserSingleurlComparison("legacy_crossbrowser_singleurl_comparison"),
    ExpeditionComparison("expedition_comparison"),
    StoryCheck("story_check");

    private final String testType;

    public String getTestType() {
        return testType;
    }

    StandardTestTypes(String testType) {
        this.testType = testType;
    }
}
