package services;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

import beans.LoginBean;
import beans.SignUpBean;
import beans.User;
import beans.Users;
import controller.Utils;
import model.enums.Role;

@Path("/authenticate")
public class LoginService {

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
	@Path("/test")
	public String test() {
		return "REST is working.";
	}
	
	@GET
	@Path("/get_logged_user")
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request) {
		LoginBean sessionUser = null;
		sessionUser = (LoginBean) request.getSession().getAttribute("user");

		if (sessionUser == null) {
//			System.out.println("No previous logged users.");
			return null;
//		} else {
//			System.out.println("User " + sessionUser.getUsername() + " is already logged!");
		}
		User user = getUsers().getUsersMapByUsername().get(sessionUser.getUsername());
		
		return user;
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
		Users users = getUsers();
//		System.out.println("Users in file-------------------------");
		for (User user2 : users.getUsers()) {
			System.out.println(user2.toString());
			if (user2.getUsername().equals(user.getUsername()) && user2.getPassword().equals(user.getPassword())) {
//				System.out.println("Access granted to " + user.getUsername());
				request.getSession().setAttribute("user", user);
				return user2;
			}
		}
//		System.out.println("Users in file----------------------END");

		return null;
	}

	/**
	 * SignUp REST call
	 * 
	 * @param request
	 *            Injektovano zaglavlje HTTP zahteva.
	 * @param request
	 *            JSON string koji reprezentuje objekat klase User.
	 * @return JSON string koji reprezentuje objekat klase User.
	 */
	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User signup(@Context HttpServletRequest request, SignUpBean user) {
		LoginBean retVal = null;
		retVal = (LoginBean) request.getSession().getAttribute("user");

		System.out.println("USER1: " +  user.toString());
//		if (retVal == null) {
//			System.out.println("No previous logged users.");
//		} else {
//			System.out.println("User " + retVal.getUsername() + " is already logged!");
//			request.getSession().invalidate();
//		}
		Users users = getUsers();
		System.out.println("Users in file-------------------------");
		for (User user2 : users.getUsers()) {
			System.out.println(user.toString());
			if (user2.getUsername().equals(user.getUsername())) {
				request.getSession().invalidate();
				System.out.println("This one already exist " + user2.getUsername());
				return null;
			}
		}
		System.out.println("Users in file----------------------END");
		//TODO: Napravi upload i za sliku usera
		User newUser = new User(Role.USER, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getTelephone(), null, user.getEmail(), Utils.getCurrentDate());

		System.out.println("USER2: " +  newUser.toString());
		addUser(newUser);
		retVal = new LoginBean(newUser.getUsername(), newUser.getPassword());
		request.getSession().setAttribute("user", retVal);
		return newUser;
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
	public String logout(@Context HttpServletRequest request) {
		LoginBean user = null;
		user = (LoginBean) request.getSession().getAttribute("user");
		if (user != null) {
			request.getSession().invalidate();
		}
		return "Successfully logged off";
	}

	private Users getUsers() {
		Users users = (Users) ctx.getAttribute("users");
		if (users == null) {
			users = new Users(ctx.getRealPath(""));
			ctx.setAttribute("users", users);
		}
		return users;
	}

	private User addUser(User newUser) {
		Users users = (Users) ctx.getAttribute("users");
		if (users == null) {
			users = new Users(ctx.getRealPath(""));
			ctx.setAttribute("users", users);
		}
		return users.saveUser(newUser);
	}

}
