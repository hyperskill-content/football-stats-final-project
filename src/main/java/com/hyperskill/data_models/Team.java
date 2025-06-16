package com.hyperskill.data_models;

import com.hyperskill.FootballStatisticsDB;

import java.util.*;

/**
 * Team
 * <p>
 * Attributes: String name, List players, Coach coach
 * Methods: display/update stats (percentage of wins/losses/draws by year
 * /total, average/total goal score by year, total wins/losses/draws, etc.)
 */

public class Team {
    private final String id;
    private String name;
    private Set<Player> players = new HashSet<>();
    private Coach coach;
    private Set<Match> allMatches = new HashSet<>();
    private int goalScored;

    public Team() {
        this.id = UUID.randomUUID().toString(); // Auto-generate unique ID
    }

    public Team(String name) {
        this.id = UUID.randomUUID().toString(); // Auto-generate unique ID
        this.name = name;
    }

    public Team(String name, Coach coach, Set<Player> playersTeam) {
        this.id = UUID.randomUUID().toString(); // Auto-generate unique ID
        this.name = name;
        this.coach = coach;
        this.players = playersTeam;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            return;
        }
        this.name = name;
    }

    public int getGoalScored() {
        return this.goalScored;
    }

    public void addGoals(int goalScored) {
        this.goalScored += goalScored;
    }

    public void addPlayer(Player player) {
        if (!validatePlayersTeam(new HashSet<>(Set.of(player)), name)) {
            System.out.println("Error. The player doesn't play for this team.");
            return;
        }

        this.players.add(player);
    }

    public void addPlayers(Set<Player> playersTeam) {
        if (!validatePlayersTeam(playersTeam, name)) {
            System.out.println("Error. Not all players play for this team.");
            return;
        }

        this.players.addAll(playersTeam);
    }

    public Player getPlayer(String firstName, String lastname) {
        return FootballStatisticsDB.getPlayerByName(firstName, lastname);
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void replaceAllPlayers(Set<Player> playersTeam) {
        if (!validatePlayersTeam(playersTeam, name)) {
            System.out.println("Error. Not all players play for that team.");
            return;
        }

        this.players.clear();
        this.players.addAll(playersTeam);
    }

    public void deletePlayer(Player player) {
        this.players.remove(player);
    }

    public void deletePlayers(Set<Player> playersTeam) {
        this.players.removeAll(playersTeam);
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        if (!validateCoachTeam(coach, name)) {
            System.out.println("Error. Coach doesn't play for this team.");
            return;
        }

        this.coach = coach;
    }

    public void deleteCoach(Coach coach) {
        if (!this.coach.equals(coach)) {
            System.out.println("Error. The coach doesn't belong to this team.");
            return;
        }
        this.coach = null;
    }

    public void addMatch(Match match) {
        allMatches.add(match);
    }

    public void addMatches(Collection<Match> matches) {
        allMatches.addAll(matches);
    }

    public Collection<Match> getAllMatches() {
        return allMatches;
    }

    public Collection<Match> getWinMatches() {
        Set<Match> winMatches = new HashSet<>();
        for (Match match : allMatches) {
            if (match.getWinner() != null && match.getWinner().equals(this)) {
                winMatches.add(match);
            }
        }
        return winMatches;
    }

    public Collection<Match> getLoseMatches() {
        Set<Match> loseMatches = new HashSet<>();
        for (Match match : allMatches) {
            if (match.getLoser() != null && match.getLoser().equals(this)) {
                loseMatches.add(match);
            }
        }
        return loseMatches;
    }

    public Collection<Match> getDrawMatches() {
        Set<Match> drawMatches = new HashSet<>();
        for (Match match : allMatches) {
            if (match.isDraw()) {
                drawMatches.add(match);
            }
        }
        return drawMatches;
    }

    public Collection<Match> getAllMatchesPerYear(int year) {
        Set<Match> allMatchesPerYear = new HashSet<>();

        for (Match match : allMatches) {
            if (match.getMatchDate().getYear() == year) {
                allMatchesPerYear.add(match);
            }
        }

        return allMatchesPerYear;
    }

    public Collection<Match> getWinsMatchesPerYear(int year) {
        Set<Match> winsMatchesPerYear = new HashSet<>();

        for (Match match : getWinMatches()) {
            if (match.getMatchDate().getYear() == year) {
                winsMatchesPerYear.add(match);
            }
        }

        return winsMatchesPerYear;
    }

    public Collection<Match> getLossesMatchesPerYear(int year) {
        Set<Match> lossesMatchesPerYear = new HashSet<>();

        for (Match match : getLoseMatches()) {
            if (match.getMatchDate().getYear() == year) {
                lossesMatchesPerYear.add(match);
            }
        }

        return lossesMatchesPerYear;
    }

    public Collection<Match> getDrawsMatchesPerYear(int year) {
        Set<Match> drawsMatchesPerYear = new HashSet<>();

        for (Match match : getDrawMatches()) {
            if (match.getMatchDate().getYear() == year) {
                drawsMatchesPerYear.add(match);
            }
        }

        return drawsMatchesPerYear;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ", coach=" + coach +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private boolean validatePlayersTeam(Set<Player> playersTeam, String teamName) {
        for (Player player : playersTeam) {
            if (!player.getTeamName().equalsIgnoreCase(teamName)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateCoachTeam(Coach coach, String teamName) {
        // Check if coach is null before calling getTeamName()
        if (coach == null) {
            return false;
        }
        return coach.getTeamName().equalsIgnoreCase(teamName);
    }
}
