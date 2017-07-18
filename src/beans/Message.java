package beans;

public class Message {

	private User sender;
	private User receiver;
	private String text;
	private boolean isRead;
	
	public Message() {
		super();
	}
	public Message(User sender, User receiver, String text, boolean isRead) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.isRead = isRead;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	
	
}
