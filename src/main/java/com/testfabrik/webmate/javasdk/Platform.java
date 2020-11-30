package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Platform {

    /**
     * Defines the platform type, like Windows, MacOS, Android, iOS or Ubuntu.
     * See com.testfabrik.webmate.javasdk.PlatformType for a comprehensive list.
     */
    private String platformType;
    private String platformVersion;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platformArchitecture;

    public Platform(String platformType, String platformVersion) {
        this.platformType = platformType;
        this.platformVersion = platformVersion;
    }

    public Platform(String platformType, String platformVersion, String platformArchitecture) {
        this.platformType = platformType;
        this.platformVersion = platformVersion;
        this.platformArchitecture = platformArchitecture;
    }

    @Override
    public String toString() {
        String suffix = platformArchitecture == null ? "" : "_" + platformArchitecture;
        return platformType + "_" + platformVersion + suffix;
    }

    /**
     * Create Platform instance from ("legacy") platform string, e.g. WINDOWS_10_64
     */
    public static Platform fromString(String opaqueString) {
        String pattern = "^([^_]+)_([^_]+)(_([^_]+))?$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(opaqueString);

        if (m.find()) {
            Optional<String> architecture = Optional.ofNullable(m.group(4));
            return architecture
                    .map(s -> new Platform(m.group(1), m.group(2), s))
                    .orElseGet(() -> new Platform(m.group(1), m.group(2)));
        } else {
           throw new WebmateApiClientException("Invalid platform string [" + opaqueString + "]. Must look like <platformType>_<version>[_<architecture>].");
        }
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getPlatformArchitecture() {
        return platformArchitecture;
    }
}
