package app;

public class Compitetor {
	int id;
	int scoreCount;
    String name;
    String email;
    String role;
    String level;

    public Compitetor(int id, String name, String email,String role,String level) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.level = level;
        
        
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
    

   
}
