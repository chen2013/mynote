package edu.sdust.mynote.function;

public class DealWithString {

	public String strToJson(String string) {
		// TODO Auto-generated method stub
		int appear = string.indexOf("{");
		
		String str2 = string.substring(appear);
		return str2;
	} 
}
