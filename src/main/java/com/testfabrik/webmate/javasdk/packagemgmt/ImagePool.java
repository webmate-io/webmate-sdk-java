package com.testfabrik.webmate.javasdk.packagemgmt;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testfabrik.webmate.javasdk.JacksonMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The image pool is a container class for image ids. It is used when images are uploaded to webmate.
 */
public class ImagePool {

    private final Set<ImageId> imageIds;

    public ImagePool(Collection<ImageId> imageIds) {
        this.imageIds = new HashSet<>(imageIds);
    }

    public ImagePool(ImageId imageId) {
        HashSet<ImageId> imageIds = new HashSet<>();
        if (imageId != null) {
            imageIds.add(imageId);
        }
        this.imageIds = imageIds;
    }

    public Set<ImageId> getImageIds() {
        return imageIds;
    }

    public boolean contains(ImageId imageId) {
        return this.imageIds.contains(imageId);
    }

    @JsonValue
    public JsonNode toJson() {
        ObjectMapper om = JacksonMapper.getInstance();
        ObjectNode root = om.createObjectNode();

        ArrayNode array = root.putArray("imagePool");
        for (ImageId imageId : this.imageIds) {
            array.add(imageId.toString());
        }

        return root;
    }

}
