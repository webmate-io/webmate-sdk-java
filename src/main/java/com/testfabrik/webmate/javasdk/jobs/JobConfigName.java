package com.testfabrik.webmate.javasdk.jobs;

/**
 * Wraps a JobConfigName.
 */
public class JobConfigName {
    public final String jobConfigName;

    public JobConfigName(String jobConfigName) {
        this.jobConfigName = jobConfigName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobConfigName jobConfigName1 = (JobConfigName) o;
        return jobConfigName.equals(jobConfigName1.jobConfigName);
    }

    @Override
    public int hashCode() {
        return jobConfigName.hashCode();
    }
}