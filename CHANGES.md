# Changelog

All notable changes to the webmate Java SDK will be documented in this file.

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

