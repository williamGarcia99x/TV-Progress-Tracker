package com.cognixia.model;

public enum WatchStatus {
    PLANNING, WATCHING, COMPLETED;

    public static boolean isValidStatus(String status){
        try {
            WatchStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }
}
