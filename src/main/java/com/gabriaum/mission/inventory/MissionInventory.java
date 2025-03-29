package com.gabriaum.mission.inventory;

import com.gabriaum.mission.mission.type.MissionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MissionInventory {

    public static void handle(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 9 * 3, "Missões");

        int slot = 10;

        for (MissionType mission : MissionType.values()) {

            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§a" + mission.getName());
            meta.setLore(Arrays.asList("§eClique para iniciar!"));

            item.setItemMeta(meta);

            inventory.setItem(slot, item);
        }

        player.openInventory(inventory);
    }
}
