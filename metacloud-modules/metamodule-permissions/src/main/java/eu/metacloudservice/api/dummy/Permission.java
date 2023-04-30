package eu.metacloudservice.api.dummy;

public class Permission {

    private String permission;
    private String canceledAt;
    private boolean set;

    public Permission(String permission, String canceledAt, boolean set) {
        this.permission = permission;
        this.canceledAt = canceledAt;
        this.set = set;
    }

    public String getPermission() {
        return permission;
    }

    public String getCanceledAt() {
        return canceledAt;
    }

    public boolean isSet() {
        return set;
    }
}
