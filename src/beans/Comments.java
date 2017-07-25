package beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;

import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

public class Comments {

	private ArrayList<Comment> commentList;
	private HashMap<Long, Comment> commentsMap;

	private static String pathToFile = "/data/comments.txt";
	private String rootPath;

	public Comments() {
		super();
	}

	public Comments(String path) {
		this.rootPath = path;
		BufferedReader in = null;
		try {
			File file = new File(rootPath + pathToFile);
			in = new BufferedReader(new FileReader(file));
			Users users = new Users(path);
			readComments(in, users);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Read entries from file.
	 * 
	 * @param in
	 * @param topic
	 *            Commented topic
	 */
	private void readComments(BufferedReader in, Users users) {
		String line, id = "", topicId = "", authorId = "", commentsDate = "", parentCommentId = "", text = "",
				likes = "", dislikes = "", edited = "";
		StringTokenizer st;
		LinkedHashMap<Long, Comment> linkedComments = new LinkedHashMap<>();
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");

				id = st.nextToken().trim();
				// System.out.println(id);
				topicId = st.nextToken().trim();
				authorId = st.nextToken().trim();
				commentsDate = st.nextToken().trim();
				parentCommentId = st.nextToken().trim();
				text = st.nextToken().trim();
				// System.out.println(text);
				likes = st.nextToken().trim();
				dislikes = st.nextToken().trim();
				edited = st.nextToken().trim();

				int likesInt = -1, dislikesInt = -1;
				long idNumber = -1, topicIdNumber = -1, parentCommentIdNumber = -1, authorIdNumber = -1;
				boolean editedBoolean = false;
				try {
					idNumber = Long.parseLong(id);
					topicIdNumber = Long.parseLong(topicId);
					parentCommentIdNumber = Long.parseLong(parentCommentId);
					authorIdNumber = Long.parseLong(authorId);
					likesInt = Integer.parseInt(likes);
					dislikesInt = Integer.parseInt(dislikes);
					editedBoolean = Boolean.parseBoolean(edited);
				} catch (NumberFormatException e) {
					System.out.println("Can't parse comments attribute");
					e.printStackTrace();
				}

				Comment entry = new Comment(idNumber, new Topic(topicIdNumber), users.getUsersMap().get(authorIdNumber), commentsDate,
						parentCommentIdNumber == 0 ? -1 : parentCommentIdNumber, text, likesInt, dislikesInt,
						editedBoolean);

				// -----------------------------------------

				linkedComments.put(entry.getCommentId(), entry);
			}

			Iterator it = linkedComments.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Comment entry = (Comment) pair.getValue();
				if (entry.hasParent()) {
					Comment parentComment = linkedComments.get(entry.getParentComment());
					entry.setParentComment(parentComment.getCommentId());
					parentComment.getChildComments().add(entry);
					it.remove(); // avoids a ConcurrentModificationException
				}

			}

			commentList = new ArrayList<>();
			if (!linkedComments.isEmpty()) {
				commentList = new ArrayList<>(linkedComments.values());
				commentsMap = linkedComments;
			} else {
				commentsMap = new HashMap<>();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	public boolean save(Comment entry) {
		BufferedWriter out = null;
		try {
			File file = new File(rootPath + pathToFile);
			System.out.println(file.getCanonicalPath());
			out = new BufferedWriter(new FileWriter(file, true));

			String data = entry.toString();

			System.out.println(data);
			out.write("\n" + data);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return true;
	}

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}

	public HashMap<Long, Comment> getCommentsMap() {
		return commentsMap;
	}

	public void setCommentsMap(HashMap<Long, Comment> commentsMap) {
		this.commentsMap = commentsMap;
	}


}
