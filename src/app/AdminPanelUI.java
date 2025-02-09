package app;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * AdminPanelUI represents the user interface for the admin panel. It provides options for the admin 
 * to perform actions such as adding, updating, or deleting questions, viewing reports, or logging out.
 */
public class AdminPanelUI {

    /**
     * Constructor for the AdminPanelUI.
     * Initializes the admin actions and sets up the dashboard UI.
     * 
     * @param admin The current administrator object, used to customize the UI with the admin's name.
     */
    public AdminPanelUI(Compitetor admin) {
        // Map to hold the admin actions and their corresponding UI pages
        Map<String, Runnable> adminActions = new LinkedHashMap<>();

        // Action to add a new question
        adminActions.put("Add Question", () -> new AddQuestionPage(admin));

        // Action to update or delete existing questions
        adminActions.put("Update or delete Questions", () -> new ManageQuestionPages(admin));

        // Action to view reports/stats
        adminActions.put("View Report", () -> new ViewStats(admin));

        // Action to logout (exit the application)
        adminActions.put("Logout", () -> System.exit(0));

        // Create the Dashboard UI with a welcome message and the available admin actions
        new DashBoardUI("Welcome admin, " + admin.getName(), adminActions);
    }
}
