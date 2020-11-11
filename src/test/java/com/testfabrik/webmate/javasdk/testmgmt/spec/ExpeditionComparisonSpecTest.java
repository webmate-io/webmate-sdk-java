package com.testfabrik.webmate.javasdk.testmgmt.spec;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.testfabrik.webmate.javasdk.*;
import com.testfabrik.webmate.javasdk.browsersession.ExpeditionSpecFactory;
import com.testfabrik.webmate.javasdk.testmgmt.testtypes.StandardTestTypes;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ExpeditionComparisonSpecTest {

    private TestExecutionSpec makeTestSpec() throws URISyntaxException {
        WebmateAPISession session = new WebmateAPISession(new WebmateAuthInfo("email", "apikey"),
                WebmateEnvironment.create(new URI("http://localhost")));

       return ExpeditionComparisonSpec.ExpeditionComparisonCheckBuilder.builder(
            "test name",
            ExpeditionSpecFactory.makeUrlListExpeditionSpec(
                    ImmutableList.of(new URI("http://bla")),
                    new Browser(BrowserType.Chrome, "83", "WINDOWS_10_64")),
            ImmutableList.of(
                    ExpeditionSpecFactory.makeUrlListExpeditionSpec(
                            ImmutableList.of(new URI("http://bla")),
                            new Browser(BrowserType.Chrome, "83", "WINDOWS_10_64"))
            )).setApiSession(session).build();
    }

    public ExpeditionComparisonSpecTest() throws URISyntaxException {}

    @Test
    public void testIfSpecCanBeCreatedWithBuilder() throws URISyntaxException {

        TestExecutionSpec spec = makeTestSpec();
        assertEquals(StandardTestTypes.ExpeditionComparison.getTestType(), spec.testType);
        assertEquals(Lists.newArrayList(), spec.associatedTestSessions);
        assertEquals("test name", spec.executionName);
    }

    @Test
    public void testIfSingleTestRunSpecIsCorrect() throws URISyntaxException, IOException {

        TestExecutionSpec spec = makeTestSpec();

        String expected = IOUtils.toString(this.getClass().getResourceAsStream("/expeditionComparisonSingleTestRunSpec.json"));
        assertEquals(expected.replaceAll("\\s+", ""),
                spec.makeTestRunCreationSpec().asJson().toPrettyString().replaceAll("\\s+", ""));
    }
}
