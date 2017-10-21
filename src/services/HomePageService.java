package services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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

import beans.Comment;
import beans.CommentDeleteBean;
import beans.CommentEditBean;
import beans.CommentLikeBean;
import beans.CommentRating;
import beans.CommentRatings;
import beans.CommentSubmitBean;
import beans.Comments;
import beans.EditProfileBean;
import beans.LoginBean;
import beans.SearchBean;
import beans.Subforum;
import beans.SubforumNewBean;
import beans.Subforums;
import beans.Topic;
import beans.TopicBean;
import beans.TopicNewBean;
import beans.TopicRating;
import beans.TopicRatingBean;
import beans.TopicRatings;
import beans.Topics;
import beans.User;
import beans.UserChangeRoleBean;
import beans.UserPublicBean;
import beans.Users;
import controller.DataManager;
import controller.Utils;
import model.enums.Role;
import model.enums.TopicType;

@Path("/homepage")
public class HomePageService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	/**
	 * Search by criterion
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("search")
	public HashMap<String, Object> search(@Context HttpServletRequest request, SearchBean bean) {
		ArrayList<Subforum> subforumsResponse = new ArrayList<>();
		ArrayList<Topic> topicsResponse = new ArrayList<>();
		ArrayList<User> usersResponse = new ArrayList<>();
		
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		for (Subforum subforum : getSubforums().getSubforumsList()) {
			if((bean.getFields().contains("subforum_title") && 
					subforum.getName().toLowerCase().contains(bean.getText().toLowerCase())) ||
					(bean.getFields().contains("subforum_description") && 
					subforum.getDescription().toLowerCase().contains(bean.getText().toLowerCase())) ||
					(bean.getFields().contains("subforum_main_moderator") && 
					getUsers().getUsersMap().get(subforum.getMainModerator().getUserId()).getUsername().toLowerCase().contains(bean.getText().toLowerCase())) ||
					(bean.getFields().size()==0) && 
					(subforum.getName().toLowerCase().contains(bean.getText().toLowerCase()) ||
					subforum.getDescription().toLowerCase().contains(bean.getText().toLowerCase()) ||
					getUsers().getUsersMap().get(subforum.getMainModerator().getUserId()).getUsername().toLowerCase().contains(bean.getText().toLowerCase()))) {
				subforumsResponse.add(subforum);
			}
		}
		
		for (Topic topic : getTopics().getTopicList()) {
			if((bean.getFields().contains("topic_title") && 
					topic.getTitle().toLowerCase().contains(bean.getText().toLowerCase())) ||
					(bean.getFields().contains("topic_content") && 
					topic.getContent().toLowerCase().contains(bean.getText().toLowerCase())) ||	
					(bean.getFields().contains("topic_author") && 
					getUsers().getUsersMap().get(topic.getAuthorId()).getUsername().toLowerCase().contains(bean.getText().toLowerCase())) ||
					(bean.getFields().contains("topic_subforum") && 
					getSubforums().getSubforumsMap().get(topic.getSubforumId()).getName().toLowerCase().contains(bean.getText().toLowerCase())) ||
					(bean.getFields().size()==0) && 
					(topic.getTitle().toLowerCase().contains(bean.getText().toLowerCase()) ||
					topic.getContent().toLowerCase().contains(bean.getText().toLowerCase()) ||	
					getUsers().getUsersMap().get(topic.getAuthorId()).getUsername().toLowerCase().contains(bean.getText().toLowerCase()) ||
					getSubforums().getSubforumsMap().get(topic.getSubforumId()).getName().toLowerCase().contains(bean.getText().toLowerCase()))) {
				topicsResponse.add(topic);
			}
		}
		
		for (User user : getUsers().getUsers()) {
			if(bean.getFields().contains("username") && 
					user.getUsername().toLowerCase().contains(bean.getText().toLowerCase()) ||
					(bean.getFields().size()==0 && user.getUsername().toLowerCase().contains(bean.getText().toLowerCase()))) {
				usersResponse.add(user);
			}
		}
		
		HashMap<Long, User> users = getUsers().getUsersMap();
		ArrayList<TopicBean> topicsWithAuthors = new ArrayList<>();
		
		for (Topic topic : topicsResponse) {
			if (users.containsKey(topic.getAuthorId())) {
				// topic.setAuthorId(users.get(topic.getAuthorId().getUserId()));
				topicsWithAuthors.add(new TopicBean(topic, 
						users.get(topic.getAuthorId()), 
						getSubforums().getSubforumsMap().get(topic.getSubforumId()).getMainModerator().getUserId()));
			}	
		}
		
		
		
		
		
		HashMap<String, Object> retVal = new HashMap<>();
		retVal.put("subforums", subforumsResponse);
		retVal.put("topics", topicsWithAuthors);
		retVal.put("users", usersResponse);

		return retVal;
	}
	
	
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
	public HashMap<String, Object> getTopicsList(@Context HttpServletRequest request,
			@PathParam("subforum") long subforum) {
		// System.out.println("Reading topics from " + subforum + " subforum.");
		ArrayList<Topic> topics = getTopics().getTopicList();
		HashMap<Long, User> users = getUsers().getUsersMap();
		ArrayList<TopicBean> topicsFromSubforum = new ArrayList<>();
		for (Topic topic : topics) {
			// trebaju nam samo teme tog podforuma
			if (topic.getSubforumId() == subforum) {
				if (users.containsKey(topic.getAuthorId())) {
					// topic.setAuthorId(users.get(topic.getAuthorId().getUserId()));
					topicsFromSubforum.add(new TopicBean(topic, 
							users.get(topic.getAuthorId()), 
							getSubforums().getSubforumsMap().get(subforum).getMainModerator().getUserId()));
				}
			}
		}
		HashMap<String, Object> retVal = new HashMap<>();
		retVal.put("topics", topicsFromSubforum);
		retVal.put("main_moderator", getSubforums().getSubforumsMap().get(subforum).getMainModerator().getUserId());

		return retVal;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("upload_new_topic")
	public Boolean uploadNewTopicData(@Context HttpServletRequest request, TopicNewBean topicBean) {

		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You're not logged.");
			return false;
		}
		
		long authorId = getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId();

		if (topicBean.getTopic_id() != -1) {
			// Topic exist, editing topic
			getTopics().getTopicsMap().get(topicBean.getTopic_id()).setTitle(topicBean.getTopic_title());
			getTopics().getTopicsMap().get(topicBean.getTopic_id())
					.setType(Utils.stringToTopicType(topicBean.getTopic_type()));
			getTopics().getTopicsMap().get(topicBean.getTopic_id()).setContent(topicBean.getContent());
			getTopics().setTopicsList(new ArrayList<>(getTopics().getTopicsMap().values()));

			if (DataManager.getInstance().saveTopics(getTopics())) {
				ctx.setAttribute("topics", DataManager.getInstance().readTopics());
				return true;
			}
		} else {
			// New topic
			String content = "";
			if (topicBean.getContent().startsWith("https://") || topicBean.getContent().startsWith("http://")
					|| topicBean.getTopic_type().equals("IMAGE") || topicBean.getTopic_type().equals("TEXT")) {
				content = topicBean.getContent();
			} else {
				if (topicBean.getContent().startsWith("www.")) {
					content = "http://" + topicBean.getContent();
				} else {
					content = "http://www." + topicBean.getContent();
				}
			}

			Topic entry = new Topic(Utils.stringToTopicType(topicBean.getTopic_type()), topicBean.getSubforum_id(),
					topicBean.getTopic_title(), authorId, content);

			if (DataManager.getInstance().saveTopic(entry)) {
				ctx.setAttribute("topics", DataManager.getInstance().readTopics());
				return true;
			}
		}

		return false;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("upload_new_subforum")
	public Boolean uploadNewSubforumData(@Context HttpServletRequest request, SubforumNewBean subforumBean) {

		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You're not logged.");
			return false;
		}

		if (subforumBean.getSubforum_id() != -1) {
			// Topic exist, editing topic
			getSubforums().getSubforumsMap().get(subforumBean.getSubforum_id())
					.setName(subforumBean.getSubforum_title());
			getSubforums().getSubforumsMap().get(subforumBean.getSubforum_id())
					.setDescription(subforumBean.getDescription());
			getSubforums().getSubforumsMap().get(subforumBean.getSubforum_id()).setIcon(subforumBean.getImage());
			getSubforums().setSubforumsList(new ArrayList<>(getSubforums().getSubforumsMap().values()));

			if (DataManager.getInstance().saveSubforums(getSubforums())) {
				ctx.setAttribute("subforums", DataManager.getInstance().readSubforums());
				return true;
			}
		} else {
			// New topic
			long mainModerator = getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId();
			Subforum entry = new Subforum(subforumBean.getSubforum_title(), subforumBean.getDescription(),
					subforumBean.getImage(), "Add rules to frontend", mainModerator);

			if (DataManager.getInstance().saveSubforum(entry)) {
				ctx.setAttribute("subforums", DataManager.getInstance().readSubforums());
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns full topic object with loaded comments
	 */
	@GET
	@Path("/topic/{topic}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> getTopic(@Context HttpServletRequest request,
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
		// Comment ratings
		ArrayList<CommentRating> commentRatings = new ArrayList<>();
		if (loggedUser != null) {
			for (CommentRating rating : getCommentRatings().getRatingsList()) {
				if (getComments().getComment(rating.getCommentId()).getTopicId() == topicId && rating
						.getUserId() == getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId()) {
					commentRatings.add(rating);
				}
			}
		}
		// Topic rating
		TopicRating topicRating = null;
		if (loggedUser != null) {
			topicRating = new TopicRating(topicId,
					getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId(), 0);
			for (TopicRating rating : getTopicRatings().getRatingsList()) {
				if (rating.getTopicId() == topicId && rating.getUserId() == getUsers().getUsersMapByUsername()
						.get(loggedUser.getUsername()).getUserId()) {
					topicRating = rating;
				}
			}
		}

		long mainModerator = getSubforums().getSubforumsMap().get(topic.getSubforumId()).getMainModerator().getUserId();

		HashMap<String, Object> retVal = new HashMap<>();
		retVal.put("topic", topic);
		retVal.put("comment_ratings", commentRatings);
		retVal.put("topic_rating", topicRating);
		retVal.put("main_moderator", mainModerator);

		System.out.println("Image path: " + topic.getContent());

		return retVal;
	}
	
	/**
	 * Returns subforum data for filling edit form
	 */
	@GET
	@Path("/subforum/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> getSubforum(@Context HttpServletRequest request, @PathParam("id") long subforumId) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");
		
		if (loggedUser == null) {
			System.out.println("You're not logged.");
			return null;
		}

		Subforum subforum = getSubforums().getSubforumsMap().get(subforumId);

		HashMap<String, Object> retVal = new HashMap<>();
		retVal.put("subforum", subforum);

		return retVal;
	}

	@DELETE
	@Path("/delete_topic/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteTopic(@Context HttpServletRequest request, @PathParam("id") long topicId) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You're not logged.");
		} else {
			if (DataManager.getInstance().deleteTopic(topicId)) {
				ctx.setAttribute("topics", DataManager.getInstance().readTopics());
				return true;
			}
		}
		return false;
	}
	@DELETE
	@Path("/subforum/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteSubforum(@Context HttpServletRequest request, @PathParam("id") long subforumId) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You're not logged.");
			return false;
		}
	
		if (DataManager.getInstance().deleteSubforum(subforumId)) {
			ctx.setAttribute("subforums", DataManager.getInstance().readSubforums());
			return true;
		}
		return false;
	}
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("user_change_role")
	public String changeUsersRole(@Context HttpServletRequest request, UserChangeRoleBean changeRoleBean) {

		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You're not logged.");
			return null;
		}
		User user = getUsers().getUsersMap().get(changeRoleBean.getUserId());
		
		if(changeRoleBean.getValue() == -1) {
			user.roleDown();
		} else if(changeRoleBean.getValue() == 1) {
			user.roleUp();
		} else {
			return null;
		}
		
		if (DataManager.getInstance().updateUser(user)) {
			ctx.setAttribute("users", DataManager.getInstance().readUsers());
			return Utils.roleToString(user.getRole());
		}
	

		return null;
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

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("upload_image")
	public Boolean uploadImage(@Context HttpServletRequest request, @FormDataParam("image") File image,
			@FormDataParam("name") String name) throws Exception {
		System.out.println(name);

		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");
		if (loggedUser == null) {
			return false;
		}

		DataManager.getInstance().saveImage(image, name);
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

	@POST
	@Path("/add_comment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean addComment(@Context HttpServletRequest request, CommentSubmitBean comment) {
		LoginBean retVal = null;
		retVal = (LoginBean) request.getSession().getAttribute("user");

		System.out.println("Coment: " + comment.getTopicId() + " " + comment.getText());

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
	@Path("/update_comment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> updateComment(@Context HttpServletRequest request, CommentEditBean newComment) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		// System.out.println("Coment: " + newComment.getTopicId() + " " +
		// newComment.getText());

		if (loggedUser == null) {
			System.out.println("You can't comment because you're not logged.");
			return null;
		} else {
			Comment comment = getComments().getComment(newComment.getCommentId());
			boolean isMainModerator = getUsers().getUsersMapByUsername().get(loggedUser.getUsername())
					.getUserId() == getSubforums().getSubforumsMap()
							.get(getTopics().getTopicsMap().get(newComment.getTopicId()).getSubforumId())
							.getMainModerator().getUserId();
			System.out.println("IS MAIN MODERATOR" + isMainModerator);
			if (DataManager.getInstance().updateComment(newComment, isMainModerator)) {
				ctx.setAttribute("comments", DataManager.getInstance().readComments());
			}

			HashMap<String, Object> retVal = new HashMap<>();
			retVal.put("comment", comment);
			return retVal;
		}
	}

	@POST
	@Path("/delete_comment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean deleteComment(@Context HttpServletRequest request, CommentDeleteBean comment) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		// System.out.println("Coment: " + newComment.getTopicId() + " " +
		// newComment.getText());

		if (loggedUser == null) {
			System.out.println("You can't comment because you're not logged.");
			return false;
		} else {
			// Comment comment =
			// getComments().getComment(newComment.getCommentId());
			// boolean isMainModerator = comment.getAuthor().getUserId() ==
			// getSubforums().getSubforumsMap().get(getTopics().getTopicsMap().get(newComment.getTopicId()).getSubforumId()).getMainModerator().getUserId();
			if (DataManager.getInstance().deleteComment(comment.getCommentId())) {
				ctx.setAttribute("comments", DataManager.getInstance().readComments());
			}

			// HashMap<String, Object> retVal = new HashMap<>();
			// retVal.put("comment", comment);
			return true;
		}
	}

	@POST
	@Path("/like_comment_toogle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> addCommentRating(@Context HttpServletRequest request, CommentLikeBean comment) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You can't comment because you're not logged.");
			return null;
		} else {

			long userId = getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId();
			DataManager.getInstance()
					.saveCommentRating(new CommentRating(comment.getCommentId(), userId, comment.getRatingValue()));
			ctx.setAttribute("comment_ratings", DataManager.getInstance().readCommentRatings());

			int likes = getComments().getComment(comment.getCommentId()).getLikes();
			int dislikes = getComments().getComment(comment.getCommentId()).getDislikes();
			int total = likes - dislikes;

			HashMap<String, Object> retVal = new HashMap<>();
			retVal.put("total", total);

			int value = 0;
			if (getCommentRatings().getRating(comment.getCommentId(), userId) != null) {
				value = getCommentRatings().getRating(comment.getCommentId(), userId).getValue();
			}
			retVal.put("rated", value);
			return retVal;
		}
	}

	@POST
	@Path("/rate_topic_toogle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> addTopicRating(@Context HttpServletRequest request, TopicRatingBean topicRating) {
		LoginBean loggedUser = null;
		loggedUser = (LoginBean) request.getSession().getAttribute("user");

		if (loggedUser == null) {
			System.out.println("You can't rate because you're not logged.");
			return null;
		} else {

			long userId = getUsers().getUsersMapByUsername().get(loggedUser.getUsername()).getUserId();
			DataManager.getInstance()
					.saveTopicRating(new TopicRating(topicRating.getTopicId(), userId, topicRating.getRatingValue()));
			ctx.setAttribute("topic_ratings", DataManager.getInstance().readTopicRatings());

			int likes = getTopics().getTopicsMap().get(topicRating.getTopicId()).getLikes();
			int dislikes = getTopics().getTopicsMap().get(topicRating.getTopicId()).getDislikes();

			HashMap<String, Object> retVal = new HashMap<>();

			int value = 0;
			if (getTopicRatings().getRating(topicRating.getTopicId(), userId) != null) {
				value = getTopicRatings().getRating(topicRating.getTopicId(), userId).getValue();
			}
			retVal.put("rated", value);
			return retVal;
		}
	}

	private Subforums getSubforums() {
		Subforums subforums = (Subforums) ctx.getAttribute("subforums");
		if (subforums == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			subforumsData();
			subforums = DataManager.getInstance().readSubforums();
			ctx.setAttribute("subforums", subforums);
		}
		return subforums;
	}

	private Topics getTopics() {
		Topics topics = (Topics) ctx.getAttribute("topics");
		if (topics == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			topicsData();
			topicRatingsData();
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
			commentRatingsData();
			comments = DataManager.getInstance().readComments();
			ctx.setAttribute("comments", comments);
		}
		return comments;
	}

	private CommentRatings getCommentRatings() {
		CommentRatings ratings = (CommentRatings) ctx.getAttribute("comment_ratings");
		if (ratings == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			ratings = DataManager.getInstance().readCommentRatings();
			ctx.setAttribute("comment_ratings", ratings);
		}
		return ratings;
	}

	private TopicRatings getTopicRatings() {
		TopicRatings ratings = (TopicRatings) ctx.getAttribute("topic_ratings");
		if (ratings == null) {
			DataManager.setUpRootPath(ctx.getRealPath(""));
			ratings = DataManager.getInstance().readTopicRatings();
			ctx.setAttribute("topic_ratings", ratings);
		}
		return ratings;
	}

	private void commentRatingsData() {

		CommentRating r1 = new CommentRating(1l, 1l, 1);
		CommentRating r2 = new CommentRating(1l, 2l, 1);
		CommentRating r3 = new CommentRating(1l, 3l, 1);
		CommentRating r4 = new CommentRating(2l, 1l, -1);

		DataManager.getInstance().saveCommentRating(r1);
		DataManager.getInstance().saveCommentRating(r2);
		DataManager.getInstance().saveCommentRating(r3);
		DataManager.getInstance().saveCommentRating(r4);
	}

	private void topicRatingsData() {

		TopicRating r1 = new TopicRating(1l, 1l, 1);
		TopicRating r2 = new TopicRating(1l, 2l, 1);
		TopicRating r3 = new TopicRating(1l, 3l, -1);

		DataManager.getInstance().saveTopicRating(r1);
		DataManager.getInstance().saveTopicRating(r2);
		DataManager.getInstance().saveTopicRating(r3);
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

	private void subforumsData() {

		Subforum s1 = new Subforum(1l, "Cars", "Topics about all kind of vehicles.", "resources/cars.jpg",
				"Standard rules.", 2);
		Subforum s2 = new Subforum(2l, "Sport", "Topics about all kind of sport.", "resources/sport.png", "No spamming",
				2);

		DataManager.getInstance().saveSubforum(s1);
		DataManager.getInstance().saveSubforum(s2);
	}
}
