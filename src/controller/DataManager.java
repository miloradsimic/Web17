package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import beans.Comment;
import beans.Comments;
import beans.Message;
import beans.Subforum;
import beans.Topic;
import beans.User;

public class DataManager {
	
	private static DataManager instance = null;
	
	private String stringUsers = "/data/users.txt";
	private static String stringComments = "/data/comments.txt";
	private String stringTopics = "/data/topics.txt";
	private String stringSubforums = "/data/subforums.txt";
	private String stringMessages = "/data/messages.txt";
	private static String rootPath = "";
	
	private ArrayList<User> users;
	private Comments comments = new Comments();
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
	
	public static void setUpRootPath(String path){
		rootPath = path;
	}
	
	public Boolean saveComment(Comment entry) {
		comments.addComment(entry);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringComments);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(comments);

			System.out.println("Write Done");

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		return true;
	}
	
//	5;1;1;	 23.05.2017 22:16:00;1; Ja sam ADMIN i komentarisem drugi put na prvi komentar!;80;2;false
//	4;1;2;	 23.05.2017 22:15:00;0;Ja sam USER i komentarisem drugi put!;40;0;false
//	3;1;1;	 23.05.2017 22:10:00;2;LOL! Ja sam ADMIN i mogu vam obrisati naloge!;20;0;false
//	2;1;3;	 23.05.2017 22:05:00;1;Ja sam MODERATOR i mogu da obrisem mikin komentar;10;0;false
//	1;1;2;	 23.05.2017 22:00:00;0;Ja sam USER i mogu da komentarisem temu kad sam logovan.;5;0;false
	
	public Comments readComments() {
		
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		Comments coms;
		try {

			fin = new FileInputStream(rootPath + stringComments);
			ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			coms = (Comments) o;
			comments = coms;
			System.out.println("Read Done");

		} catch (Exception ex) {

			ex.printStackTrace();
			return null;

		} finally {

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		return comments;
	}
				
	
	
	
	

	
	

}
