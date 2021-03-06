package beans;

import java.io.Serializable;

public class UserPublicBean implements Serializable {

	private long userId;
	private String username;
	private String firstName;
	private String lastName;
	private String avatar;
	private String role;

	public UserPublicBean(long userId, String role, String username, String firstName, String lastName, String avatar) {
		super();
		this.userId = userId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
		this.role = role;
	}

	public UserPublicBean() {
		// TODO Auto-generated constructor stub
	}

	public UserPublicBean(User user) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.avatar = user.getAvatar();
		this.role = user.getRole().toString().toLowerCase();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
