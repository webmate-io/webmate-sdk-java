package com.testfabrik.webmate.javasdk.jobs;

/**
 * Name of Job input or output port
 */
public class PortName {

    private String name;

    public PortName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortName portName = (PortName) o;
        return name != null ? name.equals(portName.name) : portName.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
