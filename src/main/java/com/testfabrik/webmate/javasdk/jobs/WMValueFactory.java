package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSpecification;
import com.testfabrik.webmate.javasdk.browsersession.ExpeditionSpec;

import java.net.URI;
import java.util.List;

public class WMValueFactory {

    public static WMValue makeBrickValue(WMDataType dataType, JsonNode value) {
        return new WMValue(dataType, value);
    }

    public static WMValue makeExpeditionId(BrowserSessionId id) {
        return makeBrickValue(WMDataType.ExpeditionId, JsonNodeFactory.instance.textNode(id.toString()));
    }

//    private static ObjectNode convertBrowserSpecificationToJson(BrowserSpecification browserSpecification, List<URI> urls) {
//        // driverSpec
//        ObjectNode driverSpecNode = JsonNodeFactory.instance.objectNode();
//        driverSpecNode.put("type", WMDataType.URLListDriverSpecification.getTpe());
//        ArrayNode urlsNode = driverSpecNode.putArray("urls");
//        // urls
//        for (URI url : urls) {
//            urlsNode.add(url.toString());
//        }
//        // vehicleSpec
//        ObjectNode vehicleSpecNode = JsonNodeFactory.instance.objectNode();
//        vehicleSpecNode.put("type", WMDataType.BrowserSpecification.getTpe());
//        JsonNode browserNode = new ObjectMapper().valueToTree(browserSpecification.browser);
//        vehicleSpecNode.put("browser", browserNode);
//        // data
//        ObjectNode dataNode = JsonNodeFactory.instance.objectNode();
//        dataNode.put("type", WMDataType.LiveExpeditionSpec.getTpe());
//        dataNode.put("driverSpec", driverSpecNode);
//        dataNode.put("vehicleSpec", vehicleSpecNode);
//
//        return dataNode;
//    }

    public static WMValue makeExpeditionSpec(ExpeditionSpec expeditionSpec) {
        ObjectMapper om = JacksonMapper.getInstance();
        return makeBrickValue(WMDataType.ExpeditionSpec, om.valueToTree(expeditionSpec));
    }

    public static WMValue makeExpeditionSpecList(List<ExpeditionSpec> expeditionSpecs) {
        ObjectMapper om = JacksonMapper.getInstance();
        return makeBrickValue(WMDataType.ListExpeditionSpec,
                om.valueToTree(expeditionSpecs.stream().map(WMValueFactory::makeExpeditionSpec)));
    }
}
