package com.testfabrik.webmate.javasdk.jobs;

import com.fasterxml.jackson.annotation.JsonValue;

public class BrickDataType {


    private String tpe;

    public BrickDataType(String tpe) {
        this.tpe = tpe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrickDataType dataType = (BrickDataType) o;
        return tpe != null ? tpe.equals(dataType.tpe) : dataType.tpe == null;
    }

    @Override
    public int hashCode() {
        return tpe != null ? tpe.hashCode() : 0;
    }

    @JsonValue
    public String getTpe() {
        return tpe;
    }

    public void setTpe(String tpe) {
        this.tpe = tpe;
    }

    @Override
    public String toString() {
        return tpe;
    }

}
