package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CommentRatings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<CommentRating> ratingsList;
	private HashMap<CommentRatings.Key, CommentRating> ratingsMap;
	// private HashMap<Long, HashMap<Long, CommentRating>> commentsMap;

	public CommentRatings() {
		super();
		this.ratingsList = new ArrayList<>();
		this.ratingsMap = new HashMap<>();
	}

	public ArrayList<CommentRating> getRatingsList() {
		return ratingsList;
	}

	public void setRatingsList(ArrayList<CommentRating> ratingsList) {
		this.ratingsList = ratingsList;
	}

	public HashMap<CommentRatings.Key, CommentRating> getRatingsMap() {
		return ratingsMap;
	}

	public void setRatingsMap(HashMap<CommentRatings.Key, CommentRating> ratingsMap) {
		this.ratingsMap = ratingsMap;
	}

	public void add(CommentRating entry) {
		// if(commentsMap.get(entry.getCommentId()).get(entry.getUserId()) ==
		// null)
		Key key = new Key(entry.getCommentId(), entry.getUserId());
		ratingsList.add(entry);
		ratingsMap.put(key, entry);
	}

	public CommentRating getRating(long commentId, long userId) {
		return ratingsMap.get(new Key(commentId, userId));
	}

	public Boolean containsRating(long commentId, long userId) {
		return getRating(commentId, userId) == null ? false : true;
	}

	public void remove(long commentId, long userId) {
		ratingsMap.remove(new Key(commentId, userId));
		for (CommentRating commentRating : ratingsList) {
			if (commentRating.getCommentId() == commentId && commentRating.getUserId() == userId) {
				ratingsList.remove(commentRating);
				break;
			}
		}

	}

	class Key implements Serializable {
		private long commentId;
		private long userId;

		public Key(long commentId, long userId) {
			this.commentId = commentId;
			this.userId = userId;
		}

		@Override
		public int hashCode() {
			String hash = commentId + ";" + userId;
			return hash.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return ((Key) obj).commentId == commentId && ((Key) obj).userId == userId;
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

	}

}
