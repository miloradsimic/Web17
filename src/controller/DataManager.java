package controller;

import java.util.ArrayList;

import beans.Comment;
import beans.Message;
import beans.Subforum;
import beans.Topic;
import beans.User;

public class DataManager {
	
	private static DataManager instance = null;
	
	private String stringUsers = "users.data";
	private String stringComments = "comments.data";
	private String stringTopics = "topics.data";
	private String stringSubforums = "subforums.data";
	private String stringMessages = "messages.data";
	private String rootPath = "";
	
	private ArrayList<User> users;
	private ArrayList<Comment> comments;
	private ArrayList<Topic> tpics;
	private ArrayList<Subforum> subforums;
	private ArrayList<Message> messages;
	
	public static DataManager getInstance() {
		if(instance == null){
			instance = new DataManager();
		}
		return instance;
	}
	
	private DataManager() {
	}
	
	public void setUpRootPath(String path){
		this.rootPath = path;
	}
	
	
	
	

	
	

}
