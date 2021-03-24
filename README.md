# webmate Java SDK <img src="https://avatars.githubusercontent.com/u/13346605" alt="webmate logo" width="28"/> [![Build Status](https://travis-ci.com/webmate-io/webmate-sdk-java.svg?branch=master)](https://travis-ci.com/webmate-io/webmate-sdk-java) ![Maven Central](https://img.shields.io/maven-central/v/com.testfabrik.webmate.sdk/java-sdk)

The webmate SaaS test automation platform provides testing services for testers and developers of web applications.
This SDK contains wrapper code used to call the webmate API from Java applications.

The webmate Java SDK is still under development and maintained regularly.
For a complete list of recent changes, please refer to the [changelog](CHANGES.md).


## Using the SDK in your Project

This release is also distributed via Maven Central. Just include the following dependency to your Maven pom.xml (or another Maven based build tool):

```xml
<dependency>
    <groupId>com.testfabrik.webmate.sdk</groupId>
    <artifactId>java-sdk</artifactId>
    <version>0.37</version>
</dependency>
```

To build the SDK from its sources, simply clone this repository and
install the compiled artifacts to your local Maven repository with

```bash
$ mvn install
```

After that, you can include the SDK as a Maven dependency to your project, i.e. either using

```xml
<dependency>
    <groupId>com.testfabrik.webmate.sdk</groupId>
    <artifactId>java-sdk</artifactId>
    <version>0.38-SNAPSHOT</version>
</dependency>
```

or using the equivalent statement in a build tool of your choice. 


## Functionality included in the SDK

<table border="1">
    <tr>
        <th>Task</th>
        <th>Abstract</th>
        <th>Technical Description</th>
    </tr>
    <tr>
        <td>
            Executing <b>Selenium</b> test
        </td>
        <td>
            The webmate SDK and infrastructure enables the execution of Selenium tests on various desktop machines, such as Windows, macOS or Ubuntu.
            The tests can be performed with numerous browsers like Chrome, Firefox, Safari, Internet Explorer, Opera or Edge in various versions.
            As webmate supports Selenium plenty of frameworks that build on top of Selenium are supported, e.g. <a href="https://cucumber.io/">Cucumber</a> and <a href="http://galenframework.com/">Galen</a>.
        </td>
        <td>
            See <a href="./src/main/java/com/testfabrik/webmate/javasdk/PlatformType.java">PlatformType</a> for available platforms and <a href="./src/main/java/com/testfabrik/webmate/javasdk/BrowserType.java">BrowserType</a>
            for most common browsers.
        </td>
    </tr>
    <tr>
        <td>
            Executing <b>Appium</b> test
        </td>
        <td>
            The webmate SDK and infrastructure enables the execution of Appium tests on various mobile devices with Android or iOS operating system.
            The tests can be performed on all browsers supported by Appium.
        </td>
        <td>
            ...
        </td>
    </tr>
    <tr>
        <td>
            Executing <b>Regression</b> test
        </td>
        <td>
            ...
        </td>
        <td>
            ...
        </td>        
    </tr>
    <tr>
        <td>
            Executing <b>Crossbrowser</b> test
        </td>
        <td>
            ...
        </td>
        <td>
            ...
        </td>
    </tr>
    <tr>
        <td>
            Comparing layout
        </td>
        <td>
            ...
        </td>
        <td>
            ...
        </td>
    </tr>
    <tr>
        <td>
            Deploying desktop machines and mobile devices
        </td>
        <td>
            Desktop machines (e.g. Windows, macOS or Ubuntu) and mobile devices (Android or iOS) can be selected and deployed,
            i.e. they become available in the webmate platform to enable manual interaction with the devices or being available for tests.
            Desktop machines and mobiles devices can be requested by various properties, such as operating system or vendor.
            For example, devices can be deployed by requiring Android 11 as a platform and Samsung as vendor. 
        </td>
        <td>
            ...
        </td>
    </tr>
    <tr>
        <td>
            Test Management
        </td>
        <td>
            ...
        </td>
        <td>
            ...
        </td>
    </tr>
    <tr>
        <td>
            Mail Testing
        </td>
        <td>
            ...
        </td>
        <td>
            ...
        </td>
    </tr>
    <tr>
        <td>
            Performing state extraction
        </td>
        <td>
            The state of a web page in a browser can be extracted.
        </td>
        <td>
            Perform state extraction in an existing browser session, e.g. one that has been created via Selenium.
        </td>
    </tr>
    <tr>
        <td>
            Uploading images
        </td>
        <td>
            Images can be uploaded to webmate and pushed to mobile devices.
            Also, the pushed images are available in the gallery apps on the mobile devices.
        </td>
        <td>
            The <a href="./src/main/java/com/testfabrik/webmate/javasdk/devices/DeviceClient.java">DeviceClient</a> is responsible for uploading and pushing images to mobile devices.
        </td>
    </tr>
    <tr>
        <td>
            Uploading apps
        </td>
        <td>
            Apps (APKs or IPAs) can be uploaded to the webmate platform.
            All apps will be instrumented after being uploaded to enable simulation features.
        </td>
        <td>
            The <a href="./src/main/java/com/testfabrik/webmate/javasdk/packagemgmt/PackageMgmtClient.java">PackageMgmtClient</a> is responsible for uploading apps.
        </td>
    </tr>
    <tr>
        <td>
            Installing apps
        </td>
        <td>
            Apps can be installed on their respective platform, i.e. APKs on Android and IPAs on iOS.
        </td>
        <td>
            The <a href="./src/main/java/com/testfabrik/webmate/javasdk/devices/DeviceClient.java">DeviceClient</a> is responsible for installing apps.
            Note that the devices must suffice the requirements of the desired apps, such as minimum platform version.
        </td>
    </tr>
    <tr>
        <td>
            Configure camera simulation
        </td>
        <td>
            The camera simulation enables simulating a custom image instead of the real camera input of Android or iOS devices.
            This is especially useful to automate app testing with camera functionality, like scanning QR codes.
            The camera simulation can be enabled, disabled and configured to use a custom image.
        </td>
        <td>
            The <a href="./src/main/java/com/testfabrik/webmate/javasdk/devices/DeviceClient.java">DeviceClient</a> is responsible for the camera simulation.
        </td>
    </tr>
</table>


## Samples

See the following sample projects:
* [Java Samples](https://github.com/webmate-io/webmate-sdk-samples)
* [JavaScript And TypeScript Samples](https://github.com/webmate-io/webmate-sdk-js-samples)

In order to use these samples, you need to have an account at webmate SaaS or a commercial on-premise installation.
Please contact Testfabrik (info@testfabrik.com) if you are interested in evaluating webmate.


## webmate API

Although, the SDK provides a number of features and convenience wrappers it doesn't exhaust the full potential of the webmate API.
See the REST API [Swagger documentation](https://app.webmate.io/api/swagger) for a comprehensive summary of the webmate functionalities.
