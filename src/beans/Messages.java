package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import model.enums.MessageType;

public class Messages implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<Message> messagesList = new ArrayList<>();
	private HashMap<Long, Message> messagesMap = new HashMap<>();

	public Messages() {
		super();
	}

	public ArrayList<Message> getMessagesList() {
		return messagesList;
	}

	public void setMessagesList(ArrayList<Message> messagesList) {
		this.messagesList = messagesList;
	}

	public HashMap<Long, Message> getMessagesMap() {
		return messagesMap;
	}

	public void setMessagesMap(HashMap<Long, Message> messagesMap) {
		this.messagesMap = messagesMap;
	}

	public void addMessage(Message entry) {
		entry.setType(MessageType.RECEIVED);
		messagesMap.put(entry.getMessageId(), entry);
		messagesList.add(entry);
		Message another = new Message(entry.getSender(), entry.getReceiver(), entry.getText());
		another.setType(MessageType.SENT);
		messagesMap.put(another.getMessageId(), another);
		messagesList.add(another);
		
		
	}

	public void remove(long messageId) {
		messagesList.remove(messagesMap.get(messageId));
		messagesMap.remove(messageId);
	}

	public void updateMessage(Message entry) {
		messagesList.remove(messagesMap.get(entry.getMessageId()));
		messagesMap.remove(entry.getMessageId());
		
		messagesMap.put(entry.getMessageId(), entry);
		messagesList.add(entry);
	}
}
