CHANGES IN VERSION 0.14 (from 0.13)
===================================

**Note: Versions prior to 0.14 are incompatible with the current version of *webmate***

#### New Features
* Add ability to use a custom HttpClientBuilder. This is usefull, when the usage of a proxy is required
* State extraction can now be customized by passing a custom BrowserSessionStateExtractionConfig object to `createState`
 
#### Fixes
* State extraction did not work due to a missing config object

