package beans;

import java.io.Serializable;

public class MessageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String receivers_username;
	private String content;
	
	
	public String getReceivers_username() {
		return receivers_username;
	}
	public void setReceivers_username(String receivers_username) {
		this.receivers_username = receivers_username;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
