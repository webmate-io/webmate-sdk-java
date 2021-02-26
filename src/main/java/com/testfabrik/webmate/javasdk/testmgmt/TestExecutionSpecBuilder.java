package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.Tag;
import com.testfabrik.webmate.javasdk.WebmateAPISession;
import com.testfabrik.webmate.javasdk.testmgmt.spec.TestExecutionSpec;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TestExecutionSpecBuilder<T extends TestExecutionSpecBuilder<T>> {

    protected List<Tag> tags;
    protected List<ApplicationModelId> models;
    protected List<TestSessionId> testSessionIds;
    protected Optional<WebmateAPISession> optSession = Optional.empty();

    protected TestExecutionSpecBuilder() {
        this.tags = new ArrayList<>();
        this.models = new ArrayList<>();
        this.testSessionIds = new ArrayList<>();
    }

    /**
     * Add a Tag corresponding to the current date, e.g. "2020-11-10".
     */
    public TestExecutionSpecBuilder<T> withCurrentDateAsTag() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.tags.add(new Tag(DateTime.now().toString(dtf)));
        return this;
    }

    public TestExecutionSpecBuilder<T> withTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    /**
     * Add a Tag corresponding to the given string.
     */
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
        this.models.add(new ApplicationModelId(modelId));
        return this;
    }

    public TestExecutionSpecBuilder<T> inTestSession(TestSessionId sessionId) {
        this.testSessionIds.add(sessionId);
        return this;
    }

    public TestExecutionSpecBuilder<T> setApiSession(WebmateAPISession session) {
       this.optSession = Optional.ofNullable(session);
       return this;
    }

    public abstract TestExecutionSpec build();
}
