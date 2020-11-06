package com.testfabrik.webmate.javasdk.testmgmt.spec;

import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.Tag;
import com.testfabrik.webmate.javasdk.jobs.WMValueFactory;
import com.testfabrik.webmate.javasdk.testmgmt.ApplicationModelId;
import com.testfabrik.webmate.javasdk.testmgmt.TestMgmtClient;
import com.testfabrik.webmate.javasdk.testmgmt.TestSessionId;
import com.testfabrik.webmate.javasdk.testmgmt.testtypes.StandardTestTypes;
import com.testfabrik.webmate.javasdk.testmgmt.testtypes.TestType;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class ExpeditionComparisonSpec extends TestExecutionSpec {

    private BrowserSpecification referenceBrowser;
    private List<BrowserSpecification> comparisonBrowsers;
    private List<URI> urls;

    public ExpeditionComparisonSpec(String executionName, BrowserSpecification referenceBrowser,
                                    List<BrowserSpecification> comparisonBrowsers, List<URI> urls) {
        this(executionName, Collections.<Tag>emptyList(), referenceBrowser, comparisonBrowsers, urls);
    }

    public ExpeditionComparisonSpec(String executionName, List<Tag> tags, BrowserSpecification referenceBrowser,
                                    List<BrowserSpecification> comparisonBrowsers, List<URI> urls) {
        this(executionName, tags, Collections.<ApplicationModelId>emptyList(), Collections.<TestSessionId>emptyList(),
                referenceBrowser, comparisonBrowsers, urls);
    }

    public ExpeditionComparisonSpec(String executionName, List<Tag> tags, List<ApplicationModelId> models,
                                    List<TestSessionId> associatedTestSessions, BrowserSpecification referenceBrowser,
                                    List<BrowserSpecification> comparisonBrowsers, List<URI> urls) {
        super(executionName, TestType.of(StandardTestTypes.ExpeditionComparison.getTestType()), "Default Expedition Comparison Test", tags, models, associatedTestSessions);
        this.referenceBrowser = referenceBrowser;
        this.comparisonBrowsers = comparisonBrowsers;
        this.urls = urls;
    }

    @Override
    public TestMgmtClient.SingleTestRunCreationSpec makeTestRunCreationSpec() {
        return new TestMgmtClient.SingleTestRunCreationSpec(ImmutableMap.of(
                "referenceExpeditionSpec", WMValueFactory.makeExpeditionSpec(referenceBrowser, urls),
                "comparisonExpeditionSpecs", WMValueFactory.makeExpeditionSpecs(comparisonBrowsers, urls)
        ));
    }
}
