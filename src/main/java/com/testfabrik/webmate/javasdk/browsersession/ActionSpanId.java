package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Id of a ActionSpan.
 */
public class ActionSpanId {
    private final UUID value;

    /**
     * Create new ActionSpanId.
     * @param value uuid of test run id
     */
    @JsonCreator
    public ActionSpanId(final UUID value) {
        this.value = value;
    }

    /**
     * Create TestId from UUID represented as a String.
     */
    @JsonCreator
    static ActionSpanId of(final String idStr) {
        return new ActionSpanId(UUID.fromString(idStr));
    }

    @Override
    @JsonValue
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionSpanId testId = (ActionSpanId) o;

        return value.equals(testId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
