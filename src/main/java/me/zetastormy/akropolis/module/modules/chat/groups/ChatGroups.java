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

package me.zetastormy.akropolis.module.modules.chat.groups;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.zetastormy.akropolis.AkropolisPlugin;
import me.zetastormy.akropolis.config.ConfigType;
import me.zetastormy.akropolis.module.Module;
import me.zetastormy.akropolis.module.ModuleType;
import me.zetastormy.akropolis.util.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class ChatGroups extends Module {
    private final Map<String, ChatGroup> chatGroups = new HashMap<>();

    public ChatGroups(AkropolisPlugin plugin) {
        super(plugin, ModuleType.CHAT_FORMAT);
    }

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig(ConfigType.SETTINGS);
        ConfigurationSection groupsSection = config.getConfigurationSection("groups");

        if (groupsSection == null) {
            getPlugin().getLogger().info("Skipping chat groups creation, configuration section is missing!");
            return;
        }

        groupsSection.getKeys(false).stream()
                .filter(key -> !key.equals("enabled"))
                .forEach(groupName -> chatGroups.put(groupName, new ChatGroup(groupName,
                groupsSection.getString(groupName + ".format", "No format."),
                groupsSection.getInt(groupName + ".cooldown.time", 0),
                groupsSection.getString(groupName + ".cooldown.message", "No cooldown message."))));
    }

    @Override
    public void onDisable() {
        chatGroups.clear();
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        ChatGroup currentGroup = null;

        for (String group : chatGroups.keySet()) {
            if (player.hasPermission("akropolis.chat.group." + group)) {
                currentGroup = chatGroups.get(group);
            }
        }

        if (currentGroup == null) return;

        event.setCancelled(true);

        if (!tryCooldown(player.getUniqueId(), "chat", currentGroup.getCooldownTime())) {
            player.sendMessage(TextUtil.replace(currentGroup.getCooldownMessage(), "time", Component.text(getCooldown(player.getUniqueId(), "chat"))));
            return;
        }

        String rawMessage = TextUtil.raw(event.originalMessage());
        String parsedMessage = TextUtil.raw(LegacyComponentSerializer
                .legacySection()
                .deserialize(ChatColor.translateAlternateColorCodes('&', rawMessage)));

        getPlugin().getServer().sendMessage(TextUtil.replace(currentGroup.getFormat(player),
                "message",
                TextUtil.parse(parsedMessage)));
    }
}
