package me.pacenstein.quidditch;

public enum QuidditchRole {
    CHASER("Chaser"),
    BEATER("Beater"),
    KEEPER("Keeper"),
    HUNTER("Hunter");

    private final String displayName;

    QuidditchRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
