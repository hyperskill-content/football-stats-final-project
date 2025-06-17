package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Coach;
import com.hyperskill.data_models.Team;
import com.hyperskill.statistics.CoachStatistics;
import com.hyperskill.statistics.TeamStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CoachStatisticsManager {
    private final Scanner scanner;
    private final CoachStatistics stats;

    public CoachStatisticsManager() {
        this.scanner = new Scanner(System.in);
        this.stats = new CoachStatistics();
    }

    public CoachStatisticsManager(Scanner scanner) {
        this.scanner = scanner;
        this.stats = new CoachStatistics();
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int option = 0;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    Coach coach = findCoachByName();
                    if (coach != null) {
                        viewSpecificCoachStats(coach);
                    }
                }
                case 2 -> viewCoachRankings();
                case 3 -> running = false;
                default -> System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("""
                👨‍💼 COACH STATISTICS 👨‍💼
                1️⃣ View statistics for a specific coach
                2️⃣ View coach rankings
                3️⃣ Back to Statistics Menu
                Select an option:""");
    }

    private Coach findCoachByName() {
        System.out.println("Enter coach's first name:");
        String firstName = scanner.nextLine().trim();

        System.out.println("Enter coach's last name:");
        String lastName = scanner.nextLine().trim();

        Coach coach = FootballStatisticsDB.getCoachByName(firstName, lastName);

        if (coach == null) {
            System.out.println("Coach not found. Please check the name and try again.");
            return null;
        }

        return coach;
    }

    private void viewSpecificCoachStats(Coach coach) {
        System.out.println("\n===== Statistics for Coach: "
                + coach.getFirstName() + " "
                + coach.getLastName() + " (Team: "
                + coach.getTeamName() + ") =====");

        // Basic statistics
        System.out.println("\n--- Basic Statistics ---");
        System.out.println("Goals scored by current team: " + stats.goalsScoredCurrentTeam(coach));
        System.out.println("Matches played total: " + stats.playedMatchesTotal(coach));
        System.out.printf("Average goals scored: %.2f\n", stats.averageGoalsScored(coach));

        // Calculate win statistics
        Team team = FootballStatisticsDB.getTeamByName(coach.getTeamName());
        if (team != null) {
            int totalMatches = TeamStatistics.amountMatchesTotal(team);
            int wins = TeamStatistics.amountWinsTotal(team);
            int losses = TeamStatistics.amountLossesTotal(team);
            int draws = TeamStatistics.amountDrawsTotal(team);

            double winPercentage = TeamStatistics.percentageWinsTotal(team);
            double lossPercentage = TeamStatistics.percentageLossesTotal(team);
            double drawPercentage = TeamStatistics.percentageDrawsTotal(team);

            System.out.println("\n--- Match Results ---");
            System.out.println("Total wins: " + wins);
            System.out.println("Total losses: " + losses);
            System.out.println("Total draws: " + draws);

            System.out.println("\n--- Performance Percentages ---");
            System.out.printf("Win percentage: %.2f%%\n", winPercentage);
            System.out.printf("Loss percentage: %.2f%%\n", lossPercentage);
            System.out.printf("Draw percentage: %.2f%%\n", drawPercentage);
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void viewCoachRankings() {
        boolean running = true;

        while (running) {
            displayCoachRankingMenu();

            int option = 0;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }
            scanner.nextLine();

            int limit;
            List<Coach> coaches;

            if (option >= 1 && option <= 5) {
                System.out.println("How many coaches would you like to see in the ranking? (Default: 5)");
                if (scanner.hasNextInt()) {
                    limit = scanner.nextInt();
                } else {
                    limit = 5;
                }
                scanner.nextLine();

                if (limit <= 0) {
                    System.out.println("Invalid number. Showing 5 coaches by default.");
                    limit = 5;
                }
            } else {
                limit = 0;
            }

            switch (option) {
                case 1 -> {
                    coaches = stats.findTopCoachesByVictories(limit);
                    printCoachRankingWithMetric(coaches, "victories");
                }
                case 2 -> {
                    coaches = stats.findTopCoachesByGoalsScored(limit);
                    printCoachRankingWithMetric(coaches, "goals scored");
                }
                case 3 -> {
                    coaches = stats.findTopCoachesByWinPercentage(limit);
                    printCoachRankingWithMetric(coaches, "win percentage");
                }
                case 4 -> {
                    coaches = stats.findWorstCoachesByLosses(limit);
                    printCoachRankingWithMetric(coaches, "losses");
                }
                case 5 -> {
                    coaches = stats.findWorstCoachesByGoalsConceded(limit);
                    printCoachRankingWithMetric(coaches, "goals conceded");
                }
                case 6 -> running = false;
                default -> System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }

    private void displayCoachRankingMenu() {
        System.out.println("""
                🏆 COACH RANKINGS 🏆
                1️⃣ Top coaches by victories
                2️⃣ Top coaches by goals scored
                3️⃣ Top coaches by win percentage
                4️⃣ Worst coaches by losses
                5️⃣ Worst coaches by goals conceded
                6️⃣ Back to Coach Statistics
                Select an option:""");
    }

    private void printCoachRankingWithMetric(List<Coach> coaches, String metric) {
        if (coaches.isEmpty()) {
            System.out.println("No coaches found for this ranking.");
            return;
        }

        System.out.println("\n===== Coach Ranking by " + metric.toUpperCase() + " =====");

        int rank = 1;
        Map<Coach, Integer> metricsMap = null;
        boolean isPercentage = false;

        // Determine which metrics map to use based on the metric
        switch (metric) {
            case "victories" -> metricsMap = stats.calculateVictoriesForAllCoaches();
            case "goals scored" -> {
                // Using a temporary map
                metricsMap = new HashMap<>();
                for (Coach coach : coaches) {
                    metricsMap.put(coach, stats.goalsScoredCurrentTeam(coach));
                }
            }
            case "win percentage" -> isPercentage = true;
            case "losses" -> metricsMap = stats.calculateLossesForAllCoaches();
            case "goals conceded" -> metricsMap = stats.calculateGoalsConcededForAllCoaches();
        }

        for (Coach coach : coaches) {
            if (isPercentage) {
                Team team = FootballStatisticsDB.getTeamByName(coach.getTeamName());
                double winPercentage = 0;
                if (team != null) {
                    winPercentage = TeamStatistics.percentageWinsTotal(team);
                }
                System.out.printf("%d. %s %s (Team: %s) - %.2f%% win rate\n",
                        rank++,
                        coach.getFirstName(),
                        coach.getLastName(),
                        coach.getTeamName(),
                        winPercentage);
            } else if (metricsMap != null) {
                int value = metricsMap.getOrDefault(coach, 0);
                System.out.printf("%d. %s %s (Team: %s) - %d %s\n",
                        rank++,
                        coach.getFirstName(),
                        coach.getLastName(),
                        coach.getTeamName(),
                        value, metric);
            }
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void displayCoachStatistics(Coach coach) {
        if (coach != null) {
            viewSpecificCoachStats(coach);
        }
    }
}
