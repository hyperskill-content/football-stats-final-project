package com.hyperskill.data_models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

/**
 * Player (base abstract class: Person)
 * <p>
 * Attributes: String firstName, String lastName, String team
 * Methods: display/update stats (goals scored, matches played, average goals scored, etc.) by year and total, etc.
 */
@Entity
public class Player extends Person {
    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private String teamName;
    private int goals;
    private int playedMatches;

    public Player() {}

    public Player(String firstName, String lastName, String teamName) {
        super(firstName, lastName);
        this.teamName = teamName;
    }

    public Player(String firstName, String lastName, String teamName, int goals) {
        super(firstName, lastName);
        this.teamName = teamName;
        this.goals = goals;
    }

    //Getters and Setters
    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return super.getFirstName();
    }

    public String getLastName() {
        return super.getLastName();
    }

    public String getTeamName() {
        return teamName;
    }

    public int getGoals() {
        return goals;
    }

    public int getPlayedMatches() {
        return playedMatches;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    @Override
    public String toString() {
        return "Player {" + "firstName = " + super.getFirstName()
                + ", lastName = " + super.getLastName()
                + ", team = " + teamName
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
