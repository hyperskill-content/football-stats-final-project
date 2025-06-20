package com.hyperskill.factory;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Player;
import com.hyperskill.utils.FootballDataLoader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface PlayerFactory {

    Player createPlayer(String firstName, String lastName, String team);

    Set<Player> createPlayers();

    Set<Player> createPlayersForTeam(String teamName);

    static PlayerFactory getFactory(String sourceType) {
        if ("file".equalsIgnoreCase(sourceType)) {
            return new FileBasedPlayerFactory();
        } else {
            return new SimplePlayerFactory();
        }
    }
}

class FileBasedPlayerFactory implements PlayerFactory {
    private String filePath = "src/main/java/com/hyperskill/resources/Generate_Data.txt"; // Default path
    private FootballDataLoader dataLoader;

    public FileBasedPlayerFactory() {
        this.dataLoader = new FootballDataLoader();
    }

    public FileBasedPlayerFactory(String filePath) {
        this.filePath = filePath;
        this.dataLoader = new FootballDataLoader();
    }

    @Override
    public Player createPlayer(String firstName, String lastName, String team) {
        Player player = new Player(firstName, lastName, team);
        FootballStatisticsDB.addPlayer(player);
        return player;
    }

    @Override
    public Set<Player> createPlayersForTeam(String teamName) {
        // Load data from file if not already loaded
        FootballStatisticsDB.loadInitialDataIfNeeded(filePath);

        // Get players from the database for the specified team
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
        // Load data from file if not already loaded
        FootballStatisticsDB.loadInitialDataIfNeeded(filePath);

        // Return all players from the database
        Collection<Player> allPlayers = FootballStatisticsDB.getPlayers();
        return new HashSet<>(allPlayers);
    }



}