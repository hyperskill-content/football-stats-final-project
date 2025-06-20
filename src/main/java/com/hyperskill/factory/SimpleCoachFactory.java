package com.hyperskill.factory;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.entity.Coach;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set; /**
 * Implementation that creates Coach objects directly in code
 */
public class SimpleCoachFactory implements CoachFactory {

    @Override
    public Coach createCoach(String firstName, String lastName, String team) {
        Coach coach = new Coach(firstName, lastName, team);
        FootballStatisticsDB.addCoach(coach);
        return coach;
    }

    @Override
    public Coach createCoachForTeam(String teamName) {
        // Return coach of the specified team from the database
        Collection<Coach> allCoaches = FootballStatisticsDB.getCoaches();


        for (Coach coach : allCoaches) {
            if (coach.getTeamName().equalsIgnoreCase(teamName)) {
                return coach;
            }
        }

        return null;
    }

    @Override
    public Set<Coach> createCoaches() {
        // Without hardcoded values, this method simply returns the existing coaches
        List<Coach> allCoaches = (List<Coach>) FootballStatisticsDB.getCoaches();
        return new HashSet<>(allCoaches);
    }
}
