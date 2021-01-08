package com.testfabrik.webmate.javasdk.packagemgmt;

/**
 * Supported image format types for uploaded images to webmate.
 */
public enum ImageType {
    PNG("PNG"),
    JPEG("JPEG"),
    GIF("GIF");

    private final String value;

    /**
     * Note that the string values need to be compatible with MIME types. Therefore,
     * they need to be in lower case.
     */
    ImageType(String value) {
        this.value = value.toLowerCase();
    }

    public String getValue() {
        return value;
    }

}
