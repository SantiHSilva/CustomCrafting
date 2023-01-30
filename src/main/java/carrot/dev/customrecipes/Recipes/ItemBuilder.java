package carrot.dev.customrecipes.Recipes;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static carrot.dev.customrecipes.Main.chatColor;

public class ItemBuilder {
    protected ItemStack is;
    protected ItemMeta im;
    protected LeatherArmorMeta leather;

    public ItemBuilder() {
        this(Material.AIR);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.is = itemStack;
    }

    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder setArrowPotion(PotionEffectType potionEffect, int duration, int amplifier) {
        PotionMeta meta = (PotionMeta) this.is.getItemMeta();
        meta.addCustomEffect(new PotionEffect(potionEffect, duration, amplifier), true);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addBookEnchant(Enchantment enchantment, int level)
    {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.is.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        this.is.setItemMeta(meta);
        return this;
    }

    // En no premium no funciona a no ser que pongas skin restorer

    public ItemBuilder setPlayerSkullOwner(String owner) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(owner);
        SkullMeta meta = (SkullMeta) this.is.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(target.getUniqueId()));
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addCustomPotionEffect(PotionEffectType potionType, int duration, int level, boolean hasExtendedDuration) {
        PotionMeta meta = (PotionMeta) this.is.getItemMeta();
        meta.addCustomEffect(new PotionEffect(potionType, duration * 20, level, false, hasExtendedDuration), true);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setPotionColorRGB(int R, int G, int B) {
        PotionMeta meta = (PotionMeta) this.is.getItemMeta();
        meta.setColor(Color.fromRGB(R, G, B));
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        this.im = this.is.getItemMeta();
        this.im.setCustomModelData(data);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean b) {
        this.im = this.is.getItemMeta();
        this.im.setUnbreakable(b);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.im = this.is.getItemMeta();
        this.im.setDisplayName(chatColor(name));
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.im = this.is.getItemMeta();
        if (this.im instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) this.im).addStoredEnchant(enchantment, level, true);
        } else {
            this.im.addEnchant(enchantment, level, true);
        }
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        this.im = this.is.getItemMeta();
        if (!enchantments.isEmpty())
            for (Enchantment ench : enchantments.keySet())
                this.im.addEnchant(ench, enchantments.get(ench), true);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemflag) {
        this.im = this.is.getItemMeta();
        this.im.addItemFlags(itemflag);
        this.is.setItemMeta(this.im);
        return this;
    }

    public static List<String> buildLore(String... lore)
    {
        List<String> loreList = new ArrayList<>();
        for (String s : lore)
        {
            loreList.add(chatColor(s));
        }
        return loreList;
    }

    public ItemBuilder setListLore(List<String> lore) {
        this.im = this.is.getItemMeta();
        this.im.setLore(lore);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.im = this.is.getItemMeta();
        List<String> finalLore = new ArrayList<>();
        for (String s : lore) finalLore.add(chatColor(s));
        this.im.setLore(finalLore);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addAttributeModifier (Attribute attribute, AttributeModifier attributeModifier) {
        this.im = this.is.getItemMeta();
        this.im.addAttributeModifier(attribute, attributeModifier);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addAttributeModifier (Attribute attribute, double value, AttributeModifier.Operation operation, EquipmentSlot slot) {
        this.im = this.is.getItemMeta();
        this.im.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.getKey().getNamespace(), value, operation, slot));
        this.is.setItemMeta(this.im);
        return this;
    }
    public ItemBuilder setLeatherColor(int red, int green, int blue){
        this.leather = (LeatherArmorMeta) this.is.getItemMeta();
        this.leather.setColor(Color.fromRGB(red,green,blue));
        this.is.setItemMeta(this.leather);
        return this;
    }

    public ItemStack build() {
        return this.is;
    }

}

