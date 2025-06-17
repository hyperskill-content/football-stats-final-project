package com.hyperskill.factory;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Match;
import com.hyperskill.data_models.Player;
import com.hyperskill.data_models.Team;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SimpleMatchFactory implements MatchFactory {
    private TeamFactory teamFactory;

    public SimpleMatchFactory() {
        this.teamFactory = TeamFactory.getFactory("simple");
    }

    @Override
    public Match createMatch(Team homeTeam, Team awayTeam, int homeScore, int awayScore, LocalDateTime matchDate) {
        return createMatch(homeTeam, awayTeam, homeScore, awayScore, matchDate, null);
    }

    @Override
    public Match createMatch(Team homeTeam, Team awayTeam, int homeScore, int awayScore,
                             LocalDateTime matchDate, Map<Player, Integer> goalScorers) {
        // If goal scorers are not provided, create an empty map
        Map<Player, Integer> finalScorers = goalScorers != null ? goalScorers : new HashMap<>();

        // Validate goal scorers
        if (goalScorers != null) {
            int homeGoals = 0;
            int awayGoals = 0;

            // Count goals by team and validate player team membership
            for (Map.Entry<Player, Integer> entry : goalScorers.entrySet()) {
                Player player = entry.getKey();
                int goals = entry.getValue();

                // Check which team the player belongs to
                if (player.getTeamName().equals(homeTeam.getName())) {
                    homeGoals += goals;
                } else if (player.getTeamName().equals(awayTeam.getName())) {
                    awayGoals += goals;
                } else {
                    throw new IllegalArgumentException("Player " + player.getFirstName() + " " +
                            player.getLastName() + " does not belong to either team in this match");
                }
            }

            // Validate total goals
            if (homeGoals != homeScore) {
                throw new IllegalArgumentException("Home goal scorers' goals don't match the home score");
            }
            if (awayGoals != awayScore) {
                throw new IllegalArgumentException("Away goal scorers' goals don't match the away score");
            }
        }

        Match match = new Match(homeTeam, awayTeam, homeScore, awayScore, finalScorers, matchDate);
        FootballStatisticsDB.addMatch(match);
        // Create and return the Match object with the consolidated goalScorers map
        return match;
    }

}
