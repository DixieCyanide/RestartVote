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

//TODO: move clearVersion(), getPlayerCount(), etc here

package com.github.dixiecyanide.restartvote.utils;

import com.github.dixiecyanide.restartvote.RestartVote;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Utils {
    public static Integer getEligiblePlayerCount() {
        Integer count = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("rvote.vote.*")) {
                count += 1;
            }
        }
        
        if (count == 0) {
            count = Bukkit.getOnlinePlayers().size();
        }

        return count;
    }

    public static UUID getSenderUUID(CommandSender sender) {
        return Bukkit.getPlayer(sender.getName()).getUniqueId();
    }

    public static boolean isOverThreshold (Integer voted) {
        RestartVote plugin = (RestartVote) RestartVote.getPlugin(RestartVote.class);
        Float votedPercent = (float)voted / getEligiblePlayerCount();
        Float threshold = (float)plugin.getConfig().getInt("Threshold") / 100;
        
        if (votedPercent >= threshold) {
            return true;
        }
        return false;
    }

    public static BossBar getBossBar(Integer voteDuration) {
        String timeLeft = timeConvert(voteDuration);
        Component name = Component.text(String.format("Restart vote ends in: %s", timeLeft))
                                            .color(NamedTextColor.DARK_PURPLE);
        BossBar bossBar = BossBar.bossBar(name, 1f, Color.PURPLE, BossBar.Overlay.NOTCHED_10);
        return bossBar;
    }

    public static BossBar updateBossBarTime(Integer voteDuration, Integer voteDurationLeft, BossBar bb) {
        String timeLeft = timeConvert(voteDurationLeft);
        Component newName = Component.text(String.format("Restart vote ends in: %s", timeLeft))
                                            .color(NamedTextColor.DARK_PURPLE);
        bb.name(newName);
        bb.progress((float) voteDurationLeft / voteDuration);
        return bb;
    }

    public static String timeConvert(Integer voteDuration) {
        Integer hours = voteDuration / 3600;
        Integer minutes = voteDuration / 60;
        Integer seconds = voteDuration % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        }

        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        }
        
        return String.format("%ds", seconds);
    }

    public static void restartSequence() {
        try {
            Thread.sleep(180 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("RestartVote"), () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
            });
    }
}
