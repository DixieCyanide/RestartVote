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

package com.github.dixiecyanide.restartvote;

import com.github.dixiecyanide.restartvote.commands.Reload;
import com.github.dixiecyanide.restartvote.commands.Vote;
import com.github.dixiecyanide.restartvote.commands.VoteNegative;
import com.github.dixiecyanide.restartvote.commands.VotePositive;
import com.github.dixiecyanide.restartvote.commands.VoteRestart;
import com.github.dixiecyanide.restartvote.commands.VoteStop;
import com.github.dixiecyanide.restartvote.commands.VoteTest;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.bossbar.BossBar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RestartVote extends JavaPlugin {
    private boolean isVoting = false;
    private Integer votedPlayersCount = 0;
    private List<UUID> votedPlayersUUID = new ArrayList<UUID>();
    private ForwardingAudience audience;
    private BossBar bossBar;
    
    @Override
    public void onDisable() {
        getLogger().info("\033[0;35m" + "Goodbye!" + "\033[0m");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.audience = (ForwardingAudience) Bukkit.getServer();
        getCommand("vote").setExecutor(new Vote(this));
        getCommand("vote-reload").setExecutor(new Reload());
        getCommand("vote-restart").setExecutor(new VoteRestart(this.audience));
        getCommand("vote-test").setExecutor(new VoteTest(this.audience));
        getCommand("vote-positive").setExecutor(new VotePositive());
        getCommand("vote-negative").setExecutor(new VoteNegative());
        getCommand("vote-stop").setExecutor(new VoteStop(this.audience));
        getConfig().options().copyDefaults(true);
        saveConfig();
        getLogger().info("\033[0;92m" + "RestartVote enabled!" + "\033[0m");
    }

    public void setVotedPlayerCount(Integer count) {
        this.votedPlayersCount = count;
    }
    public Integer getVotedPlayerCount() {
        return this.votedPlayersCount;
    }

    public void setVotedPlayerUUID(ArrayList<UUID> arrayList) {
        this.votedPlayersUUID = arrayList;
    }

    public List<UUID> getVotedPlayerUUID() {
        return this.votedPlayersUUID;
    }

    public void setIsVoting(boolean bool) {
        this.isVoting = bool;
    }

    public boolean getIsVoting() {
        return this.isVoting;
    }

    public void setAudience(ForwardingAudience audience) {
        this.audience = audience;
    }
    
    public Audience getAudience() {
        return (Audience) this.audience;
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public BossBar getBossBar() {
        return this.bossBar;
    }
}