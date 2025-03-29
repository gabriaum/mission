package com.gabriaum.mission.listener;

import com.gabriaum.mission.MissionMain;
import com.gabriaum.mission.mission.Mission;
import com.gabriaum.mission.mission.type.MissionType;
import com.gabriaum.mission.user.User;
import com.gabriaum.mission.user.assets.Party;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getInventory();

        if (inventory != null && inventory.getTitle().equalsIgnoreCase("Missões")) {

            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();

            if (item != null && !item.getType().equals(Material.AIR)) {

                ItemMeta meta = item.getItemMeta();

                if (!meta.hasDisplayName()) {
                    return;
                }

                MissionType type = MissionType.getByName(meta.getDisplayName().replace("§a", ""));

                if (type != null) {

                    User user = User.getUser(player.getUniqueId());

                    if (user == null) {
                        return;
                    }

                    Party party = user.getParty();

                    player.closeInventory();
                    player.sendMessage("§eIniciando a missão...");

                    try {

                        Mission mission = new Mission(type);

                        if (party != null && !party.getMembers().isEmpty()) {

                            mission.getParticipants().addAll(party.getMembers());
                        }

                        MissionMain.getInstance().getMissionController().add(mission);
                    } catch (Exception ex) {

                        ex.printStackTrace();

                        player.sendMessage("§cOcorreu um erro ao iniciar a missão.");
                    }
                }
            }
        }
    }
}
