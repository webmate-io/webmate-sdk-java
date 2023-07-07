package com.testfabrik.webmate.javasdk.mailtest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.UserId;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunId;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestMailAccount {

    private TestMailAddress emailAddress;
    private ProjectId projectId;

    private Optional<TestRunId> optTestRunId = Optional.absent();

    private UserId creator;

    public TestMailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(TestMailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
    }

    public Optional<TestRunId> getOptTestRunId() {
        return optTestRunId;
    }

    public void setOptTestRunId(Optional<TestRunId> optTestRunId) {
        this.optTestRunId = optTestRunId;
    }

    public UserId getCreator() {
        return creator;
    }

    public void setCreator(UserId creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestMailAccount that = (TestMailAccount) o;
        return emailAddress.equals(that.emailAddress) && projectId.equals(that.projectId) && optTestRunId.equals(that.optTestRunId) && creator.equals(that.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, projectId, optTestRunId, creator);
    }

    @Override
    public String toString() {
        return "TestMailAccount{" +
                "emailAddress=" + emailAddress +
                ", projectId=" + projectId +
                ", optTestRunId=" + optTestRunId +
                ", creator=" + creator +
                '}';
    }
}
