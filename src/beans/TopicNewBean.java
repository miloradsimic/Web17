package beans;

import java.io.Serializable;

public class TopicNewBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long subforum_id;
	private String topic_title;
	private String topic_type;
	private String content;

	public long getSubforum_id() {
		return subforum_id;
	}

	public void setSubforum_id(long subforum_id) {
		this.subforum_id = subforum_id;
	}

	public String getTopic_title() {
		return topic_title;
	}

	public void setTopic_title(String topic_title) {
		this.topic_title = topic_title;
	}

	public String getTopic_type() {
		return topic_type;
	}

	public void setTopic_type(String topic_type) {
		this.topic_type = topic_type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
