package beans;

import java.io.Serializable;

public class TopicRating  implements Serializable  {
	private static final long serialVersionUID = 1L;

	private long topicId;
	private long userId;
	private int value;
	
	public TopicRating(long topicId, long userId, int value) {
		super();
		this.topicId = topicId;
		this.userId = userId;
		this.value = value;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	
}
