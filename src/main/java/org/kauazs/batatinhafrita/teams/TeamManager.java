package org.kauazs.batatinhafrita.teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {
    public void setPlayerTeam(Player player, TeamsType teamType) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        String teamName = "";
        String teamPrefix = "";
        ChatColor teamColor = ChatColor.WHITE;
        if (teamType.equals(TeamsType.PLAYER)) {
            teamName = "Jogador";
            teamPrefix = ChatColor.GREEN + "[Jogador] ";
            teamColor = ChatColor.GREEN;
        } else if (teamType.equals(TeamsType.GUARD)){
            teamName = "Guarda";
            teamPrefix = ChatColor.RED + "[Guarda] ";
            teamColor = ChatColor.RED;
        }

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(teamPrefix);
        }

        team.setColor(teamColor);
        team.addEntry(player.getName());
        team.setSuffix("");

        player.setScoreboard(scoreboard);
    }

    public void removeScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }

        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
