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
import com.github.dixiecyanide.restartvote.utils.VoteStatusUpdate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.event.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteTest implements CommandExecutor {
    private RestartVote plugin;
    private Integer voteDuration;
    private Audience audience;

    public VoteTest(ForwardingAudience audience) {
        this.audience = (Audience) audience;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rvote.test") && !sender.isOp()) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }
        plugin = (RestartVote) RestartVote.getPlugin(RestartVote.class);

        if (plugin.getIsVoting()) {
            sender.sendMessage(ChatColor.RED + "Sorry, you can't start more than one vote.");
            return true;
        }
        
        plugin.setIsVoting(true);
        voteDuration = plugin.getConfig().getInt("VoteDuration");
        Integer threshold = plugin.getConfig().getInt("Threshold");
        
        String percentVariable = String.format("\nNeed at least %d%% of votes in favor to restart server.", threshold);
        
        // I FUCKING HATE HOW IT LOOKS
        // BUT I FUCKING LOVE HOW WELL IT WORKS
        
        audience.sendMessage(Component
                .text("Vote for restart begins!\n" +
                        "Do you want to restart server? ")
                .color(NamedTextColor.DARK_PURPLE)
                .append(Component.text("[Yes]")
                        .color(NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Vote for restart")
                                .color(NamedTextColor.GREEN)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/vote-positive")))
                .append(Component.text("/")
                        .color(NamedTextColor.DARK_PURPLE))
                .append(Component.text("[No]")
                        .color(NamedTextColor.RED)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Vote against restart")
                                .color(NamedTextColor.RED)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/vote-negative")))
                .append(Component.text(percentVariable)
                        .color(NamedTextColor.DARK_PURPLE))
                );

        Timer timer = new Timer();
        timer.schedule(new VoteStatusUpdate(timer, voteDuration, true), 0 * 1000, 1 * 1000);

        return true;
    }
}