package app;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminPanelUI {
    public AdminPanelUI(Compitetor admin) {
        Map<String, Runnable> adminActions = new LinkedHashMap<>();
        adminActions.put("Add Question", () -> new AddQuestionPage(admin));
        adminActions.put("Update or delete Questions", () -> new ManageQuestionPages(admin));
        adminActions.put("View Report", () -> new ViewStats(admin));
        adminActions.put("Logout", () -> System.exit(0));
        
        new DashBoardUI("Welcome admin, "+admin.getName(), adminActions);
    }
}
