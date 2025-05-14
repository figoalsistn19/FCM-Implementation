package com.portofolio.gox.model;

public class FcmNotificationRequestDto {
    private String targetDeviceToken;
    private String title;
    private String body;
    private String deeplinkUrl; // Untuk deeplink
    // private String utmCampaign; // Contoh data tambahan
    // private Map<String, String> customDataMap; // Untuk data kustom yang lebih fleksibel

    // Constructor, Getters, and Setters

    public FcmNotificationRequestDto() {
    }

    public FcmNotificationRequestDto(String targetDeviceToken, String title, String body, String deeplinkUrl) {
        this.targetDeviceToken = targetDeviceToken;
        this.title = title;
        this.body = body;
        this.deeplinkUrl = deeplinkUrl;
    }

    public String getTargetDeviceToken() {
        return targetDeviceToken;
    }

    public void setTargetDeviceToken(String targetDeviceToken) {
        this.targetDeviceToken = targetDeviceToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDeeplinkUrl() {
        return deeplinkUrl;
    }

    public void setDeeplinkUrl(String deeplinkUrl) {
        this.deeplinkUrl = deeplinkUrl;
    }

    // public String getUtmCampaign() { return utmCampaign; }
    // public void setUtmCampaign(String utmCampaign) { this.utmCampaign = utmCampaign; }

    // public Map<String, String> getCustomDataMap() { return customDataMap; }
    // public void setCustomDataMap(Map<String, String> customDataMap) { this.customDataMap = customDataMap; }
}
