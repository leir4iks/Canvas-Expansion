package org.dreeam.expansion.canvas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class CanvasUtils {

    private boolean isCanvas = false;

    public CanvasUtils() {
        checkCanvas();
    }

    public void checkCanvas() {
        try {
            Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer");
            isCanvas = true;
        } catch (ClassNotFoundException e) {
            isCanvas = false;
        }
    }

    public boolean isCanvasServer() {
        return isCanvas;
    }

    public List<Double> getGlobalTPS() {
        if (!isCanvas) {
            double[] serverTps = Bukkit.getServer().getTPS();
            return List.of(serverTps[0], serverTps[0], serverTps[1], serverTps[2], serverTps[2]);
        }

        try {
            Object server = Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer")
                    .getMethod("getInstance")
                    .invoke(null);
            
            Object globalTickHandle = server.getClass()
                    .getMethod("getGlobalTickHandle")
                    .invoke(server);
            
            // Получаем RollingAverage объекты и вызываем getAverage()
            Object tps5sObj = globalTickHandle.getClass()
                    .getMethod("getTps5s")
                    .invoke(globalTickHandle);
            double tps_5s = (Double) tps5sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps5sObj);
            
            Object tps15sObj = globalTickHandle.getClass()
                    .getMethod("getTps15s")
                    .invoke(globalTickHandle);
            double tps_15s = (Double) tps15sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps15sObj);
            
            Object tps1mObj = globalTickHandle.getClass()
                    .getMethod("getTps1m")
                    .invoke(globalTickHandle);
            double tps_1m = (Double) tps1mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps1mObj);
            
            Object tps5mObj = globalTickHandle.getClass()
                    .getMethod("getTps5m")
                    .invoke(globalTickHandle);
            double tps_5m = (Double) tps5mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps5mObj);
            
            Object tps15mObj = globalTickHandle.getClass()
                    .getMethod("getTps15m")
                    .invoke(globalTickHandle);
            double tps_15m = (Double) tps15mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps15mObj);
            
            return List.of(tps_5s, tps_5s, tps_15s, tps_1m, tps_5m, tps_15m);
        } catch (Exception e) {
            double[] serverTps = Bukkit.getServer().getTPS();
            return List.of(serverTps[0], serverTps[0], serverTps[1], serverTps[2], serverTps[2], serverTps[2]);
        }
    }

    public List<Double> getGlobalMSPT() {
        if (!isCanvas) {
            return List.of(20.0, 20.0, 20.0, 20.0, 20.0, 20.0);
        }

        try {
            Object server = Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer")
                    .getMethod("getInstance")
                    .invoke(null);
            
            Object globalTickHandle = server.getClass()
                    .getMethod("getGlobalTickHandle")
                    .invoke(server);
            
            Object mspt5sObj = globalTickHandle.getClass()
                    .getMethod("getMspt5s")
                    .invoke(globalTickHandle);
            double mspt_5s = (Double) mspt5sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt5sObj);
            
            Object mspt15sObj = globalTickHandle.getClass()
                    .getMethod("getMspt15s")
                    .invoke(globalTickHandle);
            double mspt_15s = (Double) mspt15sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt15sObj);
            
            Object mspt1mObj = globalTickHandle.getClass()
                    .getMethod("getMspt1m")
                    .invoke(globalTickHandle);
            double mspt_1m = (Double) mspt1mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt1mObj);
            
            Object mspt5mObj = globalTickHandle.getClass()
                    .getMethod("getMspt5m")
                    .invoke(globalTickHandle);
            double mspt_5m = (Double) mspt5mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt5mObj);
            
            Object mspt15mObj = globalTickHandle.getClass()
                    .getMethod("getMspt15m")
                    .invoke(globalTickHandle);
            double mspt_15m = (Double) mspt15mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt15mObj);
            
            return List.of(mspt_5s, mspt_5s, mspt_15s, mspt_1m, mspt_5m, mspt_15m);
        } catch (Exception e) {
            return List.of(20.0, 20.0, 20.0, 20.0, 20.0, 20.0);
        }
    }

    public List<Double> getTPS(Location location) {
        if (location == null || !isCanvas) return getGlobalTPS();

        World world = location.getWorld();
        if (world == null) return getGlobalTPS();
        
        try {
            Object server = Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer")
                    .getMethod("getInstance")
                    .invoke(null);
            
            int chunkX = location.getBlockX() >> 4;
            int chunkZ = location.getBlockZ() >> 4;
            
            Object region = server.getClass()
                    .getMethod("getRegionAtChunk", World.class, int.class, int.class)
                    .invoke(server, world, chunkX, chunkZ);
            
            if (region == null) {
                return getGlobalTPS();
            }
            
            Object tickHandle = region.getClass()
                    .getMethod("getTickHandle")
                    .invoke(region);
            
            Object tps5sObj = tickHandle.getClass()
                    .getMethod("getTps5s")
                    .invoke(tickHandle);
            double tps_5s = (Double) tps5sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps5sObj);
            
            Object tps15sObj = tickHandle.getClass()
                    .getMethod("getTps15s")
                    .invoke(tickHandle);
            double tps_15s = (Double) tps15sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps15sObj);
            
            Object tps1mObj = tickHandle.getClass()
                    .getMethod("getTps1m")
                    .invoke(tickHandle);
            double tps_1m = (Double) tps1mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps1mObj);
            
            Object tps5mObj = tickHandle.getClass()
                    .getMethod("getTps5m")
                    .invoke(tickHandle);
            double tps_5m = (Double) tps5mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps5mObj);
            
            Object tps15mObj = tickHandle.getClass()
                    .getMethod("getTps15m")
                    .invoke(tickHandle);
            double tps_15m = (Double) tps15mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(tps15mObj);
            
            return List.of(tps_5s, tps_5s, tps_15s, tps_1m, tps_5m, tps_15m);
        } catch (Exception e) {
            return getGlobalTPS();
        }
    }

    public List<Double> getMSPT(Location location) {
        if (location == null || !isCanvas) return getGlobalMSPT();

        World world = location.getWorld();
        if (world == null) return getGlobalMSPT();
        
        try {
            Object server = Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer")
                    .getMethod("getInstance")
                    .invoke(null);
            
            int chunkX = location.getBlockX() >> 4;
            int chunkZ = location.getBlockZ() >> 4;
            
            Object region = server.getClass()
                    .getMethod("getRegionAtChunk", World.class, int.class, int.class)
                    .invoke(server, world, chunkX, chunkZ);
            
            if (region == null) {
                return getGlobalMSPT();
            }
            
            Object tickHandle = region.getClass()
                    .getMethod("getTickHandle")
                    .invoke(region);
            
            Object mspt5sObj = tickHandle.getClass()
                    .getMethod("getMspt5s")
                    .invoke(tickHandle);
            double mspt_5s = (Double) mspt5sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt5sObj);
            
            Object mspt15sObj = tickHandle.getClass()
                    .getMethod("getMspt15s")
                    .invoke(tickHandle);
            double mspt_15s = (Double) mspt15sObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt15sObj);
            
            Object mspt1mObj = tickHandle.getClass()
                    .getMethod("getMspt1m")
                    .invoke(tickHandle);
            double mspt_1m = (Double) mspt1mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt1mObj);
            
            Object mspt5mObj = tickHandle.getClass()
                    .getMethod("getMspt5m")
                    .invoke(tickHandle);
            double mspt_5m = (Double) mspt5mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt5mObj);
            
            Object mspt15mObj = tickHandle.getClass()
                    .getMethod("getMspt15m")
                    .invoke(tickHandle);
            double mspt_15m = (Double) mspt15mObj.getClass()
                    .getMethod("getAverage")
                    .invoke(mspt15mObj);
            
            return List.of(mspt_5s, mspt_5s, mspt_15s, mspt_1m, mspt_5m, mspt_15m);
        } catch (Exception e) {
            return getGlobalMSPT();
        }
    }
}
