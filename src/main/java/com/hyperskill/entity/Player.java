package com.hyperskill.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * Player (base abstract class: Person)
 * <p>
 * Attributes: String firstName, String lastName, String team
 * Methods: display/update stats (goals scored, matches played, average goals scored, etc.) by year and total, etc.
 */
@Entity
public class Player extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    //deprecated, will be deleted after all changes
    private String teamName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    private int goals;
    private int playedMatches;

    public Player() {}

    //deprecated
    public Player(String firstName, String lastName, String teamName) {
        super(firstName, lastName);
        this.teamName = teamName;
    }

    public Player(String firstName, String lastName, Team team) {
        super(firstName, lastName);
        this.team = team;
    }

    public Player(String firstName, String lastName, Team team, int goals) {
        super(firstName, lastName);
        this.team = team;
        this.goals = goals;
    }

    //Getters and Setters
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return super.getFirstName();
    }

    public String getLastName() {
        return super.getLastName();
    }

    public Team getTeam() {
        return team;
    }

    public int getGoals() {
        return goals;
    }

    public int getPlayedMatches() {
        return playedMatches;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    @Override
    public String toString() {
        return "Player {" + "firstName = " + super.getFirstName()
                + ", lastName = " + super.getLastName()
                + ", team = " + team.getName()
                + ", goals = " + goals + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    //Methods
    public void addGoals(int goals) {
        if (goals < 0) {
            return;
        }

        this.goals += goals;
    }

    public void incrementMatchesPlayed() {
        this.playedMatches++;
    }
}
