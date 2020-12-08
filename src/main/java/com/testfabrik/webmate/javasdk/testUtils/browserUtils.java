package com.testfabrik.webmate.javasdk.testUtils;

import com.testfabrik.webmate.javasdk.Browser;
import com.testfabrik.webmate.javasdk.ProjectId;
import com.testfabrik.webmate.javasdk.selenium.SeleniumCapability;
import com.testfabrik.webmate.javasdk.selenium.SeleniumServiceClient;

import java.util.*;

public class browserUtils {
    /**
     * Get the newest browsers of a project as List of Selenium capabilities
     * @param seleniumServiceClient an API Client for Selenium specific methods
     * @param projectId id of the project of which the newest browsers should be retrieved
     * @return List of the newest browsers in the project (Actual type: ArrayList)
     */
    public static List<Browser> getNewestBrowsersForAnyPlatform(SeleniumServiceClient seleniumServiceClient, ProjectId projectId) {
        return getNewestBrowsersForPlatform(seleniumServiceClient, projectId, null);
    }

    /**
     * Get the newest browsers of a project for a specific OS as List of Selenium capabilities
     * @param seleniumServiceClient an API Client for Selenium specific methods
     * @param projectId id of the project of which the newest browsers should be retrieved
     * @param platform operating system which the browsers should be compatible with
     * @return List of the newest browsers compatible with the given OS (Actual type: ArrayList)
     */
    public static List<Browser> getNewestBrowsersForPlatform(SeleniumServiceClient seleniumServiceClient, ProjectId projectId, String platform) {
        ArrayList<Browser> browsers = new ArrayList<>();
        List<SeleniumCapability> capabilities = (List<SeleniumCapability>)seleniumServiceClient.getSeleniumCapabilitiesForProject(projectId);

        if(platform != null) {
            capabilities = filterCapabilitiesByPlatform(capabilities, platform);
        }

        List<SeleniumCapability> chromes = filterCapabilitiesByBrowserType(capabilities, "CHROME");
        List<SeleniumCapability> firefoxes = filterCapabilitiesByBrowserType(capabilities, "FIREFOX");
        List<SeleniumCapability> iexplores = filterCapabilitiesByBrowserType(capabilities, "IE");
        List<SeleniumCapability> edges = filterCapabilitiesByBrowserType(capabilities, "EDGE");
        List<SeleniumCapability> safaris = filterCapabilitiesByBrowserType(capabilities, "SAFARI");

        if (!chromes.isEmpty()) {
            Collections.sort(chromes, new SortByVersionDescending());
            SeleniumCapability chrome = chromes.get(0);
            browsers.add(new Browser(chrome.getBrowserName(), chrome.getVersion(), chrome.getPlatform()));
        }
        if (!firefoxes.isEmpty()) {
            Collections.sort(firefoxes, new SortByVersionDescending());
            SeleniumCapability firefox = firefoxes.get(0);
            browsers.add(new Browser(firefox.getBrowserName(), firefox.getVersion(), firefox.getPlatform()));
        }
        if (!iexplores.isEmpty()) {
            Collections.sort(iexplores, new SortByVersionDescending());
            SeleniumCapability iexplore = iexplores.get(0);
            browsers.add(new Browser(iexplore.getBrowserName(), iexplore.getVersion(), iexplore.getPlatform()));
        }
        if (!edges.isEmpty()) {
            Collections.sort(edges, new SortByVersionDescending());
            SeleniumCapability edge = edges.get(0);
            browsers.add(new Browser(edge.getBrowserName(), edge.getVersion(), edge.getPlatform()));
        }
        if (!safaris.isEmpty()) {
            Collections.sort(safaris, new SortByVersionDescending());
            SeleniumCapability safari = safaris.get(0);
            browsers.add(new Browser(safari.getBrowserName(), safari.getVersion(), safari.getPlatform()));
        }

        return browsers;
    }

    /**
     * Search in a project for the newest browser of a specific browser Type (e.g. FIREFOX, CHROME, ...) for any OS
     * @param seleniumServiceClient an API Client for Selenium specific methods
     * @param projectId id of the project of which the newest browser should be retrieved
     * @param browserType type of the browser to be retrieved
     * @return requested Browser OR null if no capabilities are fitting
     */
    public static Browser getNewestBrowserByTypeForAnyPlatform(SeleniumServiceClient seleniumServiceClient, ProjectId projectId, String browserType) {
        return getNewestBrowserByTypeForPlatform(seleniumServiceClient, projectId, browserType, null);
    }

    /**
     * Search in a project for the newest browser of a specific browser Type for a given OS
     * @param seleniumServiceClient an API Client for Selenium specific methods
     * @param projectId id of the project of which the newest browser should be retrieved
     * @param browserType type of the browser to be retrieved
     * @param platform operating system which the browser should be compatible with
     * @return requested Browser OR null if no capabilities are fitting
     */
    public static Browser getNewestBrowserByTypeForPlatform(SeleniumServiceClient seleniumServiceClient, ProjectId projectId, String browserType, String platform) {
        List<SeleniumCapability> capabilities = (List<SeleniumCapability>)seleniumServiceClient.getSeleniumCapabilitiesForProject(projectId);

        if(platform != null) {
            capabilities = filterCapabilitiesByPlatform(capabilities, platform);
        }

        capabilities = filterCapabilitiesByBrowserType(capabilities, browserType);
        Collections.sort(capabilities, new SortByVersionDescending());

        if (!capabilities.isEmpty()) {
            SeleniumCapability browserCapability = capabilities.get(0);
            return new Browser(browserCapability.getBrowserName(), browserCapability.getVersion(), browserCapability.getPlatform());
        }
        else{
            return null;
        }
    }

    private static List<SeleniumCapability> filterCapabilitiesByBrowserType(Collection<SeleniumCapability> capabilities, String browserType) {
        ArrayList<SeleniumCapability> matches = new ArrayList<>();
        for (SeleniumCapability capability : capabilities) {
            if (capability.getBrowserName().equals(browserType)) {
                matches.add(capability);
            }
        }
        return matches;
    }

    private static List<SeleniumCapability> filterCapabilitiesByPlatform(Collection<SeleniumCapability> capabilities, String platform) {
        ArrayList<SeleniumCapability> matches = new ArrayList<>();
        for (SeleniumCapability capability : capabilities) {
            if (capability.getPlatform().equals(platform)) {
                matches.add(capability);
            }
        }
        return matches;
    }

    private static class SortByVersionAscending implements Comparator<SeleniumCapability> {
        @Override
        public int compare(SeleniumCapability o1, SeleniumCapability o2) {
            return Integer.parseInt(o1.getVersion()) - Integer.parseInt(o2.getVersion());
        }
    }

    private static class SortByVersionDescending implements Comparator<SeleniumCapability> {
        @Override
        public int compare(SeleniumCapability o1, SeleniumCapability o2) {
            return Integer.parseInt(o2.getVersion()) - Integer.parseInt(o1.getVersion());
        }
    }
}


