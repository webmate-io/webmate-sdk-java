# Changelog
## [0.64] - 2026-02-11
### Fixes
- Fix UserID serialization issue

## [0.63] - 2025-07-29
### Fixes
- Add ArtifactAssociationType for TestMailAccount
- Don't fail on unknown ArtifactAssociationType

## [0.62] - 2025-07-10
### Fixes
- Make email address optional in WebmateAuthInfo

## [0.61] - 2025-06-10
### Fixes
- Update dependencies with known vulnerabilities

## [0.60] - 2025-05-06
### Fixes
- Fix filter issues in  `queryDevicesByRequirements`

## [0.59] - 2025-04-25
### New Features
- A method that allows to query deployable devices for a given Project and requirements
### Fixes
- Fix deprecated route used to query artifacts

## [0.58] 2025-04-15
This release was rolled back

## [0.57] - 2024-12-11
### Fixes
- Fix timeout issues when installing apps/packages using `installAppOnDevice`

## [0.56] - 2024-04-25
### Fixes
- Deprecate `terminateBrowsersession` whose backend route has been removed
- Add methods that accept an explicit expedition (browser session) id, rather than relying on a unique implicit one
- Replace exceptions with warnings when an implicit expedition id is not unique

## [0.55] - 2024-02-05
### New Features
- A method in the device client to configure the simulation of biometric authentication
- Methods to retrieve the browser session info (including the device id) for a running Selenium test

## [0.54] - 2024-01-16
### Fixes
- Wait for test run creation/completion to finish
  - Methods to create or finish a test run are now blocking
  - Selenium actions will no longer go missing

## [0.53] - 2023-12-15
### Fixes
- Add missing DevicePropertyName enum values

## [0.52] - 2023-09-26
### Fixes
- Compatibility change for webmate release 2023.4.0

## [0.51] - 2023-07-11
### Fixes
- Fix problem where testmails could not be created

## [0.50] - 2023-02-10
### New Features
- Added polling API for some long-running operations
- Added endpoint for deleting Packages

## [0.45.0] - [0.49.0]
### New Features
- Added endpoint for TestSession export

### Fixes
- Fix for an issue with default values when creating tags

## [0.44] - 2022-11-28
### Fixes
- Fix for a problem where blob uploads would not respect custom HTTP client configurations


## [0.43] - 2022-04-12
### Fixes
- Fix dependencies for selenium 4


## [0.42] - 2022-03-18
### Fixes
- Fix encoding issues in certain API calls


## [0.41] - 2021-12-17
### New Features
- Added support for retrieving screenshot artifacts


## [0.39.0] - 2020-12-07
### Fixes
- Fix for breaking change in Test Run API.


## [0.32.0] - [0.38.0] 
### New Features
- Major extension of the SDK with new facades. Covers most important API use cases, by now.


## [0.31.0] - 2020-12-07
### New Features
- Added helper for finishing Selenium tests with a result.


## [0.29.0] - 2020-10-15
### New Features
- TestMgmt subsystem added
- Artifact subsystem added


## [0.28.0] - 2020-02-10
### New Features
- Synchronized versioning with webmate JavaScript SDK
- Artifacts can be queried by BrowserSessionId

## [0.27] - 2019-11-26
### Fixes
- Fixes a regression that broke state creation when not providing a BrowserSessionExtractionConfig


## [0.26] - 2019-11-18
### New Features
- Added Mailtest functionality
  - Users can request a mail address that they can use during a test
  - Users can also retrieve Mails that are sent to this address to execute additional tests on the mail

### Fixes
- Adjusted api methods to match webmate release 18.11


## [0.25] - 2019-04-08
### Fixes
- Fixed an issue that prevented correct retrieval of test run results


## [0.24] - 2019-04-08
### New Features
- Added Functionality to interact with Tests and Testruns and retrieve their result
- Added missing functionality of Jobs and Jobruns.


## [0.23] - 2018-12-04
### Fixes
- Fixes rare Serialization Issue


## [0.22] - 2018-11-27
### New Features
- Added Selenium Service API methods
- Added Browser Utils to retrieve the most recent Browsers available in a given Project


## [0.21] - 2018-10-05
### Fixes
- Added timeout to BrowserSessionRefs createState Method


## [0.20] - 2018-09-05
### Fixes
- Race condition fixed when creating jobs


## [0.19] - 2018-08-08
### Fixes
- Ensures compatibility with current Webmate Release. NOTE: prior versions will no longer work!


## [0.18] - 2018-06-12
### New
- You can now use the SDK to interact with your devices


## [0.17] - 2017-12-15
### Fixes
- Fixed overriding of HttpClientBuilder.


## [0.16] - 2017-11-28
### Fixes
- Fixed input parameters of browsersession-regression-analysis job.


## [0.15] - 2017-11-20
### New Features
- New well-known JobConfig for browsersession-regression-analysis jobs.
- Changed Job config defaults (!!!) wrt full page screenshots and state extraction.
- Changes for webmate Jenkins plugin.

### Fixes
- Changed Java version back from 8 to 7.


## [0.14] - 2017-11-07
**Note: Versions prior to 0.14 are incompatible with the current version of *webmate***

### New Features
- Add ability to use a custom HttpClientBuilder. This is usefull, when the usage of a proxy is required
- State extraction can now be customized by passing a custom BrowserSessionStateExtractionConfig object to `createState`
 
### Fixes
- State extraction did not work due to a missing config object
- Properly release HttpClient connections. 

