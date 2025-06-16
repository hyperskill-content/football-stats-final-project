package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Player;
import com.hyperskill.data_models.Team;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class TeamManager {
    Scanner scanner;

    public TeamManager() {
        scanner = new Scanner(System.in);
    }

    public void startTeamManager() {
        while (true) {
            teamMenu();
            int option = 0;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }
            scanner.nextLine();

            switch (option) {
                case 1 -> addTeam();
                case 2 -> deleteTeam();
                case 3 -> viewTeamDetails();
                case 4 -> showAllTeams();
                case 5 -> updateTeam();
                case 6 -> replaceAllPlayers();
                case 7 -> replacePlayer();
                case 8 -> {
                    return;
                }
                default -> System.out.println("Invalid option... please select correct option");
            }
        }
    }

    private void showAllTeams() {
        FootballStatisticsDB.getTeams().forEach(team -> {
            System.out.printf("Team name: %s \n", team.getName());
        });
    }

    private void updateTeam() {
        Team team = findTeamByName();
        if (team == null) {
            System.out.println("Team doesn't exist. Please check again.");
            return;
        }

        String oldName = team.getName();
        System.out.println("Enter new name: ");

        String newName = validateName(oldName);

        System.out.println("Are you sure you want to rename " + oldName + " to " + newName + "? (yes/no)");
        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Update canceled.");
            return;
        }

        team.setName(newName);
        updateCoachTeam(oldName, newName);
        updatePlayerTeam(oldName, newName);

        System.out.println("Team name updated successfully.");
    }

    private void updatePlayerTeam(String oldTeamName, String newTeamName) {
        FootballStatisticsDB.getPlayers().forEach(player -> {
            if (player.getTeamName().equalsIgnoreCase(oldTeamName)) {
                player.setTeamName(newTeamName);
            }
        });
    }

    private void updateCoachTeam(String oldTeamName, String newTeamName) {
        FootballStatisticsDB.getCoachesByTeam(oldTeamName).forEach(coach -> {
            coach.setTeamName(newTeamName);
        });
    }

    private String getTeamName() {
        System.out.println("Enter team name: ");
        return validateName();
    }

    private Team findTeamByName() {
        System.out.println("Enter team name: ");
        String teamName = scanner.nextLine().trim();
        return FootballStatisticsDB.getTeamByName(teamName);
    }

    private void viewTeamDetails() {
        Team team = findTeamByName();
        if (team == null) {
            System.out.println("Team doesn't exist. Please check again.");
            return;
        }
        System.out.printf("Team name: %s, Coach: %s, Goal scored: %s, Matches: %s \n", team.getName(), team.getCoach(), team.getGoalScored(), team.getAllMatches());
    }

    private void deleteTeam() {
        Team team = findTeamByName();
        if (team == null) {
            System.out.println("Team doesn't exist. Please check again.");
            return;
        }
        FootballStatisticsDB.deleteTeam(team.getId());
        System.out.println("Team deleted successfully...");
    }

    private void addTeam() {
        Team team = new Team();
        System.out.println("Enter team name: ");
        String teamName = scanner.nextLine().trim();
        team.setName(teamName);
        FootballStatisticsDB.addTeam(team);
    }

    private void replacePlayer() {
        Team team = findTeamByName();
        if (team == null) {
            System.out.println("Team doesn't exist. Please check again.");
            return;
        }

        System.out.println("The current player data:");
        Player player = addPlayer(team);
        if (player == null) {
            System.out.println("Invalid input. Return to the menu.");
            return;
        }

        Player oldPlayer = FootballStatisticsDB.getPlayerByName(player.getFirstName(), player.getLastName());
        if (oldPlayer == null) {
            System.out.println("Invalid input. Return to the menu.");
            return;
        }

        if (!team.getPlayers().contains(oldPlayer)) {
            System.out.println("There is no such player in the current team. Return to the menu.");
            return;
        }

        team.deletePlayer(oldPlayer);
        oldPlayer.setTeamName(null);

        System.out.println("The new player data:");
        Player newPlayer = addPlayer(team);
        if (newPlayer == null) {
            System.out.println("Invalid input. Return to the menu.");
            return;
        }
        FootballStatisticsDB.addPlayer(newPlayer);
        newPlayer = FootballStatisticsDB.getPlayerByName(newPlayer.getFirstName(), newPlayer.getLastName());
        team.addPlayer(newPlayer);
    }

    private void replaceAllPlayers() {
        String teamName = getTeamName();
        Team team = FootballStatisticsDB.getTeamByName(teamName);
        if (team == null) {
            Team newTeam = new Team(teamName);
            FootballStatisticsDB.addTeam(newTeam);
        }
        team = FootballStatisticsDB.getTeamByName(teamName);

        Set<Player> newPlayers = new HashSet<>();
        System.out.println("How many players are you going to replace?");
        int amount = 0;
        try {
            amount = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Your input is invalid. Expected number.");
            return;
        }

        if (amount <= 0 || amount > 50) {
            System.out.println("Your input is invalid. Expected number from 0 to 50.");
            return;
        }

        for (int i = 0; i < amount; i++) {
            Player player = addPlayer(team);
            if (player == null) {
                amount++;
                continue;
            }
            newPlayers.add(player);
        }

        Set<Player> oldPlayers = new HashSet<>(team.getPlayers());
        oldPlayers.forEach(player -> player.setTeamName(null));

        team.replaceAllPlayers(newPlayers);
    }

    private Player addPlayer(Team team) {
        System.out.println("Enter player data: ");
        System.out.println("Enter first name: ");
        String firstName = validateName();
        System.out.println("Enter last name: ");
        String lastName = validateName();
        if (firstName == null || lastName == null) {
            System.out.println("Your input is invalid. Return to the menu. ");
            return null;
        }

        System.out.println("Check the input data: first name " + firstName + ", last name " + lastName
                + "? (yes/no)");
        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Update canceled.");
            return null;
        }

        return new Player(firstName, lastName, team.getName());
    }

    private String validateName() {
        String newName;
        try {
            newName = scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("Error reading input. Please try again.");
            return null;
        }

        if (newName.isEmpty()) {
            System.out.println("Invalid name. It cannot be empty.");
            return null;
        }

        return newName;
    }

    private String validateName(String oldName) {
        String newName;
        try {
            newName = scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("Error reading input. Please try again.");
            return null;
        }

        if (newName.isEmpty()) {
            System.out.println("Invalid name. It cannot be empty.");
            return null;
        }

        if (newName.equalsIgnoreCase(oldName)) {
            System.out.println("The new name is the same as the current one. No changes made.");
            return null;
        }
        return newName;
    }

    private void teamMenu() {
        System.out.println("""
                1. Add a new team
                2. Delete a team
                3. View team details
                4. Show all teams
                5. Update a team
                6. Replace all players
                7. Replace a player
                8. Exit
                """);
    }
}
