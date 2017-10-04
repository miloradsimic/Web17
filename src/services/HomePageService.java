package services;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.jws.soap.SOAPBinding.Use;
import javax.print.attribute.ResolutionSyntax;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

import beans.AvatarBean;
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
import beans.TopicBean;
import beans.Topics;
import beans.User;
import beans.UserPublicBean;
import beans.Users;
import controller.DataManager;
import controller.Utils;
import javassist.bytecode.ByteArray;
import model.enums.Role;
import model.enums.TopicType;
import sun.misc.BASE64Decoder;

@Path("/homepage")
public class HomePageService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	/**
	 * Returns all subforums available on forum
	 */
	@GET
	@Path("/subforums")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Subforum> getSubformData() {
		return getSubforums().getSubforumsList();
	}

	/**
	 * Returns all topic of selected subforum
	 */
	@GET
	@Path("/topics/{subforum}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<TopicBean> test3(@Context HttpServletRequest request, @PathParam("subforum") long subforum) {
		// System.out.println("Reading topics from " + subforum + " subforum.");
		ArrayList<Topic> topics = getTopics().getTopicsList();
		HashMap<Long, User> users = getUsers().getUsersMap();
		ArrayList<TopicBean> topicsFromSubforum = new ArrayList<>();
		for (Topic topic : topics) {
			// trebaju nam samo teme tog podforuma
			if (topic.getSubforumId() == subforum) { 
				if (users.containsKey(topic.getAuthorId())) {
//					topic.setAuthorId(users.get(topic.getAuthorId().getUserId()));
					topicsFromSubforum.add(new TopicBean(topic, users.get(topic.getAuthorId())));
				}
			}
		}
		return topicsFromSubforum;
		// HashMap<String, Object> retVal = new HashMap<>();
		// retVal.put("topics", topicsFromSubforum);
		// retVal.put("ratings", ratings);
		//
		// return retVal;
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
				if (getComments().getComment(rating.getCommentId()).getTopicId() == topicId && rating
						.getUserId() == getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId()) {
					ratings.add(rating);
				}
			}
		}

		HashMap<String, Object> retVal = new HashMap<>();
		retVal.put("topic", topic);
		retVal.put("ratings", ratings);

		return retVal;
	}

	/**
	 * Returns registered users
	 */
	@GET
	@Path("/users_list/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<UserPublicBean> getUsersDataREST(@Context HttpServletRequest request,
			@PathParam("id") long userId) {

		ArrayList<UserPublicBean> retValList = new ArrayList<>();
		UserPublicBean tempUser;
		for (User user : getUsers().getUsers()) {

			if (user.getUserId() == userId) {
				continue;
			}
			tempUser = new UserPublicBean();

			tempUser.setUserId(user.getUserId());
			tempUser.setUsername(user.getUsername());
			tempUser.setFirstName(user.getFirstName());
			tempUser.setLastName(user.getLastName());
			tempUser.setAvatar(user.getAvatar());
			tempUser.setRole(user.getRole().toString());

			System.out.println("username: " + tempUser.getUsername() + " role: " + user.getRole().toString());

			retValList.add(tempUser);

		}
		return retValList;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("submit_profile_data")
	public Boolean uploadProfileData(@Context HttpServletRequest request, EditProfileBean profileData)
			throws Exception {

		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		User old = getUsers().getUsersMapByUsername().get(loggedUser.getUsername());
		old.setFirstName(profileData.getFirstName());
		old.setLastName(profileData.getLastName());
		old.setEmail(profileData.getEmail());
		if (!profileData.getPassword().equals("")) {
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
	// @Produces(MediaType.APPLICATION_JSON)
	@Path("upload_avatar")
	public Boolean uploadAvatar(@Context HttpServletRequest request, @FormDataParam("image") File image,
			@FormDataParam("name") String name) throws Exception {
		System.out.println(name);

		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");
		if (loggedUser == null) {
			return null;
		}

		DataManager.getInstance().saveAvatar(image, name, loggedUser.getUsername());

		return true;
	}

	@GET
	@Path("/profile/{profile}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Object getProfileData(@Context HttpServletRequest request, @PathParam("profile") long profileId) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser != null
				&& getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId() == profileId) {
			return getUsers().getUsersMapByUsername().get(loggedUser.getUsername());
			// return full profile data.

		} else if (getUsers().getUsersMap().containsKey(profileId)) {
			return new UserPublicBean(getUsers().getUsersMap().get(profileId));
		} else {
			System.out.println("User doesn't exist dude!");
			return null;
		}
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
			DataManager.getInstance()
					.saveRating(new CommentRating(comment.getCommentId(), userId, comment.getRatingValue()));
			ctx.setAttribute("ratings", DataManager.getInstance().readRatings());

			int likes = getComments().getComment(comment.getCommentId()).getLikes();
			int dislikes = getComments().getComment(comment.getCommentId()).getDislikes();
			int total = likes - dislikes;

			HashMap<String, Object> retVal = new HashMap<>();
			retVal.put("total", total);

			int value = 0;
			if (getRatings().getRating(comment.getCommentId(), userId) != null) {
				value = getRatings().getRating(comment.getCommentId(), userId).getValue();
			}
			retVal.put("rated", value);
			return retVal;
		}
	}

	// TODO: Not gonna happen, only edit exit in specs
	// @DELETE
	// @Path("/user/{id}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Boolean removeUser(@Context HttpServletRequest request, long id) {
	// LoginBean loggedUser = null;
	// loggedUser = (LoginBean) request.getSession().getAttribute("user");
	// User userLogged =
	// getUsers().getUsersMapByUsername().get((loggedUser.getUsername()));
	// User userDelete = getUsers().getUsersMap().get(id);
	//
	// if (loggedUser == null) {
	// System.out.println("You're not logged.");
	// return false;
	// } else if (id != userClient.getUserId() && ){
	// System.out.println("You don't have privileges to do that.");
	// return false;
	// } else {
	//
	// long userId =
	// getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId();
	// DataManager.getInstance()
	// .saveRating(new CommentRating(comment.getCommentId(), userId,
	// comment.getRatingValue()));
	// ctx.setAttribute("ratings", DataManager.getInstance().readRatings());
	//
	// int likes = getComments().getComment(comment.getCommentId()).getLikes();
	// int dislikes =
	// getComments().getComment(comment.getCommentId()).getDislikes();
	// int total = likes - dislikes;
	//
	// HashMap<String, Object> retVal = new HashMap<>();
	// retVal.put("total", total);
	//
	// int value = 0;
	// if (getRatings().getRating(comment.getCommentId(), userId) != null) {
	// value = getRatings().getRating(comment.getCommentId(),
	// userId).getValue();
	// }
	// retVal.put("rated", value);
	// return retVal;
	// }
	//
	// return false;
	// }

	private Subforums getSubforums() {
		Subforums subforums = (Subforums) ctx.getAttribute("subforums");
		if (subforums == null) {
			subforums = new Subforums(ctx.getRealPath(""));
			ctx.setAttribute("subforums", subforums);
		}
		return subforums;
	}

	private Topics getTopics() {
		Topics topics = (Topics) ctx.getAttribute("topics");
		if (topics == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			topicsData();
			topics = DataManager.getInstance().readTopics();
			ctx.setAttribute("topics", topics);
		}
		return topics;
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
		Comment c1 = new Comment(1l, 1l, getUsers().getUsersMapByUsername().get("admin"), Utils.getCurrentDate(), 0,
				"Text komentara1", 0, 0, false);
		Comment c2 = new Comment(2l, 1l, getUsers().getUsersMapByUsername().get("admin"), Utils.getCurrentDate(), 1,
				"Text komentara2", 0, 0, false);
		Comment c3 = new Comment(3l, 1l, getUsers().getUsersMapByUsername().get("moderator"), Utils.getCurrentDate(), 0,
				"Text komentara3", 0, 0, false);
		Comment c4 = new Comment(4l, 1l, getUsers().getUsersMapByUsername().get("admin"), Utils.getCurrentDate(), 2,
				"Text komentara4", 0, 0, false);
		Comment c5 = new Comment(5l, 1l, getUsers().getUsersMapByUsername().get("user"), Utils.getCurrentDate(), 4,
				"Text komentara5", 0, 0, false);
		Comment c6 = new Comment(6l, 1l, getUsers().getUsersMapByUsername().get("user"), Utils.getCurrentDate(), 4,
				"Text komentara6", 0, 0, false);
		DataManager.getInstance().saveComment(c1);
		DataManager.getInstance().saveComment(c2);
		DataManager.getInstance().saveComment(c3);
		DataManager.getInstance().saveComment(c4);
		DataManager.getInstance().saveComment(c5);
		DataManager.getInstance().saveComment(c6);
	}

	private void usersData() {

		User u1 = new User(1, Role.ADMIN, "admin", "admin", "Al", "Andereson", "0651111111", "al@gmail.com",
				"resources/lav.jpg");
		User u2 = new User(2, Role.MODERATOR, "moderator", "moderator", "Mike", "Morison", "0652222222",
				"mike@gmail.com", "resources/lav.jpg");
		User u3 = new User(3, Role.USER, "user", "user", "Usain", "Ulman", "0653333333", "usain@gmail.com",
				"resources/slon.jpg");

		DataManager.getInstance().saveUser(u1);
		DataManager.getInstance().saveUser(u2);
		DataManager.getInstance().saveUser(u3);
	}

	private void topicsData() {

		Topic t1 = new Topic(1l, TopicType.TEXT, 1, "Prodajem golfa dvojku povoljno!", 2,
				"Prodaje golfa dvojku, crvene boje, dizelas, '88 godiste. Cena 200e, nije fiksna.",
				Utils.getCurrentDate(), 0, 0);
		Topic t2 = new Topic(2l, TopicType.LINK, 1, "Testing new Audi A4!", 2,
				"https://www.youtube.com/watch?v=H7yGMvLtINs", Utils.getCurrentDate(), 0, 0);
		Topic t3 = new Topic(3l, TopicType.IMAGE, 2, "Slika palme na plazi!", 1, "resources/drvo.jpg",
				Utils.getCurrentDate(), 0, 0);

		DataManager.getInstance().saveTopic(t1);
		DataManager.getInstance().saveTopic(t2);
		DataManager.getInstance().saveTopic(t3);
	}
}
