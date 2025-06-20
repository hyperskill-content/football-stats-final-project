package com.hyperskill.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private Team homeTeam;
    private Team awayTeam;
    private int homeScore;
    private int awayScore;
    private Map<Player, Integer> goalScorers;
    private LocalDateTime matchDate;

    public Match() {}

    public Match(Team homeTeam, Team awayTeam, LocalDateTime matchDate) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.goalScorers = new HashMap<>();
        this.matchDate = matchDate;
    }

    public Match(Team homeTeam, Team awayTeam, int homeScore, int awayScore, LocalDateTime matchDate) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchDate = matchDate;
        this.goalScorers = new HashMap<>();
    }

    // New constructor that takes goalScorers
    public Match(Team homeTeam, Team awayTeam, int homeScore, int awayScore, Map<Player, Integer> goalScorers, LocalDateTime matchDate) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.goalScorers = new HashMap<>(goalScorers);
        this.matchDate = matchDate;
    }

    public long getId() {
        return id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public boolean isDraw() {
        return homeScore == awayScore;
    }

    public Team getWinner() {
        if (isDraw()) {
            return null;
        }
        return homeScore > awayScore ? homeTeam : awayTeam;
    }

    public Team getLoser() {
        if (isDraw()) {
            return null;
        }
        return homeScore < awayScore ? homeTeam : awayTeam;
    }

    public void updateStats() {
        // Update team statistics
        updateTeamStats();

        // Update player statistics
        updatePlayerStats();

        // Update coach statistics
        updateCoachStats();
    }

    private void updateTeamStats() {// Add this match to each team's match collection
        homeTeam.addMatch(this);
        awayTeam.addMatch(this);

        homeTeam.addGoals(homeScore);
        awayTeam.addGoals(awayScore);

    }

    private void updatePlayerStats() {
        for (Player player : homeTeam.getPlayers()) {
            player.incrementMatchesPlayed();
        }

        for (Player player : awayTeam.getPlayers()) {
            player.incrementMatchesPlayed();
        }

        for (Map.Entry<Player, Integer> entry : goalScorers.entrySet()) {
            Player player = entry.getKey();
            int goals = entry.getValue();
            player.addGoals(goals);
        }
    }

    private void updateCoachStats() {
        Coach homeCoach = homeTeam.getCoach();
        Coach awayCoach = awayTeam.getCoach();

        if (homeCoach != null) {
            homeCoach.incrementMatchesPlayed();
        }

        if (awayCoach != null) {
            awayCoach.incrementMatchesPlayed();
        }

    }

    public String getMatchSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Match Date: ").append(matchDate).append("\n");
        summary.append(String.format("%s %d - %d %s\n",
                homeTeam.getName(), homeScore, awayScore, awayTeam.getName()));
        summary.append("Goal Scorers:\n");

        for (Map.Entry<Player, Integer> entry : goalScorers.entrySet()) {
            summary.append(String.format("- %s %s: %d goals\n",
                    entry.getKey().getFirstName(),
                    entry.getKey().getLastName(),
                    entry.getValue()));
        }

        return summary.toString();
    }

    // Getters and setters
    public Map<Player, Integer> getGoalScorers() {
        return goalScorers;
    }

    public void setGoalScorers(Map<Player, Integer> goalScorers) {
        this.goalScorers = goalScorers;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    @Override
    public String toString() {
        return "Match{" +
                "homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                ", matchDate=" + matchDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id);
    }
}
