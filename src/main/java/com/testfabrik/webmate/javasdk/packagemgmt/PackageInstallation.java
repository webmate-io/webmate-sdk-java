package com.testfabrik.webmate.javasdk.packagemgmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageInstallation {

    private UUID installationId;
    private PackageId packageId;
    private String packageType;
    private PackageInstallationState state;

    // Constructor
    @JsonCreator
    public PackageInstallation(
            @JsonProperty("installationId") UUID installationId,
            @JsonProperty("packageId") PackageId packageId,
            @JsonProperty("packageType") String packageType,
            @JsonProperty("state") PackageInstallationState state
    ) {
        this.installationId = installationId;
        this.packageId = packageId;
        this.packageType = packageType;
        this.state = state;
    }

    @Override
    public String toString() {
        return "PackageInstallation{" +
                "installationId=" + installationId +
                ", packageId=" + packageId +
                ", packageType=" + packageType +
                ", state=" + state +
                '}';
    }

    public UUID getInstallationId() {
        return installationId;
    }

    public PackageId getPackageId() {
        return packageId;
    }

    public String getPackageType() {
        return packageType;
    }

    public PackageInstallationState getState() {
        return state;
    }


    // toJson method returning JsonNode
    public JsonNode toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.valueToTree(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return objectMapper.createObjectNode(); // Return empty JsonNode if serialization fails
        }
    }
}


