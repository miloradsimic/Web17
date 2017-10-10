package beans;

import java.io.Serializable;

public class CommentEditBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String text;
	private long commentId;
	private long topicId;

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

}