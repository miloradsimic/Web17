package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

import controller.Utils;
import model.enums.Role;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final AtomicLong count = new AtomicLong(100); 
	private long userId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	protected Role role;
	private String telephone;
	private String email;
	private String registrationDate;
	private String avatar;
	private ArrayList<Subforum> followedSubforums;
	private ArrayList<Topic> savedTopics;
	private ArrayList<Comment> savedComments;

	/**
	 * Any user
	 * @param id
	 */
	public User(long id) {
		this.userId = id;
		this.role = null;
		this.username = null;
		this.password = null;
		this.firstName = null;
		this.lastName = null;
		this.telephone = null;
		this.avatar = null;
	}
	/**
	 * Specific role user
	 * @param id
	 * @param role
	 */
	public User(long id, Role role) {
		this.userId = id;
		this.role = role;
		this.username = null;
		this.password = null;
		this.firstName = null;
		this.lastName = null;
		this.telephone = null;
		this.avatar = null;
	}
	/**
	 * Save user (generic ID field)
	 * @param id
	 * @param role
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param telephone
	 * @param avatar
	 * @param email 
	 * @param registrationDate 
	 */
	public User(Role role, String username, String password, String firstName, String lastName, String telephone,
			String avatar, String email, String registrationDate) {
		this.userId = count.incrementAndGet();
		this.role = role;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.telephone = telephone;
		this.email = email;
		this.registrationDate = registrationDate;
		this.avatar = avatar;
	}
	
	public User(long id, Role role, String username, String password, String firstName, String lastName, String telephone, String email, 
			String avatar) {
		this.userId = id;
		this.role = role;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.telephone = telephone;
		this.email = email;
		this.avatar = avatar;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public ArrayList<Subforum> getFollowedSubforums() {
		return followedSubforums;
	}

	public void setFollowedSubforums(ArrayList<Subforum> followedSubforums) {
		this.followedSubforums = followedSubforums;
	}

	public ArrayList<Topic> getSavedTopics() {
		return savedTopics;
	}

	public void setSavedTopics(ArrayList<Topic> savedTopics) {
		this.savedTopics = savedTopics;
	}

	public ArrayList<Comment> getSavedComments() {
		return savedComments;
	}

	public void setSavedComments(ArrayList<Comment> savedComments) {
		this.savedComments = savedComments;
	}

	@Override
	public String toString() {
		return userId + ";" + Utils.roleToString(role) + ";" + username + ";" + password + ";" + firstName + ";" + lastName + ";"
				+ telephone + ";" + email +  ";" + avatar;// + avatar==null?"":avatar
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	

}
