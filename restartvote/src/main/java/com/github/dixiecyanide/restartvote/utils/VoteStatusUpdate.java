package com.github.dixiecyanide.restartvote.utils;

import com.github.dixiecyanide.restartvote.RestartVote;

import net.kyori.adventure.audience.Audience;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class VoteStatusUpdate extends TimerTask{
    private RestartVote plugin;
    private Integer cycles;
    private Timer timer;
    private Integer votedPlayerCount;
    private Integer eligiblePlayerCount;
    private Audience audience;
    private Integer voteDuration;
    private boolean isFake;

    public VoteStatusUpdate(Timer timer, Integer cycles, boolean isFake) {
        this.timer = timer;
        this.plugin = RestartVote.getPlugin(RestartVote.class);
        this.voteDuration = plugin.getConfig().getInt("VoteDuration");
        this.cycles = cycles;
        this.audience = plugin.getAudience();
        this.isFake = isFake;
        plugin.setBossBar(Utils.getBossBar(cycles));
    }

    public void run() {
        if (!plugin.getIsVoting()) {
            this.cycles = -1; 
        }

        if (cycles > 0) {
            if (cycles % 30 == 0) {
                this.votedPlayerCount = plugin.getVotedPlayerCount();
                this.eligiblePlayerCount = Utils.getEligiblePlayerCount();
                String message;

                    if (Utils.isOverThreshold(votedPlayerCount)) {
                        message = String.format(ChatColor.GREEN + "[%d/%d] " +
                                                        ChatColor.DARK_PURPLE + "players want to restart server.",
                                                        votedPlayerCount, eligiblePlayerCount);
                    } else {
                        message = String.format(ChatColor.YELLOW + "[%d/%d] " +
                                                        ChatColor.DARK_PURPLE + "players want to restart server.",
                                                        votedPlayerCount, eligiblePlayerCount);
                    }
                
                Bukkit.broadcastMessage(message);
            }
            
            plugin.setBossBar(Utils.updateBossBarTime(voteDuration, cycles, plugin.getBossBar()));
            audience.showBossBar(plugin.getBossBar());
            cycles--;
        } else if (cycles == -1){
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Vote has been canceled by outside forces.");
            cancelVote();
        } else {
            if (Utils.isOverThreshold(votedPlayerCount)) {
                Bukkit.broadcastMessage(String.format(ChatColor.GREEN +
                                        "Vote ended, [%d/%d] players voted in favor, restarting server in 3 minutes."
                                        , votedPlayerCount
                                        , eligiblePlayerCount));

                cancelVote();
                if (!isFake) {
                    Utils.restartSequence();
                } else {
                    isFake = false;
                    Bukkit.broadcastMessage(ChatColor.AQUA + "HA-HA, BENIS!");
                }
            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "Vote ended, not enough players voted in favor.");
                cancelVote();
            }
        }
    }

    private void cancelVote() {
        audience.hideBossBar(plugin.getBossBar());
        //plugin.setBossBar(null);
        plugin.setVotedPlayerUUID(new ArrayList<UUID>());
        plugin.setVotedPlayerCount(0);
        plugin.setIsVoting(false);

        timer.cancel();
    }
}
