package edu.sdust.mynote.bean;





public class User {
	

	private  String is_logon;
	
	public User(){}
	public User(String is_logon) {

		this.setIs_logon(is_logon);
	}
	
	public void setIs_logon(String is_logon) {
		this.is_logon = is_logon;
	}
	
	public String getIs_logon() {
		return is_logon;
	}
	
}
