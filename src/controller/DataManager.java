package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import beans.Comment;
import beans.CommentRating;
import beans.CommentRatings;
import beans.Comments;
import beans.Message;
import beans.Subforum;
import beans.Topic;
import beans.User;
import beans.Users;

public class DataManager {

	private static DataManager instance = null;

	private static String stringUsers = "/data/users.txt";
	private static String stringComments = "/data/comments.txt";
	private static String stringRatings = "/data/ratings";
//	private String stringTopics = "/data/topics.txt";
//	private String stringSubforums = "/data/subforums.txt";
//	private String stringMessages = "/data/messages.txt";
	private static String rootPath = "";

	
	private Users users = new Users();
	private Comments comments = new Comments();
	private CommentRatings commentRatings = new CommentRatings();
//	private ArrayList<Topic> tpics;
//	private ArrayList<Subforum> subforums;
//	private ArrayList<Message> messages;

	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager();
		}
		return instance;
	}

	private DataManager() {
	}

	public static void setUpRootPath(String path) {
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
	
	public Boolean saveComments(Comments comments) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringComments);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(comments);

			System.out.println("Write Comments Done");

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

	public CommentRatings saveRating(CommentRating entry) {
		CommentRating old = commentRatings.getRating(entry.getCommentId(), entry.getUserId());
		
		// If it's already rated before.
		if (old != null) {
			System.out.println("If it's already rated before.");
			// If it's already liked
			if (old.getValue() == 1) {
				System.out.println("If it's already liked");
				// Remove old like
				comments.getComment(entry.getCommentId()).removeLike();
				commentRatings.remove(entry.getCommentId(), entry.getUserId());
				// If it's now disliked
				if (entry.getValue() == -1) {
					System.out.println("If it's now disliked");
					comments.getComment(entry.getCommentId()).dislike();
					commentRatings.add(entry);
				}
				// If it's already disliked
			} else {
				System.out.println();
				// Remove old dislike
				comments.getComment(entry.getCommentId()).removeDislike();
				commentRatings.remove(entry.getCommentId(), entry.getUserId());
				// If it's now liked
				if (entry.getValue() == 1) {
					System.out.println("If it's now liked");
					comments.getComment(entry.getCommentId()).like();
					commentRatings.add(entry);
				}
			}
			// If it's not rated before
		} else {
			System.out.println("If it's not rated before");

			// If it's now liked
			if (entry.getValue() == 1) {
				System.out.println("If it's now liked");
				comments.getComment(entry.getCommentId()).like();
				commentRatings.add(entry);
				// If it's now disliked
			} else {
				System.out.println("If it's now disliked");
				comments.getComment(entry.getCommentId()).dislike();
				commentRatings.add(entry);
			}
		}

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		saveComments(comments);
		
		try {

			fout = new FileOutputStream(rootPath + stringRatings);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(commentRatings);

			System.out.println("Write Ratings Done");

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

		return commentRatings;
	}

	public CommentRatings readRatings() {

		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {

			fin = new FileInputStream(rootPath + stringRatings);
			ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			commentRatings = (CommentRatings) o;
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

		return commentRatings;
	}

	public boolean updateProfile(User edited) {
		if(users == null) {
			readUsers();
		}
		users.updateUser(edited);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringUsers);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(users);

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

	public Users readUsers() {
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {

			fin = new FileInputStream(rootPath + stringUsers);
			ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			users = (Users) o;
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

		return users;
	}

	public Boolean saveUser(User entry) {
//		comments.addComment(entry);
		users.addUser(entry);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringUsers);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(users);

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

}
