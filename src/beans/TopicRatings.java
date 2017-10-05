package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TopicRatings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<TopicRating> ratingsList;
	private HashMap<TopicRatings.Key, TopicRating> ratingsMap;

	public TopicRatings() {
		super();
		this.ratingsList = new ArrayList<>();
		this.ratingsMap = new HashMap<>();
	}


	public ArrayList<TopicRating> getRatingsList() {
		return ratingsList;
	}


	public void setRatingsList(ArrayList<TopicRating> ratingsList) {
		this.ratingsList = ratingsList;
	}


	public HashMap<TopicRatings.Key, TopicRating> getRatingsMap() {
		return ratingsMap;
	}


	public void setRatingsMap(HashMap<TopicRatings.Key, TopicRating> ratingsMap) {
		this.ratingsMap = ratingsMap;
	}


	public void add(TopicRating entry) {
		Key key = new Key(entry.getTopicId(), entry.getUserId());
		ratingsList.add(entry);
		ratingsMap.put(key, entry);
	}

	public TopicRating getRating(long topicId, long userId) {
		return ratingsMap.get(new Key(topicId, userId));
	}

	public Boolean containsRating(long topicId, long userId) {
		return getRating(topicId, userId) == null ? false : true;
	}

	public void remove(long topicId, long userId) {
		ratingsMap.remove(new Key(topicId, userId));
		for (TopicRating topicRating : ratingsList) {
			if (topicRating.getTopicId() == topicId && topicRating.getUserId() == userId) {
				ratingsList.remove(topicRating);
				break;
			}
		}

	}

	class Key implements Serializable {
		private long topicId;
		private long userId;

		public Key(long topicId, long userId) {
			this.topicId = topicId;
			this.userId = userId;
		}

		@Override
		public int hashCode() {
			String hash = topicId + ";" + userId;
			return hash.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return ((Key) obj).topicId == topicId && ((Key) obj).userId == userId;
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


	}
}