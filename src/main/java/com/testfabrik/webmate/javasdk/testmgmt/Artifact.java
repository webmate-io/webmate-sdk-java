package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Objects;

/**
 * Result of a Test, e.g. a defect that has been found.
 */
public class Artifact {

    private ArtifactId id;
    private ArtifactType artifactType;
    private ProjectId projectId;
    private DateTime creationTime;
    private Optional<DateTime> endTime;
    private Optional<BrowserSessionId> associatedBrowserSession;
    private Optional<TestRunId> associatedTestRun;
    private ArtifactAssociation associations;
    private JsonNode data;

    // for jackson
    private Artifact() {}

    public Artifact(ArtifactId id, ArtifactType artifactType, ProjectId projectId, DateTime creationTime, Optional<DateTime> endTime, Optional<BrowserSessionId> associatedBrowserSession, Optional<TestRunId> associatedTestRun, ArtifactAssociation associations, JsonNode data) {
        this.id = id;
        this.artifactType = artifactType;
        this.projectId = projectId;
        this.creationTime = creationTime;
        this.endTime = endTime;
        this.associatedBrowserSession = associatedBrowserSession;
        this.associatedTestRun = associatedTestRun;
        this.associations = associations;
        this.data = data;
    }

    public ArtifactId getId() {
        return id;
    }

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public Optional<DateTime> getEndTime() {
        return endTime;
    }

    public Optional<BrowserSessionId> getAssociatedBrowserSession() {
        return associatedBrowserSession;
    }

    public Optional<TestRunId> getAssociatedTestRun() {
        return associatedTestRun;
    }

    public JsonNode getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return id.equals(artifact.id) &&
                artifactType.equals(artifact.artifactType) &&
                projectId.equals(artifact.projectId) &&
                creationTime.equals(artifact.creationTime) &&
                endTime.equals(artifact.endTime) &&
                associatedBrowserSession.equals(artifact.associatedBrowserSession) &&
                associatedTestRun.equals(artifact.associatedTestRun) &&
                data.equals(artifact.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artifactType, projectId, creationTime, endTime, associatedBrowserSession, associatedTestRun, data);
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "id=" + id +
                ", artifactType=" + artifactType +
                ", projectId=" + projectId +
                ", creationTime=" + creationTime +
                ", endTime=" + endTime +
                ", associatedBrowserSession=" + associatedBrowserSession +
                ", associatedTestRun=" + associatedTestRun +
                '}';
    }

    public ArtifactAssociation getAssociations() {
        return associations;
    }

    public void setAssociations(ArtifactAssociation associations) {
        this.associations = associations;
    }

    public static Artifact fromJsonString(String string) throws IOException {
        ObjectMapper mapper = JacksonMapper.getInstance();
        return mapper.readValue(string, Artifact.class);
    }
}
