package com.testfabrik.webmate.javasdk;

public class TextOptions {

    public final boolean enableTextFilter;
    public final boolean ignoreNonPrintableCharactersInTexts;

    public TextOptions() {
        this.enableTextFilter = false;
        this.ignoreNonPrintableCharactersInTexts = false;
    }

    public TextOptions(boolean enableTextFilter, boolean ignoreNonPrintableCharactersInTexts) {
        this.enableTextFilter = enableTextFilter;
        this.ignoreNonPrintableCharactersInTexts = ignoreNonPrintableCharactersInTexts;
    }
}
