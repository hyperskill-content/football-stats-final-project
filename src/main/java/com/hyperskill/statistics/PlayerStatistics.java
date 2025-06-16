package com.hyperskill.statistics;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Match;
import com.hyperskill.data_models.Player;
import com.hyperskill.data_models.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerStatistics {

    public List<Player> getAllPlayers() {
        return new ArrayList<>(FootballStatisticsDB.getPlayers());
    }

    public List<Player> getTopPlayersByGoalsScored(int n) {
        var players = getAllPlayers();
        players.sort((p1, p2) -> Integer.compare(p2.getGoals(), p1.getGoals()));
        return getNTopPlayersFromList(players, n);
    }

    public List<Player> getTopPlayersByMatchesPlayed(int n) {
        var players = getAllPlayers();
        players.sort((p1, p2) -> Integer.compare(p2.getPlayedMatches(), p1.getPlayedMatches()));
        return getNTopPlayersFromList(players, n);
    }

    private List<Player> getNTopPlayersFromList(List<Player> players, int n) {
        List<Player> topPlayers = new ArrayList<>();
        for (int i = 0; i < Math.min(n, players.size()); i++) {
            topPlayers.add(players.get(i));
        }
        return topPlayers;
    }

    public int goalsScoredTotal(Player player) {
        return player.getGoals();
    }

    public int goalsScoredPerYear(Player player, int year) {
        int amountGoals = 0;
        Team currTeam = FootballStatisticsDB.getTeamByName(player.getTeamName());
        if (currTeam == null) {
            return 0;
        }
        for (Match match : currTeam.getAllMatches()) {
            if (match.getMatchDate().getYear() == year) {
                Map<Player, Integer> goalsScorer = match.getGoalScorers();
                for (Player scorer : goalsScorer.keySet()) {
                    if (scorer.equals(player)) {
                        amountGoals += goalsScorer.get(scorer);
                    }
                }
            }
        }
        return amountGoals;
    }

    public int matchesPlayedTotal(Player player) {
        return player.getPlayedMatches();
    }

    public int matchesPlayedPerYear(Player player, int year) {
        int amountPlayedMatches = 0;
        Team currTeam = FootballStatisticsDB.getTeamByName(player.getTeamName());
        if (currTeam == null) {
            return 0;
        }
        for (Match match : currTeam.getAllMatches()) {
            if (match.getMatchDate().getYear() == year) {
                amountPlayedMatches++;
            }
        }
        return amountPlayedMatches;
    }

    public double averageGoalsScoredTotal(Player player) {
        int matches = matchesPlayedTotal(player);
        return matches == 0 ? 0.0 : (double) goalsScoredTotal(player) / matches;
    }

    public double averageGoalsScoredPerYear(Player player, int year) {
        int matchesPerYear = matchesPlayedPerYear(player, year);
        return matchesPerYear == 0 ? 0.0 : (double) goalsScoredPerYear(player, year) / matchesPerYear;
    }
}
