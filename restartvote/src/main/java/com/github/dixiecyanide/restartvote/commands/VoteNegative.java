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
import com.github.dixiecyanide.restartvote.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class VoteNegative implements CommandExecutor{
    RestartVote plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rvote.vote.negative") && !sender.isOp()) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        this.plugin = (RestartVote) RestartVote.getPlugin(RestartVote.class);
        UUID UUID = Utils.getSenderUUID(sender);

        if (!plugin.getIsVoting()) {
            sender.sendMessage(ChatColor.RED + "No vote is going on currently.");
            return true;
        }

        if (!plugin.getVotedPlayerUUID().contains(UUID)) {
            Integer votedPlayerCount = plugin.getVotedPlayerCount();
            Integer eligiblePlayerCount = Utils.getEligiblePlayerCount();
            ArrayList<UUID> UUIDs = new ArrayList<UUID>(plugin.getVotedPlayerUUID());
            UUIDs.add(UUID);
            plugin.setVotedPlayerUUID(UUIDs);
            
            String message = ChatColor.GREEN + "Thanks for your vote,\n";
            String message2 = String.format("[%d/%d]" + ChatColor.DARK_PURPLE +
                                            " players want to restart server.",
                                            votedPlayerCount, eligiblePlayerCount);
            
            if (Utils.isOverThreshold(votedPlayerCount)) {
                sender.sendMessage(message + ChatColor.GREEN + message2);
            } else {
                sender.sendMessage(message + ChatColor.YELLOW + message2);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Sorry, you can't vote more than one time.");
        }
        return true;
    }
}
