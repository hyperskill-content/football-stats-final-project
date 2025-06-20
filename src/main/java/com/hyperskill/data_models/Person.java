package com.hyperskill.data_models;

import jakarta.persistence.Entity;

@Entity
public abstract class Person {
    private String firstName;
    private String lastName;
    private String teamName;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName, String teamName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamName = teamName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}

