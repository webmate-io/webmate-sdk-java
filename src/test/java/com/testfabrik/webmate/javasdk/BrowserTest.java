package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfabrik.webmate.javasdk.selenium.SeleniumSession;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

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

    @Test
    public void readsSeleniumSessionWithVersionlessPlatform() throws Exception {
        String json = "{\"browser\":{\"browserType\":\"CHROME\",\"version\":\"124\",\"platform\":\"LINUX\"}}";
        ObjectMapper mapper = new ObjectMapper();
        Browser browser = mapper.treeToValue(mapper.readTree(json), SeleniumSession.class).getBrowser();

        assertEquals("LINUX", browser.getPlatform().getPlatformType());
        assertNull(browser.getPlatform().getPlatformVersion());
        assertNull(browser.getPlatform().getPlatformArchitecture());
        assertEquals("LINUX", browser.getPlatform().toString());

        String serialized = mapper.writeValueAsString(browser);
        assertEquals(mapper.readTree("{\"platformType\":\"LINUX\"}"), mapper.readTree(serialized).get("platform"));
        assertNull(mapper.readValue(serialized, Browser.class).getPlatform().getPlatformVersion());
    }
}
