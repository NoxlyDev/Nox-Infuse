package com.catadmirer.infuseSMP.util;

import java.lang.reflect.Method;
import java.util.UUID;
import org.bukkit.Bukkit;

/**
 * Detects Bedrock players connected through Geyser without needing a compile-time dependency.
 */
public final class BedrockUtil {
    private static Object geyserApi;
    private static Method isBedrockPlayer;
    private static boolean resolved;

    private BedrockUtil() {}

    public static boolean isBedrock(UUID uuid) {
        if (!resolved) {
            resolved = true;
            if (Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot")) {
                try {
                    Class<?> api = Class.forName("org.geysermc.geyser.api.GeyserApi");
                    geyserApi = api.getMethod("api").invoke(null);
                    isBedrockPlayer = api.getMethod("isBedrockPlayer", UUID.class);
                } catch (ReflectiveOperationException | LinkageError ignored) {
                    geyserApi = null;
                }
            }
        }
        if (geyserApi == null) return false;
        try {
            return (boolean) isBedrockPlayer.invoke(geyserApi, uuid);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}
