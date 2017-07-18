package services;

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
import beans.Comments;
import beans.LoginBean;
import beans.Subforum;
import beans.Subforums;
import beans.Topic;
import beans.Topics;
import beans.User;
import beans.Users;

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
	public Topic test4(@Context HttpServletRequest request,
			/* @PathParam("subforum") long subforumId, */ @PathParam("topic") long topicId) {
		System.out.println("Reading topic with id:  " + topicId + ". Loading comments.");
		Topic topic = getTopics().getTopicsMap().get(topicId);
		ArrayList<Comment> allComments = getComments().getCommentList();
		HashMap<Long, User> users = getUsers().getUsersMap();

		if (topic.getComments().isEmpty()) {
			// Find all comments of topic
			for (Comment comment : allComments) {
				if (topic.getTopicId() == comment.getTopic().getTopicId()) {
					topic.getComments().add(comment);
				}
			}
		}
		return topic;
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
		/*
		 * Users users = getUsers(); for (User user2 : users.getUsers()) { if
		 * (user2.getUsername().equals(user.getUsername()) &&
		 * user2.getPassword().equals(user.getPassword())) {
		 * request.getSession().setAttribute("user", user); return user2; } }
		 */

		return null;
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
			users = new Users(ctx.getRealPath(""));
			ctx.setAttribute("users", users);
		}
		return users;
	}

	private Comments getComments() {
		Comments comments = (Comments) ctx.getAttribute("comments");
		if (comments == null) {
			comments = new Comments(ctx.getRealPath(""));
			ctx.setAttribute("comments", comments);
		}
		return comments;
	}

}