package beans;

import java.io.Serializable;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;

import model.enums.TopicType;

public class Topic implements Serializable{

	private long topicId;
	private TopicType type;
	private Subforum subforum;
	/**
	 * Unique
	 */
	private String title;
	private User author;
	//napravi novi rest call za dobijanje komentara
	private ArrayList<Comment> comments;
	private String content;
	private String creationDate;
	private int likes;
	private int dislikes;

	public Topic(long id) {
		super();
		this.topicId = id;
		this.type = null;
		this.subforum = null;
		this.title = null;
		this.author = null;
		this.content = null;
		this.creationDate = null;
		this.likes = -1;
		this.dislikes = -1;
		comments = new ArrayList<>();
	}
	
	public Topic(long id, TopicType type, Subforum subforum, String title, User author, String content, String creationDate,
			int likes, int dislikes) {
		super();
		this.topicId = id;
		this.type = type;
		this.subforum = subforum;
		this.title = title;
		this.author = author;
		this.content = content;
		this.creationDate = creationDate;
		this.likes = likes;
		this.dislikes = dislikes;
		this.comments = new ArrayList<>();
	}

	public TopicType getType() {
		return type;
	}

	public void setType(TopicType type) {
		this.type = type;
	}

	public Subforum getSubforum() {
		return subforum;
	}

	public void setSubforum(Subforum subforum) {
		this.subforum = subforum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

}
