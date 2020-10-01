package com.testfabrik.webmate.javasdk.testmgmt;

import java.util.UUID;

public class ArtifactAssociation {

    private UUID id;
    private ArtifactAssociationType associationType;
    private AssociationRole relationName;

    // For jackson
    private ArtifactAssociation() {}

    private ArtifactAssociation(UUID id, ArtifactAssociationType associationType, AssociationRole relationName) {
        this.id = id;
        this.associationType = associationType;
        this.relationName = relationName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ArtifactAssociationType getAssociationType() {
        return associationType;
    }

    public void setAssociationType(ArtifactAssociationType associationType) {
        this.associationType = associationType;
    }

    public AssociationRole getRelationName() {
        return relationName;
    }

    public void setRelationName(AssociationRole relationName) {
        this.relationName = relationName;
    }

}
