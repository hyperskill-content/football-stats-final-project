# Football Statistics Final Project

In this project, you will develop a football statistics management system as a Spring Boot web application that
tracks teams, players, and match results using Object-Oriented Programming (OOP) principles and a relational database.
You'll implement RESTful APIs and features like sorting, filtering, and statistical analysis to determine top teams
and players. This hands-on project will enhance your problem-solving and teamwork abilities, preparing you for
real-world software development.

## Project Description

You need to implement an application, that stores the data of football match statistics.

✅ Implement a proper layered architecture (Controller, Service, Repository, Model layers).<br>
✅ Create appropriate REST endpoints for all CRUD operations (Use CrudRepository or JpaRepository).<br>
✅ Implement proper validation and error handling (You need to implement at least three custom exceptions).<br>
✅ Each class should implement the necessary CRUD operations relevant to its role.<br>
✅ Each class should only perform operations directly related to its attributes and role in the system.<br>
✅ In addition to the logic described below, you must implement various sorting and filtering operations of your
choice. For example, identifying the top three teams in 2024 or the player with the highest goal count in a team.

Refactor the code you already have based on the feedback you received and integrate it into a Spring project.

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

⚽ Match (Updates the records of other classes when a match is added.)

- Attributes: Team team1, Team team2, int team1score, int team2Score, Map<Player, Integer> goalScorers, LocalDateTime matchDate
- Methods: Display the summary of matches, update team stats, update coach stats, update player stats

🏁  Championship (You need to implement logic that displays the match map before the championship begins. Each time you input the result of a match, the application should automatically promote the winning team to the next round. For example, if there's a 1/8 final match between Team X and Team Y, and Team X wins, it should automatically appear in the 1/4 finals bracket. You must include data for at least two different championships, displaying the tournament tree with both completed and upcoming matches.)

- Attributes: Championship name, List<Team> teams, List<match> matches, LocalDateTime matchDate
- Methods: Display the summary of matches in each round and different statistical data.

🛢️ Use PostgreSQL or MySQL as a database

🔎 Add tests for the service layer logic, and write API tests for the controllers using the [Java 11 HTTP Client](https://hyperskill.org/learn/step/14268).

🔐 Add basic security features described in the following [Spring Security](https://hyperskill.org/learn/step/31611) topic. You will also need to use a login form.

## Workflow

We suggest applying the following workflow:

- **Define roles and responsibilities**, and assign tasks (e.g., one student works on UI, another on data handling, etc.).
- **Break down the project into tasks** and describe each task (or a small group of related tasks) in a new GitHub issue. Use **To Do**, **In Progress**, and **Done** labels to track progress.
- **Create a new pull request (PR) for each issue** (or a related group of issues), but keep them manageable. Each PR name should start with `#issue-number PR Description`. For example: `#1 Generate Data for players`.
- **Use a new branch for each PR**, then merge it into the `main` branch when it is approved by the team. Follow this branch naming convention: `issue-number-task-description`. For example: `issue-1-generaate-data`
- **Limit each PR to maximum 50 lines of code**, and keep them as short as possible. Avoid modifying the same class if another team member has an open PR affecting it, as this can lead to merge conflicts. 
