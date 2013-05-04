package edu.sdust.mynote.adapter;


import java.util.ArrayList;
import java.util.List;

import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.bean.Note;
import edu.sdust.mynote.pull.R;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter {

	private List<String> list_titles;
	private List<Integer> list_drawables;
	private List<Integer> va;
	private List<String> contents;
	private Context context;

	private int i = 0;
	private List<Note> list;
	private int id;
	public NoteListAdapter(Context context, List<String> titles,List<String> contents,
			List<Integer> drawables,List<Note> list) {
		if (titles == null) {
			this.list_titles = new ArrayList<String>();
		} else {
			this.list_titles = titles;
		}
		if (drawables == null) {
			this.list_drawables = new ArrayList<Integer>();
		}else {
			this.list_drawables = drawables;
		}
		if (contents == null) {
			this.contents = new ArrayList<String>();
		}
		else{
			this.contents=contents;
		}
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {

		return this.list_drawables.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.note_item_layout, null);
		TextView title = (TextView) view.findViewById(R.id.note_title);
		ImageView iv = (ImageView) view.findViewById(R.id.note_iv);
		TextView content = (TextView)view.findViewById(R.id.note_content);
	
		title.setText(this.list_titles.get(position));
		content.setText(this.contents.get(position));
		view.setTag(list.get(position));
		
		iv.setImageResource(this.list_drawables.get(position));
		return view;
	}

	public void exchange(int start, int end) {
		Integer drawable = this.list_drawables.get(start);
		String title = list_titles.get(start);
		String content = contents.get(start);
		Note org1 = list.get(start);
		
		list.remove(start);
		list.add(end, org1);
		
		list_drawables.remove(start);
		list_titles.remove(start);
		contents.remove(start);
		list_drawables.add(end, drawable);
		list_titles.add(end, title);
		contents.add(end,content);
		
		this.notifyDataSetChanged();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public class ViewData<R,T>
	{
		public R name;
		public T value;
		public ViewData(R arg1,T arg2){name = arg1;value = arg2;}
	}
}

