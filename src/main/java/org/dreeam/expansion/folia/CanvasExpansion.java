package org.dreeam.expansion.canvas;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class CanvasExpansion extends PlaceholderExpansion {

    private final CanvasUtils canvasUtils;
    private final DecimalFormat format = new DecimalFormat("#.##");

    public CanvasExpansion() {
        this.canvasUtils = new CanvasUtils();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "canvas";
    }

    @Override
    public @NotNull String getAuthor() {
        return "YourName";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return canvasUtils.isCanvasServer();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Player onlinePlayer = null;
        if (player != null && player.isOnline()) {
            onlinePlayer = player.getPlayer();
        }

        switch (params.toLowerCase()) {
            case "tps":
                return getCurrentTPS(onlinePlayer, 0);
            case "tps_5s":
                return getCurrentTPS(onlinePlayer, 0);
            case "tps_15s":
                return getCurrentTPS(onlinePlayer, 2);
            case "tps_1m":
                return getCurrentTPS(onlinePlayer, 3);
            case "tps_5m":
                return getCurrentTPS(onlinePlayer, 4);
            case "tps_15m":
                return getCurrentTPS(onlinePlayer, 5);
            case "mspt":
                return getCurrentMSPT(onlinePlayer, 0);
            case "mspt_5s":
                return getCurrentMSPT(onlinePlayer, 0);
            case "mspt_15s":
                return getCurrentMSPT(onlinePlayer, 2);
            case "mspt_1m":
                return getCurrentMSPT(onlinePlayer, 3);
            case "mspt_5m":
                return getCurrentMSPT(onlinePlayer, 4);
            case "mspt_15m":
                return getCurrentMSPT(onlinePlayer, 5);
            default:
                return null;
        }
    }

    private String getCurrentTPS(@Nullable Player player, int index) {
        try {
            List<Double> tpsList;
            if (player != null && player.isOnline()) {
                tpsList = canvasUtils.getTPS(player.getLocation());
            } else {
                tpsList = canvasUtils.getGlobalTPS();
            }
            
            if (index >= 0 && index < tpsList.size()) {
                double tps = tpsList.get(index);
                return format.format(Math.min(tps, 20.0));
            }
            return "0.00";
        } catch (Exception e) {
            return "Error";
        }
    }

    private String getCurrentMSPT(@Nullable Player player, int index) {
        try {
            List<Double> msptList;
            if (player != null && player.isOnline()) {
                msptList = canvasUtils.getMSPT(player.getLocation());
            } else {
                msptList = canvasUtils.getGlobalMSPT();
            }
            
            if (index >= 0 && index < msptList.size()) {
                double mspt = msptList.get(index);
                return format.format(mspt);
            }
            return "0.00";
        } catch (Exception e) {
            return "Error";
        }
    }
}
