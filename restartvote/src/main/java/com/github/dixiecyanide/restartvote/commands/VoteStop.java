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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class VoteStop implements CommandExecutor {
    RestartVote plugin;
    Audience audience;

    public VoteStop(ForwardingAudience audience) {
        this.audience = audience;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rvote.stop") && !sender.isOp()) {
            return false;
        }

        this.plugin = RestartVote.getPlugin(RestartVote.class);
        plugin.setIsVoting(false);

        return true;
    }
}
