package com.testfabrik.webmate.javasdk.jobs;

import java.util.Map;

/**
 * Wraps parameters for a Job.
 */
public class JobParams {
    public final Map<String, String> params;

    public JobParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobParams jobParams = (JobParams) o;
        return params.equals(jobParams.params);
    }

    @Override
    public int hashCode() {
        return params.hashCode();
    }

}
