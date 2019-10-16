package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import org.joda.time.DateTime;

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
    private Optional<BrowserSessionId> associatedBrowserSession;
    private Optional<TestRunId> associatedTestRun;

    // for jackson
    private ArtifactInfo() {}

    public ArtifactInfo(ArtifactId id, ArtifactType artifactType, ProjectId projectId, DateTime creationTime, Optional<DateTime> endTime, Optional<BrowserSessionId> associatedBrowserSession, Optional<TestRunId> associatedTestRun) {
        this.id = id;
        this.artifactType = artifactType;
        this.projectId = projectId;
        this.creationTime = creationTime;
        this.endTime = endTime;
        this.associatedBrowserSession = associatedBrowserSession;
        this.associatedTestRun = associatedTestRun;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactInfo artifact = (ArtifactInfo) o;
        return id.equals(artifact.id) &&
                artifactType.equals(artifact.artifactType) &&
                projectId.equals(artifact.projectId) &&
                creationTime.equals(artifact.creationTime) &&
                endTime.equals(artifact.endTime) &&
                associatedBrowserSession.equals(artifact.associatedBrowserSession) &&
                associatedTestRun.equals(artifact.associatedTestRun);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artifactType, projectId, creationTime, endTime, associatedBrowserSession, associatedTestRun);
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
}
