package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class BrowserTest {

    final String EXPECTED_SERIALIZATION = "{\"browserType\":\"CHROME\",\"version\":\"83\",\"platform\":{\"platformType\":\"WINDOWS\",\"platformVersion\":\"10\",\"platformArchitecture\":\"64\"}}";

    @Test
    public void testIfBrowserSerializesCorrectly() {
        ObjectMapper om = JacksonMapper.getInstance();

        Browser browserWPlatformStr = new Browser(BrowserType.CHROME, "83", "WINDOWS_10_64");
        assertEquals("Serialization correct", EXPECTED_SERIALIZATION, om.valueToTree(browserWPlatformStr).toString());

        Platform platform = new Platform(PlatformType.WINDOWS, "10", "64");
        Browser browserWPlatform = new Browser(BrowserType.CHROME, "83", platform);
        assertEquals("Serialization correct", EXPECTED_SERIALIZATION, om.valueToTree(browserWPlatform).toString());
    }
}
