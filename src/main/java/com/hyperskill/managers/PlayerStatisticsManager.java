package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Player;
import com.hyperskill.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;
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
        Player player = findPlayerByNumber();
        if (player == null) {
            return;
        }
        System.out.printf("Player: %s %s, Goals: %s\n",
                player.getFirstName(),
                player.getLastName(),
                player.getGoals());
    }

    private void showPlayerMatches() {
        Player player = findPlayerByNumber();
        if (player == null) {
            return;
        }
        System.out.printf("Player: %s %s, Matches: %s\n",
                player.getFirstName(),
                player.getLastName(),
                player.getPlayedMatches());
    }

    private Player findPlayerByNumber() {
        List<Player> players = new ArrayList<>(FootballStatisticsDB.getPlayers());
        if (players.isEmpty()) {
            System.out.println("No players found in the database.");
            return null;
        }

        displayPlayersList(players);

        System.out.print("Enter the number of the player:");
        int selection = 0;
        boolean validInput = false;

        while (!validInput) {
            if (scanner.hasNextInt()) {
                selection = scanner.nextInt();
                if (selection > 0 && selection <= players.size()) {
                    validInput = true;
                } else {
                    System.out.print("Invalid selection. Please enter a number between 1 and " + players.size() + ":");
                }
            } else {
                System.out.print("Invalid input. Please enter a number:");
                scanner.next(); // Clear invalid input
            }
        }
        scanner.nextLine(); // Clear the newline

        return players.get(selection - 1);
    }

    private void displayPlayersList(List<Player> players) {
        System.out.println("\n===== Available Players =====");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("%d. %s %s (Team: %s)\n", 
                    i + 1, 
                    player.getFirstName(), 
                    player.getLastName(), 
                    player.getTeamName());
        }
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
