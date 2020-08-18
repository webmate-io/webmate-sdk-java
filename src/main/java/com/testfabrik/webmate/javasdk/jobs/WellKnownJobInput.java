package com.testfabrik.webmate.javasdk.jobs;

import java.util.Map;

/**
 * This interface is implemented by instantiation helpers for concrete JobConfigs.
 */
public interface WellKnownJobInput {
    JobConfigName getName();

    /**
     * Create Job input values.
     * @return input values map for concrete WellKnownJob.
     */
    Map<PortName, WMValue> makeInputValues();
}

