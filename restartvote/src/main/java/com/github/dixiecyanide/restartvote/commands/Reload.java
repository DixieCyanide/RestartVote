/*
 * RestartVote, vote for restart
 * Copyright 2024 (C) DixieCyanide
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.dixiecyanide.restartvote.commands;

import com.github.dixiecyanide.restartvote.RestartVote;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class Reload implements CommandExecutor {
    private static final Plugin plugin = RestartVote.getPlugin(RestartVote.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rvote.reload") && !sender.isOp()) {
            return false;
        }

        plugin.reloadConfig();

        if (sender instanceof Player) {                                  // just because i want fancy colors here and there 
            sender.sendMessage(ChatColor.DARK_PURPLE + "Plugin reloaded.");
        } else {
            sender.sendMessage("\033[0;35m" + "Plugin reloaded." + "\033[0m");
        }
        return true;
    }
}
