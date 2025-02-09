package app;

/**
 * The Compitetor class represents a competitor participating in the quiz system.
 * It contains personal information, scores, and provides methods to manage the competitor's data.
 */
public class Compitetor {
    private int id;
    private int scoreCount;
    private String name;
    private String email;
    private String role;
    private String level;
    private int[] scores;

    /**
     * Constructor for creating a new competitor.
     * 
     * @param id The unique identifier for the competitor.
     * @param name The name of the competitor.
     * @param email The email address of the competitor.
     * @param role The role of the competitor (e.g., admin, user).
     * @param level The difficulty level of the competitor (e.g., Beginner, Intermediate, Advanced).
     * @param scores The scores of the competitor across multiple questions.
     */
    public Compitetor(int id, String name, String email, String role, String level, int[] scores) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.level = level;
        this.scores = scores;
    }

    /**
     * Returns the competitor's level.
     * 
     * @return The level of the competitor.
     */
    public String getLevel() {
        return level;
    }

    /**
     * Returns the unique identifier for the competitor.
     * 
     * @return The competitor's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the competitor.
     * 
     * @return The competitor's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the email address of the competitor.
     * 
     * @return The competitor's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the count of scores for the competitor.
     * 
     * @return The score count.
     */
    public int getScoreCount() {
        return scoreCount;
    }

    /**
     * Increments the score count by one.
     */
    public void incrementScoreCount() {
        this.scoreCount++;
    }

    /**
     * Returns the array of scores for the competitor.
     * 
     * @return An array containing the competitor's scores.
     */
    public int[] getScores() {
        return scores;
    }

    /**
     * Returns the role of the competitor (e.g., admin, user).
     * 
     * @return The competitor's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns a string representation of the competitor's full details, 
     * including ID, name, level, and overall score.
     * 
     * @return A string containing the full details of the competitor.
     */
    public String getFullDetails() {
        return "Competitor ID: " + id + "\n" +
               "Name: " + name + "\n" +
               "Level: " + level.toUpperCase() + "\n" +               
               "Overall Score: " + getOverallScore();
    }

    /**
     * Calculates and returns the overall score for the competitor.
     * The overall score is the sum of all individual scores.
     * 
     * @return The overall score.
     */
    public double getOverallScore() {
        if (scores == null || scores.length == 0) {
            return 0;
        }

        double sum = 0;
        for (int score : scores) {
            sum += score;
        }

        return sum;
    }

    /**
     * Sets the scores of the competitor.
     * 
     * @param scores The array of scores to set for the competitor.
     */
    public void setScores(int[] scores) {
        this.scores = scores;
    }
}
