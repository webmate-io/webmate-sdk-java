package com.testfabrik.webmate.javasdk.devices;

import com.sun.net.httpserver.HttpServer;
import com.testfabrik.webmate.javasdk.WebmateAPISession;
import com.testfabrik.webmate.javasdk.WebmateApiClientException;
import com.testfabrik.webmate.javasdk.WebmateAuthInfo;
import com.testfabrik.webmate.javasdk.WebmateEnvironment;
import com.testfabrik.webmate.javasdk.packagemgmt.PackageId;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DeviceClientInstallAppTest {

    private final DeviceId deviceId = new DeviceId(UUID.randomUUID());
    private final PackageId packageId = new PackageId(UUID.randomUUID());
    private final PackageId origPackageId = new PackageId(UUID.randomUUID());

    /** Installation states the fake device reports, one per device lookup; the last one is repeated. */
    private final LinkedList<String> reportedStates = new LinkedList<>();
    private final List<String> requirements = Collections.synchronizedList(new ArrayList<>());
    /** If true, the fake device also reports a finished installation of the same package from an earlier request. */
    private volatile boolean reportPreviousInstallation = false;

    private HttpServer server;
    private DeviceClient client;

    @Before
    public void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/package/packages/" + packageId, exchange -> respond(exchange,
                "{\"id\":\"" + packageId + "\",\"origPackageId\":\"" + origPackageId + "\",\"origPackageType\":\"apk\"}"));
        server.createContext("/device/devices/" + deviceId + "/requirements/propertyRequirement", exchange -> {
            requirements.add(IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8));
            respond(exchange, "{}");
        });
        server.createContext("/device/devices/" + deviceId, exchange -> {
            String state;
            synchronized (reportedStates) {
                state = reportedStates.size() > 1 ? reportedStates.removeFirst() : reportedStates.getFirst();
            }
            List<String> installations = new ArrayList<>();
            if (reportPreviousInstallation) {
                installations.add(installation(UUID.randomUUID().toString(), "finished"));
            }
            if (state != null) {
                installations.add(installation(requestedInstallationId(), state));
            }
            respond(exchange, "{\"id\":\"" + deviceId + "\",\"properties\":{\"package.installedPackages\":[" +
                    String.join(",", installations) + "]}}");
        });
        server.start();

        URI baseUri = URI.create("http://localhost:" + server.getAddress().getPort() + "/");
        WebmateAPISession session = new WebmateAPISession(new WebmateAuthInfo("token"), WebmateEnvironment.create(baseUri));
        client = session.device;
    }

    @After
    public void tearDown() {
        server.stop(0);
    }

    @Test
    public void installRequiresOriginalPackageAndDoesNotWaitByDefault() throws InterruptedException {
        reportedStates.add(null);

        client.installAppOnDevice(deviceId, packageId);

        assertEquals(1, requirements.size());
        assertTrue(requirements.get(0), requirements.get(0).contains("\"packageId\":\"" + origPackageId + "\""));
    }

    @Test
    public void installWaitsUntilDeviceReportsPackageAsInstalled() throws InterruptedException {
        reportedStates.add(null);
        reportedStates.add("requested");
        reportedStates.add("finished");

        client.installAppOnDevice(deviceId, packageId, false, true);

        synchronized (reportedStates) {
            assertEquals("finished", reportedStates.getFirst());
            assertEquals(1, reportedStates.size());
        }
    }

    @Test
    public void installFailsIfDeviceReportsFailedInstallation() throws InterruptedException {
        reportedStates.add("failed");

        try {
            client.installAppOnDevice(deviceId, packageId, false, true);
            fail("Expected the failed installation to be reported");
        } catch (WebmateApiClientException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("failed"));
        }
    }

    @Test
    public void installDoesNotMistakePreviousInstallationOfSamePackageForTheRequestedOne() throws InterruptedException {
        reportPreviousInstallation = true;
        reportedStates.add(null);
        reportedStates.add("requested");
        reportedStates.add("finished");

        client.installAppOnDevice(deviceId, packageId, false, true);

        synchronized (reportedStates) {
            assertEquals("finished", reportedStates.getFirst());
            assertEquals(1, reportedStates.size());
        }
    }

    private String installation(String installationId, String state) {
        return "{\"installationId\":\"" + installationId + "\",\"packageId\":\"" + origPackageId +
                "\",\"packageType\":\"apk\",\"state\":\"" + state + "\"}";
    }

    /** Installation id of the latest install requirement sent to the fake device. */
    private String requestedInstallationId() {
        synchronized (requirements) {
            String requirement = requirements.get(requirements.size() - 1);
            Matcher m = Pattern.compile("\"installationId\":\"([^\"]+)\"").matcher(requirement);
            assertTrue(requirement, m.find());
            return m.group(1);
        }
    }

    private static void respond(com.sun.net.httpserver.HttpExchange exchange, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
