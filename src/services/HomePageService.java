package services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

import beans.Comment;
import beans.CommentLikeBean;
import beans.CommentRating;
import beans.CommentRatings;
import beans.CommentSubmitBean;
import beans.Comments;
import beans.EditProfileBean;
import beans.LoginBean;
import beans.Subforum;
import beans.Subforums;
import beans.Topic;
import beans.Topics;
import beans.User;
import beans.Users;
import controller.DataManager;
import controller.Utils;
import model.enums.Role;

@Path("/homepage")
public class HomePageService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	/**
	 * Testira REST sistem. URL izgleda ovako: <code>rest/demo/test</code>
	 * 
	 * @return Vra�a tekst da vidimo da sve radi.
	 */
	@GET
	@Path("/subforums")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Subforum> test() {
		return getSubforums().getSubforumsList();
	}

	/**
	 * Returns all topic of selected subforum
	 */
	@GET
	@Path("/topics/{subforum}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Topic> test3(@Context HttpServletRequest request, @PathParam("subforum") long subforum) {
		// System.out.println("Reading topics from " + subforum + " subforum.");
		ArrayList<Topic> topics = getTopics().getTopicsList();
		HashMap<Long, User> users = getUsers().getUsersMap();
		ArrayList<Topic> topicsFromSubforum = new ArrayList<>();
		for (Topic topic : topics) {
			if (topic.getSubforum().getSubforumId() == subforum) { // trebaju
																	// nam samo
																	// teme tog
																	// podforuma
				if (users.containsKey(topic.getAuthor().getUserId())) {
					topic.setAuthor(users.get(topic.getAuthor().getUserId()));
				}
				topicsFromSubforum.add(topic);
			}
		}
		return topicsFromSubforum;
	}

	/**
	 * Returns full topic object with loaded comments
	 */
	@GET
	@Path("/topic/{topic}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> test4(@Context HttpServletRequest request,
			/* @PathParam("subforum") long subforumId, */ @PathParam("topic") long topicId) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");
		
		System.out.println("Reading topic with id:  " + topicId + ". Loading comments.");
		Topic topic = getTopics().getTopicsMap().get(topicId);
		ArrayList<Comment> allComments = getComments().getCommentList();
		HashMap<Long, User> users = getUsers().getUsersMap();

		topic.getComments().clear();
		// Find all comments of topic
		for (Comment comment : allComments) {
			if (topic.getTopicId() == comment.getTopicId()) {
				topic.getComments().add(comment);
			}
		}
		ArrayList<CommentRating> ratings = new ArrayList<>();
		if (loggedUser != null) {
			for (CommentRating rating : getRatings().getRatingsList()) {
				if(getComments().getComment(rating.getCommentId()).getTopicId() == topicId && 
						rating.getUserId() == getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId()) {
					ratings.add(rating);
				}
			}
		}
		
		HashMap<String, Object> retVal = new HashMap<>();
		retVal.put("topic", topic);
		retVal.put("ratings", ratings);

		return retVal;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("submit_profile_data")
	public Boolean uploadProfileData(@Context HttpServletRequest request, EditProfileBean profileData) throws Exception {
	    
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");
		
		User old = getUsers().getUsersMapByUsername().get(loggedUser.getUsername());
		old.setFirstName(profileData.getFirstName());
		old.setLastName(profileData.getLastName());
		old.setEmail(profileData.getEmail());
		if(!profileData.getPassword().equals("")){
			old.setPassword(profileData.getPassword());
		}
		
		if (DataManager.getInstance().updateProfile(old)) {
			ctx.setAttribute("users", DataManager.getInstance().readUsers());
			return true;
		}
		
	    return false;
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("upload_avatar")
	public Boolean uploadAvatar(InputStream fileInputStream) throws Exception {
	    
		System.out.println("File: " + fileInputStream);
	    return true;
	}
	
	@GET
	@Path("/profile/{profile}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Object test5(@Context HttpServletRequest request,
			/* @PathParam("subforum") long subforumId, */ @PathParam("profile") long profileId) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");
		
		User retVal;
		if(loggedUser != null && (retVal = getUsers().getUsersMapByUsername().get(loggedUser.getUsername())).getUserId() == profileId) {
			// return full profile data.
			
			
		} else {
			User temp = getUsers().getUsersMap().get(profileId);
			if(temp == null) {
				System.out.println("User doesn't exist dude!");
				return null;
			}
			retVal = new User(temp);
			
			// return min profile data
			// not gonna be implemented for now
		}

		return retVal;
		
		
//		System.out.println("Reading topic with id:  " + topicId + ". Loading comments.");
//		Topic topic = getTopics().getTopicsMap().get(topicId);
//		ArrayList<Comment> allComments = getComments().getCommentList();
//		HashMap<Long, User> users = getUsers().getUsersMap();
//
//		topic.getComments().clear();
		// Find all comments of topic
//		for (Comment comment : allComments) {
//			if (topic.getTopicId() == comment.getTopicId()) {
//				topic.getComments().add(comment);
//			}
//		}
//		ArrayList<CommentRating> ratings = new ArrayList<>();
//		if (loggedUser != null) {
//			for (CommentRating rating : getRatings().getRatingsList()) {
//				if(getComments().getComment(rating.getCommentId()).getTopicId() == topicId && 
//						rating.getUserId() == getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId()) {
//					ratings.add(rating);
//				}
//			}
//		}
//		
//		HashMap<String, Object> retVal = new HashMap<>();
//		retVal.put("topic", topic);
//		retVal.put("ratings", ratings);

	}

	@GET
	@Path("/test")
	public String test2() {
		return "Server radi";
	}

	/**
	 * Demonstrira injektovanje HTTP zahteva u parametre metode. Injektovani
	 * zahtev �emo iskoristiti da iz njega izvu�emo sesiju, a nju �emo
	 * iskoristiti da ve�emo objekat klase User na sesiju, pod imenom 'user'.
	 * 
	 * @param request
	 *            Injektovano zaglavlje HTTP zahteva.
	 * @param request
	 *            JSON string koji reprezentuje objekat klase User.
	 * @return JSON string koji reprezentuje objekat klase User.
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, LoginBean user) {
		LoginBean retVal = null;
		retVal = (LoginBean) request.getSession().getAttribute("user");

		if (retVal == null) {
			System.out.println("No previous logged users.");
		} else {
			System.out.println("User " + retVal.getUsername() + " is already logged!");
		}
		return null;
	}

	@POST
	@Path("/add_comment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean addComment(@Context HttpServletRequest request, CommentSubmitBean comment) {
		LoginBean retVal = null;
		retVal = (LoginBean) request.getSession().getAttribute("user");

		if (retVal == null) {
			System.out.println("You can't comment because you're not logged.");
			return false;
		} else {
			// System.out.println("User " + retVal.getUsername() + " is already
			// logged!");
			User author = getUsers().getUsersMapByUsername().get(retVal.getUsername());
			String date = Utils.getCurrentDate();
			Comment entry = new Comment(comment.getTopicId(), author, date, comment.getParentId(), comment.getText(), 0,
					0, false);

			if (DataManager.getInstance().saveComment(entry)) {
				// Comments comments = new Comments(ctx.getRealPath(""));
				ctx.setAttribute("comments", DataManager.getInstance().readComments());
			}
			return true;
		}
	}

	@POST
	@Path("/like_comment_toogle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> addLike(@Context HttpServletRequest request, CommentLikeBean comment) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You can't comment because you're not logged.");
			return null;
		} else {
			
			long userId = getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId();
			DataManager.getInstance().saveRating(new CommentRating(comment.getCommentId(),userId, comment.getRatingValue()));
			ctx.setAttribute("ratings", DataManager.getInstance().readRatings());
			
			int likes = getComments().getComment(comment.getCommentId()).getLikes();
			int dislikes = getComments().getComment(comment.getCommentId()).getDislikes();
			int total = likes - dislikes;

			HashMap<String, Object> retVal = new HashMap<>();
			retVal.put("total", total);
			
			int value = 0;
			if(getRatings().getRating(comment.getCommentId(), userId) != null){
				value = getRatings().getRating(comment.getCommentId(), userId).getValue();
			}
			retVal.put("rated", value);
			return retVal;
		}
	}

	@GET
	@Path("/testlogin")
	@Produces(MediaType.TEXT_PLAIN)
	public String testLogin(@Context HttpServletRequest request) {
		LoginBean retVal = null;
		retVal = (LoginBean) request.getSession().getAttribute("user");
		if (retVal == null) {
			return "No logged users.";
		}
		return "User " + retVal.getUsername() + " is currently logged!";
	}

	/**
	 * Invalidira sesiju.
	 * 
	 * @param request
	 *            Injektovano zaglavlje HTTP zahteva.
	 * @return Potvrda invalidacije sesije.
	 */
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String logout(@Context HttpServletRequest request) {
		LoginBean user = null;
		user = (LoginBean) request.getSession().getAttribute("user");
		if (user != null) {
			request.getSession().invalidate();
		}
		return "Successfully logged off";
	}

	private Subforums getSubforums() {
		Subforums subforums = (Subforums) ctx.getAttribute("subforums");
		if (subforums == null) {
			subforums = new Subforums(ctx.getRealPath(""));
			ctx.setAttribute("subforums", subforums);
		}
		return subforums;
	}

	private Topics getTopics() {
		Topics allTopics = (Topics) ctx.getAttribute("topics");
		if (allTopics == null) {
			allTopics = new Topics(ctx.getRealPath(""));
			ctx.setAttribute("topics", allTopics);
		}
		return allTopics;
	}

	private Users getUsers() {
		Users users = (Users) ctx.getAttribute("users");
		if (users == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			usersData();
			users = DataManager.getInstance().readUsers();
			ctx.setAttribute("users", users);
		}
		return users;
	}

	private Comments getComments() {
		Comments comments = (Comments) ctx.getAttribute("comments");
		if (comments == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			commentsData();
			ratingsData();
			comments = DataManager.getInstance().readComments();
			ctx.setAttribute("comments", comments);
		}
		return comments;
	}

	private CommentRatings getRatings() {
		CommentRatings ratings = (CommentRatings) ctx.getAttribute("ratings");
		if (ratings == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			ratings = DataManager.getInstance().readRatings();
			ctx.setAttribute("ratings", ratings);
		}
		return ratings;
	}

	private void ratingsData() {

		CommentRating r1 = new CommentRating(1l, 1l, 1);
		CommentRating r2 = new CommentRating(1l, 2l, 1);
		CommentRating r3 = new CommentRating(1l, 3l, 1);
		CommentRating r4 = new CommentRating(2l, 1l, -1);
		
		DataManager.getInstance().saveRating(r1);
		DataManager.getInstance().saveRating(r2);
		DataManager.getInstance().saveRating(r3);
		DataManager.getInstance().saveRating(r4);
	}

	private void commentsData() {
		Comment c1 = new Comment(1l, 1l, getUsers().getUsersMapByUsername().get("johnnydoe"), Utils.getCurrentDate(), 0,
				"Text komentara1", 0, 0, false);
		Comment c2 = new Comment(2l, 1l, getUsers().getUsersMapByUsername().get("johnnydoe"), Utils.getCurrentDate(), 1,
				"Text komentara2", 0, 0, false);
		Comment c3 = new Comment(3l, 1l, getUsers().getUsersMapByUsername().get("mika"), Utils.getCurrentDate(), 0,
				"Text komentara3", 0, 0, false);
		Comment c4 = new Comment(4l, 1l, getUsers().getUsersMapByUsername().get("johnsmith"), Utils.getCurrentDate(), 2,
				"Text komentara4", 0, 0, false);
		Comment c5 = new Comment(5l, 1l, getUsers().getUsersMapByUsername().get("johnsmith"), Utils.getCurrentDate(), 4,
				"Text komentara5", 0, 0, false);
		Comment c6 = new Comment(6l, 1l, getUsers().getUsersMapByUsername().get("johnsmith"), Utils.getCurrentDate(), 4,
				"Text komentara6", 0, 0, false);
		DataManager.getInstance().saveComment(c1);
		DataManager.getInstance().saveComment(c2);
		DataManager.getInstance().saveComment(c3);
		DataManager.getInstance().saveComment(c4);
		DataManager.getInstance().saveComment(c5);
		DataManager.getInstance().saveComment(c6);
	}
	
	private void usersData() {
	
		User u1 = new User(1, Role.ADMIN, "admin", "admin", "Al", "Andereson", "0651111111", "al@gmail.com", "resources/lav.jpg");
		User u2 = new User(2, Role.MODERATOR, "moderator", "moderator", "Mike", "Morison", "0652222222", "mike@gmail.com", "resources/lav.jpg");
		User u3 = new User(3, Role.USER, "user", "user", "Usain", "Ulman", "0653333333", "usain@gmail.com", "resources/slon.jpg");
		
		DataManager.getInstance().saveUser(u1);
		DataManager.getInstance().saveUser(u2);
		DataManager.getInstance().saveUser(u3);
	}

}
