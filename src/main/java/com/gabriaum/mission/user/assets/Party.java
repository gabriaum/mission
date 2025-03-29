package com.gabriaum.mission.user.assets;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Party {

    protected final UUID ownerId;

    private Set<UUID> members = new HashSet<>();

    public void sendMessage(String message) {

        for (UUID memberId : members) {

            Player member = Bukkit.getPlayer(memberId);

            member.sendMessage("§d[PARTY] §f" + message);
        }
    }

    public void addMember(UUID memberId) {
        members.add(memberId);
    }

    public void removeMember(UUID memberId) {
        members.remove(memberId);
    }
}
