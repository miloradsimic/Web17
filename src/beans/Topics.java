package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Topics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Topic> topicsList = new ArrayList<>();
	private HashMap<Long, Topic> topicsMap = new HashMap<>();

	public Topics() {
		super();
	}

	public ArrayList<Topic> getTopicList() {
		return topicsList;
	}

	public void setTopicsList(ArrayList<Topic> topicsList) {
		this.topicsList = topicsList;
	}

	public HashMap<Long, Topic> getTopicsMap() {
		return topicsMap;
	}

	public void setTopicsMap(HashMap<Long, Topic> topicsMap) {
		this.topicsMap = topicsMap;
	}

	public void addTopic(Topic entry) {

		topicsList.add(entry);
		topicsMap.put(entry.getTopicId(), entry);
	}

	public boolean updateTopic(Topic topic) {

		topicsList.remove(topic);
		topicsMap.remove(topic.getTopicId());
		topicsList.add(topic);
		topicsMap.put(topic.getTopicId(), topic);
		return true;
	}

}
