package com.greazi.plugin.util;

import com.greazi.plugin.Common;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * A Util that returns a itemstack with a custom model ID
 */
public class CustomMaterial {

	public static ItemStack get(final Material material, final int id, final String name) {
		final ItemStack itemStack = new ItemStack(material);
		final ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setCustomModelData(id);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(Common.colorizeLegacy(name));

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	public static ItemStack get(final Material material, final int id, final String name, final Boolean enchanted) {
		final ItemStack itemStack = new ItemStack(material);
		final ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setCustomModelData(id);
		if (enchanted) {
			itemMeta.addEnchant(Enchantment.MENDING, 1, false);
		}
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		itemMeta.setDisplayName(Common.colorizeLegacy(name));

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	public static ItemStack get(final Material material, final int id, final String name, @NonNull final String... lore) {
		final ItemStack itemStack = new ItemStack(material);
		final ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setCustomModelData(id);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(Common.colorizeLegacy(name));
		itemMeta.setLore(Common.colorizeLegacy(Arrays.asList(lore)));

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	public static ItemStack get(final Material material, final int id, final String name, final Boolean enchanted, @NonNull final String... lore) {
		final ItemStack itemStack = new ItemStack(material);
		final ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setCustomModelData(id);
		if (enchanted) {
			itemMeta.addEnchant(Enchantment.MENDING, 1, false);
		}
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		itemMeta.setDisplayName(Common.colorizeLegacy(name));
		itemMeta.setLore(Common.colorizeLegacy(Arrays.asList(lore)));

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}
}
