package beans;
import java.util.ArrayList;

public class Subforum {
	
	private long subforumId;
	private String name;
	private String description;
	private String icon;
	private String rules;
	private User mainModerator;
	private ArrayList<User> moderators;
	
	public Subforum(long subforumId) {
		super();
		this.subforumId = subforumId;
		this.name = null;
		this.description = null;
		this.icon = null;
		this.rules = null;
		this.mainModerator = null;
	}
	
	public Subforum(long subforumId, String name, String description, String icon, String rules, User mainModerator) {
		super();
		this.subforumId = subforumId;
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.rules = rules;
		this.mainModerator = mainModerator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public User getMainModerator() {
		return mainModerator;
	}

	public void setMainModerator(User mainModerator) {
		this.mainModerator = mainModerator;
	}

	public ArrayList<User> getModerators() {
		return moderators;
	}

	public void setModerators(ArrayList<User> moderators) {
		this.moderators = moderators;
	}

	public long getSubforumId() {
		return subforumId;
	}

	public void setSubforumId(long subforumId) {
		this.subforumId = subforumId;
	}
	
	
	
	
	
	
}
