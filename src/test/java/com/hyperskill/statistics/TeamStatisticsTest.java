package com.hyperskill.statistics;

import com.hyperskill.FootballStatisticsDB;
import com.hyperskill.data_models.Match;
import com.hyperskill.data_models.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TeamStatisticsTest {

    @Mock
    private Team teamMock;

    @Mock
    private Match matchMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAmountWinsTotal() {
        // Arrange
        Collection<Match> winMatches = new ArrayList<>();
        winMatches.add(matchMock); // simulate one win match
        Mockito.when(teamMock.getWinMatches()).thenReturn(winMatches);

        // Act
        int result = TeamStatistics.amountWinsTotal(teamMock);

        // Assert
        assertEquals(1, result, "Amount of wins should be 1");
    }

    @Test
    public void testAmountLossesTotal() {
        // Arrange
        Collection<Match> loseMatches = new ArrayList<>();
        loseMatches.add(matchMock); // simulate one loss match
        Mockito.when(teamMock.getLoseMatches()).thenReturn(loseMatches);

        // Act
        int result = TeamStatistics.amountLossesTotal(teamMock);

        // Assert
        assertEquals(1, result, "Amount of losses should be 1");
    }

    @Test
    public void testAmountDrawsTotal() {
        // Arrange
        Collection<Match> drawMatches = new ArrayList<>();
        drawMatches.add(matchMock); // simulate one draw match
        Mockito.when(teamMock.getDrawMatches()).thenReturn(drawMatches);

        // Act
        int result = TeamStatistics.amountDrawsTotal(teamMock);

        // Assert
        assertEquals(1, result, "Amount of draws should be 1");
    }

    @Test
    public void testAmountWinsPerYear() {
        // Arrange
        int year = Year.now().getValue();
        Collection<Match> winMatches = new ArrayList<>();
        winMatches.add(matchMock);
        Mockito.when(teamMock.getWinsMatchesPerYear(year)).thenReturn(winMatches);

        // Act
        int result = TeamStatistics.amountWinsPerYear(teamMock, year);

        // Assert
        assertEquals(1, result, "Amount of wins for the year should be 1");
    }

    @Test
    public void testPercentageWinsTotal() {
        // Arrange
        Collection<Match> winMatches = new ArrayList<>();
        Collection<Match> allMatches = new ArrayList<>();
        winMatches.add(matchMock);
        allMatches.add(matchMock);
        Mockito.when(teamMock.getWinMatches()).thenReturn(winMatches);
        Mockito.when(teamMock.getAllMatches()).thenReturn(allMatches);

        // Act
        double result = TeamStatistics.percentageWinsTotal(teamMock);

        // Assert
        assertEquals(1.0, result, "Percentage of wins should be 1.0");
    }

    @Test
    public void testTopWinsTeams() {
        // Arrange
        List<Team> allTeams = new ArrayList<>();
        Team team1 = Mockito.mock(Team.class);
        Team team2 = Mockito.mock(Team.class);

        // Mocking the number of wins
        Mockito.when(team1.getWinMatches()).thenReturn(Collections.singletonList(matchMock));
        Mockito.when(team2.getWinMatches()).thenReturn(Collections.emptyList());

        allTeams.add(team1);
        allTeams.add(team2);

        // Mock FootballStatisticsDB to return our teams list
        Mockito.mockStatic(FootballStatisticsDB.class);
        Mockito.when(FootballStatisticsDB.getTeams()).thenReturn(allTeams);

        // Act
        Collection<Map.Entry<String, Integer>> result = TeamStatistics.topWinsTeams(1);

        // Assert
        assertEquals(1, result.size(), "There should be 1 team with the most wins");
        assertEquals(team1.getName(), result.iterator().next().getKey(), "The team with the most wins should be team1");
    }

    // Additional test cases for other methods can be written in a similar fashion

    @Test
    public void testTopLosesTeams() {
        // Similar structure as topWinsTeams but testing for losses
    }

    @Test
    public void testTopDrawsTeams() {
        // Similar structure as topWinsTeams but testing for draws
    }

    @Test
    public void testAverageGoalsScoredTotal() {
        // Similar structure for average goals scored methods
    }

    // Additional tests for other methods can be added as needed

}
