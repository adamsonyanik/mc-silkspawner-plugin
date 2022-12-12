package io.github.adamson;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SpawnerBreakEventListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.SPAWNER)
            return;

        if (!event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
            return;

        CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();

        event.setExpToDrop(0);

        ItemStack spawnerItem = new ItemStack(event.getBlock().getType(), 1);
        ItemMeta spawnerMeta = spawnerItem.getItemMeta();
        spawnerMeta.setDisplayName(ChatColor.RESET +
                Arrays.stream(creatureSpawner.getSpawnedType().getName().replace("_", " ").split(" "))
                        .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining(" ")) + " Spawner");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(creatureSpawner.getSpawnedType().getName());
        spawnerMeta.setLore(lore);
        spawnerItem.setItemMeta(spawnerMeta);

        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), spawnerItem);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.SPAWNER)
            return;

        CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlockPlaced().getState();
        try {
            creatureSpawner.setCreatureTypeByName(event.getItemInHand().getItemMeta().getLore().get(0));
        } catch (Exception ignored) {
        }
        creatureSpawner.update();
    }
}
