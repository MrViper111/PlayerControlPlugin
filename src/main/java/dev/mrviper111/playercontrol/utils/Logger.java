package dev.mrviper111.playercontrol.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logger {

    public static void logInfo(String message) {
        Bukkit.getLogger().info(message);
    }

    public static void logWarning(String message) {
        Bukkit.getLogger().warning(message);
    }

    public static void logError(String message) {
        Bukkit.getLogger().severe(message);
    }

    public static void logError(String message, Throwable thrown) {
        Bukkit.getLogger().log(Level.SEVERE, message, thrown);
    }

}
