package com.testfabrik.webmate.javasdk;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class PlatformTest {
    @Test
    public void testIfPlatformIsParsedCorrectlyFromOpaqueString() {
        Platform platform = Platform.fromString("WINDOWS_10_64");
        assertEquals("platform type correct", "WINDOWS", platform.getPlatformType());
        assertEquals("platform version correct", "10", platform.getPlatformVersion());
        assertEquals("platform architecture correct", "64", platform.getPlatformArchitecture());
    }

    @Test
    public void testIfPlatformIsParsedCorrectlyFromOpaqueString_NoArchitecture() {
        Platform platform = Platform.fromString("WINDOWS_10");
        assertEquals("platform type correct", "WINDOWS", platform.getPlatformType());
        assertEquals("platform version correct", "10", platform.getPlatformVersion());
        assertEquals("platform architecture correct", null, platform.getPlatformArchitecture());
    }

    @Test(expected = WebmateApiClientException.class)
    public void testIfPlatformIsParsedCorrectlyFromOpaqueString_IncorrectString() {
        Platform.fromString("WINDOWS10"); // must fail because string has wrong format
    }

    @Test(expected = WebmateApiClientException.class)
    public void testIfPlatformIsParsedCorrectlyFromOpaqueString_IncorrectString2() {
        Platform.fromString("WINDOWS_10_64_20"); // must fail because string has wrong format
    }
}
