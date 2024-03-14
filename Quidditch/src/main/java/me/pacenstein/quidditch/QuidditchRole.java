package me.pacenstein.quidditch;

public enum QuidditchRole {
    GUARDIAN("Guardian"),
    HUNTER("Hunter"),
    SEEKER("Seeker"),
    DRIVER("Driver");

    private final String displayName;

    QuidditchRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
