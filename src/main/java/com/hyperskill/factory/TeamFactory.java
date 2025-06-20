package com.hyperskill.factory;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Coach;
import com.hyperskill.entity.Player;
import com.hyperskill.entity.Team;
import com.hyperskill.utils.FootballDataLoader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface TeamFactory {
    Team createTeam(String name, Coach coach, Set<Player> players);
    Set<Team> createTeams();
    Team createTeamByName(String teamName);

    static TeamFactory getFactory(String sourceType) {
        if ("file".equalsIgnoreCase(sourceType)) {
            return new FileBasedTeamFactory();
        } else {
            return new SimpleTeamFactory();
        }
    }
}

class SimpleTeamFactory implements TeamFactory {
    private PlayerFactory playerFactory;
    private CoachFactory coachFactory;

    public SimpleTeamFactory() {
        this.playerFactory = PlayerFactory.getFactory("simple");
        this.coachFactory = CoachFactory.getFactory("simple");
    }

    @Override
    public Team createTeam(String name, Coach coach, Set<Player> players) {
        Team team = new Team(name, coach, players);
        FootballStatisticsDB.addTeam(team);
        return team;
    }

    @Override
    public Set<Team> createTeams() {
        // Return all existing teams from the database
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        return new HashSet<>(allTeams);
    }

    @Override
    public Team createTeamByName(String teamName) {
        // Check if team already exists in database
        Team existingTeam = FootballStatisticsDB.getTeamByName(teamName);
        if (existingTeam != null) {
            return existingTeam;
        }

        // If not found, create a new team with appropriate coach and players
        Coach coach = coachFactory.createCoachForTeam(teamName);
        Set<Player> players = playerFactory.createPlayersForTeam(teamName);
        return createTeam(teamName, coach, players);
    }
}

class FileBasedTeamFactory implements TeamFactory {
    private String filePath = "src/main/java/com/hyperskill/resources/Generate_Data.txt"; // Default path
    private FootballDataLoader dataLoader;
    private PlayerFactory playerFactory;
    private CoachFactory coachFactory;

    public FileBasedTeamFactory() {
        this.dataLoader = new FootballDataLoader();
        this.playerFactory = PlayerFactory.getFactory("file");
        this.coachFactory = CoachFactory.getFactory("file");
    }

    public FileBasedTeamFactory(String filePath) {
        this.filePath = filePath;
        this.dataLoader = new FootballDataLoader();
        this.playerFactory = PlayerFactory.getFactory("file");
        this.coachFactory = CoachFactory.getFactory("file");
    }

    @Override
    public Team createTeam(String name, Coach coach, Set<Player> players) {
        Team team = new Team(name, coach, players);
        FootballStatisticsDB.addTeam(team);
        return team;
    }

    @Override
    public Set<Team> createTeams() {
        // Load data from file if not already loaded
        FootballStatisticsDB.loadInitialDataIfNeeded(filePath);

        // Return all teams from the database
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        return new HashSet<>(allTeams);
    }

    @Override
    public Team createTeamByName(String teamName) {
        // Load data from file if not already loaded
        FootballStatisticsDB.loadInitialDataIfNeeded(filePath);

        // Try to find the team in the database
        Team team = FootballStatisticsDB.getTeamByName(teamName);

        if (team == null) {
            // If team doesn't exist yet, create it with appropriate coach and players
            Coach coach = coachFactory.createCoachForTeam(teamName);
            Set<Player> players = playerFactory.createPlayersForTeam(teamName);
            team = createTeam(teamName, coach, players);
        }

        return team;
    }
}