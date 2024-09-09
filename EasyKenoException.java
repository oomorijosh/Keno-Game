public class EasyKenoException extends Exception {
	private String message = "OOPSIE!!";
	
	public EasyKenoException() {
	}

	public void setMessage(String newMessage) {
		this.message = newMessage;
	}
	
	public String getMessage() {
		return this.message;
	}
}