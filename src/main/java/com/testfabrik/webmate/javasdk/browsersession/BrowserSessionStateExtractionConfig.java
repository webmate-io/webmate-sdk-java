package com.testfabrik.webmate.javasdk.browsersession;

import com.testfabrik.webmate.javasdk.commonutils.Dimension;

/**
 * A Wrapper class for proper state extraction Configuration. The different parameters determine the behaviour during state extraction. If parameters are
 * set to null, the default value will be taken. NOTE: In some cases, this means thah specific operations are not done, e.g if the warmUpConfig parameter is
 * set to null, the state extraction will not perform a warmup before extracting the state. For further Information, please refer to the online documentation
 */
public class BrowserSessionStateExtractionConfig {

    private BrowserSessionStateId stateId;
    private Integer extractionDelay;
    private Integer extractionCooldown;
    private Dimension optViewportDimension;
    private Integer maxAdditionWaitingTimeForStateExtraction;
    private Boolean extractDomStateData;
    private BrowserSessionScreenshotExtractionConfig screenShotConfig;
    private BrowserSessionWarmUpConfig warmUpConfig;

    /**
     * Creates a new StateExtractionConfig.
     *
     * @param stateId The Session Id the state should belong to.
     * @param extractionDelay The delay before state extraction is triggered.
     * @param extractionCooldown The delay after state extraction is finished.
     * @param optViewportDimension The dimensions the viewport should be resized to.
     * @param maxAdditionWaitingTimeForStateExtraction How much additional time the state extraction should get, after the projected end time is reached.
     * @param extractDomStateData wether DOM data should be extracted or not.
     * @param screenShotConfig The configuration for the screenshot extraction. If set to null, the default behaviour is chosen (Take no screenshot)
     * @param warmUpConfig The configuration for the warmup. If set to null, the default behaviour is chosen (Do not warm up)
     */

    public BrowserSessionStateExtractionConfig(BrowserSessionStateId stateId, Integer extractionDelay, Integer extractionCooldown, Dimension optViewportDimension, Integer maxAdditionWaitingTimeForStateExtraction, Boolean extractDomStateData, BrowserSessionScreenshotExtractionConfig screenShotConfig, BrowserSessionWarmUpConfig warmUpConfig) {
        this.stateId = stateId;
        this.extractionDelay = extractionDelay;
        this.extractionCooldown = extractionCooldown;
        this.optViewportDimension = optViewportDimension;
        this.maxAdditionWaitingTimeForStateExtraction = maxAdditionWaitingTimeForStateExtraction;
        this.extractDomStateData = extractDomStateData;
        this.screenShotConfig = screenShotConfig;
        this.warmUpConfig = warmUpConfig;
    }

    /**
     * Creates a BrowserSessionStateExtractionConfig with all parameters set to null, i.e the choice of parameters is the webmate default.
     */

    public BrowserSessionStateExtractionConfig(){
        this.stateId = null;
        this.extractionDelay = null;
        this.extractionCooldown = null;
        optViewportDimension = null;
        this.maxAdditionWaitingTimeForStateExtraction = null;
        this.extractDomStateData = null;
        this.screenShotConfig = null;
        this.warmUpConfig = null;
    }

    public BrowserSessionStateId getStateId() {
        return stateId;
    }

    public void setStateId(BrowserSessionStateId stateId) {
        this.stateId = stateId;
    }

    public Integer getExtractionDelay() {
        return extractionDelay;
    }

    public void setExtractionDelay(Integer extractionDelay) {
        this.extractionDelay = extractionDelay;
    }

    public Integer getExtractionCooldown() {
        return extractionCooldown;
    }

    public void setExtractionCooldown(Integer extractionCooldown) {
        this.extractionCooldown = extractionCooldown;
    }

    public Dimension getOptViewportDimension() {
        return optViewportDimension;
    }

    public void setOptViewportDimension(Dimension optViewportDimension) {
        this.optViewportDimension = optViewportDimension;
    }

    public Integer getMaxAdditionWaitingTimeForStateExtraction() {
        return maxAdditionWaitingTimeForStateExtraction;
    }

    public void setMaxAdditionWaitingTimeForStateExtraction(Integer maxAdditionWaitingTimeForStateExtraction) {
        this.maxAdditionWaitingTimeForStateExtraction = maxAdditionWaitingTimeForStateExtraction;
    }

    public Boolean isExtractDomStateData() {
        return extractDomStateData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrowserSessionStateExtractionConfig that = (BrowserSessionStateExtractionConfig) o;

        if (extractionDelay != that.extractionDelay) return false;
        if (extractionCooldown != that.extractionCooldown) return false;
        if (maxAdditionWaitingTimeForStateExtraction != that.maxAdditionWaitingTimeForStateExtraction) return false;
        if (extractDomStateData != that.extractDomStateData) return false;
        if (stateId != null ? !stateId.equals(that.stateId) : that.stateId != null) return false;
        if (optViewportDimension != null ? !optViewportDimension.equals(that.optViewportDimension) : that.optViewportDimension != null) return false;
        if (screenShotConfig != null ? !screenShotConfig.equals(that.screenShotConfig) : that.screenShotConfig != null) return false;
        return warmUpConfig != null ? warmUpConfig.equals(that.warmUpConfig) : that.warmUpConfig == null;
    }

    @Override
    public int hashCode() {
        Integer result = stateId != null ? stateId.hashCode() : 0;
        result = 31 * result + extractionDelay;
        result = 31 * result + extractionCooldown;
        result = 31 * result + (optViewportDimension != null ? optViewportDimension.hashCode() : 0);
        result = 31 * result + maxAdditionWaitingTimeForStateExtraction;
        result = 31 * result + (extractDomStateData ? 1 : 0);
        result = 31 * result + (screenShotConfig != null ? screenShotConfig.hashCode() : 0);
        result = 31 * result + (warmUpConfig != null ? warmUpConfig.hashCode() : 0);
        return result;
    }

    public void setExtractDomStateData(Boolean extractDomStateData) {

        this.extractDomStateData = extractDomStateData;
    }

    public BrowserSessionScreenshotExtractionConfig getScreenShotConfig() {
        return screenShotConfig;
    }

    public void setScreenShotConfig(BrowserSessionScreenshotExtractionConfig screenShotConfig) {
        this.screenShotConfig = screenShotConfig;
    }

    public BrowserSessionWarmUpConfig getWarmUpConfig() {
        return warmUpConfig;
    }

    public void setWarmUpConfig(BrowserSessionWarmUpConfig warmUpConfig) {
        this.warmUpConfig = warmUpConfig;
    }
}
