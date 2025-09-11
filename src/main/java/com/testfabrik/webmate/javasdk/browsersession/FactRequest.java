package com.testfabrik.webmate.javasdk.browsersession;

public class FactRequest {
    private FactType factType;
    private FactParams params;

    public FactRequest(FactType type) {
        this.factType = type;
    }

    public FactRequest(FactType type, FactParams params) {
        this.factType = type;
        this.params = params;
    }

    public FactType getFactType() {
        return factType;
    }

    public void setFactType(FactType type) {
        this.factType = type;
    }

    public FactParams getParams() {
        return params;
    }

    public void setParams(FactParams params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "FactRequest{" +
                "type=" + factType +
                ", params=" + params +
                '}';
    }


}
