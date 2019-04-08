# Changelog

All notable changes to the webmate Java SDK will be documented in this file.

## [0.24] - 2019-04-08
###New
- Added Functionality to interact with Tests and Testruns and retrieve their result
- Added missing functionality of Jobs and Jobruns.



## [0.23] - 2018-12-04
###Fixes
- Fixes rare Serialization Issue


## [0.22] - 2018-11-27
###New
- Added Selenium Service API methods
- Added Browser Utils to retrieve the most recent Browsers available in a given Project

## [0.21] - 2018-10-05

### Fixes
- Added timeout to BrowserSessionRefs createState Method

## [0.20] - 2018-09-05

### Fixes
- Racecondition fixed when creating jobs

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

### New

- New well-known JobConfig for browsersession-regression-analysis jobs.
- Changed Job config defaults (!!!) wrt full page screenshots and state extraction.
- Changes for webmate Jenkins plugin.

### Fixes

- Changed Java version back from 8 to 7.

## [0.14] - 2017-11-07

**Note: Versions prior to 0.14 are incompatible with the current version of *webmate***

### New Features
* Add ability to use a custom HttpClientBuilder. This is usefull, when the usage of a proxy is required
* State extraction can now be customized by passing a custom BrowserSessionStateExtractionConfig object to `createState`
 
### Fixes
* State extraction did not work due to a missing config object
- Properly release HttpClient connections. 

