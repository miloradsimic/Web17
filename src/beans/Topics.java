package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class Topics {
	
	private ArrayList<Topic> topicsList = new ArrayList<>();
	private HashMap<Long, Topic> topicsMap = new HashMap<>();
	
	public Topics() {
		super();
	}
	
	public Topics(String path) {
		
		BufferedReader in = null;
		try {
			File file = new File(path + "/data/topics.txt");
			in = new BufferedReader(new FileReader(file));
			readTopics(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}

	
	/**
	 * Cita subforume iz datoteke i smesta ih u asocijativnu listu subforuma.
	 * Kljuc je naziv subforuma.
	 */
	private void readTopics(BufferedReader in) {
		String line, id = "", title = "", type = "", subforumId = "", authorId = "", content = "", creationDate = "", likes = "", dislikes = "";
		StringTokenizer st;
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				
				id = st.nextToken().trim();
				type = st.nextToken().trim();
				subforumId = st.nextToken().trim();
				title = st.nextToken().trim();
				authorId = st.nextToken().trim();
				content = st.nextToken().trim();
				creationDate = st.nextToken().trim();
				likes = st.nextToken().trim();
				dislikes = st.nextToken().trim();
				
				
				int likesInt=-1, dislikesInt=-1;
				long idNumber=-1, subforumIdNumber = -1, authorIdNumber = -1;
				
				try {
					idNumber = Long.parseLong(id);
					subforumIdNumber = Long.parseLong(subforumId);
					authorIdNumber = Long.parseLong(authorId);
					likesInt = Integer.parseInt(likes);
					dislikesInt = Integer.parseInt(dislikes);
				} catch (NumberFormatException e) {
					System.out.println("Can't parse likes and dislikes to integer.");
					e.printStackTrace();
				}

				
				Topic entry = new Topic(idNumber, Utils.stringToTopicType(type),
						new Subforum(subforumIdNumber),
						title,
						new User(authorIdNumber),
						content,
						creationDate,
						likesInt,
						dislikesInt);

				topicsList.add(entry);
				topicsMap.put(idNumber, entry);
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
	
	

}
