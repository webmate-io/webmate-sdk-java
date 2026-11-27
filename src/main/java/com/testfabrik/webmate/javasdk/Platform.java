package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Platform {

    private static final Pattern PLATFORM_PATTERN = Pattern.compile("^([^_]+)(?:_([^_]+)(?:_([^_]+))?)?$");

    /**
     * Defines the platform type, like Windows, MacOS, Android, iOS or Ubuntu.
     * See com.testfabrik.webmate.javasdk.PlatformType for a comprehensive list.
     */
    private String platformType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platformVersion;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platformArchitecture;

    public Platform(String platformType, String platformVersion) {
        this(platformType, platformVersion, null);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Platform(@JsonProperty(value = "platformType", required = true) String platformType,
                    @JsonProperty("platformVersion") String platformVersion,
                    @JsonProperty("platformArchitecture") String platformArchitecture) {
        this.platformType = platformType;
        this.platformVersion = platformVersion;
        this.platformArchitecture = platformArchitecture;
    }

    @Override
    public String toString() {
        String versionSuffix = platformVersion == null ? "" : "_" + platformVersion;
        String suffix = platformArchitecture == null ? "" : "_" + platformArchitecture;
        return platformType + versionSuffix + suffix;
    }

    /**
     * Create a platform from a legacy string such as LINUX or WINDOWS_10_64.
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Platform fromString(String opaqueString) {
        Matcher match = PLATFORM_PATTERN.matcher(opaqueString);
        if (!match.matches()) {
            throw new WebmateApiClientException("Invalid platform string [" + opaqueString
                    + "]. Must look like <platformType>[_<version>[_<architecture>]].");
        }
        return new Platform(match.group(1), match.group(2), match.group(3));
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
