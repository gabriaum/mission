package com.gabriaum.mission.mission.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public enum MissionType {

    BREAK_BLOCKS("Quebre 40 blocos", 40, (players -> {

        for (UUID uniqueId : players) {

            Player player = Bukkit.getPlayer(uniqueId);

            if (player != null) {

                player.setLevel(player.getLevel() + 1);
                player.sendMessage("§aBoaaaa!");
            }
        }
    })),
    ;

    private final String name;

    private final int limit;

    private final EventAction winAction;

    public static MissionType getByName(String name) {

        for (MissionType mission : values()) {

            if (mission.getName().equalsIgnoreCase(name)) {

                return mission;
            }
        }

        return null;
    }

    public interface EventAction {

        void execute(Set<UUID> players);
    }
}