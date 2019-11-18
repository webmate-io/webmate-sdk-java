package com.testfabrik.webmate.javasdk.mailtest;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class TestMailAddress {

    private String address;

    public TestMailAddress(String address) {
        this.address = address;
    }

    @JsonValue
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        return obj instanceof TestMailAddress && Objects.equals(address, ((TestMailAddress) obj).address);
    }
}
