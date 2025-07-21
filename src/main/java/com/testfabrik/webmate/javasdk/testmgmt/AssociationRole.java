package com.testfabrik.webmate.javasdk.testmgmt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AssociationRole {
    PartOf("PartOf"),
    @JsonEnumDefaultValue Unknown("Unknown");

    public final String associationRole;

    AssociationRole(String associationRole) {
        this.associationRole = associationRole;
    }
}
