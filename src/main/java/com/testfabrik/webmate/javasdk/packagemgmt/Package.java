package com.testfabrik.webmate.javasdk.packagemgmt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.UserId;
import com.testfabrik.webmate.javasdk.blobs.BlobId;
import com.google.common.base.Optional;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Objects;

/**
 * Result of a Test, e.g. a defect that has been found.
 */
public class Package {

    private PackageId id;
    private ProjectId projectId;
    private UserId creator;
    private DateTime creationTime;
    private String name;
    private String description;
    private String versionComment;
    private int index;
    private int amountOfVersions;
    private PackageId origPackageId;
    private String origPackageType;
    private BlobId origBlobId;
    private JsonNode origMetaData;
    private JsonNode origProperties;



    private Optional<PackageId> instrumentedPackageId;
    private Optional<String> instrumentedPackageType;
    private Optional<BlobId> instrumentedBlobId;

    // for jackson
    private Package() {}

    public Package(PackageId id, ProjectId projectId, UserId creator, DateTime creationTime, String name,
                   String description, String versionComment, int index, int amountOfVersions, PackageId origPackageId,
                   String origPackageType, BlobId origBlobId, JsonNode origMetaData, JsonNode origProperties,
                   Optional<PackageId> instrumentedPackageId, Optional<String> instrumentedPackageType,
                   Optional<BlobId> instrumentedBlobId) {
        this.id = id;
        this.projectId = projectId;
        this.creator = creator;
        this.creationTime = creationTime;
        this.name = name;
        this.description = description;
        this.versionComment = versionComment;
        this.index = index;
        this.amountOfVersions = amountOfVersions;
        this.origPackageId = origPackageId;
        this.origPackageType = origPackageType;
        this.origBlobId = origBlobId;
        this.origMetaData = origMetaData;
        this.origProperties = origProperties;
        this.instrumentedPackageId = instrumentedPackageId;
        this.instrumentedPackageType = instrumentedPackageType;
        this.instrumentedBlobId = instrumentedBlobId;
    }


    public void setId(PackageId id) {
        this.id = id;
    }

    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
    }

    public void setCreator(UserId creator) {
        this.creator = creator;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersionComment(String versionComment) {
        this.versionComment = versionComment;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setAmountOfVersions(int amountOfVersions) {
        this.amountOfVersions = amountOfVersions;
    }

    public void setOrigPackageId(PackageId origPackageId) {
        this.origPackageId = origPackageId;
    }

    public void setOrigPackageType(String origPackageType) {
        this.origPackageType = origPackageType;
    }

    public void setOrigBlobId(BlobId origBlobId) {
        this.origBlobId = origBlobId;
    }

    public void setOrigMetaData(JsonNode origMetaData) {
        this.origMetaData = origMetaData;
    }

    public void setOrigProperties(JsonNode origProperties) {
        this.origProperties = origProperties;
    }

    public void setInstrumentedPackageId(Optional<PackageId> instrumentedPackageId) {
        this.instrumentedPackageId = instrumentedPackageId;
    }

    public void setInstrumentedPackageType(Optional<String> instrumentedPackageType) {
        this.instrumentedPackageType = instrumentedPackageType;
    }

    public void setInstrumentedBlobId(Optional<BlobId> instrumentedBlobId) {
        this.instrumentedBlobId = instrumentedBlobId;
    }

    public PackageId getId() {
        return id;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getCreator() {
        return creator;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersionComment() {
        return versionComment;
    }

    public int getIndex() {
        return index;
    }

    public int getAmountOfVersions() {
        return amountOfVersions;
    }

    public PackageId getOrigPackageId() {
        return origPackageId;
    }

    public String getOrigPackageType() {
        return origPackageType;
    }

    public BlobId getOrigBlobId() {
        return origBlobId;
    }

    public JsonNode getOrigMetaData() {
        return origMetaData;
    }

    public JsonNode getOrigProperties() {
        return origProperties;
    }

    public Optional<PackageId> getInstrumentedPackage() {
        return instrumentedPackageId;
    }

    public Optional<BlobId> getInstrumentedBlob() {
        return instrumentedBlobId;
    }

    public Optional<String> getInstrumentedPackageType() {return instrumentedPackageType;}

    @Override
    public String toString() {
        return "Package{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", creator=" + creator +
                ", creationTime=" + creationTime +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", versionComment='" + versionComment + '\'' +
                ", index=" + index +
                ", amountOfVersions=" + amountOfVersions +
                ", origPackageId=" + origPackageId +
                ", origPackageType='" + origPackageType + '\'' +
                ", origBlobId=" + origBlobId +
                ", origMetaData=" + origMetaData +
                ", origProperties=" + origProperties +
                ", instrumentedPackageId=" + instrumentedPackageId +
                ", instrumentedPackageType=" + instrumentedPackageType +
                ", instrumentedBlobId=" + instrumentedBlobId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return index == aPackage.index &&
                amountOfVersions == aPackage.amountOfVersions &&
                id.equals(aPackage.id) &&
                projectId.equals(aPackage.projectId) &&
                creator.equals(aPackage.creator) &&
                creationTime.equals(aPackage.creationTime) &&
                name.equals(aPackage.name) &&
                description.equals(aPackage.description) &&
                versionComment.equals(aPackage.versionComment) &&
                origPackageId.equals(aPackage.origPackageId) &&
                origPackageType.equals(aPackage.origPackageType) &&
                origBlobId.equals(aPackage.origBlobId) &&
                origMetaData.equals(aPackage.origMetaData) &&
                origProperties.equals(aPackage.origProperties) &&
                Objects.equals(instrumentedPackageId, aPackage.instrumentedPackageId) &&
                Objects.equals(instrumentedPackageType, aPackage.instrumentedPackageType) &&
                Objects.equals(instrumentedBlobId, aPackage.instrumentedBlobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, creator, creationTime, name, description, versionComment, index, amountOfVersions, origPackageId, origPackageType, origBlobId, origMetaData, origProperties, instrumentedPackageId, instrumentedPackageType, instrumentedBlobId);
    }

    public static Package fromJsonString(String string) throws IOException {
        ObjectMapper mapper = JacksonMapper.getInstance();
        return mapper.readValue(string, Package.class);
    }
}
