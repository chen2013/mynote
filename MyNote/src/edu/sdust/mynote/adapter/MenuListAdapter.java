package edu.sdust.mynote.adapter;



import edu.sdust.mynote.LoginActivity;
import edu.sdust.mynote.MyApplication;
import edu.sdust.mynote.R;
import edu.sdust.mynote.bean.ViewHolder;
import edu.sdust.mynote.database.ListCount;
import edu.sdust.mynote.database.Lists;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @Description: TODO

 * @File: MenuListAdapter.java


 * @Version 
 */
public class MenuListAdapter extends BaseAdapter {
	
	
	
	private Context mContext;
 //	int [] listTextSrc = new int[]{R.string.ago,R.string.yesterday,
//			R.string.today,R.string.tomorrow,
//			R.string.future,R.string.more,
//			};
	static int listCnt;
	public static String[] listTextSrc;
	int [] listImageSrc = new int[]{R.drawable.navigation_classify_ico_science,R.drawable.navigation_classify_ico_social,
			R.drawable.navigation_classify_ico_entertainment,R.drawable.navigation_classify_ico_financial,
			R.drawable.navigation_classify_ico_sport,R.drawable.navigation_classify_ico_fashion,R.drawable.navigation_classify_ico_funny,
			R.drawable.navigation_classify_ico_creative,R.drawable.navigation_classify_ico_carefully_chosen
			};
	
	private LayoutInflater inflater;
	
	public MenuListAdapter(Context mContext){
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		

		//获得列表个数
		SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
		listCnt=preferences.getInt("listCount", 3);
		
		listTextSrc=new String[listCnt];		
		Log.v("listCount_menu",""+listCnt+",,"+listTextSrc.length);
		
		
		
		//从数据库中获取list数据
		Lists listsDB=new Lists(mContext);
		listsDB.open();
		Cursor c=listsDB.getAllLists();
		if(c.moveToFirst())
		{
			listTextSrc[0]=c.getString(1);
			for(int i=1;c.moveToNext();++i){
				listTextSrc[i]=c.getString(1);
			}
		}
	}
	

	public MenuListAdapter(){}

	@Override
	public int getCount() {
		return listTextSrc.length;
	}

	@Override
	public Object getItem(int arg0) {
		return listTextSrc[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.menu_item_list, null);
			viewHolder.ivSelected = (ImageView)convertView.findViewById(R.id.ivSelected);
			viewHolder.ivIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
			viewHolder.tvMenu = (TextView)convertView.findViewById(R.id.tvMenu);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvMenu.setText(listTextSrc[position]);
		viewHolder.ivIcon.setBackgroundResource(listImageSrc[position]);
		convertView.setTag(viewHolder);
		return convertView;
	}
}
