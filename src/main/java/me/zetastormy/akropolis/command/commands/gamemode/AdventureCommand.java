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

package me.zetastormy.akropolis.command.commands.gamemode;

import me.zetastormy.akropolis.AkropolisPlugin;
import me.zetastormy.akropolis.Permissions;
import me.zetastormy.akropolis.command.InjectableCommand;
import me.zetastormy.akropolis.config.Message;
import me.zetastormy.akropolis.util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdventureCommand extends InjectableCommand {

    public AdventureCommand(AkropolisPlugin plugin, List<String> aliases) {
        super(plugin, "gma", "Change to adventure mode", "/gma [player]", aliases);
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                Message.CONSOLE_NOT_ALLOWED.sendFrom(sender);
                return;
            }

            if (!player.hasPermission(Permissions.COMMAND_GAMEMODE.getPermission())) {
                Message.NO_PERMISSION.sendFrom(player);
                return;
            }

            Message.GAMEMODE_CHANGE.sendFromWithReplacement(player, "gamemode", Component.text("ADVENTURE"));
            player.setGameMode(GameMode.ADVENTURE);
        } else if (args.length == 1) {
            if (!sender.hasPermission(Permissions.COMMAND_GAMEMODE_OTHERS.getPermission())) {
                Message.NO_PERMISSION.sendFrom(sender);
                return;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                Message.INVALID_PLAYER.sendFromWithReplacement(sender, "player", Component.text(args[0]));
                return;
            }

            if (sender.getName().equals(player.getName())) {
                Message.GAMEMODE_CHANGE.sendFromWithReplacement(player, "gamemode", Component.text("ADVENTURE"));
            } else {
                Message.GAMEMODE_CHANGE.sendFromWithReplacement(player, "gamemode", Component.text("ADVENTURE"));
                sender.sendMessage(TextUtil.replace(TextUtil.replace(Message.GAMEMODE_CHANGE_OTHER.toComponent(), "player", player.name()), "gamemode", Component.text("ADVENTURE")));
            }

            player.setGameMode(GameMode.ADVENTURE);
        }

    }
}
