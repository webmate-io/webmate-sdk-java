package com.testfabrik.webmate.javasdk.testmgmt;

import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.Tag;
import com.testfabrik.webmate.javasdk.WebmateAPISession;

import java.util.ArrayList;
import java.util.List;

public abstract class TestExecutionSpecBuilder<T extends TestExecutionSpecBuilder<T>> {

    protected List<Tag> tags;
    protected List<ApplicationModelId> models;
    protected List<TestSessionId> testSessionIds;
    protected Optional<WebmateAPISession> optSession = Optional.absent();

    protected TestExecutionSpecBuilder() {
        this.tags = new ArrayList<>();
        this.models = new ArrayList<>();
        this.testSessionIds = new ArrayList<>();
    }

   public TestExecutionSpecBuilder<T> withTag(Tag tag) {
       this.tags.add(tag);
       return this;
   }

    public TestExecutionSpecBuilder<T> withTag(String tagName) {
        this.tags.add(new Tag(tagName));
        return this;
    }

    public TestExecutionSpecBuilder<T> withTag(String tagName, String value) {
        this.tags.add(new Tag(tagName, value));
        return this;
    }

   public TestExecutionSpecBuilder<T> withModel(ApplicationModelId model) {
        this.models.add(model);
        return this;
    }

    public TestExecutionSpecBuilder<T> withModel(String modelId) {
        this.models.add(ApplicationModelId.of(modelId));
        return this;
    }

    public TestExecutionSpecBuilder<T> inTestSession(TestSessionId sessionId) {
        this.testSessionIds.add(sessionId);
        return this;
    }

    public TestExecutionSpecBuilder<T> setApiSession(WebmateAPISession session) {
       this.optSession = Optional.fromNullable(session);
       return this;
    }

    public abstract TestExecutionSpec build();
}
