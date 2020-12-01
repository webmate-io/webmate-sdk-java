package com.testfabrik.webmate.javasdk.testmgmt.testtypes;

public enum StandardTestTypes {
    AdHoc(TestType.of("adhoc")),
    Selenium(TestType.of("selenium")),
    LegacyCrossbrowserSingleurlComparison(TestType.of("legacy_crossbrowser_singleurl_comparison")),
    ExpeditionComparison(TestType.of("expedition_comparison")),
    StoryCheck(TestType.of("story_check"));

    private final TestType testType;

    public TestType getTestType() {
        return testType;
    }

    StandardTestTypes(TestType testType) {
        this.testType = testType;
    }
}
