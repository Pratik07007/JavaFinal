package app;

public class Compitetor {
    private int id;
    private int scoreCount;
    private String name;
    private String email;
    private String role;
    private String level;
    private int[] scores;

    public Compitetor(int id, String name, String email, String role, String level, int[] scores) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.level = level;
        this.scores = scores;
    }

    public String getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void incrementScoreCount() {
        this.scoreCount++;
    }

    public int[] getScores() {
        return scores;
    }

    public String getRole() {
        return role;
    }
    
    public String getFullDetails() {
        
        return "Competitor ID: " + id + "\n" +
               "Name: " + name + "\n" +
               "Level: " + level.toUpperCase() + "\n" +               
               "Overall Score: " + getOverallScore();
    }

 


    public double getOverallScore() {
        if (scores == null || scores.length == 0) {
            return 0;
        }

        double weight = 1.0;
        switch (level.toUpperCase()) {
            case "BEGINNER":
                weight = 1.0;
                break;
            case "INTERMEDIATE":
                weight = 1.3;
                break;
            case "ADVANCED":
                weight = 1.5;
                break;
        }

        double sum = 0;
        for (int score : scores) {
            sum += score;
        }
        double average = sum;
        
        if(average * weight>=25) {
        	return 25.00;
        }

        return Math.round(average * weight);
    }
}
