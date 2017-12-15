# webmate Java SDK

The webmate SaaS test automation platform provides testing services for testers and developers of web applications.
This SDK contains wrapper code used to call the webmate API from Java applications.

There is no official version of the webmate Java SDK, yet. This is a preview release, which provides wrappers for the following tasks:

* Perform state extraction in an existing browser session, e.g. one that has been created via Selenium.
* Execute a new JobRun in the webmate Job service, e.g. to start a Job comparing the layout of web pages in multiple browsers.
* There is a convenience builder for a BrowserSessionCrossbrowserAnalysis job that may be used to compare the layout of states / web pages from multiple Selenium sessions.
* There is a convenience builder for a BrowserSessionRegressionAnalysis job that may be used to compare the layout of states / web pages from multiple Selenium sessions.

This SDK pre-release gives access to a small subset of the features available in the API. The SDK will be completed over time.


## Using the SDK in your Project

This release is also distributed via Maven Central. Just include the following dependency to your Maven pom.xml (or another Maven based build tool):

```xml
    <groupId>com.testfabrik.webmate.sdk</groupId>
    <artifactId>java-sdk</artifactId>
    <version>0.17</version>
```

To build the SDK from its sources, simply clone this repository and
install the compiled artifacts to your local Maven repository with

```bash
$ mvn install
```

After that, you can include the SDK as a Maven dependency to your project, i.e. either using

```xml
    <groupId>com.testfabrik.webmate.sdk</groupId>
    <artifactId>java-sdk</artifactId>
    <version>0.18-SNAPSHOT</version>
```

or using the equivalent statement in a build tool of your choice. 


## Sample Code (Java)

Sample code using the webmate Java SDK is available at https://github.com/webmate-io/webmate-sdk-samples.
In order to use this example, you need to have an account at webmate SaaS or a commercial on-premise installation.
Please contact Testfabrik (info@testfabrik.com) if you are interested in evaluating webmate.

