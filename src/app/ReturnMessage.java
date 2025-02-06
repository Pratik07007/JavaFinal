package app;

public class ReturnMessage {
	boolean success;
	String msg;
	Compitetor user;
	
	
	public ReturnMessage(boolean success,String msg,Compitetor user) {
		this.success=success;
		this.msg=msg;
		this.user= user;
	}
	
	

}