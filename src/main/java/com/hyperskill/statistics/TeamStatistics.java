package com.hyperskill.statistics;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Match;
import com.hyperskill.entity.Team;

import java.time.Year;
import java.util.*;

public class TeamStatistics {
    public static int amountMatchesTotal(Team team) {
        if (team == null) {
            return 0;
        }

        Collection<Match> matches = team.getAllMatches();
        if (matches == null || matches.isEmpty()) {
            return 0;
        }

        return matches.size();
    }

    public static int amountWinsTotal(Team team) {
        if (team == null) {
            return 0;
        }

        Collection<Match> winMatches = team.getWinMatches();
        if (winMatches == null || winMatches.isEmpty()) {
            return 0;
        }

        return winMatches.size();
    }

    public static int amountLossesTotal(Team team) {
        if (team == null) {
            return 0;
        }

        Collection<Match> loseMatches = team.getLoseMatches();
        if (loseMatches == null || loseMatches.isEmpty()) {
            return 0;
        }

        return loseMatches.size();
    }

    public static int amountDrawsTotal(Team team) {
        if (team == null) {
            return 0;
        }

        Collection<Match> drawMatches = team.getDrawMatches();
        if (drawMatches == null || drawMatches.isEmpty()) {
            return 0;
        }

        return drawMatches.size();
    }

