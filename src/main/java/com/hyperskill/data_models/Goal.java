package com.hyperskill.data_models;

import java.util.Objects;

public class Goal {
    private Player player;   // Player who scored
    private Team team;     // Team that scored
    private int minute;      // Minute of the match when the goal was scored
    private boolean isOwnGoal; // Whether it's an own goal

    public Goal(Player player, Team team, int minute, boolean isOwnGoal) {
        if (minute < 0 || minute > 120) {
            throw new IllegalArgumentException("Minute must be between 0 and 120");
        }

        this.player = player;
        this.team = team;
        this.minute = minute;
        this.isOwnGoal = isOwnGoal;
    }

    // Getters and setters
    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isOwnGoal() {
        return isOwnGoal;
    }

    @Override
    public String toString() {
        return String.format("%s scored for %s at minute %d%s",
                player.getLastName(),
                team.getName(), minute, isOwnGoal ? " (own goal)" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return minute == goal.minute
                && isOwnGoal == goal.isOwnGoal
                && Objects.equals(player.getId(), goal.player.getId())
                && Objects.equals(team.getId(), goal.team.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(player.getId(), team.getId(), minute, isOwnGoal);
    }
}
