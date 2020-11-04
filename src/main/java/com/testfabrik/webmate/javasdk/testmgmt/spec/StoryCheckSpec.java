package com.testfabrik.webmate.javasdk.testmgmt.spec;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.testfabrik.webmate.javasdk.Tag;
import com.testfabrik.webmate.javasdk.WebmateAPISession;
import com.testfabrik.webmate.javasdk.WebmateApiClientException;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;
import com.testfabrik.webmate.javasdk.jobs.WMValueFactory;
import com.testfabrik.webmate.javasdk.testmgmt.*;
import com.testfabrik.webmate.javasdk.testmgmt.testtypes.StandardTestTypes;
import com.testfabrik.webmate.javasdk.testmgmt.testtypes.TestType;

import java.util.List;

/**
 * A StoryCheckSpec is a test type that sits on an existing Expedition, e.g. a Selenium or Appium Session, and allows
 * adding test results or having an individual overall execution result. It can be used when your Expedition
 * is used for multiple tests, e.g. when you create a single driver in your test set up code and reuse that
 * driver in multiple tests, features, specs, etc.
 *
 * The {@code executionName} is the name that is shown in your reports. You can set it to whatever helps you
 * identifying or grouping the test execution later, such as the feature or method name.
 */
public class StoryCheckSpec extends TestExecutionSpec {

    private BrowserSessionId expeditionId;

    private StoryCheckSpec(String executionName, List<Tag> tags, List<ApplicationModelId> models,
                           List<TestSessionId> testSessions, BrowserSessionId expeditionId) {
        super(executionName, TestType.of(StandardTestTypes.StoryCheck.getTestType()), "Default StoryCheck", tags, models, testSessions);
        this.expeditionId = expeditionId;
    }

    @Override
    public TestMgmtClient.SingleTestRunCreationSpec makeTestRunCreationSpec() {
        return new TestMgmtClient.SingleTestRunCreationSpec(ImmutableMap.of(
                "expeditionId", WMValueFactory.makeExpeditionId(this.expeditionId))
        );
    }

    public static class StoryCheckBuilder extends TestExecutionSpecBuilder<StoryCheckBuilder> {
        private String storyName;

        private StoryCheckBuilder(final String storyName) {
            this.storyName = storyName;
        }

        public static StoryCheckBuilder builder(String storyName) {
            return new StoryCheckBuilder(storyName);
        }

        @Override
        public StoryCheckSpec build() {
            WebmateAPISession session;
            if (optSession.isPresent()) {
              session = optSession.get();
            } else {
                throw new WebmateApiClientException("Session not available. This is an internal error.");
            }

            List<BrowserSessionId> expeditions = session.getAssociatedExpeditions();
            if (expeditions.size() != 1) {
                throw new WebmateApiClientException("The webmate session must be associated with exactly one expedition. Currently there are " + expeditions.size() + ".");
            }

            List<TestSessionId> allTestSessionsToAssociate = Lists.newArrayList(Iterables.concat(session.getAssociatedTestSessions(),
                    this.testSessionIds));

            List<ApplicationModelId> allModels = Lists.newArrayList(Iterables.concat(session.getAssociatedModels(),
                    this.models));

            List<Tag> allTags = Lists.newArrayList(Iterables.concat(session.getAssociatedTags(),
                    this.tags));

            return new StoryCheckSpec(this.storyName, allTags, allModels, allTestSessionsToAssociate, expeditions.get(0));
        }
    }
}
