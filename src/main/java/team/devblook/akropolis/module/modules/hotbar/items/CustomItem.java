/*
 * This file is part of Akropolis
 *
 * Copyright (c) 2024 DevBlook Team and others
 *
 * Akropolis free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Akropolis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Akropolis. If not, see <http://www.gnu.org/licenses/>.
 */

package team.devblook.akropolis.module.modules.hotbar.items;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import team.devblook.akropolis.config.ConfigType;
import team.devblook.akropolis.config.Message;
import team.devblook.akropolis.module.modules.hotbar.HotbarItem;
import team.devblook.akropolis.module.modules.hotbar.HotbarManager;

import java.util.Collections;
import java.util.List;

public class CustomItem extends HotbarItem {
    private final String key;
    private final int cooldown;
    private final List<String> actions;

    public CustomItem(HotbarManager hotbarManager, ItemStack item, int slot, String key) {
        super(hotbarManager, item, slot, key);
        this.key = key;

        ConfigurationSection itemSettings = getPlugin().getConfigManager().getFile(ConfigType.SETTINGS).get()
                .getConfigurationSection("custom_join_items.items." + key);

        if (itemSettings == null) {
            cooldown = 0;
            actions = Collections.emptyList();
            return;
        }

        cooldown = itemSettings.getInt("cooldown", 0);
        actions = itemSettings.getStringList("actions");
    }

    @Override
    protected void onInteract(Player player) {
        if (!getHotbarManager().tryCooldown(player.getUniqueId(), key, cooldown)) {
            Message.COOLDOWN_ACTIVE.sendFromWithReplacement(player, "time", Component.text(getHotbarManager().getCooldown(player.getUniqueId(), key)));
            return;
        }

        getPlugin().getActionManager().executeActions(player, actions);
    }
}
