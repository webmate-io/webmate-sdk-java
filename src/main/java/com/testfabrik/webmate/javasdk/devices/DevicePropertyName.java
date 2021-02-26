package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DevicePropertyName {
    ProviderType("webmate.providerType"),
    ProviderId("webmate.providerId"),
    SlotId("webmate.slotId"),
    DeviceId("webmate.deviceId"),
    DeviceOfferId("webmate.deviceOfferId"),

    Unavailable("webmate.deviceUnavailable"),

    Browsers("machine.browsers"),
    Platform("machine.platform"),
    Model("machine.model"),
    Manufacturer("machine.manufacturer"),

    MachinePoolId("machine.pool.id"),

    Language("os.language"),
    Locale("os.locale"),
    InstallPackages("os.canInstallPackages"),

    MaxExpeditionCapacity("automation.maxExpeditionCapacity"),
    MaxInstancesPerBrowser("automation.maxInstancesPerBrowser"),

    AutomationAvailable("automation.available"),

    SeleniumNode("selenium.node"),
    SeleniumGrid("service.seleniumGrid"),

    CommandExecutor("service.commandExecutor"),
    ClipboardAccess("command.clipboardAccess"),
    ClipboardContent("clipboard.lastKnownContent"),

    DevtoolsPort("devtools-ports"),

    ExternalRotation("environment.externalRotation"),
    ExternalTranslation("environment.externalTranslation"),
    ExternalLocation("environment.externalLocation"),

    CreateSnapshot("state.createSnapshot"),
    RevertToSnapshot("state.revertToSnapshot"),
    LastSnapshot("state.lastSnapshot"),

    Reset("state.reset"),
    Reconnect("state.reconnect"),

    ReadOnlyConsole("console.readOnly"),
    InteractiveConsole("console.interactive"),
    ChangeKeyboardLayout("console.changeKeyboardLayout"),
    CreateThumbnail("console.createThumbnail"),
    ChangeResolution("console.changeResolution"),
    Resolution("console.resolution"),

    VCloudTemplateId("vcloud.templateId"),
    VCloudMachineState("vcloud.state"),
    OpenSTFMachineId("openstf.machineId"),
    OpenSTFMachinePort("openstf.machinePort"),
    OpenSTFSerial("openstf.serial"),

    BiometricAuthentication("sensorsimulation.acceptBiometricAuthentication"),
    SimulateBiometrics("sensorsimulation.simulateBiometrics"),
    SimulateCamera("sensorsimulation.simulateCamera"),
    SimulateGPS("sensorsimulation.gps"),
    MediaSettings("settings.media"),
    WebView("simulation.simulateWebView"),
    SimCardInfo("openstf.simcardinfo"),

    RecordNetworkTraffic("network.recordNetworkTraffic"),
    ThrottleNetworkTraffic("network.throttleNetworkTraffic"),
    ToggleNetwork("network.toggle"),
    ModifyRequest("network.modifyRequest"),
    ModifyResponse("network.modifyResponse"),

    NetworkConnectivity("network.connectivity"),
    IpAddress("network.ip");

    public final String property;

    DevicePropertyName(String property) {
        this.property = property;
    }

    @JsonValue
    public String toValue() {
        return this.property;
    }

}
