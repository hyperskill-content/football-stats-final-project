package com.hyperskill.factory;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Match;
import com.hyperskill.entity.Player;
import com.hyperskill.entity.Team;
import com.hyperskill.utils.FootballDataLoader;

import java.time.LocalDateTime;
import java.util.*;


public interface MatchFactory {
    Match createMatch(Team homeTeam, Team awayTeam, int homeScore, int awayScore, LocalDateTime matchDate);
    Match createMatch(Team homeTeam, Team awayTeam, int homeScore, int awayScore,
                      LocalDateTime matchDate, Map<Player, Integer> goalScorers);
}


class JsonBasedMatchFactory implements MatchFactory {
    private String jsonFilePath = "src/main/java/com/hyperskill/resources/FootballMatchResults.json"; // Default path
    private FootballDataLoader dataLoader;
    private TeamFactory teamFactory;
    private boolean dataLoaded = false;


    @Override
    public Match createMatch(Team homeTeam, Team awayTeam, int homeScore, int awayScore, LocalDateTime matchDate) {
        Match match = new Match(homeTeam, awayTeam, homeScore, awayScore, matchDate);
        FootballStatisticsDB.addMatch(match);
        return match;
    }

    @Override
    public Match createMatch(Team homeTeam, Team awayTeam, int homeScore, int awayScore,
                             LocalDateTime matchDate, Map<Player, Integer> goalScorers) {
        // Implementation similar to SimpleMatchFactory but with JSON data processing
        // This would need to be updated in the actual implementation
        return null; // Simplified placeholder
    }
}
