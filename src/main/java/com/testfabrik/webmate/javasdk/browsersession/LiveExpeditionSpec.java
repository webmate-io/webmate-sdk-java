package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

/**
 * A specification of an expedition that may still be executed. The expedition uses a
 * Vehicle, e.g. a Browser or mobile App, and is controlled by a Driver, e.g. a URL list,
 * a Selenium script, or a user in an interactive session.
 */
public class LiveExpeditionSpec implements ExpeditionSpec {
    private DriverSpecification driverSpec;
    private VehicleSpecification vehicleSpec;

    /**
     * Create LiveExpeditionSpec from a driver specification and a vehicle specification.
     */
    public LiveExpeditionSpec(final DriverSpecification driverSpec, final VehicleSpecification vehicleSpec) {
        this.driverSpec = driverSpec;
        this.vehicleSpec = vehicleSpec;
    }

    public DriverSpecification getDriverSpec() {
        return driverSpec;
    }

    public VehicleSpecification getVehicleSpec() {
        return vehicleSpec;
    }

    @Override
    public String toString() {
        return "LiveExpeditionSpec{" +
                "driverSpec=" + driverSpec +
                ", vehicleSpec=" + vehicleSpec +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiveExpeditionSpec that = (LiveExpeditionSpec) o;
        return Objects.equals(driverSpec, that.driverSpec) &&
                Objects.equals(vehicleSpec, that.vehicleSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverSpec, vehicleSpec);
    }
}
