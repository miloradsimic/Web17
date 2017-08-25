package beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import controller.Utils;
import model.enums.Role;

public class Users implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String pathToFile = "/data/users.txt";
	private String rootPath;
	private ArrayList<User> usersList = new ArrayList<>();
	private HashMap<Long, User> usersMap = new HashMap<Long, User>();
	private HashMap<String, User> usersMapByUsername = new HashMap<String, User>();

	public ArrayList<User> getUsers() {
		return usersList;
	}

	public void setUsers(ArrayList<User> users) {
		this.usersList = users;
	}

	public HashMap<Long, User> getUsersMap() {
		return usersMap;
	}

	public void setUsersMap(HashMap<Long, User> usersMap) {
		this.usersMap = usersMap;
	}

	public HashMap<String, User> getUsersMapByUsername() {
		return usersMapByUsername;
	}

	public void setUsersMapByUsername(HashMap<String, User> usersMapByUsername) {
		this.usersMapByUsername = usersMapByUsername;
	}

	public Users() {

	}
	
	public void addUser(User entry) {
	
		usersList.add(entry);
		usersMap.put(entry.getUserId(), entry);
		usersMapByUsername.put(entry.getUsername(), entry);
	}

	public boolean updateUser(User user) {

		usersList.remove(user);
		usersMap.remove(user.getUserId());
		usersMapByUsername.remove(user.getUsername());
		usersList.add(user);
		usersMap.put(user.getUserId(), user);
		usersMapByUsername.put(user.getUsername(), user);

		return true;
	}
}
