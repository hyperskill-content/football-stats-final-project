package com.hyperskill.utils;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Utility class containing patterns for parsing data from files
 */
public class ParseUtil {

    public static final Pattern PLAYER_PATTERN =
            Pattern.compile("new Player\\(\"([^\"]+)\", \"([^\"]+)\", \"([^\"]+)\"\\)");

    public static final Pattern COACH_PATTERN =
            Pattern.compile("new Coach\\(\"([^\"]+)\", \"([^\"]+)\", \"([^\"]+)\"\\)\\)?");

    public static final Pattern TEAM_PATTERN =
        Pattern.compile("new Team\\(\"([^\"]+)\", ([^,]+), ([^\\)]+)\\)");
    
    public static final Pattern MATCH_PATTERN =
        Pattern.compile("new Match\\(\"([^\"]+)\", \"([^\"]+)\", (\\d+), (\\d+), \"([^\"]+)\"\\)");
    
    public static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}