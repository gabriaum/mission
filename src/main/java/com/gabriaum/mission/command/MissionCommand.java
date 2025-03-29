package com.gabriaum.mission.command;

import com.gabriaum.mission.MissionMain;
import com.gabriaum.mission.inventory.MissionInventory;
import com.gabriaum.mission.mission.Mission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MissionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!(sender instanceof Player player)) {
            return false;
        }

        Mission mission = MissionMain.getInstance().getMissionController().stream().filter(m -> m.getParticipants().contains(player.getUniqueId())).findFirst().orElse(null);

        if (mission != null) {
            player.sendMessage("§cVocê já está em uma missão.");
            return false;
        }

        MissionInventory.handle(player);

        return true;
    }
}
