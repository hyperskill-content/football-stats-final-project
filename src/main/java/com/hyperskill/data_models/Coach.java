package com.hyperskill.data_models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

/**
 * Coach (base abstract class: Person)
 * <p>
 * Attributes: String firstName, String lastName, String team
 * Methods: display/update stats (goals scored, matches played, average goals scored, etc.)
 */
@Entity
public class Coach extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String teamName;
    private int playedMatches;

    public Coach() {}

    public Coach(String firstName, String lastName, String teamName) {
        super(firstName, lastName);
        this.teamName = teamName;
    }

    public long getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPlayedMatches() {
        return playedMatches;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void incrementMatchesPlayed() {
        this.playedMatches++;
    }

    @Override
    public String toString() {
        return "Coach{" + "firstName = " + super.getFirstName() +
                ", lastName = " + super.getLastName()
                + ", team=" + teamName + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(id, coach.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
