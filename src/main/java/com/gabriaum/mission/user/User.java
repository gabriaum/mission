package com.gabriaum.mission.user;

import com.gabriaum.mission.MissionMain;
import com.gabriaum.mission.user.assets.Party;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class User {

    protected final UUID uniqueId;

    protected final String name;

    private Player player;

    private Party party;

    private final Set<UUID> invites = new HashSet<>();

    public boolean hasParty() {
        return party != null;
    }

    public static User getUser(UUID uniqueId) {
        return MissionMain.getInstance().getUserController().get(uniqueId);
    }
}
