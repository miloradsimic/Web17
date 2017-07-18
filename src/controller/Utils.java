package controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.enums.Role;
import model.enums.TopicType;

public class Utils {
	private static SimpleDateFormat dt = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss"); 

	public static Role stringToRole(String role){
		switch(role){
		case "ADMIN": return Role.ADMIN;
		case "MODERATOR": return Role.MODERATOR;
		case "USER": return Role.USER;
		default: return null;
		}
	}
	
	public static String roleToString(Role role){
		switch(role){
		case ADMIN: return "ADMIN";
		case MODERATOR: return "MODERATOR";
		case USER: return "USER";
		default: return null;
		}
	}
	
	public static TopicType stringToTopicType(String type){
		switch(type){
		case "TEXT": return TopicType.TEXT;
		case "IMAGE": return TopicType.IMAGE;
		case "LINK": return TopicType.LINK;
		default: return null;
		}
	}
	
	public static String topicTypeToString(TopicType type){
		switch(type){
		case TEXT: return "TEXT";
		case IMAGE: return "IMAGE";
		case LINK: return "LINK";
		default: return null;
		}
	}
	
	public static String formatDateToString(Date date){
		return dt.format(date);
	}
	
}
