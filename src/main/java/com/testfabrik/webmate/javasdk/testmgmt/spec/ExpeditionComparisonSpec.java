package com.testfabrik.webmate.javasdk.testmgmt.spec;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.testfabrik.webmate.javasdk.Tag;
import com.testfabrik.webmate.javasdk.WebmateAPISession;
import com.testfabrik.webmate.javasdk.WebmateApiClientException;
import com.testfabrik.webmate.javasdk.browsersession.ExpeditionSpec;
import com.testfabrik.webmate.javasdk.jobs.WMValueFactory;
import com.testfabrik.webmate.javasdk.testmgmt.ApplicationModelId;
import com.testfabrik.webmate.javasdk.testmgmt.TestExecutionSpecBuilder;
import com.testfabrik.webmate.javasdk.testmgmt.TestMgmtClient;
import com.testfabrik.webmate.javasdk.testmgmt.TestSessionId;
import com.testfabrik.webmate.javasdk.testmgmt.testtypes.StandardTestTypes;

import java.util.List;

/**
 * A TestRun specification for a Test of type "ExpeditionComparison".
 */
public class ExpeditionComparisonSpec extends TestExecutionSpec {

    private ExpeditionSpec referenceSpec;
    private List<ExpeditionSpec> compareSpecs;

    private ExpeditionComparisonSpec(String executionName, List<Tag> tags, List<ApplicationModelId> models,
                                     List<TestSessionId> testSessions, ExpeditionSpec referenceSpec,
                                     List<ExpeditionSpec> compareSpecs) {
            super(executionName, StandardTestTypes.ExpeditionComparison.getTestType(),
                    "Default Cross-Browser Test", tags, models, testSessions);

            this.referenceSpec = referenceSpec;
            this.compareSpecs = compareSpecs;
    }

    @Override
    public TestMgmtClient.SingleTestRunCreationSpec makeTestRunCreationSpec() {
        return new TestMgmtClient.SingleTestRunCreationSpec(ImmutableMap.of(
                "referenceExpeditionSpec", WMValueFactory.makeExpeditionSpec(referenceSpec),
                "comparisonExpeditionSpecs", WMValueFactory.makeExpeditionSpecList(compareSpecs)
        ));
    }

    /**
     * Builder that can/should be used for creating the specification.
     */
    public static class ExpeditionComparisonCheckBuilder extends TestExecutionSpecBuilder<ExpeditionComparisonCheckBuilder> {
        private final String executionName;
        private final ExpeditionSpec referenceSpec;
        private final List<ExpeditionSpec> compareSpecs;

        private ExpeditionComparisonCheckBuilder(final String executionName, final ExpeditionSpec referenceSpec,
                                                 final List<ExpeditionSpec> compareSpecs) {
            this.executionName = executionName;
            this.referenceSpec = referenceSpec;
            this.compareSpecs = ImmutableList.copyOf(compareSpecs);
        }

        public static ExpeditionComparisonCheckBuilder builder(final String executionName, final ExpeditionSpec referenceSpec,
                                                         final List<ExpeditionSpec> compareSpecs) {
            return new ExpeditionComparisonCheckBuilder(executionName, referenceSpec, compareSpecs);
        }

        @Override
        public ExpeditionComparisonSpec build() {
            WebmateAPISession session;
            if (optSession.isPresent()) {
                session = optSession.get();
            } else {
                throw new WebmateApiClientException("Session not available. This is an internal error.");
            }

            List<TestSessionId> allTestSessionsToAssociate = Lists.newArrayList(Iterables.concat(session.getAssociatedTestSessions(),
                    this.testSessionIds));

            List<ApplicationModelId> allModels = Lists.newArrayList(Iterables.concat(session.getAssociatedModels(),
                    this.models));

            List<Tag> allTags = Lists.newArrayList(Iterables.concat(session.getAssociatedTags(),
                    this.tags));

            return new ExpeditionComparisonSpec(this.executionName, allTags, allModels, allTestSessionsToAssociate, referenceSpec, compareSpecs);
        }
    }
}
