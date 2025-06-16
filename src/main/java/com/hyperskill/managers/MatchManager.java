package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Match;
import com.hyperskill.data_models.Player;
import com.hyperskill.data_models.Team;
import com.hyperskill.factory.MatchFactory;
import com.hyperskill.factory.SimpleMatchFactory;

import java.time.LocalDateTime;
import java.util.*;

public class MatchManager {
    private Scanner scanner;
    private MatchFactory matchFactory;

    public MatchManager(Scanner scanner) {
        this.scanner = scanner;
        this.matchFactory = new SimpleMatchFactory();
    }

    /**
     * Main method to handle the match recording workflow
     */
    public void recordNewMatch() {
        System.out.println("\n===== Record New Match =====");

        Team homeTeam = selectHomeTeam();
        if (homeTeam == null) {
            System.out.println("Match recording cancelled.");
            return;
        }

        Team awayTeam = selectAwayTeam(homeTeam);
        if (awayTeam == null) {
            System.out.println("Match recording cancelled.");
            return;
        }

        Map<Player, Integer> goalScorers = new HashMap<>();
        System.out.println("\nSelect " + homeTeam.getName() + " (Home Team) Goal Scorers:");
        selectGoalScorers(homeTeam, goalScorers);

        System.out.println("\nSelect " + awayTeam.getName() + " (Away Team) Goal Scorers:");
        selectGoalScorers(awayTeam, goalScorers);

        // Calculate scores based on goal scorers
        int homeScore = calculateTeamScore(homeTeam, goalScorers);
        int awayScore = calculateTeamScore(awayTeam, goalScorers);

        // Create the match using SimpleMatchFactory
        Match match = matchFactory.createMatch(
                homeTeam,
                awayTeam,
                homeScore,
                awayScore,
                LocalDateTime.now(),
                goalScorers
        );

        System.out.println("\nMatch recorded successfully!");
        System.out.println(match.getMatchSummary());
    }

    private Team getTeam(List<Team> teams) {
        for (int i = 0; i < teams.size(); i++) {
            System.out.println((i + 1) + ". " + teams.get(i).getName());
        }
        System.out.println("0. Cancel");

        int choice = getUserChoice(0, teams.size());
        if (choice == 0) {
            return null;
        }

        return teams.get(choice - 1);
    }

    private Team selectHomeTeam() {
        List<Team> teams = new ArrayList<>(FootballStatisticsDB.getTeams());

        if (teams.isEmpty()) {
            System.out.println("No teams available in the system. Please add teams first.");
            return null;
        }

        System.out.println("\nChoose the Home Team:");
        return getTeam(teams);
    }


    private Team selectAwayTeam(Team homeTeam) {
        List<Team> availableTeams = new ArrayList<>();

        // Get all teams except the home team
        for (Team team : FootballStatisticsDB.getTeams()) {
            if (!team.getName().equalsIgnoreCase(homeTeam.getName())) {
                availableTeams.add(team);
            }
        }

        if (availableTeams.isEmpty()) {
            System.out.println("No other teams available to play against. Please add more teams first.");
            return null;
        }

        System.out.println("\nChoose the Away Team:");
        return getTeam(availableTeams);
    }

    /**
     * Steps 3 & 4: Select goal scorers from a team
     *
     * @param team        the team to select goal scorers from
     * @param goalScorers map to store goal scorers and their goals
     */
    private void selectGoalScorers(Team team, Map<Player, Integer> goalScorers) {
        List<Player> players = new ArrayList<>(FootballStatisticsDB.getPlayersByTeam(team.getName()));

        if (players.isEmpty()) {
            System.out.println("No players available in team " + team.getName() + ".");
            return;
        }

        boolean done = false;
        while (!done) {
            displayPlayersList(players);
            System.out.println((players.size() + 1) + ". Done");

            int choice = getUserChoice(1, players.size() + 1);

            if (choice == players.size() + 1) {
                done = true;
            } else {
                Player selectedPlayer = players.get(choice - 1);
                System.out.println("Enter number of goals for " + selectedPlayer.getFirstName() + " " + selectedPlayer.getLastName() + ":");
                int goals = getUserChoice(1, 10); // Reasonable max goals per player

                // Add or update goals for the selected player
                if (goalScorers.containsKey(selectedPlayer)) {
                    goalScorers.put(selectedPlayer, goalScorers.get(selectedPlayer) + goals);
                } else {
                    goalScorers.put(selectedPlayer, goals);
                }

                System.out.println(selectedPlayer.getFirstName() + " " + selectedPlayer.getLastName() +
                        " recorded with " + goalScorers.get(selectedPlayer) + " goal(s).");
            }
        }
    }

    private void displayPlayersList(List<Player> players) {
        System.out.println("\nSelect a player:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getFirstName() + " " + player.getLastName());
        }
    }

    private int calculateTeamScore(Team team, Map<Player, Integer> goalScorers) {
        int totalGoals = 0;

        for (Map.Entry<Player, Integer> entry : goalScorers.entrySet()) {
            Player player = entry.getKey();
            if (player.getTeamName().equalsIgnoreCase(team.getName())) {
                totalGoals += entry.getValue();
            }
        }

        return totalGoals;
    }

    private int getUserChoice(int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            try {
                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < min || choice > max) {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }
}