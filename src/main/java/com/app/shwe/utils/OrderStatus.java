package com.app.shwe.utils;

public enum OrderStatus {
    Cancel_Order("Cancel Order"),
    PENDING("Pending"),
    ON_PROGRESS("On Progress"),
    COMPLETED("Completed");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
