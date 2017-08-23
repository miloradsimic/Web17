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

	public Users(String path) {
		this.rootPath = path;
		BufferedReader in = null;
		try {
			File file = new File(path + "/data/users.txt");
			in = new BufferedReader(new FileReader(file));
			readUsers(in);
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
	
	public void addUser(User entry) {
	
		usersList.add(entry);
		usersMap.put(entry.getUserId(), entry);
		usersMapByUsername.put(entry.getUsername(), entry);
	}

	/**
	 * Cita korisnike iz datoteke i smesta ih u asocijativnu listu korisnika.
	 * Kljuc je username.
	 */
	private void readUsers(BufferedReader in) {
		String line, id = "", role = "", username = "", password = "", firstName = "", lastName = "", telephone = "",
				email = "", avatar = "";
		StringTokenizer st;
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					role = st.nextToken().trim();
					username = st.nextToken().trim();
					password = st.nextToken().trim();
					firstName = st.nextToken().trim();
					lastName = st.nextToken().trim();
					telephone = st.nextToken().trim();
					email = st.nextToken().trim();
					if (st.hasMoreTokens()) {
						avatar = st.nextToken().trim();
					}
				}

				long idNumber = -1;

				try {
					idNumber = Long.parseLong(id);
				} catch (NumberFormatException e) {
					System.out.println("Can't parse comments attribute");
					e.printStackTrace();
				}
				User user = new User(idNumber, Utils.stringToRole(role), username, password, firstName, lastName,
						telephone, email, avatar);
				usersList.add(user);
				usersMap.put(idNumber, user);
				usersMapByUsername.put(username, user);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
