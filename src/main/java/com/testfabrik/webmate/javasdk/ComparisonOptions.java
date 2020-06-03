package com.testfabrik.webmate.javasdk;

public class ComparisonOptions {

    public final TextOptions textOptions;
    public final ScrollbarOptions scrollbarOptions;

    public ComparisonOptions() {
        this.textOptions = new TextOptions();
        this.scrollbarOptions = new ScrollbarOptions();
    }

    public ComparisonOptions(TextOptions textOptions, ScrollbarOptions scrollbarOptions) {
        this.textOptions = textOptions;
        this.scrollbarOptions = scrollbarOptions;
    }

}
