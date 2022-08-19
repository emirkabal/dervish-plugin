package com.emirkabal.dervish.core;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class CustomItem {
    private static ItemStack i;
    public static ItemStack eventItem = CustomItem.of(Material.BLAZE_ROD).withName("§4Event Item").get();


    public CustomItem(Material material, int amount, int damage) {
        if (amount == 0) {
            i = new ItemStack(material, 1, (short)damage);
        } else {
            i = new ItemStack(material, amount, (short)damage);
        }

    }
    public static boolean isEventItem(ItemStack item){
        if(item.getItemMeta().getDisplayName() == eventItem.getItemMeta().getDisplayName() && item.getType() == eventItem.getType()){
            return true;
        }else {
            return false;
        }
    }

    public static ItemStack getArrow() {
        ItemStack arrow = CustomItem.of(Material.ARROW).withName("§lEmax:§r I'm hate this item.").get();
        return arrow;
    }
    public static ItemStack getArrow(int amount) {
        ItemStack arrow = CustomItem.of(Material.ARROW, amount).withName("§lEmax:§r I'm hate this item.").get();
        return arrow;
    }

    public static ItemStack getFood() {
        ItemStack arrow = CustomItem.of(Material.GRILLED_PORK).withName("§7§lBattlegrounds§r Food").get();
        return arrow;
    }

    public static ItemStack getFood(int amount) {
        ItemStack arrow = CustomItem.of(Material.GRILLED_PORK, amount).withName("§7§lBattlegrounds§r Food").get();
        return arrow;
    }


    public static ItemStack getPlayerSkull(String owner, int amount) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short)3);
        SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
        itemMeta.setOwner(owner);
        item.setItemMeta(itemMeta);
        return item;
    }

    public CustomItem(Material material, int amount) {
        if (amount == 0) {
            i = new ItemStack(material, 1);
        } else {
            i = new ItemStack(material, amount);
        }

    }

    public CustomItem(Material material) {
        i = new ItemStack(material, 1);
    }

    public CustomItem(ItemStack itemStack) {
        i = itemStack;
    }

    public static CustomItem of(Material material, int amount, int damage) {
        return new CustomItem(material, amount, damage);
    }

    public static CustomItem of(Material material, int amount) {
        return new CustomItem(material, amount);
    }

    public static CustomItem of(Material material) {
        return new CustomItem(material, 1);
    }

    public static CustomItem of(ItemStack itemStack) {
        return new CustomItem(itemStack);
    }

    public CustomItem withEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = i.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        i.setItemMeta(meta);
        return this;
    }

    public CustomItem addUnsafeEnchant(Enchantment enchantment, int level) {
        i.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public CustomItem withName(String string) {
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(string.replace("&", "§"));
        i.setItemMeta(meta);
        return this;
    }

    public CustomItem setUnbreakable(boolean bool) {
        ItemMeta meta = i.getItemMeta();
        meta.spigot().setUnbreakable(bool);
        i.setItemMeta(meta);
        return this;
    }

    public CustomItem withLore(String... args) {
        ItemMeta meta = i.getItemMeta();
        meta.setLore(Arrays.asList(args));
        i.setItemMeta(meta);
        return this;
    }

    public CustomItem withLore(List<String> list) {
        ItemMeta meta = i.getItemMeta();

        for(int j = 0; j < list.size(); ++j) {
            String str = ((String)list.get(j)).replace("&", "§");
            list.set(j, str);
        }

        meta.setLore(list);
        i.setItemMeta(meta);
        return this;
    }

    public CustomItem infiniteDurability() {
        i.setDurability((short)-32768);
        return this;
    }

    public CustomItem setDurability(Short durability) {
        i.setDurability(durability);
        return this;
    }

    public ItemStack get() {
        return i;
    }

    public void setItemStack(ItemStack itemStack) {
        i = itemStack;
    }
}
