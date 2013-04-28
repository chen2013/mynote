package edu.sdust.mynote.adapter;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.pull.R;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DragListAdapter extends BaseAdapter {

	private List<String> list_titles;
	private List<Integer> list_drawables;
	private List<Integer> va;
	private Context context;

	private int i = 0;
	private List<Memo> list;
	private int id;
	public DragListAdapter(Context context, List<String> titles,
			List<Integer> drawables,List<Memo> list) {
		if (titles == null) {
			this.list_titles = new ArrayList<String>();
		} else {
			this.list_titles = titles;
		}
		if (drawables == null) {
			this.list_drawables = new ArrayList<Integer>();
		} else {
			this.list_drawables = drawables;
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
		View view = LayoutInflater.from(context).inflate(R.layout.item, null);
		TextView tv = (TextView) view.findViewById(R.id.tv);
		ImageView iv = (ImageView) view.findViewById(R.id.iv);
		ImageView iv_star = (ImageView) view.findViewById(R.id.iv_star);
	
		tv.setText(this.list_titles.get(position));
		view.setTag(list.get(position));
		if(list.get(position).getStarrted() == 0)
		{
			iv_star.setImageResource(R.drawable.star_important);
		}else if(list.get(position).getStarrted() == 1)
		{
			iv_star.setImageResource(R.drawable.star_normal);
		}else 
		{
			iv_star.setImageResource(R.drawable.star_nothing);
		}
		iv.setImageResource(this.list_drawables.get(position));
		return view;
	}

	public void exchange(int start, int end) {
		Integer drawable = this.list_drawables.get(start);
		String title = list_titles.get(start);
		Memo org1 = list.get(start);
		
		list.remove(start);
		list.add(end, org1);
		
		list_drawables.remove(start);
		list_titles.remove(start);
		list_drawables.add(end, drawable);
		list_titles.add(end, title);
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
