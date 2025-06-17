# Football Statistics Project

In this project, you will develop a football statistics application that tracks teams, players, and match results using 
Object-Oriented Programming (OOP) and collections as a database. You'll implement features like sorting, filtering, and 
statistical analysis to determine top teams and players. This hands-on project will enhance your problem-solving 
and teamwork abilities, preparing you for real-world software development.

## Project Description

You need to implement an application, that stores the data of football match statistics. 

✅ Each class should implement the necessary CRUD operations relevant to its role.
✅ Each class should only perform operations directly related to its attributes and role in the system.
✅ In addition to the logic described below, you must implement various sorting and filtering operations of your choice.
    For example, identifying the top three teams in 2024 or the player with the highest goal count in a team.

The suggested class structure and logic are:

🏃 Player (base abstract class: Person)

- Attributes: String firstName, String lastName, String team
- Methods: display/update stats (goals scored, matches played, average goals scored, etc.) by year and total, etc.

🧑‍🏫 Coach (base abstract class: Person)

- Attributes: String firstName, String lastName, String team
- Methods: display/update stats (goals scored, matches played, average goals scored, etc.)

👨‍👨 Team

- Attributes: String name, List<Player> players, Coach coach
- Methods: display/update stats (percentage of wins/losses/draws by year/total, average/total goal score by year, total wins/losses/draws, etc.)

⚽ Match (updates the records of other classes when a match is added)

- Attributes: Team team1, Team team2, int team1score, int team2Score, Map<Player, Integer> goalScorers, LocalDateTime matchDate
- Methods: display summary of matches, update team stats, update coach stats, update player stats

🛢️ FootballStatisticsDB (Database simulation to store all the data in collections. You may use the factory pattern to generate data)

📺 SportsStatisticsInterface (Main UI Class)

- Handles user interaction (adding/removing teams, players, recording matches, viewing stats). 

Use a similar interface:

```
  🏆 FOOTBALL STATISTICS SYSTEM 🏆
  1️⃣ Manage Teams
  2️⃣ Manage Players
  3️⃣ Record a Match
  4️⃣ View Statistics
  5️⃣ Exit
  Select an option:
```

After selecting an option, relevant sub-options should be displayed.

⛔ Restrictions 
Don't use Stream API

🟡 Optional
File I/O – Save and load data from a file

```
interface FileHandler {
    void saveData(String filename);
    void loadData(String filename);
}
```

## Workflow

We suggest applying the following workflow:  

- **Define roles and responsibilities**, and assign tasks (e.g., one student works on UI, another on data handling, etc.).  
- **Break down the project into tasks** and describe each task (or a small group of related tasks) in a new GitHub issue. Use **To Do**, **In Progress**, and **Done** labels to track progress.  
- **Create a new pull request (PR) for each issue** (or a related group of issues), but keep them manageable. Each PR name should start with `#issue-number PR Description`. For example: `#1 Generate Data for players`.
- **Use a new branch for each PR**, then merge it into the `main` branch when it is approved by the team. Follow this branch naming convention: `issue-number-task-description`. For example: `issue-1-generaate-data`
- **Limit each PR to maximum 50-70 lines of code**, and keep them as short as possible. Avoid modifying the same class if another team member has an open PR affecting it, as this can lead to merge conflicts.  

