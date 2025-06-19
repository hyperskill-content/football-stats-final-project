package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Player;
import com.hyperskill.data_models.Team;
import com.hyperskill.factory.SimplePlayerFactory;

import java.util.*;

public class PlayerManager {
    private final Scanner scanner;
    private final SimplePlayerFactory playerFactory;

    public PlayerManager(Scanner scanner) {
        this.scanner = scanner;
        this.playerFactory = new SimplePlayerFactory();
    }

    public void managePlayersMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== Player Management =====");
            System.out.println("1. Show player details");
            System.out.println("2. Add a new player");
            System.out.println("3. Edit an existing player");
            System.out.println("4. Delete player");
            System.out.println("5. Back");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    showPlayerDetails();
                    break;
                case 2:
                    addNewPlayer();
                    break;
                case 3:
                    editPlayer();
                    break;
                case 4:
                    deletePlayer();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showPlayerDetails() {
        Team team = selectTeam();
        if (team == null) {
            System.out.println("Operation canceled.");
            return;
        }

        Player player = selectPlayerFromTeam(team.getName());
        if (player == null) {
            System.out.println("No player selected or no players in this team.");
            return;
        }

        System.out.println("\n===== Player Details =====");
        System.out.println(player);
        System.out.println("ID: " + player.getId());
        System.out.println("Goals: " + player.getGoals());
        System.out.println("Matches played: " + player.getPlayedMatches());
        System.out.println("Average goals per match: " +
                (player.getPlayedMatches() > 0 ?
                        String.format("%.2f", (double) player.getGoals() / player.getPlayedMatches()) :
                        "No matches played"));
    }

    private void addNewPlayer() {
        System.out.println("\n===== Add New Player =====");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();

        Team team = selectTeam();
        if (team == null) {
            System.out.println("Operation canceled.");
            return;
        }

        Player newPlayer = playerFactory.createPlayer(firstName, lastName, team.getName());
        System.out.println("Player " + firstName + " " + lastName + " added to team " + team.getName() + " successfully!");
    }

    private void editPlayer() {
        System.out.println("\n===== Edit Player =====");

        Team team = selectTeam();
        if (team == null) {
            System.out.println("Operation canceled.");
            return;
        }

        Player player = selectPlayerFromTeam(team.getName());
        if (player == null) {
            System.out.println("No player selected or no players in this team.");
            return;
        }

        editPlayerMenu(player);
    }

    private void editPlayerMenu(Player player) {
        boolean editing = true;

        while (editing) {
            System.out.println("\n===== Edit " + player.getFirstName() + " " + player.getLastName() + " =====");
            System.out.println("Current details: " + player);
            System.out.println("1. Change team");
            System.out.println("2. Update goals");
            System.out.println("3. Back to player management");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    Team newTeam = selectTeam();
                    if (newTeam != null) {
                        player.setTeamName(newTeam.getName());
                        System.out.println("Team updated successfully!");
                    }
                    break;
                case 2:
                    System.out.print("Enter new goals count: ");
                    try {
                        int goals = Integer.parseInt(scanner.nextLine());
                        if (goals >= 0) {
                            player.setGoals(goals);
                            System.out.println("Goals updated successfully!");
                        } else {
                            System.out.println("Goals cannot be negative.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                    break;
                case 3:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void deletePlayer() {
        System.out.println("\n===== Delete Player =====");

        Team team = selectTeam();
        if (team == null) {
            System.out.println("Operation canceled.");
            return;
        }

        Player player = selectPlayerFromTeam(team.getName());
        if (player == null) {
            System.out.println("No player selected or no players in this team.");
            return;
        }

        System.out.println("Player found: " + player);
        System.out.print("Are you sure you want to delete this player? (y/n): ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
            FootballStatisticsDB.deletePlayer(player.getId());
            System.out.println("Player deleted successfully.");

        } else {
            System.out.println("Deletion canceled.");
        }
    }

    // Helper methods
    private Team selectTeam() {
        Collection<Team> teams = FootballStatisticsDB.getTeams();

        if (teams.isEmpty()) {
            System.out.println("No teams available. Please add a team first.");
            return null;
        }

        System.out.println("\nAvailable Teams:");
        int index = 1;
        List<Team> teamList = new ArrayList<>(teams);

        for (Team team : teamList) {
            System.out.println(index++ + ". " + team.getName());
        }
        System.out.println(index + ". Cancel");

        System.out.print("Select a team: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            if (choice == index) {
                return null;
            } else if (choice > 0 && choice < index) {
                return teamList.get(choice - 1);
            } else {
                System.out.println("Invalid selection.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return null;
        }
    }

    private Player selectPlayerFromTeam(String teamName) {
        Set<Player> players = playerFactory.createPlayersForTeam(teamName);

        if (players.isEmpty()) {
            System.out.println("No players found in team " + teamName + ".");
            return null;
        }

        System.out.println("\nPlayers in " + teamName + ":");
        int index = 1;
        List<Player> playerList = new ArrayList<>(players);

        for (Player player : playerList) {
            System.out.println(index++ + ". " + player.getFirstName() + " " + player.getLastName());
        }
        System.out.println(index + ". Cancel");

        System.out.print("Select a player: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            if (choice == index) {
                return null;
            } else if (choice > 0 && choice < index) {
                return playerList.get(choice - 1);
            } else {
                System.out.println("Invalid selection.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return null;
        }
    }

    private Player findPlayerByName(String firstName, String lastName) {
        Collection<Player> allPlayers = FootballStatisticsDB.getPlayers();

        for (Player player : allPlayers) {
            if (player.getFirstName().equalsIgnoreCase(firstName) &&
                    player.getLastName().equalsIgnoreCase(lastName)) {
                return player;
            }
        }

        return null;
    }
}
