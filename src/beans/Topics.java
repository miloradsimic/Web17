package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

import controller.Utils;
import model.enums.Role;
import model.enums.TopicType;

public class Topics implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Topic> topicsList = new ArrayList<>();
	private HashMap<Long, Topic> topicsMap = new HashMap<>();
	
	public Topics() {
		super();
	}

	public ArrayList<Topic> getTopicsList() {
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
