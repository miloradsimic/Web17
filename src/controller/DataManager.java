package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import beans.Comment;
import beans.CommentRating;
import beans.CommentRatings;
import beans.Comments;
import beans.Topic;
import beans.TopicRating;
import beans.TopicRatings;
import beans.Topics;
import beans.User;
import beans.Users;

public class DataManager {

	private static DataManager instance = null;

	private static String stringUsers = "/data/users.txt";
	private static String stringComments = "/data/comments.txt";
	private static String stringCommentRatings = "/data/comment_ratings";
	private static String stringTopics = "/data/topics.txt";
	private static String stringTopicRatings = "/data/topic_ratings";
	// private String stringSubforums = "/data/subforums.txt";
	// private String stringMessages = "/data/messages.txt";
	private static String rootPath = "";

	private Users users = new Users();
	private Comments comments = new Comments();
	private Topics topics = new Topics();
	private CommentRatings commentRatings = new CommentRatings();
	private TopicRatings topicRatings = new TopicRatings();
	// private ArrayList<Topic> tpics;
	// private ArrayList<Subforum> subforums;
	// private ArrayList<Message> messages;

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

	public Boolean saveAvatar(File img, String name, String username) {

		User user = users.getUsersMapByUsername().get(username);

		String localPath = "resources/" + user.getUserId() + name;
		try {
			// retrieve image
			BufferedImage bi = ImageIO.read(img);
			File outputfile = new File(rootPath + '/' + localPath);
			ImageIO.write(bi, name.substring(name.lastIndexOf('.') + 1), outputfile);

		} catch (IOException e) {
			e.printStackTrace();
		}

		user.setAvatar(localPath);

		updateUser(user);

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

	public CommentRatings saveCommentRating(CommentRating entry) {
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

			fout = new FileOutputStream(rootPath + stringCommentRatings);
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

	public CommentRatings readCommentRatings() {

		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {

			fin = new FileInputStream(rootPath + stringCommentRatings);
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
		if (users == null) {
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

	public Boolean updateUser(User entry) {
		users.updateUser(entry);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringUsers);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(users);

			System.out.println("Write update user Done");

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

	public Topics readTopics() {
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {

			fin = new FileInputStream(rootPath + stringTopics);
			ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			topics = (Topics) o;
			System.out.println("Read Topics Done");

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

		return topics;
	}

	public Boolean saveTopic(Topic entry) {
		topics.addTopic(entry);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringTopics);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(topics);

			System.out.println("Write Topics Done");

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

	public Boolean saveTopics(Topics topics) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringTopics);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(topics);

			System.out.println("Write Topics Done");

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

	public Boolean updateTopic(Topic entry) {
		topics.updateTopic(entry);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(rootPath + stringTopics);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(topics);

			System.out.println("Update Topic Done");

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

	public TopicRatings saveTopicRating(TopicRating entry) {
		TopicRating old = topicRatings.getRating(entry.getTopicId(), entry.getUserId());

		// If it's already rated before.
		if (old != null) {
			System.out.println("If it's already rated before.");
			// If it's already liked
			if (old.getValue() == 1) {
				System.out.println("If it's already liked");
				// Remove old like
				topics.getTopicsMap().get(entry.getTopicId()).removeLike();
				topicRatings.remove(entry.getTopicId(), entry.getUserId());
				// If it's now disliked
				if (entry.getValue() == -1) {
					System.out.println("If it's now disliked");
					topics.getTopicsMap().get(entry.getTopicId()).dislike();
					topicRatings.add(entry);
				}
				// If it's already disliked
			} else {
				System.out.println();
				// Remove old dislike
				topics.getTopicsMap().get(entry.getTopicId()).removeDislike();
				topicRatings.remove(entry.getTopicId(), entry.getUserId());
				// If it's now liked
				if (entry.getValue() == 1) {
					System.out.println("If it's now liked");
					topics.getTopicsMap().get(entry.getTopicId()).like();
					topicRatings.add(entry);
				}
			}
			// If it's not rated before
		} else {
			System.out.println("If it's not rated before");

			// If it's now liked
			if (entry.getValue() == 1) {
				System.out.println("If it's now liked");
				topics.getTopicsMap().get(entry.getTopicId()).like();
				topicRatings.add(entry);
				// If it's now disliked
			} else {
				System.out.println("If it's now disliked");
				topics.getTopicsMap().get(entry.getTopicId()).dislike();
				topicRatings.add(entry);
			}
		}

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		topics.getTopicList().clear();
		topics.getTopicList().addAll(topics.getTopicsMap().values());

		saveTopics(topics);

		try {

			fout = new FileOutputStream(rootPath + stringTopicRatings);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(topicRatings);

			System.out.println("Write Topic Ratings Done");

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

		return topicRatings;
	}

	public TopicRatings readTopicRatings() {

		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {

			fin = new FileInputStream(rootPath + stringTopicRatings);
			ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			topicRatings = (TopicRatings) o;
			System.out.println("Read Topic Ratings Done");

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

		return topicRatings;
	}

}
