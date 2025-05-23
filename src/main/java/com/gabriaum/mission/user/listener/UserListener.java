package com.gabriaum.mission.user.listener;

import com.gabriaum.mission.MissionMain;
import com.gabriaum.mission.user.User;
import com.gabriaum.mission.user.assets.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {

        Player player = event.getPlayer();

        try {

            User user = new User(player.getUniqueId(), player.getName());

            user.setPlayer(player);

            MissionMain.getInstance().getUserController().register(user);
        } catch (Exception ex) {

            throw new RuntimeException("[gMission] Error while trying to login player " + player.getName(), ex);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        try {

            User user = User.getUser(player.getUniqueId());

            if (user != null && user.hasParty()) {

                Party party = user.getParty();

                party.removeMember(player.getUniqueId());

                for (UUID uniqueId : party.getMembers()) {

                    User member = User.getUser(uniqueId);

                    if (member != null) {

                        member.setParty(party);
                    }
                }
            }

            MissionMain.getInstance().getUserController().remove(player.getUniqueId());
            MissionMain.getInstance().getMissionController().stream().filter(mission -> mission.getParticipants().contains(player.getUniqueId())).forEach(mission -> mission.getParticipants().remove(player.getUniqueId()));
        } catch (Exception ex) {

            throw new RuntimeException("[gMission] Error while trying to quit player " + player.getName(), ex);
        }
    }
}
