package app;

public class ReturnMessage {
	boolean success;
	String msg;
	Users user;
	
	
	public ReturnMessage(boolean success,String msg,Users user) {
		this.success=success;
		this.msg=msg;
		this.user= user;
	}
	
	

}