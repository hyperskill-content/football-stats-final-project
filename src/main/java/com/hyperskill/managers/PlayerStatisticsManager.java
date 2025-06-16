package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Player;
import com.hyperskill.statistics.PlayerStatistics;

import java.util.Scanner;

public class PlayerStatisticsManager {
    Scanner scanner;

    public PlayerStatisticsManager() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            playerStatsMenu();
            int option = 0;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }
            scanner.nextLine();

            switch (option) {
                case 1 -> showAllPlayers();
                case 2 -> showTopPlayersByGoals();
                case 3 -> showTopPlayersByMatches();
                case 4 -> showPlayerGoals();
                case 5 -> showPlayerMatches();
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }


    private void showPlayerGoals() {
        System.out.println("Enter player name: ");
        String[] playerName = scanner.nextLine().trim().split(" ");
        Player player = FootballStatisticsDB.getPlayerByName(playerName[0], playerName[1]);
        if (player == null) {
            System.out.println("Player not found. Please check the name and try again.");
            return;
        }
        System.out.printf("Player: %s %s, Goals: %s\n",
                player.getFirstName(),
                player.getLastName(),
                player.getGoals());
    }

    private void showPlayerMatches() {
        System.out.println("Enter player name: ");
        String[] playerName = scanner.nextLine().trim().split(" ");
        Player player = FootballStatisticsDB.getPlayerByName(playerName[0], playerName[1]);
        if (player == null) {
            System.out.println("Player not found. Please check the name and try again.");
            return;
        }
        System.out.printf("Player: %s %s, Matches: %s\n",
                player.getFirstName(),
                player.getLastName(),
                player.getPlayedMatches());
    }

    private int takeNoOfTopPlayers() {
        System.out.println("Enter number of top players: ");
        int noOfPlayers = 0;
        if (scanner.hasNextInt()) {
            noOfPlayers = scanner.nextInt();
        }
        return noOfPlayers;
    }

    private void showTopPlayersByMatches() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        int noOfPlayers = takeNoOfTopPlayers();
        for (Player player : playerStatistics.getTopPlayersByMatchesPlayed(noOfPlayers)) {
            System.out.printf("Name: %s %s, Matches: %s \n",
                    player.getFirstName(),
                    player.getLastName(),
                    player.getPlayedMatches());
        }
    }

    private void showTopPlayersByGoals() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        int noOfPlayers = takeNoOfTopPlayers();
        for (Player player : playerStatistics.getTopPlayersByGoalsScored(noOfPlayers)) {
            System.out.printf("Name: %s %s, Goals: %s \n",
                    player.getFirstName(),
                    player.getLastName(),
                    player.getGoals());
        }
    }

    private void showAllPlayers() {
        PlayerStatistics playerStatistics = new PlayerStatistics();
        for (Player player : playerStatistics.getAllPlayers()) {
            System.out.printf("Name: %s %s, Team: %s, Goals: %s, Matches: %s \n",
                    player.getFirstName(),
                    player.getLastName(),
                    player.getTeamName(),
                    player.getGoals(),
                    player.getPlayedMatches());
        }
    }

    private void playerStatsMenu() {
        System.out.println("""
                👨‍💼 PLAYER STATISTICS 👨‍💼
                1️⃣ View all players
                2️⃣ View top players by goals
                3️⃣ View top players by played matches
                4️⃣ View Player's goals
                5️⃣ View Player's matches
                6️⃣ Back to Main Menu
                Select an option:""");
    }
}
