package beans;

import java.io.Serializable;
import java.util.ArrayList;

import model.enums.TopicType;

public class TopicBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long topicId;
	private long subforumId;
	/**
	 * Unique
	 */
	private String title;
	private TopicType type;
	private User author;
	//napravi novi rest call za dobijanje komentara
	private ArrayList<Comment> comments;
	private String content;
	private String creationDate;
	private int likes;
	private int dislikes;
	
	public TopicBean(long topicId, long subforumId, String title, TopicType type, User author,
			ArrayList<Comment> comments, String content, String creationDate, int likes, int dislikes) {
		super();
		this.topicId = topicId;
		this.subforumId = subforumId;
		this.title = title;
		this.type = type;
		this.author = author;
		this.comments = comments;
		this.content = content;
		this.creationDate = creationDate;
		this.likes = likes;
		this.dislikes = dislikes;
	}
	public TopicBean(Topic topic, User author) {
		super();
		this.topicId = topic.getTopicId();
		this.subforumId = topic.getSubforumId();
		this.title = topic.getTitle();
		this.type = topic.getType();
		this.author = author;
		this.comments = topic.getComments();
		this.content = topic.getContent();
		this.creationDate = topic.getCreationDate();
		this.likes = topic.getLikes();
		this.dislikes = topic.getDislikes();
	}
	

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public long getSubforumId() {
		return subforumId;
	}

	public void setSubforumId(long subforumId) {
		this.subforumId = subforumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TopicType getType() {
		return type;
	}

	public void setType(TopicType type) {
		this.type = type;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
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
	
	
}
