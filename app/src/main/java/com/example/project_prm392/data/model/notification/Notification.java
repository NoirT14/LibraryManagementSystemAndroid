package com.example.project_prm392.data.model.notification;

public class Notification {
    private int notificationId;
    private String message;
    private String notificationDate;
    private boolean readStatus;
    private String notificationType;
    private int receiverId;
    private String relatedTable;
    private Integer relatedId;
    private boolean forStaff;
    private boolean handledStatus;
    private String handledAt;

    // Getters
    public int getNotificationId() { return notificationId; }
    public String getMessage() { return message; }
    public String getNotificationDate() { return notificationDate; }
    public boolean isReadStatus() { return readStatus; }
    public String getNotificationType() { return notificationType; }
    public int getReceiverId() { return receiverId; }
    public String getRelatedTable() { return relatedTable; }
    public Integer getRelatedId() { return relatedId; }
    public boolean isForStaff() { return forStaff; }
    public boolean isHandledStatus() { return handledStatus; }
    public String getHandledAt() { return handledAt; }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

}
