package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.ProjectId;
import org.joda.time.DateTime;

import java.security.cert.PKIXRevocationChecker;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDTO {
    private DeviceId id;
    private String state;
    private DateTime creationTime;
    private String name;
    private DeviceRequest request;
    private ObjectNode metaData;
    private Map<String, JsonNode> properties;
    private ObjectNode ticketCapacity;
    private ProjectId projectId;
    private DeviceSlotId slot;

    private DeviceDTO() {}

    public DeviceDTO(DeviceId id, String state, DateTime creationTime, String name, DeviceRequest request, ObjectNode metaData,
                     Map<String, JsonNode> properties, ObjectNode ticketCapacity, ProjectId projectId,
                     DeviceSlotId slot) {
        this.id = id;
        this.state = state;
        this.creationTime = creationTime;
        this.name = name;
        this.request = request;
        this.metaData = metaData;
        this.properties = properties;
        this.ticketCapacity = ticketCapacity;
        this.projectId = projectId;
        this.slot = slot;
    }

    public DeviceId getId() {
        return id;
    }

    public String getState() {
        return state;
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

    public ObjectNode getTicketCapacity() {
        return ticketCapacity;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public DeviceSlotId getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", creationTime=" + creationTime +
                ", name='" + name + '\'' +
                ", request=" + request +
                ", metaData=" + metaData +
                ", properties=" + properties +
                ", ticketCapacity=" + ticketCapacity +
                ", projectId=" + projectId +
                ", slot=" + slot +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Objects.equals(id, deviceDTO.id) &&
                Objects.equals(state, deviceDTO.state) &&
                Objects.equals(creationTime, deviceDTO.creationTime) &&
                Objects.equals(name, deviceDTO.name) &&
                Objects.equals(request, deviceDTO.request) &&
                Objects.equals(metaData, deviceDTO.metaData) &&
                Objects.equals(properties, deviceDTO.properties) &&
                Objects.equals(ticketCapacity, deviceDTO.ticketCapacity) &&
                Objects.equals(projectId, deviceDTO.projectId) &&
                Objects.equals(slot, deviceDTO.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, creationTime, name, request, metaData, properties, ticketCapacity, projectId, slot);
    }
}
