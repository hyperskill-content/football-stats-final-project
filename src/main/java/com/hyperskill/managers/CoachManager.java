package com.hyperskill.managers;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Coach;
import com.hyperskill.factory.CoachFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CoachManager {
    private final CoachFactory coachFactory;
    private Scanner scanner;
    private final CoachStatisticsManager coachStatisticsManager;


    public CoachManager(CoachFactory coachFactory) {
        this.coachFactory = coachFactory;
        this.scanner = new Scanner(System.in);
        this.coachStatisticsManager = new CoachStatisticsManager(scanner);
    }

    /**
     * Start the coach management UI
     */
    public void startCoachManager() {
        int choice;
        do {
            displayCoachMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addNewCoach();
                case 2 -> updateCoachInfo();
                case 3 -> deleteCoach();
                case 4 -> viewCoachDetails();
                case 5 -> listAllCoaches();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option, please try again.");
            }
        } while (choice != 0);
    }

    private void displayCoachMenu() {
        System.out.println("\n===== Coach Management =====");
        System.out.println("1. Add a new coach");
        System.out.println("2. Update coach information");
        System.out.println("3. Delete a coach");
        System.out.println("4. View coach details");
        System.out.println("5. List all coaches");
        System.out.println("0. Return to main menu");
        System.out.print("Enter your choice: ");
    }

    private void addNewCoach() {
        System.out.println("\n--- Add New Coach ---");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter team name: ");
        String teamName = scanner.nextLine();

        Coach newCoach = addCoach(firstName, lastName, teamName);

        if (newCoach != null) {
            System.out.println("Coach added successfully!");
            System.out.println("Coach ID: " + newCoach.getId());
        } else {
            System.out.println("Failed to add coach.");
        }
    }

    private void updateCoachInfo() {
        System.out.println("\n--- Update Coach Information ---");

        Coach coach = selectCoachFromList();
        if (coach == null) {
            System.out.println("Coach not found.");
            return;
        }

        System.out.println("Current information:");
        System.out.println("Name: " + coach.getFirstName() + " " + coach.getLastName());
        System.out.println("Team: " + coach.getTeamName());

        System.out.print("\nEnter new first name (or press Enter to keep current): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            coach.setFirstName(firstName);
        }

        System.out.print("Enter new last name (or press Enter to keep current): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            coach.setLastName(lastName);
        }

        System.out.print("Enter new team name (or press Enter to keep current): ");
        String teamName = scanner.nextLine();
        if (!teamName.isEmpty()) {
            coach.setTeamName(teamName);
        }

        boolean success = updateCoach(coach);
        if (success) {
            System.out.println("Coach information updated successfully!");
        } else {
            System.out.println("Failed to update coach information.");
        }
    }

    private void deleteCoach() {
        System.out.println("\n--- Delete Coach ---");

        Coach coach = selectCoachFromList();
        if (coach == null) {
            System.out.println("Coach not found.");
            return;
        }

        System.out.println("Coach found: " + coach.getFirstName() + " " + coach.getLastName());
        System.out.print("Are you sure you want to delete this coach? (y/n): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            boolean success = deleteCoach(coach.getId());
            if (success) {
                System.out.println("Coach deleted successfully!");
            } else {
                System.out.println("Failed to delete coach.");
            }
        } else {
            System.out.println("Coach deletion cancelled.");
        }
    }

    private void viewCoachDetails() {
        System.out.println("\n--- View Coach Details ---");

        Coach coach = selectCoachFromList();
        if (coach == null) {
            System.out.println("Coach not found.");
            return;
        }

        // Display basic coach information
        System.out.println("\nCoach Basic Information:");
        System.out.println("ID: " + coach.getId());
        System.out.println("Name: " + coach.getFirstName() + " " + coach.getLastName());
        System.out.println("Team: " + coach.getTeamName());

        // Use the CoachStatisticsManager to display detailed coach statistics
        coachStatisticsManager.displayCoachStatistics(coach);
    }

    private void listAllCoaches() {
        System.out.println("\n--- All Coaches ---");

        Collection<Coach> coaches = getAllCoaches();

        if (coaches.isEmpty()) {
            System.out.println("No coaches found in the system.");
            return;
        }

        System.out.printf("%-4s %-20s %-20s %-8s\n",
                "ID", "Name", "Team", "Matches");
        System.out.println("------------------------------------------------");

        for (Coach coach : coaches) {
            System.out.printf("%-4s %-20s %-20s %-8d\n",
                    coach.getId(),
                    coach.getFirstName() + " " + coach.getLastName(),
                    coach.getTeamName(),
                    coach.getPlayedMatches());
        }
    }

    /**
     * Displays a list of coaches and returns the index for the "Cancel" option
     *
     * @param coachList The list of coaches to display
     * @return The index number that represents the "Cancel" option
     */
    private int displayCoachList(List<Coach> coachList) {
        System.out.println("\n===== Available Coaches =====");
        int index = 1;

        for (Coach coach : coachList) {
            System.out.printf("%d. %s %s (Team: %s)\n", 
                    index++, 
                    coach.getFirstName(), 
                    coach.getLastName(), 
                    coach.getTeamName());
        }
        System.out.println(index + ". Cancel");
        System.out.print("Enter the number of the coach:");

        return index;
    }

    /**
     * Gets a valid coach selection from user input
     *
     * @param maxIndex The maximum valid index (inclusive)
     * @return The selected index (1-based) or -1 for cancel
     */
    private int getValidCoachSelection(int maxIndex) {
        int selection = 0;
        boolean validInput = false;

        while (!validInput) {
            if (scanner.hasNextInt()) {
                selection = scanner.nextInt();
                if (selection == maxIndex) {
                    scanner.nextLine(); // Clear the newline
                    return -1; // Cancel selected
                } else if (selection > 0 && selection < maxIndex) {
                    validInput = true;
                } else {
                    System.out.print("Invalid selection. Please enter a number between 1 and " + (maxIndex-1) + ":");
                }
            } else {
                System.out.print("Invalid input. Please enter a number:");
                scanner.next(); // Clear invalid input
            }
        }
        scanner.nextLine(); // Clear the newline

        return selection;
    }

    /**
     * Allows the user to select a coach from a displayed list
     *
     * @return The selected Coach object or null if selection was cancelled
     */
    private Coach selectCoachFromList() {
        Collection<Coach> coaches = getAllCoaches();

        if (coaches.isEmpty()) {
            System.out.println("No coaches available. Please add a coach first.");
            return null;
        }

        List<Coach> coachList = new ArrayList<>(coaches);
        int cancelIndex = displayCoachList(coachList);
        int selection = getValidCoachSelection(cancelIndex);

        if (selection == -1) {
            return null; // User cancelled
        }

        return coachList.get(selection - 1);
    }

    /**
     * Add a new coach to the system
     *
     * @param firstName The coach's first name
     * @param lastName  The coach's last name
     * @param teamName  The team the coach is assigned to
     * @return The newly created coach
     */
    public Coach addCoach(String firstName, String lastName, String teamName) {
        return coachFactory.createCoach(firstName, lastName, teamName);
    }

    /**
     * Update an existing coach's information
     *
     * @param coach The coach with updated information
     * @return true if the update was successful, false otherwise
     */
    public boolean updateCoach(Coach coach) {
        return FootballStatisticsDB.updateCoach(coach);
    }

    /**
     * Delete a coach from the system
     *
     * @param coachId The ID of the coach to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteCoach(Long coachId) {
        return FootballStatisticsDB.deleteCoach(coachId);
    }

    /**
     * Find a coach by ID
     *
     * @param id The coach ID to search for
     * @return The found coach or null if not found
     */
    public Coach findCoachById(Long id) {
        return FootballStatisticsDB.getCoachById(id);
    }

    /**
     * Find a coach by name
     *
     * @param firstName The coach's first name
     * @param lastName  The coach's last name
     * @return An Optional containing the found coach, or empty if not found
     */
    public Optional<Coach> findCoachByName(String firstName, String lastName) {
        Collection<Coach> coaches = FootballStatisticsDB.getCoaches();

        for (Coach coach : coaches) {
            if (coach.getFirstName().equalsIgnoreCase(firstName) &&
                    coach.getLastName().equalsIgnoreCase(lastName)) {
                return Optional.of(coach);
            }
        }
        return Optional.empty();
    }

    /**
     * Get all coaches in the system
     *
     * @return Collection of all coaches
     */
    public Collection<Coach> getAllCoaches() {
        return FootballStatisticsDB.getCoaches();
    }
}