    public static int amountMatchesPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0;
        }

        Collection<Match> matches = team.getAllMatchesPerYear(year);
        if (matches == null || matches.isEmpty()) {
            return 0;
        }

        return matches.size();
    }

    public static int amountWinsPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0;
        }

        Collection<Match> winMatches = team.getWinsMatchesPerYear(year);
        if (winMatches == null || winMatches.isEmpty()) {
            return 0;
        }

        return winMatches.size();
    }

    public static int amountLossesPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0;
        }

        Collection<Match> loseMatches = team.getLossesMatchesPerYear(year);
        if (loseMatches == null || loseMatches.isEmpty()) {
            return 0;
        }

        return loseMatches.size();
    }

    public static int amountDrawsPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0;
        }

        Collection<Match> drawMatches = team.getDrawsMatchesPerYear(year);
        if (drawMatches == null || drawMatches.isEmpty()) {
            return 0;
        }

        return drawMatches.size();
    }

    public static double percentageWinsTotal(Team team) {
        if (team == null) {
            return 0.0;
        }

        int winMatches = TeamStatistics.amountWinsTotal(team);
        int allMatches = TeamStatistics.amountMatchesTotal(team);
        return allMatches == 0 ? 0.0 : (double) winMatches / allMatches * 100;
    }

    public static double percentageLossesTotal(Team team) {
        if (team == null) {
            return 0.0;
        }

        int loseMatches = amountLossesTotal(team);
        int allMatches = amountMatchesTotal(team);
        return allMatches == 0 ? 0.0 : (double) loseMatches / allMatches * 100;
    }

    public static double percentageDrawsTotal(Team team) {
        if (team == null) {
            return 0.0;
        }

        int drawMatches = amountDrawsTotal(team);
        int allMatches = amountMatchesTotal(team);
        return allMatches == 0 ? 0.0 : (double) drawMatches / allMatches * 100;
    }

    public static double percentageWinsPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0.0;
        }

        int winMatches = TeamStatistics.amountWinsPerYear(team, year);
        int allMatches = TeamStatistics.amountMatchesPerYear(team, year);
        return allMatches == 0 ? 0.0 : (double) winMatches / allMatches * 100;
    }

    public static double percentageLossesPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0.0;
        }

        int loseMatches = TeamStatistics.amountLossesPerYear(team, year);
        int allMatches = TeamStatistics.amountMatchesPerYear(team, year);
        return allMatches == 0 ? 0.0 : (double) loseMatches / allMatches * 100;
    }

    public static double percentageDrawsPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0.0;
        }

        int drawMatches = TeamStatistics.amountDrawsPerYear(team, year);
        int allMatches = TeamStatistics.amountMatchesPerYear(team, year);
        return allMatches == 0 ? 0.0 : (double) drawMatches / allMatches * 100;
    }

    public static double percentageScoredGoalsTotal(Team team) {
        if (team == null) {
            return 0.0;
        }

        int allMatches = amountMatchesTotal(team);
        return allMatches == 0 ? 0.0 : (double) team.getGoalScored() / allMatches * 100;
    }

    public static int amountGoalsScoredPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0;
        }

        Collection<Match> matchesPerYear = team.getAllMatchesPerYear(year);
        if (matchesPerYear == null || matchesPerYear.isEmpty()) {
            return 0;
        }
        int totalGoalsPerYear = 0;
        for (Match match : matchesPerYear) {
            if (match.getHomeTeam().equals(team)) {
                totalGoalsPerYear += match.getHomeScore();
            } else {
                totalGoalsPerYear += match.getAwayScore();
            }
        }
        return totalGoalsPerYear;
    }

    public static double percentageScoredGoalsPerYear(Team team, int year) {
        if (team == null || year <= 0 || year > Year.now().getValue()) {
            return 0.0;
        }

        int allMatchesPerYear = amountMatchesPerYear(team, year);
        return allMatchesPerYear == 0 ? 0.0 :
                (double) amountGoalsScoredPerYear(team, year) / allMatchesPerYear * 100;
    }

    /**
     * Retrieves a collection of the top teams ranked by the number of wins.
     *
     * @param amountTeams the maximum number of teams to include in the result.
     * @return a collection of map entries, where each entry consists of a team's name as the key
     * and its win count as the value, sorted in descending order of wins.
     */
    public static Collection<Map.Entry<String, Integer>> topWinsTeams(int amountTeams) {
        Map<String, Integer> allTeamsWithWinMatches = new HashMap<>();
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        for (Team team : allTeams) {
            allTeamsWithWinMatches.put(team.getName(), amountWinsTotal(team));
        }

        List<Map.Entry<String, Integer>> teams = new ArrayList<>(allTeamsWithWinMatches.entrySet());
        teams.sort((t1, t2) -> t2.getValue().compareTo(t1.getValue()));
        return teams.subList(0, Math.min(amountTeams, teams.size()));
    }

    /**
     * Retrieves a collection of the top teams ranked by the number of losses.
     *
     * @param amountTeams the maximum number of teams to include in the result.
     * @return a collection of map entries, where each entry consists of a team's name as the key
     * and its loss count as the value, sorted in descending order of losses.
     */
    public static Collection<Map.Entry<String, Integer>> topLosesTeams(int amountTeams) {
        Map<String, Integer> allTeamsWithLoseMatches = new HashMap<>();
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        for (Team team : allTeams) {
            allTeamsWithLoseMatches.put(team.getName(), amountLossesTotal(team));
        }

        List<Map.Entry<String, Integer>> teams = new ArrayList<>(allTeamsWithLoseMatches.entrySet());
        teams.sort((t1, t2) -> t2.getValue().compareTo(t1.getValue()));
        return teams.subList(0, Math.min(amountTeams, teams.size()));
    }

    /**
     * Retrieves a collection of the top teams ranked by the number of draws.
     *
     * @param amountTeams the maximum number of teams to include in the result.
     * @return a collection of map entries, where each entry consists of a team's name as the key
     * and its draw count as the value, sorted in descending order of draws.
     */
    public static Collection<Map.Entry<String, Integer>> topDrawsTeams(int amountTeams) {
        Map<String, Integer> allTeamsWithDrawMatches = new HashMap<>();
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        for (Team team : allTeams) {
            allTeamsWithDrawMatches.put(team.getName(), amountDrawsTotal(team));
        }

        List<Map.Entry<String, Integer>> teams = new ArrayList<>(allTeamsWithDrawMatches.entrySet());
        teams.sort((t1, t2) -> t2.getValue().compareTo(t1.getValue()));
        return teams.subList(0, Math.min(amountTeams, teams.size()));
    }

    public static Collection<Map.Entry<String, Integer>> topTeamsByScoredGoalsTotal(int amountTeams) {
        Map<String, Integer> allTeamsWithScoredGoals = new HashMap<>();
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        for (Team team : allTeams) {
            allTeamsWithScoredGoals.put(team.getName(), team.getGoalScored());
        }

        // Convert map entries to a List
        List<Map.Entry<String, Integer>> sortedTeams = new ArrayList<>(allTeamsWithScoredGoals.entrySet());
        sortedTeams.sort((t1, t2) -> t2.getValue().compareTo(t1.getValue()));
        return sortedTeams.subList(0, Math.min(amountTeams, sortedTeams.size()));
    }

    public static Collection<Map.Entry<String, Integer>> topTeamsByScoredGoalsPerYear(int amountTeams, int year) {
        if (year <= 0 || year > Year.now().getValue()) {
            return null;
        }
        Map<String, Integer> allTeamsWithScoredGoalsPerYear = new HashMap<>();
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        for (Team team : allTeams) {
            allTeamsWithScoredGoalsPerYear.put(team.getName(), amountGoalsScoredPerYear(team, year));
        }

        // Convert map entries to a List
        List<Map.Entry<String, Integer>> sortedTeams = new ArrayList<>(allTeamsWithScoredGoalsPerYear.entrySet());
        sortedTeams.sort((t1, t2) -> t2.getValue().compareTo(t1.getValue()));
        return sortedTeams.subList(0, Math.min(amountTeams, sortedTeams.size()));
    }

    public static Collection<Map.Entry<String, Integer>> lowestRankedTeamsByScoredGoalsTotal(int amountTeams) {
        Map<String, Integer> allTeamsWithScoredGoals = new HashMap<>();
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        for (Team team : allTeams) {
            allTeamsWithScoredGoals.put(team.getName(), team.getGoalScored());
        }

        // Convert map entries to a List
        List<Map.Entry<String, Integer>> sortedTeams = new ArrayList<>(allTeamsWithScoredGoals.entrySet());
        sortedTeams.sort((t1, t2) -> t1.getValue().compareTo(t2.getValue()));
        return sortedTeams.subList(0, Math.min(amountTeams, sortedTeams.size()));
    }

    public static Collection<Map.Entry<String, Integer>> lowestRankedTeamsByScoredGoalsPerYear(int amountTeams, int year) {
        Map<String, Integer> allTeamsWithScoredGoals = new HashMap<>();
        Collection<Team> allTeams = FootballStatisticsDB.getTeams();
        for (Team team : allTeams) {
            allTeamsWithScoredGoals.put(team.getName(), amountGoalsScoredPerYear(team, year));
        }

        // Convert map entries to a List
        List<Map.Entry<String, Integer>> sortedTeams = new ArrayList<>(allTeamsWithScoredGoals.entrySet());
        sortedTeams.sort((t1, t2) -> t1.getValue().compareTo(t2.getValue()));
        return sortedTeams.subList(0, Math.min(amountTeams, sortedTeams.size()));
    }
}
