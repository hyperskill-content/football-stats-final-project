package com.hyperskill.ui;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.factory.SimpleCoachFactory;
import com.hyperskill.managers.*;
import com.hyperskill.utils.FootballDataLoader;

import java.util.Scanner;

public class SportsStatisticsInterface {
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        startMenu();
    }

    private static void startMenu() {
        FootballStatisticsDB.loadInitialDataIfNeeded("src/main/java/com/hyperskill/resources/Generate_Data.txt");
        FootballDataLoader dataLoader = new FootballDataLoader();
        dataLoader.loadJsonMatchData("src/main/java/com/hyperskill/resources/FootballMatchResults.json");
        while (true) {
            mainMenu();
            int option = 0;
            if (scanner.hasNextInt())
                option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> manageTeams();
                case 2 -> managePlayers();
                case 3 -> manageCoaches();
                case 4 -> recordMatch();
                case 5 -> viewStatistics();
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid option...Please select an option");
            }
        }
    }

    private static void viewStatistics() {
        boolean running = true;
        while (running) {
            displayStatisticsMenu();
            int option = 0;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }
            scanner.nextLine();

            switch (option) {
                case 1 -> handleCoachStatistics();
                case 2 -> handlePlayerStatistics();
                case 3 -> handleTeamStatistics();
                case 4 -> running = false;
                default -> System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }

    private static void handlePlayerStatistics() {
        PlayerStatisticsManager playerStatisticsManager = new PlayerStatisticsManager();
        playerStatisticsManager.start();
    }

    private static void displayStatisticsMenu() {
        System.out.println("""
                📊 STATISTICS MENU 📊
                1️⃣ Coach Statistics
                2️⃣ Player Statistics
                3️⃣ Team Statistics
                4️⃣ Back to Main Menu
                Select an option:""");
    }

    private static void handleCoachStatistics() {
        CoachStatisticsManager coachStatisticsManager = new CoachStatisticsManager(scanner);
        coachStatisticsManager.start();
    }

    private static void recordMatch() {
        MatchManager matchManager = new MatchManager(scanner);
        matchManager.recordNewMatch();
    }

    private static void managePlayers() {
        PlayerManager playerManager = new PlayerManager(scanner);
        playerManager.managePlayersMenu();
    }

    private static void manageTeams() {
        TeamManager teamManager = new TeamManager();
        teamManager.startTeamManager();
    }

    public static void manageCoaches() {
        CoachManager coachManager = new CoachManager(new SimpleCoachFactory());
        coachManager.startCoachManager();
    }

    private static void handleTeamStatistics() {
        TeamStatisticsManager teamStatisticsManager = new TeamStatisticsManager();
        teamStatisticsManager.start();
    }

    private static void mainMenu() {
        System.out.println("""
                🏆 FOOTBALL STATISTICS SYSTEM 🏆
                1️⃣ Manage Teams
                2️⃣ Manage Players
                3️⃣ Manage Coaches
                4️⃣ Record a Match
                5️⃣ View Statistics
                6️⃣ Exit
                
                Select an option:""");
    }
}
