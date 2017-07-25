package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Comments implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Comment> commentList;
	private HashMap<Long, Comment> commentsMap;


	public Comments() {
		commentList = new ArrayList<>();
		commentsMap = new HashMap<>();	
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

	public void addComment(Comment entry) {
		Boolean isSuccessful = false;
		// No parent
		if(entry.getParentComment() == 0) {
			commentList.add(entry);
			commentsMap.put(entry.getCommentId(), entry);
		}
		// Finding parent in list
		for (int i=0; i<commentList.size(); i++) {
			// First in root (commentList.get(i) has no parent)
			if(commentList.get(i).getCommentId() == entry.getParentComment()) {
				commentList.get(i).getChildComments().add(entry);
				isSuccessful = true;
//				return;
			} else {
				// Looking in children
				if ( findComment(commentList.get(i), entry) ) {
					isSuccessful = true;
				}
			}
			// If found and replaced in list, take his ancestor without parent and put him in map instead old non-updated comment  
			if (isSuccessful) {
				commentsMap.put(commentList.get(i).getCommentId(), commentList.get(i));
				return;
			}
		}
	}
	/**
	 * Looks for entries parent in comments children. If founds than connects them
	 */
	private Boolean findComment(Comment parent, Comment entry) {
		//Boolean retVal = false;
		for (Comment comment : parent.getChildComments()) {
			if(comment.getCommentId() == entry.getParentComment()) {
				parent.getChildComments().add(entry);
				return true;
			} else {
				// TODO: Might need check if there are any children
				findComment(comment, entry);
			}
		}
		return false;
	}


}
