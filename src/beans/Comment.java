package beans;

import java.io.Serializable;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

public class Comment implements Serializable{

	private long commmentId;
	private Topic topic;
	private User author;
	private String commentDate;
	private long parentCommentId;
	private ArrayList<Comment> childComments;
	private String text;
	private int likes;
	private int dislikes;
	private boolean edited;
	
	public Comment(long commentId, Topic topic) {
		super();
		this.commmentId = commentId;
		this.topic = topic;
		this.author = null;
		this.commentDate = null;
		this.parentCommentId = -1;
		this.text = null;
		this.likes = -1;
		this.dislikes = -1;
		this.edited = false;
		this.childComments = new ArrayList<>();
	}
	
	public Comment(long commentId, Topic topic, User author, String commentDate, long parentCommentId, String text, int likes,
			int dislikes, boolean edited) {
		super();
		this.commmentId = commentId;
		this.topic = topic;
		this.author = author;
		this.commentDate = commentDate;
		this.parentCommentId = parentCommentId;
		this.text = text;
		this.likes = likes;
		this.dislikes = dislikes;
		this.edited = edited;
		this.childComments = new ArrayList<>();
	}
	
	
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	public long getParentComment() {
		return parentCommentId;
	}
	public void setParentComment(long parentComment) {
		this.parentCommentId = parentComment;
	}
	public ArrayList<Comment> getChildComments() {
		return childComments;
	}
	public void setChildComments(ArrayList<Comment> childComments) {
		this.childComments = childComments;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public boolean isEdited() {
		return edited;
	}
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	public long getCommmentId() {
		return commmentId;
	}
	public void setCommmentId(long commmentId) {
		this.commmentId = commmentId;
	}

	public boolean hasParent(){
		return parentCommentId != -1;
	}

	@Override
	public String toString() {
		return "Comement ID: " + commmentId + " Text: " + text;
	}
	
}
