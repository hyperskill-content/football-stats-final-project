package com.hyperskill.factory;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Coach;
import com.hyperskill.utils.FootballDataLoader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface CoachFactory {

    Coach createCoach(String firstName, String lastName, String team);

    Set<Coach> createCoaches();

    Coach createCoachForTeam(String teamName);

    static CoachFactory getFactory(String sourceType) {
        if ("file".equalsIgnoreCase(sourceType)) {
            return new FileBasedCoachFactory();
        } else {
            return new SimpleCoachFactory();
        }
    }
}

/**
 * Implementation that loads Coach data from files
 */
class FileBasedCoachFactory implements CoachFactory {
    private String filePath = "src/main/java/com/hyperskill/resources/Generate_Data.txt"; // Default path
    private FootballDataLoader dataLoader;

    public FileBasedCoachFactory() {
        this.dataLoader = new FootballDataLoader();
    }

    public FileBasedCoachFactory(String filePath) {
        this.filePath = filePath;
        this.dataLoader = new FootballDataLoader();
    }

    @Override
    public Coach createCoach(String firstName, String lastName, String team) {
        Coach coach = new Coach(firstName, lastName, team);
        FootballStatisticsDB.addCoach(coach);
        return coach;
    }

    @Override
    public Coach createCoachForTeam(String teamName) {
        // Load data from file if not already loaded
        FootballStatisticsDB.loadInitialDataIfNeeded(filePath);

        // Get coach from the database for the specified team
        Collection<Coach> allCoaches = FootballStatisticsDB.getCoaches();

        for (Coach coach : allCoaches) {
            if (coach.getTeamName().equalsIgnoreCase(teamName)) {
                return coach;
            }
        }

        // If no coach found for this team, create a default one
        SimpleCoachFactory defaultFactory = new SimpleCoachFactory();
        return defaultFactory.createCoachForTeam(teamName);
    }

    @Override
    public Set<Coach> createCoaches() {
        // Use the improved database loading mechanism
        FootballStatisticsDB.loadInitialDataIfNeeded(filePath);

        // Return all coaches from the database
        Collection<Coach> allCoaches = FootballStatisticsDB.getCoaches();
        return new HashSet<>(allCoaches);
    }
}