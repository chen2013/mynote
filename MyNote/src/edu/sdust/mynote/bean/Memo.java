package edu.sdust.mynote.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Memo{
	private String item_id;
	private String item_content;
	private Long create_time;
	private int starrted;
	private Long due_date;
	private int completed;
	private int repeat_type;

	public String getItem_content() {
		return item_content;
	}



	public void setItem_content(String itemContent) {
		item_content = itemContent;
	}



	public String getItem_id() {
		return item_id;
	}



	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}



	public int getStarrted() {
		return starrted;
	}



	public void setStarrted(int starrted) {
		this.starrted = starrted;
	}



	public Long getDue_date() {
		return due_date;
	}



	public void setDue_date(Long dueDate) {
		due_date = dueDate;
	}



	public Long getCreate_time() {
		return create_time;
	}



	public void setCreate_time(Long createTime) {
		create_time = createTime;
	}



	public int getCompleted() {
		return completed;
	}



	public void setCompleted(int completed) {
		this.completed = completed;
	}



	public int getRepeat_type() {
		return repeat_type;
	}



	public void setRepeat_type(int repeatType) {
		repeat_type = repeatType;
	}



	public class Date
	{
		public Date(int year,int month,int day,int hour,int min,int sec)
		{
			this.year = year;
			this.month = month;
			this.day = day;
			this.hour = hour;
			this.min = min;
			this.sec = sec;
		}
		public int year;
		public int month;
		public int day;
		public int hour;
		public int min;
		public int sec;
	}
	
}
