package com.gabriaum.mission.mission;

import com.gabriaum.mission.mission.stage.MissionStage;
import com.gabriaum.mission.mission.type.MissionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class Mission {

    private final MissionType type;

    private MissionStage stage = MissionStage.STARTING;

    private int time = 5;
    private int accumulated = 0;

    private final Set<UUID> participants = new HashSet<>();

    public void sendMessage(String message) {

        participants.forEach(uuid -> {

            Player player = Bukkit.getPlayer(uuid);

            player.sendMessage(message);
        });
    }

    public void sendTitle(String title, String subtitle) {

        participants.forEach(uuid -> {

            Player player = Bukkit.getPlayer(uuid);

            player.sendTitle(new Title(title, subtitle, 10, 8, 10));
        });
    }

    public void playSound(Sound sound) {

        participants.forEach(uuid -> {

            Player player = Bukkit.getPlayer(uuid);

            player.playSound(player.getLocation(), sound, 1f, 1f);
        });
    }
}