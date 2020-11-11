package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class BrowserTest {

    final String EXPECTED_SERIALIZATION = "{\"browserType\":\"CHROME\",\"version\":\"83\",\"platform\":{\"platformType\":\"WINDOWS\",\"platformVersion\":\"10\",\"platformArchitecture\":\"64\"}}";

    @Test
    public void testIfBrowserSerializesCorrectly() {
        Browser browser = new Browser(BrowserType.Chrome, "83", "WINDOWS_10_64");
        ObjectMapper om = JacksonMapper.getInstance();
        assertEquals("Serialization correct", EXPECTED_SERIALIZATION, om.valueToTree(browser).toString());
    }
}
