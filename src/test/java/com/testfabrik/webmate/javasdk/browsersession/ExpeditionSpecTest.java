package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.testfabrik.webmate.javasdk.Browser;
import com.testfabrik.webmate.javasdk.BrowserType;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import static junit.framework.Assert.assertEquals;

public class ExpeditionSpecTest {
    @Test
    public void testIfExpeditionSpecSerializesCorrectly() throws URISyntaxException, IOException {
        ExpeditionSpec expeditionSpec = ExpeditionSpecFactory.makeUrlListExpeditionSpec(
                ImmutableList.of(new URI("http://test"), new URI("http://test2")),
                new Browser(BrowserType.CHROME, "83", "WINDOWS_10_64"));

        ObjectMapper om = JacksonMapper.getInstance();
        JsonNode json = om.valueToTree(expeditionSpec);
        String expected = IOUtils.toString(this.getClass().getResourceAsStream("/expeditionSpecTestJson1.json"));
        assertEquals("json correct", expected.trim(), json.toPrettyString().trim());
    }
}
