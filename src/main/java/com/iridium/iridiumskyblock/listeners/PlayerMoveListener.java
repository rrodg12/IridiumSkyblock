package com.iridium.iridiumskyblock.listeners;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            final Player player = event.getPlayer();
            final User user = User.getUser(player);
            final Island userIsland = user.getIsland();
            final Location location = player.getLocation();
            final IslandManager islandManager = IridiumSkyblock.getIslandManager();
            if (!islandManager.isIslandWorld(location)) return;

            final Config config = IridiumSkyblock.getConfiguration();

            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ() || event.getFrom().getY() != event.getTo().getY() && event.getTo().getY() < 0) {
                final Island island = islandManager.getIslandViaLocation(location);

                if (island != null && !island.isVisit() && !island.equals(userIsland) && !island.isCoop(userIsland) && !user.bypassing) {
                    island.spawnPlayer(event.getPlayer());
                    return;
                }

                if (location.getY() < 0 && config.voidTeleport) {
                    final World world = location.getWorld();
                    if (world == null) return;

                    if (island != null) {
                        if (!IridiumSkyblock.getConfiguration().keepInventoryOnVoid) player.getInventory().clear();
                        if (world.getName().equals(islandManager.getWorld().getName()))
                            island.teleportHome(player);
                        else
                            island.teleportNetherHome(player);
                    } else {
                        if (userIsland != null) {
                            if (world.getName().equals(islandManager.getWorld().getName()))
                                userIsland.teleportHome(player);
                            else if (world.getName().equals(islandManager.getNetherWorld().getName()))
                                userIsland.teleportNetherHome(player);
                        } else if (islandManager.isIslandWorld(world)) {
                            if (Bukkit.getPluginManager().isPluginEnabled("EssentialsSpawn")) {
                                final PluginManager pluginManager = Bukkit.getPluginManager();
                                final EssentialsSpawn essentialsSpawn = (EssentialsSpawn) pluginManager.getPlugin("EssentialsSpawn");
                                final Essentials essentials = (Essentials) pluginManager.getPlugin("Essentials");
                                if (essentials != null && essentialsSpawn != null)
                                    player.teleport(essentialsSpawn.getSpawn(essentials.getUser(player).getGroup()));
                            } else
                                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                        }
                    }
                }
            }
            if (userIsland == null) return;

            if (user.flying
                    && (!userIsland.isInIsland(location) || userIsland.getFlightBooster() == 0)
                    && !player.getGameMode().equals(GameMode.CREATIVE)
                    && !(player.hasPermission("IridiumSkyblock.Fly")
                    || player.hasPermission("iridiumskyblock.fly"))) {
                player.setAllowFlight(false);
                player.setFlying(false);
                user.flying = false;
                player.sendMessage(Utils.color(IridiumSkyblock.getMessages().flightDisabled
                        .replace("%prefix%", config.prefix)));
            }
        } catch (Exception e) {
            IridiumSkyblock.getInstance().sendErrorMessage(e);
        }
    }
}
