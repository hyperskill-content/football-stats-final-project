package com.hyperskill.statistics;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Coach;
import com.hyperskill.data_models.Match;
import com.hyperskill.data_models.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoachStatistics {
    public int goalsScoredCurrentTeam(Coach coach){
        Team currTeam = FootballStatisticsDB.getTeamByName(coach.getTeamName());
        if(currTeam == null){
            return 0;
        }
        return currTeam.getGoalScored();
    }

    public int playedMatchesTotal(Coach coach){
        return coach.getPlayedMatches();
    }

    public double averageGoalsScored(Coach coach){
        return (double) goalsScoredCurrentTeam(coach) / playedMatchesTotal(coach);
    }

    public List<Coach> findTopCoachesByVictories(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter n must be a positive integer");
        }

        Map<Coach, Integer> coachVictories = calculateVictoriesForAllCoaches();
        return getTopNCoaches(coachVictories, n);
    }

    /**
     * Finds the top n coaches based on goals scored by their teams
     * @param n The number of top coaches to return
     * @return A list of coaches sorted by goals scored (descending)
     * @throws IllegalArgumentException if n is less than or equal to 0
     */
    public List<Coach> findTopCoachesByGoalsScored(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter n must be a positive integer");
        }

        Map<Coach, Integer> coachGoals = calculateGoalsScoredForAllCoaches();
        return getTopNCoaches(coachGoals, n);
    }

    public List<Coach> findTopCoachesByWinPercentage(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter n must be a positive integer");
        }

        Map<Coach, Double> coachWinPercentage = new HashMap<>();

        for (Coach coach : FootballStatisticsDB.getCoaches()) {
            Team team = FootballStatisticsDB.getTeamByName(coach.getTeamName());
            if (team != null) {
                int totalMatches = team.getAllMatches().size();
                int wins = team.getWinMatches().size();
                double winPercentage = (totalMatches > 0) ? ((double) wins / totalMatches) * 100 : 0;
                coachWinPercentage.put(coach, winPercentage);
            }
        }

        List<Map.Entry<Coach, Double>> entryList = new ArrayList<>(coachWinPercentage.entrySet());
        entryList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        List<Coach> topCoaches = new ArrayList<>();
        for (int i = 0; i < n && i < entryList.size(); i++) {
            topCoaches.add(entryList.get(i).getKey());
        }

        return topCoaches;
    }


    /**
     * Calculates the number of victories for each coach based on match results
     * @return A map with coaches as keys and their victories as values
     */
    public Map<Coach, Integer> calculateVictoriesForAllCoaches() {
        Map<Coach, Integer> victories = new HashMap<>();

        // Initialize all coaches with 0 victories
        for (Coach coach : FootballStatisticsDB.getCoaches()) {
            victories.put(coach, 0);
        }

        // Calculate victories for each coach based on match results
        for (Match match : FootballStatisticsDB.getMatches()) {
            Team winnerTeam = match.getWinner();
            // Skip if there's no winner (likely a draw)
            if (winnerTeam != null) {
                Coach winnerCoach = winnerTeam.getCoach();
                victories.put(winnerCoach, victories.get(winnerCoach) + 1);
            }
        }
        return victories;
    }

    /**
     * Calculates the goals scored by teams for each coach
     * @return A map with coaches as keys and their team's goals as values
     */
    private Map<Coach, Integer> calculateGoalsScoredForAllCoaches() {
        Map<Coach, Integer> goalsScored = new HashMap<>();

        // Initialize all coaches with 0 goals
        for (Coach coach : FootballStatisticsDB.getCoaches()) {
            Team team = FootballStatisticsDB.getTeamByName(coach.getTeamName());
            int goals = (team != null) ? team.getGoalScored() : 0;
            goalsScored.put(coach, goals);
        }

        return goalsScored;
    }

    /**
     * Helper method to get the top n coaches from a map of coach statistics
     *
     * @param coachStats Map containing coaches and their associated statistic value
     * @param n          The number of top coaches to return
     * @return A list of the top n coaches sorted by the statistic value (descending)
     */
    private List<Coach> getTopNCoaches(Map<Coach, Integer> coachStats, int n) {
        List<Map.Entry<Coach, Integer>> entryList = new ArrayList<>(coachStats.entrySet());
        entryList.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        List<Coach> topCoaches = new ArrayList<>();
        for (int i = 0; i < n && i < entryList.size(); i++) {
            topCoaches.add(entryList.get(i).getKey());
        }
        return topCoaches;
    }

    /**
     * Finds the worst n coaches based on number of losses
     * @param n The number of coaches to return
     * @return A list of coaches sorted by number of losses (descending)
     * @throws IllegalArgumentException if n is less than or equal to 0
     */
    public List<Coach> findWorstCoachesByLosses(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter n must be a positive integer");
        }

        Map<Coach, Integer> coachLosses = calculateLossesForAllCoaches();
        return getTopNCoaches(coachLosses, n);
    }

    /**
     * Finds the worst n coaches based on goals conceded by their teams
     * @param n The number of coaches to return
     * @return A list of coaches sorted by goals conceded (descending)
     * @throws IllegalArgumentException if n is less than or equal to 0
     */
    public List<Coach> findWorstCoachesByGoalsConceded(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter n must be a positive integer");
        }

        Map<Coach, Integer> coachGoalsConceded = calculateGoalsConcededForAllCoaches();
        return getTopNCoaches(coachGoalsConceded, n);
    }

    /**
     * Calculates the number of losses for each coach based on match results
     * @return A map with coaches as keys and their losses as values
     */
    public Map<Coach, Integer> calculateLossesForAllCoaches() {
        Map<Coach, Integer> losses = new HashMap<>();

        // Initialize all coaches with 0 losses
        for (Coach coach : FootballStatisticsDB.getCoaches()) {
            losses.put(coach, 0);
        }

        // Calculate losses for each coach based on match results
        for (Match match : FootballStatisticsDB.getMatches()) {
            Team loserTeam = match.getLoser();
            // Skip if there's no loser (likely a draw)
            if (loserTeam != null) {
                Coach loserCoach = loserTeam.getCoach();
                losses.put(loserCoach, losses.get(loserCoach) + 1);
            }
        }
        return losses;
    }

    /**
     * Calculates the goals conceded by teams for each coach
     * @return A map with coaches as keys and their team's goals conceded as values
     */
    public Map<Coach, Integer> calculateGoalsConcededForAllCoaches() {
        Map<Coach, Integer> goalsConceded = new HashMap<>();

        // Initialize all coaches with 0 goals conceded
        for (Coach coach : FootballStatisticsDB.getCoaches()) {
            goalsConceded.put(coach, 0);
        }

        // Calculate goals conceded for each coach based on match results
        for (Match match : FootballStatisticsDB.getMatches()) {
            Team homeTeam = match.getHomeTeam();
            Team awayTeam = match.getAwayTeam();

            if (homeTeam != null && homeTeam.getCoach() != null) {
                Coach homeCoach = homeTeam.getCoach();
                goalsConceded.put(homeCoach, goalsConceded.get(homeCoach) + match.getAwayScore());
            }

            if (awayTeam != null && awayTeam.getCoach() != null) {
                Coach awayCoach = awayTeam.getCoach();
                goalsConceded.put(awayCoach, goalsConceded.get(awayCoach) + match.getHomeScore());
            }
        }
        return goalsConceded;
    }

}