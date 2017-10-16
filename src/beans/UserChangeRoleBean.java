package beans;

import java.io.Serializable;

public class UserChangeRoleBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long userId;
	private int value;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
