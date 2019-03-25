package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfabrik.webmate.javasdk.commonutils.Dimension;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionScreenshotExtractionConfig;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionStateId;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionWarmUpConfig;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionStateExtractionConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static junit.framework.Assert.assertEquals;


@RunWith(JUnit4.class)
public class BrowserSessionStateExtractionConfigTest {
    String serializationStringNonNull = "{\"stateId\":\"7dfaca76-9a99-4770-ad29-3731de31f241\",\"extractionDelay\":1,\"extractionCooldown\":2,\"optViewportDimension\":{\"width\":600,\"height\":800},\"maxAdditionWaitingTimeForStateExtraction\":5,\"extractDomStateData\":true,\"screenShotConfig\":{\"fullPage\":false,\"hideFixedElements\":false},\"warmUpConfig\":{\"pageWarmUpScrollBy\":42,\"pageWarmUpScrollDelay\":2,\"pageWarmUpMaxScrolls\":5458}}";
    String serializationStringWithoutWarmUp = "{\"stateId\":\"7dfaca76-9a99-4770-ad29-3731de31f241\",\"extractionDelay\":1,\"extractionCooldown\":2,\"optViewportDimension\":{\"width\":600,\"height\":800},\"maxAdditionWaitingTimeForStateExtraction\":5,\"extractDomStateData\":true,\"screenShotConfig\":{\"fullPage\":false,\"hideFixedElements\":false}}";
    String emptyObject = "{}";
    @Test
    public void serializationNonNullFieldsTest(){
        BrowserSessionStateExtractionConfig config = new BrowserSessionStateExtractionConfig(new BrowserSessionStateId(UUID.fromString("7dfaca76-9a99-4770-ad29-3731de31f241")), 1,2,
                new Dimension(600,800),5,true, new BrowserSessionScreenshotExtractionConfig(false, false),
                new BrowserSessionWarmUpConfig(42,2,5458));
        ObjectMapper mapper = new ObjectMapper();
        String serialized = mapper.valueToTree(config).toString();
        assertEquals(serializationStringNonNull, serialized);
    }

    @Test
    public void serializationNullFieldsTest(){
        BrowserSessionStateExtractionConfig config = new BrowserSessionStateExtractionConfig(new BrowserSessionStateId(UUID.fromString("7dfaca76-9a99-4770-ad29-3731de31f241")), 1,2,
                new Dimension(600,800),5,true, new BrowserSessionScreenshotExtractionConfig(false, false),
                null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String serialized = mapper.valueToTree(config).toString();
        assertEquals(serializationStringWithoutWarmUp, serialized);
    }

    @Test
    public void serializationAllNullFieldsTest(){
        BrowserSessionStateExtractionConfig config = new BrowserSessionStateExtractionConfig();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String serialized = mapper.valueToTree(config).toString();
        assertEquals(emptyObject, serialized);
    }



}
