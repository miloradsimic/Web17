package beans;

import java.io.Serializable;

public class SubforumNewBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long subforum_id = -1;
	private String subforum_title;
	private String description;
	private String image;

	public long getSubforum_id() {
		return subforum_id;
	}

	public void setSubforum_id(long subforum_id) {
		this.subforum_id = subforum_id;
	}

	public String getSubforum_title() {
		return subforum_title;
	}

	public void setSubforum_title(String subforum_title) {
		this.subforum_title = subforum_title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
