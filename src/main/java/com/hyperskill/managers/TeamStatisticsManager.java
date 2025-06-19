package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Team;
import com.hyperskill.statistics.TeamStatistics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeamStatisticsManager {
    Scanner scanner;

    public TeamStatisticsManager() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            teamStatsMenu();
            int option = 0;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                return;
            }

            switch (option) {
                case 1 -> showAllTeams();
                case 2 -> showTopTeamsByGoalsTotal();
                case 3 -> showTopTeamsByGoalsPerYear();
                case 4 -> showTopWinTeamsByMatches();
                case 5 -> showTopLoseTeamsByMatches();
                case 6 -> showTopDrawTeamsByMatches();
                case 7 -> showLowestRankedTeamsByGoals();
                case 8 -> showStatisticsByTeam();
                case 9 -> showStatisticsByTeamPerYear();
                case 10 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }

    private void showAllTeams() {
        FootballStatisticsDB.getTeams().forEach(System.out::println);
    }

    private void showTopTeamsByGoalsTotal() {
        System.out.print("How many teams would you like to see in the ranking? (Default: 5): ");
        int amount = 5;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Showing 5 top teams by default.");
        }
        TeamStatistics.topTeamsByScoredGoalsTotal(amount).forEach(System.out::println);
    }

    private void showTopTeamsByGoalsPerYear() {
        System.out.print("How many teams would you like to see in the ranking? (Default: 5): ");
        int amount = 5;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Showing 5 top teams by default.");
        }

        System.out.print("Enter the year (Default: the current year): ");
        int year = LocalDate.now().getYear();
        try {
            int inputYear = Integer.parseInt(scanner.nextLine());
            if (inputYear > 0 && inputYear < year) {
                year = inputYear;
            }
        } catch (NumberFormatException e) {
            System.out.printf("Invalid number. Showing %d top teams in the %d.\n", amount, year);
        }
        TeamStatistics.topTeamsByScoredGoalsPerYear(amount, year).forEach(System.out::println);
    }

    private void showTopWinTeamsByMatches() {
        System.out.print("How many teams would you like to see in the ranking? (Default: 5): ");
        int amount = 5;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Showing 5 top teams by default.");
        }
        TeamStatistics.topWinsTeams(amount).forEach(System.out::println);
    }

    private void showTopLoseTeamsByMatches() {
        System.out.print("How many teams would you like to see in the ranking? (Default: 5): ");
        int amount = 5;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Showing 5 top teams by default.");
        }
        TeamStatistics.topLosesTeams(amount).forEach(System.out::println);
    }

    private void showTopDrawTeamsByMatches() {
        System.out.print("How many teams would you like to see in the ranking? (Default: 5): ");
        int amount = 5;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Showing 5 top teams by default.");
        }
        TeamStatistics.topDrawsTeams(amount).forEach(System.out::println);
    }

    private void showLowestRankedTeamsByGoals() {
        System.out.print("How many teams would you like to see in the ranking? (Default: 5): ");
        int amount = 5;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Showing 5 top teams by default.");
        }
        TeamStatistics.lowestRankedTeamsByScoredGoalsTotal(amount).forEach(System.out::println);
    }

    private void showStatisticsByTeam() {

        Team team = findTeamByNumber();
        if (team == null) {
            return;
        }

        System.out.printf("The amount of Wins = %d\n", TeamStatistics.amountWinsTotal(team));
        System.out.printf("The amount of Loses = %d\n", TeamStatistics.amountLossesTotal(team));
        System.out.printf("The amount of Draws = %d\n", TeamStatistics.amountDrawsTotal(team));

        System.out.printf("The percentage of Wins = %.2f%%\n", TeamStatistics.percentageWinsTotal(team));
        System.out.printf("The percentage of Loses = %.2f%%\n", TeamStatistics.percentageLossesTotal(team));
        System.out.printf("The percentage of Draws = %.2f%%\n", TeamStatistics.percentageDrawsTotal(team));
    }

    private void showStatisticsByTeamPerYear() {

        Team team = findTeamByNumber();
        if (team == null) {
            return;
        }

        System.out.print("Enter the year: ");
        int year = LocalDate.now().getYear();
        try {
            int inputYear = Integer.parseInt(scanner.nextLine());
            if (inputYear > 0 && inputYear < year) {
                year = inputYear;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Show the statistics of the current year");
        }

        System.out.printf("In %d the amount of Wins = %d\n", year, TeamStatistics.amountWinsPerYear(team, year));
        System.out.printf("In %d the amount of Loses = %d\n", year, TeamStatistics.amountLossesPerYear(team, year));
        System.out.printf("In %d the amount of Draws = %d\n", year, TeamStatistics.amountDrawsPerYear(team, year));

        System.out.printf("In %d the percentage of Wins = %.2f%%\n", year, TeamStatistics.percentageWinsPerYear(team, year));
        System.out.printf("In %d the percentage of Loses = %.2f%%\n", year, TeamStatistics.percentageLossesPerYear(team, year));
        System.out.printf("In %d the percentage of Draws = %.2f%%\n", year, TeamStatistics.percentageDrawsPerYear(team, year));
    }

    private Team findTeamByNumber() {
        List<Team> teams = new ArrayList<>(FootballStatisticsDB.getTeams());
        if (teams.isEmpty()) {
            System.out.println("No teams found in the database.");
            return null;
        }

        displayTeamsList(teams);

        System.out.print("Enter the number of the team:");
        int selection = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection > 0 && selection <= teams.size()) {
                    validInput = true;
                } else {
                    System.out.print("Invalid selection. Please enter a number between 1 and " + teams.size() + ":");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number:");
            }
        }

        return teams.get(selection - 1);
    }

    private void displayTeamsList(List<Team> teams) {
        System.out.println("\n===== Available Teams =====");
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            System.out.printf("%d. %s\n", i + 1, team.getName());
        }
    }

    private void teamStatsMenu() {
        System.out.print("""
                👨‍💼 TEAM STATISTICS 👨‍💼
                1️⃣ View all teams
                2️⃣ View top teams by goals in total
                3️⃣ View top teams by goals per year
                4️⃣ View top win teams by played matches
                5️⃣ View top lose teams by played matches
                6️⃣ View top draw teams by played matches
                7️⃣ View lowest ranked teams by goals in total
                8️⃣ View total amount of win, lose and draws matches by Team in total
                9️⃣ View total amount of win, lose and draws matches by Team per year
                🔟 Back to Main Menu
                Select an option: """);
    }
}
