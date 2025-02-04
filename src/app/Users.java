package app;

public class Users {
	int id;
    String name;
    String email;
    String role;

    
   

    public Users(int id, String name, String email,String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        
        
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

   
}
