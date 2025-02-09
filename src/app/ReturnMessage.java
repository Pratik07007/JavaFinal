package app;

/**
 * Represents a return message containing success status, a message, and a user.
 */
public class ReturnMessage {
    boolean success;  // Indicates whether the operation was successful
    String msg;       // Message describing the result
    Compitetor user;  // The user associated with the message

    /**
     * Constructs a ReturnMessage object.
     * 
     * @param success Whether the operation was successful
     * @param msg The message describing the result
     * @param user The user associated with the message
     */
    public ReturnMessage(boolean success, String msg, Compitetor user) {
        this.success = success;
        this.msg = msg;
        this.user = user;
    }
}
