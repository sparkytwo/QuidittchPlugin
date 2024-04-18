package me.pacenstein.quidditch;

import org.bukkit.ChatColor;

public enum HogwartsHouse {
    GRYFFINDOR, RAVENCLAW, HUFFLEPUFF, SLYTHERIN;

    public static String getDisplayName(HogwartsHouse house) {
        switch (house) {
            case GRYFFINDOR:
                return ChatColor.RED + "Gryffindor";
            case RAVENCLAW:
                return ChatColor.BLUE + "Ravenclaw";
            case HUFFLEPUFF:
                return ChatColor.YELLOW + "Hufflepuff";
            case SLYTHERIN:
                return ChatColor.GREEN + "Slytherin";
            default:
                return ChatColor.GRAY + "Unknown";
        }
    }
}
