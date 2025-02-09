package app;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The UserPannelUI class represents the main user dashboard.
 * It provides options for playing the quiz, viewing high scores, viewing player details, and logging out.
 */
public class UserPannelUI {
    /**
     * Constructs the UserPannelUI dashboard.
     * @param user The competitor user for whom the dashboard is created.
     */
    public UserPannelUI(Compitetor user) {
        Map<String, Runnable> userActions = new LinkedHashMap<>();
        userActions.put("Play Quiz", () -> new PlayQuiz(user));
        userActions.put("View High Scores", () -> new LeaderBoardUI(user));
        userActions.put("View Player Details", () -> new PlayerDetailsUI(user));
        userActions.put("Logout", () -> System.exit(0));

        new DashBoardUI("Welcome " + user.getName(), userActions);
    }
}
