package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.JacksonMapper;
import com.testfabrik.webmate.javasdk.ProjectId;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
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
    private List<ArtifactAssociation> associations;
    private JsonNode data;

    // For jackson
    private Artifact() {}

    public Artifact(ArtifactId id,
                    ArtifactType artifactType,
                    ProjectId projectId,
                    DateTime creationTime,
                    Optional<DateTime> endTime,
                    List<ArtifactAssociation> associations,
                    JsonNode data) {
        this.id = id;
        this.artifactType = artifactType;
        this.projectId = projectId;
        this.creationTime = creationTime;
        this.endTime = endTime;
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
                associations.equals(artifact.associations) &&
                data.equals(artifact.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artifactType, projectId, creationTime, endTime, associations, data);
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "id=" + id +
                ", artifactType=" + artifactType +
                ", projectId=" + projectId +
                ", creationTime=" + creationTime +
                ", endTime=" + endTime +
                ", associations=" + associations +
                '}';
    }

    public static Artifact fromJsonString(String string) throws IOException {
        ObjectMapper mapper = JacksonMapper.getInstance();
        return mapper.readValue(string, Artifact.class);
    }

    public List<ArtifactAssociation> getAssociations() {
        return associations;
    }

    public void setAssociations(List<ArtifactAssociation> associations) {
        this.associations = associations;
    }
}
