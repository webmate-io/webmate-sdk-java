package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.devices.DeviceId;
import com.testfabrik.webmate.javasdk.devices.DeviceRequest;
import com.testfabrik.webmate.javasdk.devices.DeviceSlotId;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Objects;

/**
 * Device information of a Test Run.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRunDeviceInfo {
    private DeviceId id;
    private DateTime creationTime;
    private String name;
    private DeviceRequest request;
    private ObjectNode metaData;
    private Map<String, JsonNode> properties;
    private ProjectId projectId;

    private TestRunDeviceInfo() {}

    public TestRunDeviceInfo(DeviceId id, DateTime creationTime, String name, DeviceRequest request, ObjectNode metaData,
                     Map<String, JsonNode> properties, ProjectId projectId) {
        this.id = id;
        this.creationTime = creationTime;
        this.name = name;
        this.request = request;
        this.metaData = metaData;
        this.properties = properties;
        this.projectId = projectId;
    }

    public DeviceId getId() {
        return id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public String getName() {
        return name;
    }

    public DeviceRequest getRequest() {
        return request;
    }

    public ObjectNode getMetaData() {
        return metaData;
    }

    public Map<String, JsonNode> getProperties() {
        return ImmutableMap.copyOf(properties);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public String toString() {
        return "TestRunDeviceInfo{" +
                "id=" + id +
                ", creationTime=" + creationTime +
                ", name='" + name + '\'' +
                ", request=" + request +
                ", metaData=" + metaData +
                ", properties=" + properties +
                ", projectId=" + projectId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.testfabrik.webmate.javasdk.testmgmt.TestRunDeviceInfo deviceDTO = (com.testfabrik.webmate.javasdk.testmgmt.TestRunDeviceInfo) o;
        return Objects.equals(id, deviceDTO.id) &&
                Objects.equals(creationTime, deviceDTO.creationTime) &&
                Objects.equals(name, deviceDTO.name) &&
                Objects.equals(request, deviceDTO.request) &&
                Objects.equals(metaData, deviceDTO.metaData) &&
                Objects.equals(properties, deviceDTO.properties) &&
                Objects.equals(projectId, deviceDTO.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationTime, name, request, metaData, properties, projectId);
    }
}
