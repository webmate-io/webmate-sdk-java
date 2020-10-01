package com.testfabrik.webmate.javasdk.testmgmt;

import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.ProjectId;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Objects;

/**
 * Result of a Test, e.g. a defect that has been found.
 */
public class ArtifactInfo {

    private ArtifactId id;
    private ArtifactType artifactType;
    private ProjectId projectId;
    private DateTime creationTime;
    private Optional<DateTime> endTime;
    private List<ArtifactAssociation> associations;

    // For jackson
    private ArtifactInfo() {}

    public ArtifactInfo(ArtifactId id,
                        ArtifactType artifactType,
                        ProjectId projectId,
                        DateTime creationTime,
                        Optional<DateTime> endTime,
                        List<ArtifactAssociation> associations) {
        this.id = id;
        this.artifactType = artifactType;
        this.projectId = projectId;
        this.creationTime = creationTime;
        this.endTime = endTime;
        this.associations = associations;
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

    public List<ArtifactAssociation> getAssociations() {
        return associations;
    }

    public void setAssociations(List<ArtifactAssociation> associations) {
        this.associations = associations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactInfo artifact = (ArtifactInfo) o;
        return id.equals(artifact.id) &&
                artifactType.equals(artifact.artifactType) &&
                projectId.equals(artifact.projectId) &&
                creationTime.equals(artifact.creationTime) &&
                endTime.equals(artifact.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artifactType, projectId, creationTime, endTime, associations);
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

}
