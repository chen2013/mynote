package edu.sdust.mynote.bean;

public class Note{
	
	private String note_id ;
	private String note_content ;
	private String note_title;
	private Long note_created_time;
	
	
	public String getNote_id() {
		return note_id;
	}
	public void setNote_id(String noteId) {
		note_id = noteId;
	}
	public String getNote_content() {
		return note_content;
	}
	public void setNote_content(String noteContent) {
		note_content = noteContent;
	}
	public String getNote_title() {
		return note_title;
	}
	public void setNote_title(String noteTitle) {
		note_title = noteTitle;
	}
	public Long getNote_created_time() {
		return note_created_time;
	}
	public void setNote_created_time(Long noteCreatedTime) {
		note_created_time = noteCreatedTime;
	}
	
	
}

