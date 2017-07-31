package beans;

import java.io.Serializable;

public class CommentRating implements Serializable  {
	private long commentId;
	private long userId;
	private int value;
	
	public CommentRating(long commentId, long userId, int value) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.value = value;
	}
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
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
	
	public Boolean isRated() {
		return value==0?false:true;
	}

}
