package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.configs.Missions.Mission;
import com.iridium.iridiumskyblock.configs.Missions.MissionData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class PlayerFishListener implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        try {
            final Player player = event.getPlayer();
            final Location location = player.getLocation();
            final IslandManager islandManager = IridiumSkyblock.getIslandManager();
            if (!islandManager.isIslandWorld(location)) return;

            if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

            final User user = User.getUser(player);
            final Island island = user.getIsland();
            if (island == null) return;

            for (Mission mission : IridiumSkyblock.getMissions().missions) {
                final Map<String, Integer> levels = island.getMissionLevels();
                levels.putIfAbsent(mission.name, 1);

                final MissionData level = mission.levels.get(levels.get(mission.name));
                if (level.type == MissionType.FISH_CATCH)
                    island.addMission(mission.name, 1);
            }
        } catch (Exception e) {
            IridiumSkyblock.getInstance().sendErrorMessage(e);
        }
    }
}
