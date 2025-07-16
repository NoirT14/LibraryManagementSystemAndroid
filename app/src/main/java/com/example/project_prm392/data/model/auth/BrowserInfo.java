package com.example.project_prm392.data.model.auth;

import android.os.Build;

import java.util.Locale;

public class BrowserInfo {
    private String browserName;
    private String browserVersion;
    private String operatingSystem;
    private String language;
    private String timezone;
    private String screenResolution;
    private String userAgent;

    public static BrowserInfo createDefault() {
        BrowserInfo info = new BrowserInfo();
        info.browserName = "Android App";
        info.browserVersion = "1.0";
        info.operatingSystem = "Android " + Build.VERSION.RELEASE;
        info.language = Locale.getDefault().toString();
        info.timezone = java.util.TimeZone.getDefault().getID();
        info.userAgent = "LibraryApp/1.0 (Android " + Build.VERSION.RELEASE + ")";
        return info;
    }

    // Getters and Setters
    public String getBrowserName() { return browserName; }
    public void setBrowserName(String browserName) { this.browserName = browserName; }

    public String getBrowserVersion() { return browserVersion; }
    public void setBrowserVersion(String browserVersion) { this.browserVersion = browserVersion; }

    public String getOperatingSystem() { return operatingSystem; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public String getScreenResolution() { return screenResolution; }
    public void setScreenResolution(String screenResolution) { this.screenResolution = screenResolution; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}
