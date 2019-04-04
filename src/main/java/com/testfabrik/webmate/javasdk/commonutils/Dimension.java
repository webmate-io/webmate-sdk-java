package com.testfabrik.webmate.javasdk.commonutils;

/**
 * Wrapper class to describe screen and viewport resolutions.
 */

public class Dimension {

    private int width;
    private int height;

    /**
     * Creates a new Dimension Object.
     * @param width The width of the screen/viewport.
     * @param height The height of the screen/viewport,.
     */
    public Dimension(int width, int height) {
        this.width = width;
        this.height= height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dimension dimension = (Dimension) o;

        if (width != dimension.width) return false;
        return height == dimension.height;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        return result;
    }

    public int getWidth() {

        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
