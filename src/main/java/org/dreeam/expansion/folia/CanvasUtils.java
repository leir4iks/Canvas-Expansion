package org.dreeam.expansion.canvas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class CanvasUtils {

    public boolean isCanvas = false;

    public void checkCanvas() {
        try {
            Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer");
            isCanvas = true;
        } catch (ClassNotFoundException e) {
            isCanvas = false;
        }
    }

    public List<Double> getGlobalTPS() {
        try {
            // Получаем глобальные TPS для всех интервалов времени
            Object server = Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer")
                    .getMethod("getInstance")
                    .invoke(null);
            
            Object globalTickHandle = server.getClass()
                    .getMethod("getGlobalTickHandle")
                    .invoke(server);
            
            double tps_5s = (Double) globalTickHandle.getClass()
                    .getMethod("getTps5s")
                    .invoke(globalTickHandle);
            
            double tps_10s = (Double) globalTickHandle.getClass()
                    .getMethod("getTps10s")
                    .invoke(globalTickHandle);
                    
            double tps_15s = (Double) globalTickHandle.getClass()
                    .getMethod("getTps15s")
                    .invoke(globalTickHandle);
            
            double tps_1m = (Double) globalTickHandle.getClass()
                    .getMethod("getTps1m")
                    .invoke(globalTickHandle);
            
            return List.of(tps_5s, tps_10s, tps_15s, tps_1m, tps_1m);
        } catch (Exception e) {
            // Fallback to server TPS if Canvas API fails
            double[] serverTps = Bukkit.getServer().getTPS();
            return List.of(serverTps[0], serverTps[0], serverTps[1], serverTps[2], serverTps[2]);
        }
    }

    public List<Double> getGlobalMSPT() {
        try {
            Object server = Class.forName("io.github.dueris.canvas.server.ThreadedBukkitServer")
                    .getMethod("getInstance")
                    .invoke(null);
            
            Object globalTickHandle = server.getClass()
                    .getMethod("getGlobalTickHandle")
                    .invoke(server);
            
            double mspt_5s = (Double) globalTickHandle.getClass()
                    .getMethod("getMspt5s")
                    .invoke(globalTickHandle);
            
            double mspt_10s = (Double) globalTickHandle.getClass()
                    .getMethod("getMspt10s")
                    .invoke(globalTickHandle);
                    
            double mspt_15s = (Double) globalTickHandle.getClass()
                    .getMethod("getMspt15s")
                    .invoke(globalTickHandle);
            
            double mspt_1m = (Double) globalTickHandle.getClass()
                    .getMethod("getMspt1m")
                    .invoke(globalTickHandle);
            
            return List.of(mspt_5s, mspt_10s, mspt_15s, mspt_1m, mspt_1m);
        } catch (Exception e) {
            // Fallback values
            return List.of(20.0, 20.0, 20.0, 20.0, 20.0);
        }
    }

    public List<Double> getTPS(Location location) {
        if (location == null) return getGlobalTPS();

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
            
            double tps_5s = (Double) tickHandle.getClass()
                    .getMethod("getTps5s")
                    .invoke(tickHandle);
            
            double tps_10s = (Double) tickHandle.getClass()
                    .getMethod("getTps10s")
                    .invoke(tickHandle);
                    
            double tps_15s = (Double) tickHandle.getClass()
                    .getMethod("getTps15s")
                    .invoke(tickHandle);
            
            double tps_1m = (Double) tickHandle.getClass()
                    .getMethod("getTps1m")
                    .invoke(tickHandle);
            
            return List.of(tps_5s, tps_10s, tps_15s, tps_1m, tps_1m);
        } catch (Exception e) {
            return getGlobalTPS();
        }
    }

    public List<Double> getMSPT(Location location) {
        if (location == null) return getGlobalMSPT();

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
            
            double mspt_5s = (Double) tickHandle.getClass()
                    .getMethod("getMspt5s")
                    .invoke(tickHandle);
            
            double mspt_10s = (Double) tickHandle.getClass()
                    .getMethod("getMspt10s")
                    .invoke(tickHandle);
                    
            double mspt_15s = (Double) tickHandle.getClass()
                    .getMethod("getMspt15s")
                    .invoke(tickHandle);
            
            double mspt_1m = (Double) tickHandle.getClass()
                    .getMethod("getMspt1m")
                    .invoke(tickHandle);
            
            return List.of(mspt_5s, mspt_10s, mspt_15s, mspt_1m, mspt_1m);
        } catch (Exception e) {
            return getGlobalMSPT();
        }
    }
}
