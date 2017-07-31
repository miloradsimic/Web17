package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CommentRatings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<CommentRating> commentList;
	private HashMap<CommentRatings.Key, CommentRating> commentsMap;
	// private HashMap<Long, HashMap<Long, CommentRating>> commentsMap;

	public CommentRatings() {
		super();
		this.commentList = new ArrayList<>();
		this.commentsMap = new HashMap<>();
	}

	public ArrayList<CommentRating> getCommentList() {
		return commentList;
	}

	public HashMap<CommentRatings.Key, CommentRating> getCommentsMap() {
		return commentsMap;
	}

	public void setCommentsMap(HashMap<CommentRatings.Key, CommentRating> commentsMap) {
		this.commentsMap = commentsMap;
	}

	public void setCommentList(ArrayList<CommentRating> commentList) {
		this.commentList = commentList;
	}

	public void add(CommentRating entry) {
		// if(commentsMap.get(entry.getCommentId()).get(entry.getUserId()) ==
		// null)
		Key key = new Key(entry.getCommentId(), entry.getUserId());
		commentList.add(entry);
		commentsMap.put(key, entry);
	}

	public CommentRating getRating(long commentId, long userId) {
		return commentsMap.get(new Key(commentId, userId));
	}

	public Boolean containsRating(long commentId, long userId) {
		return getRating(commentId, userId) == null ? false : true;
	}

	public void remove(long commentId, long userId) {
		commentsMap.remove(new Key(commentId, userId));
		for (CommentRating commentRating : commentList) {
			if (commentRating.getCommentId() == commentId && commentRating.getUserId() == userId) {
				commentList.remove(commentRating);
				break;
			}
		}

	}

	class Key implements Serializable{
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
			return ((Key)obj).commentId==commentId&&((Key)obj).userId==userId;
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
