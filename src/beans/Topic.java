package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;

import controller.Utils;
import model.enums.TopicType;

public class Topic implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final AtomicLong count = new AtomicLong(100);

	private long topicId;
	private long subforumId;
	/**
	 * Unique
	 */
	private String title;
	private TopicType type;
	private long authorId;
	// napravi novi rest call za dobijanje komentara
	private ArrayList<Comment> comments;
	private String content;
	private String creationDate;
	private boolean deleted = false;
	private int likes;
	private int dislikes;

	public Topic(TopicType type, long subforumId, String title, long authorId, String content) {

		this.topicId = count.incrementAndGet();
		this.type = type;
		this.subforumId = subforumId;
		this.title = title;
		this.authorId = authorId;
		this.content = content;
		this.creationDate = Utils.getCurrentDate();
		this.likes = 0;
		this.dislikes = 0;
		this.comments = new ArrayList<>();
	}

	public Topic(long id, TopicType type, long subforumId, String title, long authorId, String content,
			String creationDate, int likes, int dislikes) {
		super();
		this.topicId = id;
		this.type = type;
		this.subforumId = subforumId;
		this.title = title;
		this.authorId = authorId;
		this.content = content;
		this.creationDate = creationDate;
		this.likes = likes;
		this.dislikes = dislikes;
		this.comments = new ArrayList<>();
	}

	public long getSubforumId() {
		return subforumId;
	}

	public void setSubforumId(long subforumId) {
		this.subforumId = subforumId;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long author) {
		this.authorId = author;
	}

	public TopicType getType() {
		return type;
	}

	public void setType(TopicType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void like() {
		likes++;
	}

	public void removeLike() {
		likes--;
	}

	public void dislike() {
		dislikes++;
	}

	public void removeDislike() {
		dislikes--;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
