package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;

public class BrickValueFactory {

    public static BrickValue makeBrickValue(BrickDataType dataType, JsonNode value) {
        return new BrickValue(dataType, value);
    }

}
