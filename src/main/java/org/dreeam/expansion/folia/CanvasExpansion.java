package org.dreeam.expansion.canvas;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class CanvasExpansion extends PlaceholderExpansion implements Cacheable, Configurable {

    private CanvasUtils canvasUtils = null;

    private final Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public void clear() {
        canvasUtils = null;
        cache.invalidateAll();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "canvas";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Dreeam__";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public Map<String, Object> getDefaults() {
        final Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.putIfAbsent("tps_color.high", "&a");
        defaults.putIfAbsent("tps_color.medium", "&e");
        defaults.putIfAbsent("tps_color.low", "&c");
        return defaults;
    }

    private @Nullable String getCached(String key, Callable<String> callable) {
        try {
            return cache.get(key, callable);
        } catch (ExecutionException e) {
            if (getPlaceholderAPI().getPlaceholderAPIConfig().isDebugMode()) {
                getPlaceholderAPI().getLogger().log(Level.SEVERE, "[canvas] Could not access cache key " + key, e);
            }
            return "";
        }
    }

    @Override
    public String onRequest(OfflinePlayer p, @NotNull String identifier) {
        if (canvasUtils == null) {
            canvasUtils = new CanvasUtils();
            canvasUtils.checkCanvas();
        }

        if (!canvasUtils.isCanvas) return null;

        if (p == null || !p.isOnline()) return "";
        Player player = p.getPlayer();
        if (player == null) return "";

        switch (identifier) {
            case "global_tps":
                return getCanvasGlobalTPS(null);
            case "global_mspt":
                return getCanvasGlobalMSPT(null);
            case "tps":
                return getCanvasTPS(null, player.getLocation());
            case "mspt":
                return getCanvasMSPT(null, player.getLocation());
        }

        if (identifier.startsWith("global_tps_")) {
            identifier = identifier.replace("global_tps_", "");
            return getCanvasGlobalTPS(identifier);
        }

        if (identifier.startsWith("global_mspt_")) {
            identifier = identifier.replace("global_mspt_", "");
            return getCanvasGlobalMSPT(identifier);
        }

        if (identifier.startsWith("tps_")) {
            identifier = identifier.replace("tps_", "");
            return getCanvasTPS(identifier, player.getLocation());
        }

        if (identifier.startsWith("mspt_")) {
            identifier = identifier.replace("mspt_", "");
            return getCanvasMSPT(identifier, player.getLocation());
        }

        return null;
    }

    public String getCanvasGlobalTPS(String arg) {
        if (arg == null || arg.isEmpty()) {
            StringJoiner joiner = new StringJoiner(toLegacy(Component.text(", ", NamedTextColor.GRAY)));
            for (double tps : canvasUtils.getGlobalTPS()) {
                joiner.add(getColoredTPS(tps));
            }
            return joiner.toString();
        }
        return switch (arg) {
            case "5s" -> fixTPS(canvasUtils.getGlobalTPS().get(0));
            case "10s" -> fixTPS(canvasUtils.getGlobalTPS().get(1));
            case "15s" -> fixTPS(canvasUtils.getGlobalTPS().get(2));
            case "1m" -> fixTPS(canvasUtils.getGlobalTPS().get(3));
            case "5m" -> fixTPS(canvasUtils.getGlobalTPS().get(4));
            case "5s_colored" -> getColoredTPS(canvasUtils.getGlobalTPS().get(0));
            case "10s_colored" -> getColoredTPS(canvasUtils.getGlobalTPS().get(1));
            case "15s_colored" -> getColoredTPS(canvasUtils.getGlobalTPS().get(2));
            case "1m_colored" -> getColoredTPS(canvasUtils.getGlobalTPS().get(3));
            case "5m_colored" -> getColoredTPS(canvasUtils.getGlobalTPS().get(4));
            default -> null;
        };
    }

    public String getCanvasGlobalMSPT(String arg) {
        if (arg == null || arg.isEmpty()) {
            StringJoiner joiner = new StringJoiner(toLegacy(Component.text(", ", NamedTextColor.GRAY)));
            for (double mspt : canvasUtils.getGlobalMSPT()) {
                joiner.add(getColoredMSPT(mspt));
            }
            return joiner.toString();
        }
        return switch (arg) {
            case "5s" -> fixMSPT(canvasUtils.getGlobalMSPT().get(0));
            case "10s" -> fixMSPT(canvasUtils.getGlobalMSPT().get(1));
            case "15s" -> fixMSPT(canvasUtils.getGlobalMSPT().get(2));
            case "1m" -> fixMSPT(canvasUtils.getGlobalMSPT().get(3));
            case "5m" -> fixMSPT(canvasUtils.getGlobalMSPT().get(4));
            case "5s_colored" -> getColoredMSPT(canvasUtils.getGlobalMSPT().get(0));
            case "10s_colored" -> getColoredMSPT(canvasUtils.getGlobalMSPT().get(1));
            case "15s_colored" -> getColoredMSPT(canvasUtils.getGlobalMSPT().get(2));
            case "1m_colored" -> getColoredMSPT(canvasUtils.getGlobalMSPT().get(3));
            case "5m_colored" -> getColoredMSPT(canvasUtils.getGlobalMSPT().get(4));
            default -> null;
        };
    }

    public String getCanvasTPS(String arg, Location location) {
        if (arg == null || arg.isEmpty()) {
            StringJoiner joiner = new StringJoiner(toLegacy(Component.text(", ", NamedTextColor.GRAY)));
            for (double tps : canvasUtils.getTPS(location)) {
                joiner.add(getColoredTPS(tps));
            }
            return joiner.toString();
        }
        return switch (arg) {
            case "5s" -> fixTPS(canvasUtils.getTPS(location).get(0));
            case "10s" -> fixTPS(canvasUtils.getTPS(location).get(1));
            case "15s" -> fixTPS(canvasUtils.getTPS(location).get(2));
            case "1m" -> fixTPS(canvasUtils.getTPS(location).get(3));
            case "5m" -> fixTPS(canvasUtils.getTPS(location).get(4));
            case "5s_colored" -> getColoredTPS(canvasUtils.getTPS(location).get(0));
            case "10s_colored" -> getColoredTPS(canvasUtils.getTPS(location).get(1));
            case "15s_colored" -> getColoredTPS(canvasUtils.getTPS(location).get(2));
            case "1m_colored" -> getColoredTPS(canvasUtils.getTPS(location).get(3));
            case "5m_colored" -> getColoredTPS(canvasUtils.getTPS(location).get(4));
            default -> null;
        };
    }

    public String getCanvasMSPT(String arg, Location location) {
        if (arg == null || arg.isEmpty()) {
            StringJoiner joiner = new StringJoiner(toLegacy(Component.text(", ", NamedTextColor.GRAY)));
            for (double mspt : canvasUtils.getMSPT(location)) {
                joiner.add(getColoredMSPT(mspt));
            }
            return joiner.toString();
        }
        return switch (arg) {
            case "5s" -> fixMSPT(canvasUtils.getMSPT(location).get(0));
            case "10s" -> fixMSPT(canvasUtils.getMSPT(location).get(1));
            case "15s" -> fixMSPT(canvasUtils.getMSPT(location).get(2));
            case "1m" -> fixMSPT(canvasUtils.getMSPT(location).get(3));
            case "5m" -> fixMSPT(canvasUtils.getMSPT(location).get(4));
            case "5s_colored" -> getColoredMSPT(canvasUtils.getMSPT(location).get(0));
            case "10s_colored" -> getColoredMSPT(canvasUtils.getMSPT(location).get(1));
            case "15s_colored" -> getColoredMSPT(canvasUtils.getMSPT(location).get(2));
            case "1m_colored" -> getColoredMSPT(canvasUtils.getMSPT(location).get(3));
            case "5m_colored" -> getColoredMSPT(canvasUtils.getMSPT(location).get(4));
            default -> null;
        };
    }

    private String toLegacy(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component).replaceAll("&", "§");
    }

    private String fixTPS(double tps) {
        String finalTPS = String.format("%.2f", tps);
        return (tps > 20.00 ? "*" : "") + finalTPS;
    }

    private String getColoredTPS(double tps) {
        String color;
        if (tps >= 18.0) {
            color = getString("tps_color.high", "&a");
        } else if (tps >= 15.0) {
            color = getString("tps_color.medium", "&e");
        } else {
            color = getString("tps_color.low", "&c");
        }
        return color + fixTPS(tps);
    }

    private String fixMSPT(double mspt) {
        return String.format("%.2f", mspt);
    }

    private String getColoredMSPT(double mspt) {
        String color;
        if (mspt <= 45.0) {
            color = getString("tps_color.high", "&a");
        } else if (mspt <= 55.0) {
            color = getString("tps_color.medium", "&e");
        } else {
            color = getString("tps_color.low", "&c");
        }
        return color + fixMSPT(mspt);
    }
}
