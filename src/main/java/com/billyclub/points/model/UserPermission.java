package com.billyclub.points.model;

public enum UserPermission {
    EVENT_READ("event:read"),
    EVENT_WRITE("event:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }
    public String getPermission() {
        return this.permission;
    }
}
