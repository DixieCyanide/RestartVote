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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Vote implements CommandExecutor {
    private String version;
    private Integer playerCount;
    
    public Vote(RestartVote rv) {
        this.version = cleanVersion(rv.getDescription().getVersion());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rvote") && !sender.isOp()) {
            return false;
        }
        
        this.playerCount = Bukkit.getOnlinePlayers().size();
        String message = String.format("RestrartVote %s\n%d players can vote", version, playerCount);
        sender.sendMessage(ChatColor.DARK_PURPLE + message);
        return true;
    }
    
    private static String cleanVersion(String dirtyVersion) {
        return dirtyVersion.replaceAll("[^0-9.]", "");
    }
}