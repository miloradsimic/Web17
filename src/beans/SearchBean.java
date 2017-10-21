package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String text;
	private List<String> fields;
	
	public SearchBean() {
		super();
		this.fields = new ArrayList<>();
	}
	
	public SearchBean(String text, List<String> fields) {
		super();
		this.text = text;
		this.fields = fields;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
