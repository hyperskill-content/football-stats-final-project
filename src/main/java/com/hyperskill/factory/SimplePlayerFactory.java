package com.hyperskill.factory;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SimplePlayerFactory implements PlayerFactory {

    @Override
    public Player createPlayer(String firstName, String lastName, String team) {
        Player player = new Player(firstName, lastName, team);
        FootballStatisticsDB.addPlayer(player);
        return player;
    }

    @Override
    public Set<Player> createPlayersForTeam(String teamName) {
        // Return players of the specified team from the database
        Collection<Player> allPlayers = FootballStatisticsDB.getPlayers();
        Set<Player> teamPlayers = new HashSet<>();

        for (Player player : allPlayers) {
            if (player.getTeam().getName().equalsIgnoreCase(teamName)) {
                teamPlayers.add(player);
            }
        }

        return teamPlayers;
    }

    @Override
    public Set<Player> createPlayers() {
        // Without hardcoded values, this method simply returns the existing players
        Collection<Player> allPlayers = FootballStatisticsDB.getPlayers();
        return new HashSet<>(allPlayers);
    }
}
