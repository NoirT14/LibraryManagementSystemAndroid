package com.example.project_prm392.data.model.auth;

import java.util.Date;

public class SessionInfo {
    private String sessionId;
    private Date loginTime;
    private String ipAddress;
    private BrowserInfo browserInfo;

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Date getLoginTime() { return loginTime; }
    public void setLoginTime(Date loginTime) { this.loginTime = loginTime; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public BrowserInfo getBrowserInfo() { return browserInfo; }
    public void setBrowserInfo(BrowserInfo browserInfo) { this.browserInfo = browserInfo; }
}
