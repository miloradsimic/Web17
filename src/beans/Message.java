package beans;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import model.enums.MessageType;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final AtomicLong count = new AtomicLong(100);

	private long messageId;
	private User sender;
	private User receiver;
	private String text;
	private boolean isRead;
	private MessageType type;

	public Message() {
		super();
	}

	public Message(long messageId, User sender, User receiver, String text, boolean isRead) {
		super();
		this.messageId = messageId;
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.isRead = isRead;
	}

	public Message(User sender, User receiver, String text) {
		super();
		this.messageId = count.incrementAndGet();
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.isRead = false;
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

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

}
