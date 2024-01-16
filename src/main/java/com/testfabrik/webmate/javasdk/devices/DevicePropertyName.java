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

    PlatformType("machine.platform.type"),

    Model("machine.model"),
    Manufacturer("machine.manufacturer"),

    MachinePoolId("machine.pool.id"),
    MachineId("machine.id"),
    Annotations("machine.annotations"),
    UserAnnotations("machine.userAnnotations"),

    Language("os.language"),
    Locale("os.locale"),
    InstallPackages("os.canInstallPackages"),
    IsDebuggingVM("os.isDebuggingVM"),

    MaxExpeditionCapacity("automation.maxExpeditionCapacity"),
    MaxInstancesPerBrowser("automation.maxInstancesPerBrowser"),
    ActiveVehicles("automation.activeVehicles"),
    AutomationAvailable("automation.available"),

    SeleniumNode("selenium.node"),
    SeleniumGrid("service.seleniumGrid"),

    AppiumPort("appium.port"),

    CDPAvailable("cdp.available"),
    CDPPortRange("cdp.portRange"),
    CDPAssignedPorts("cdp.assignedPorts"),
    CommandExecutor("service.commandExecutor"),
    CommandExecutorState("command.executor.state"),
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
    VCloudTemplateGroup("vcloud.template.group"),
    VCloudTemplateCoordinates("vcloud.template.coordinates"),
    VCloudMachineState("vcloud.state"),
    VCloudFastProvisioned("vcloud.fastprovisioned"),
    VCloudVappId("vcloud.vappId"),
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

    NetworkReachability("network.reachability"),
    SetupDone("webmate.setup"),
    NetworkConnectivity("network.connectivity"),
    IpAddress("network.ip"),

    SimulateBandwidth("network.simulation.bandwidth"),
    SimulatePaketLoss("network.simulation.packetloss"),
    SimulateDisconnect("network.simulation.disconnect"),

    StartupScripts("webmate.startupScriptState"),
    MobileBridgeAutoConnect("mobileBridge.autoConnect"),
    MobileBridgeRunning("mobileBridge.running"),

    KeepDeviceDeployed("webmate.keepDeviceDeployed");
    public final String property;

    DevicePropertyName(String property) {
        this.property = property;
    }

    @JsonValue
    public String toValue() {
        return this.property;
    }

}
