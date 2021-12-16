package com.testfabrik.webmate.javasdk.images;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfabrik.webmate.javasdk.JacksonMapper;

import java.io.IOException;
import java.util.Objects;

/**
 * Metadata of a screenshot, width and height
 */
public class ScreenshotMetadata {

    private Integer width;

    private Integer height;

    // For jackson
    private ScreenshotMetadata() {
    }

    public ScreenshotMetadata(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public static ScreenshotMetadata fromJsonString(String string) throws IOException {
        ObjectMapper mapper = JacksonMapper.getInstance();
        return mapper.readValue(string, ScreenshotMetadata.class);
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenshotMetadata screenshot = (ScreenshotMetadata) o;
        return width.equals(screenshot.width) && height.equals(screenshot.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return "ScreenshotMetadata{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
