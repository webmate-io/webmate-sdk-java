package com.testfabrik.webmate.javasdk;

/**
 * WebmateCapabilityType defines custom webmate capabilities, analogously to org.openqa.selenium.remote.CapabilityType.
 */
public interface WebmateCapabilityType {

    /**
     * Email for the user account.
     */
    String USERNAME = "email";

    /**
     * Api key that is associated with the user account. Note that the email and
     * api key need to belong to the same user account.
     */
    String API_KEY = "apikey";

    /**
     * The project id of a project that is created for the passed email.
     */
    String PROJECT = "project";

    /**
     * Enable video recording.
     */
    String ENABLE_VIDEO_RECORDING = "wm:video";

    /**
     * Disable video recording.
     */
    String PREVENT_VIDEO_RECORDING = "wm:noVideo";

    String AUTOMATION_SCREENSHOTS = "wm:autoScreenshots";

    String TAGS = "wm:tags";

    String NAME = "wm:name";

}
