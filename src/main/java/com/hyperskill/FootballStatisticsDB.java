package com.hyperskill;

import com.hyperskill.data_models.Coach;
import com.hyperskill.data_models.Match;
import com.hyperskill.data_models.Player;
import com.hyperskill.data_models.Team;
import com.hyperskill.utils.FootballDataLoader;

import java.util.*;

public class FootballStatisticsDB {
    private final static Map<String, Player> players = new HashMap<>();
    private final static Map<String, Coach> coaches = new HashMap<>();
    private final static Map<String, Team> teams = new HashMap<>();
    private final static Map<String, Match> matches = new HashMap<>();
    private static boolean initialDataLoaded = false;

    // Create
    public static void addPlayer(Player player) {
        if (player == null) {
            System.out.println("player is null.");
            return;
        }

        players.put(player.getId(), player);
    }

    // Read
    public static Player getPlayer(String id) {
        if (!players.containsKey(id)) {
            return null;
        }
        return players.get(id);
    }

    public static Player getPlayerByName(String firstName, String lastName) {
        for (Player player : players.values()) {
            if (player.getFirstName().equalsIgnoreCase(firstName) && player.getLastName().equalsIgnoreCase(lastName)) {
                return player;
            }
        }
        return null;
    }

    public static Collection<Player> getPlayers() {
        return players.values();
    }

    public static Collection<Player> getPlayersByTeam(String teamName) {
        Set<Player> playersTeam = new HashSet<>();
        for (Player player : players.values()) {
            if (player.getTeamName().equalsIgnoreCase(teamName)) {
                playersTeam.add(player);
            }
        }
        return playersTeam;
    }

    // Update
    public static void updatePlayerGoals(String id, int goals) {
        Player player = getPlayer(id);
        if (player != null) {
            player.setGoals(goals);
            players.put(id, player);
        }
    }

    public static void updatePlayerTeam(String id, String teamName) {
        Player player = getPlayer(id);
        if (player != null) {
            player.setTeamName(teamName);
            players.put(id, player);
        }
    }

    // Delete
    public static void deletePlayer(String id) {
        players.remove(id);
    }

    // Create
    public static void addCoach(Coach coach) {
        if (coach == null) {
            System.out.println("coach is null.");
            return;
        }
        coaches.put(coach.getId(), coach);
    }

    // Read
    public static Coach getCoachById(String id) {
        if (!coaches.containsKey(id)) {
            return null;
        }
        return coaches.get(id);
    }

    public static Collection<Coach> getCoaches() {
        return coaches.values();
    }

    public static Collection<Coach> getCoachesByTeam(String teamName) {
        Set<Coach> coachesTeam = new HashSet<>();
        for (Coach coach : coaches.values()) {
            if (coach.getTeamName().equalsIgnoreCase(teamName)) {
                coachesTeam.add(coach);
            }
        }
        return coachesTeam;
    }

    public static Coach getCoachByName(String firstName, String lastName) {
        for (Coach coach : coaches.values()) {
            if (coach.getFirstName().equalsIgnoreCase(firstName) && coach.getLastName().equalsIgnoreCase(lastName)) {
                return coach;
            }
        }
        return null;
    }

    public static boolean updateCoach(Coach coach) {
        if (coach == null || !coaches.containsKey(coach.getId())) {
            return false;
        }
        coaches.put(coach.getId(), coach);
        return true;
    }

    // Update
    public static void updateCoachTeam(String id, String teamName) {
        Coach coach = getCoachById(id);
        if (coach != null) {
            coach.setTeamName(teamName);
            coaches.put(id, coach);
        }
    }

    // Delete
    public static boolean deleteCoach(String id) {
        if (coaches.containsKey(id)) {
            coaches.remove(id);
            return true;
        }
        return false;
    }

    // Create
    public static void addTeam(Team team) {
        if (team == null) {
            System.out.println("Coach is null.");
            return;
        }
        teams.put(team.getId(), team);
    }

    // Read
    public static Team getTeamById(String id) {
        if (!teams.containsKey(id)) {
            return null;
        }
        return teams.get(id);
    }

    public static Team getTeamByName(String teamName) {
        for (Team team : teams.values()) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }

    public static Collection<Team> getTeams() {
        return teams.values();
    }

    // Update
    public static void updateTeamName(String id, String name) {
        Team team = getTeamById(id);
        if (team != null) {
            team.setName(name);
            teams.put(id, team);
        }
    }

    // Delete
    public static void deleteTeam(String id) {
        teams.remove(id);
    }

    // CRUD operations for Matches
    public static void addMatch(Match match) {
        if (match == null) {
            System.out.println("match is null.");
            return;
        }

        matches.put(match.getId(), match);
        // This call to updateStats() updates the match regardless of the match's origin.
        match.updateStats();
    }

    public static Collection<Match> getMatches() {
        return new HashSet<>(matches.values());
    }

    /**
     * Load initial data from source file if not already loaded
     */
    public static void loadInitialDataIfNeeded(String filePath) {
        if (!initialDataLoaded) {
            FootballDataLoader loader = new FootballDataLoader();
            loader.loadData(filePath);
            initialDataLoaded = true;
        }
    }

    /**
     * Reset the database to allow reloading initial data
     */
    public static void reset() {
        players.clear();
        coaches.clear();
        teams.clear();
        matches.clear();
        initialDataLoaded = false;
    }
}
