package com.testfabrik.webmate.javasdk;

/**
 * WebmateCapabilityType defines the capabilities that a user needs to use when creating
 * a session. The capabilities need to be added to the desired capabilities, analogously to
 * org.openqa.selenium.remote.CapabilityType.
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
}
