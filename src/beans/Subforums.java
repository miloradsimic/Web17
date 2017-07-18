package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import model.enums.Role;

public class Subforums implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Subforum> subforumsList = new ArrayList<>();
	private HashMap<Long, Subforum> subforumsMap = new HashMap<>();
	
	public Subforums() {
		super();
	}
	
	public Subforums(String path) {
		
		BufferedReader in = null;
		try {
			File file = new File(path + "/data/subforums.txt");
			in = new BufferedReader(new FileReader(file));
			readSubforums(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	/**
	 * Cita subforume iz datoteke i smesta ih u asocijativnu listu subforuma.
	 * Kljuc je naziv subforuma.
	 */
	private void readSubforums(BufferedReader in) {
		String line, id = "", name = "", description = "", icon = "", rules = "", moderatorId = "";
		StringTokenizer st;
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					name = st.nextToken().trim();
					description = st.nextToken().trim();
					icon = st.nextToken().trim();
					rules = st.nextToken().trim();
					moderatorId = st.nextToken().trim();
				}
				long idNumber = -1, moderatorIdNumber = -1;
				try {
					idNumber = Long.parseLong(id);
					moderatorIdNumber = Long.parseLong(moderatorId);
				} catch (NumberFormatException e) {
					System.out.println("Can't parse one subforums attribute");
					e.printStackTrace();
				}
				Subforum entry = new Subforum(idNumber, name, description, icon, rules, new User(moderatorIdNumber, Role.MODERATOR));
				subforumsList.add(entry);
				subforumsMap.put(idNumber, entry);
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public HashMap<Long, Subforum> getSubforumsMap() {
		return subforumsMap;
	}
	public void setSubforumsMap(HashMap<Long, Subforum> subforumsMap) {
		this.subforumsMap = subforumsMap;
	}
	public ArrayList<Subforum> getSubforumsList() {
		return subforumsList;
	}
	public void setSubforumsList(ArrayList<Subforum> subforumsList) {
		this.subforumsList = subforumsList;
	}
	
	

}
