package com.hyperskill.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hyperskill.entity.Coach;
import com.hyperskill.entity.Player;
import com.hyperskill.entity.Team;
import com.hyperskill.entity.Match;
import com.hyperskill.FootballStatisticsDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

public class FootballDataLoader {

    public void loadJsonMatchData(String jsonFilename) {
        try {
            // Create mapper with support for LocalDateTime
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            // Read the JSON file
            File jsonFile = new File(jsonFilename);
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode matchesNode = rootNode.get("matches");

            // Process each match
            for (JsonNode matchNode : matchesNode) {
                String homeTeamName = matchNode.get("homeTeam").asText();
                String awayTeamName = matchNode.get("awayTeam").asText();
                int homeScore = matchNode.get("homeScore").asInt();
                int awayScore = matchNode.get("awayScore").asInt();

                // Parse the date
                String dateString = matchNode.get("matchDate").asText();
                LocalDateTime matchDate = LocalDateTime.parse(dateString);

                // Look up the actual Team objects from the database
                Team homeTeam = null;
                for (Team t : FootballStatisticsDB.getTeams()) {
                    if (t.getName().equalsIgnoreCase(homeTeamName)) {
                        homeTeam = t;
                        break;
                    }
                }

                Team awayTeam = null;
                for (Team t : FootballStatisticsDB.getTeams()) {
                    if (t.getName().equalsIgnoreCase(awayTeamName)) {
                        awayTeam = t;
                        break;
                    }
                }

                if (homeTeam != null && awayTeam != null) {
                    // Create the match
                    Match match = new Match(homeTeam, awayTeam, homeScore, awayScore, matchDate);

                    // Process goal scorers if present
                    if (matchNode.has("goalScorers") && matchNode.get("goalScorers").isArray()) {
                        Map<Player, Integer> goalScorers = new HashMap<>();

                        for (JsonNode scorerNode : matchNode.get("goalScorers")) {
                            String firstName = scorerNode.get("firstName").asText();
                            String lastName = scorerNode.get("lastName").asText();
                            String teamName = scorerNode.get("teamName").asText();
                            int goals = scorerNode.get("goals").asInt();

                            // Find the player
                            Player player = null;
                            for (Player p : FootballStatisticsDB.getPlayers()) {
                                if (p.getFirstName().equals(firstName) &&
                                        p.getLastName().equals(lastName) &&
                                        p.getTeam().getName().equalsIgnoreCase(teamName)) {
                                    player = p;
                                    break;
                                }
                            }

                            if (player != null) {
                                goalScorers.put(player, goals);
                            } else {
                                System.err.println("Player not found: " + firstName + " " + lastName +
                                        " of " + teamName);
                            }
                        }

                        // Set goal scorers if we found any
                        if (!goalScorers.isEmpty()) {
                            match.setGoalScorers(goalScorers);
                        }
                    }

                    FootballStatisticsDB.addMatch(match);
                } else {
                    System.err.println("Error: Team not found - " +
                            (homeTeam == null ? homeTeamName : awayTeamName));
                }
            }

            System.out.println("JSON match data loading complete!");

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }


    /**
     * Loads data from the specified file into the FootballStatisticsDB
     *
     * @param filename the name of the file to load data from
     */
    public void loadData(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line.trim());
            }
            // After loading all entities, connect them properly
            connectEntities();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void processLine(String line) {
        if (line.isEmpty()) return;

        // Fix the pattern matching by removing unnecessary requirements
        // Look for data that contains the pattern, not requires exact match
        if (line.contains("new Player(")) {
            Matcher playerMatcher = ParseUtil.PLAYER_PATTERN.matcher(line);
            if (playerMatcher.find()) {
                String firstName = playerMatcher.group(1);
                String lastName = playerMatcher.group(2);
                String teamName = playerMatcher.group(3);

                Player player = new Player(firstName, lastName, teamName);
                FootballStatisticsDB.addPlayer(player);
            }
        } else if (line.contains("new Coach(")) {
            Matcher coachMatcher = ParseUtil.COACH_PATTERN.matcher(line);
            if (coachMatcher.find()) {
                String firstName = coachMatcher.group(1);
                String lastName = coachMatcher.group(2);
                String teamName = coachMatcher.group(3);

                Coach coach = new Coach(firstName, lastName, teamName);
                FootballStatisticsDB.addCoach(coach);
            }
        } else if (line.contains("new Team(")) {
            Matcher teamMatcher = ParseUtil.TEAM_PATTERN.matcher(line);
            if (teamMatcher.find()) {
                String teamName = teamMatcher.group(1);
                // Don't try to process the variable references directly from file
                // We'll connect teams with coaches and players later
                Team team = new Team(teamName, null, new HashSet<>());
                FootballStatisticsDB.addTeam(team);
            }
        } else if (line.contains("new Match(")) {
            Matcher matchMatcher = ParseUtil.MATCH_PATTERN.matcher(line);
            if (matchMatcher.find()) {
                String team1Name = matchMatcher.group(1);
                String team2Name = matchMatcher.group(2);
                int team1Score = Integer.parseInt(matchMatcher.group(3));
                int team2Score = Integer.parseInt(matchMatcher.group(4));
                LocalDateTime matchDate = LocalDateTime.parse(matchMatcher.group(5), ParseUtil.DATE_FORMATTER);

                // Look up the actual Team objects from the database
                Team team1 = null;
                for (Team t : FootballStatisticsDB.getTeams()) {
                    if (t.getName().equalsIgnoreCase(team1Name)) {
                        team1 = t;
                        break;
                    }
                }

                Team team2 = null;
                for (Team t : FootballStatisticsDB.getTeams()) {
                    if (t.getName().equalsIgnoreCase(team2Name)) {
                        team2 = t;
                        break;
                    }
                }

                if (team1 != null && team2 != null) {
                    Match match = new Match(team1, team2, team1Score, team2Score, matchDate);
                    FootballStatisticsDB.addMatch(match);
                }
            }
        }
    }

    /**
     * Connect teams with their coaches and players after loading all entities
     */
    private void connectEntities() {
        // For each team in the database
        for (Team team : FootballStatisticsDB.getTeams()) {
            String teamName = team.getName();

            // Find and assign coach
            for (Coach coach : FootballStatisticsDB.getCoaches()) {
                if (coach.getTeamName().equalsIgnoreCase(teamName)) {
                    team.setCoach(coach);
                    break;
                }
            }

            // Find and assign players
            Set<Player> teamPlayers = new HashSet<>();
            for (Player player : FootballStatisticsDB.getPlayers()) {
                if (player.getTeam().getName().equalsIgnoreCase(teamName)) {
                    teamPlayers.add(player);
                }
            }
            team.addPlayers(teamPlayers);
        }
    }
}