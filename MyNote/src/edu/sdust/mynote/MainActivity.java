package edu.sdust.mynote;



import java.util.List;

import edu.sdust.mynote.MyApplication;
import edu.sdust.mynote.MyLinearLayout.OnScrollListener;
import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RemoteViews.ActionException;

public class MainActivity extends ActivityGroup implements OnTouchListener,
		GestureDetector.OnGestureListener {
	
	
	static int listCnt;
	public static String[] title;
	
	
	public static final int SNAP_VELOCITY = 400;
	
	private VelocityTracker velocityTracker;
	
	private int velocityX;
	
	public static boolean isMenuOpen = false;
	
	private boolean hasMeasured = false;// �Ƿ�Measured.
	private LinearLayout layout_left;
	private LinearLayout layout_right;
	private ImageView iv_set;
	private ListView lv_set;
	private TextView app_label;

	private MyLinearLayout rightContent;
	
	private View listView ;
	
	private Builder builder;
	private String curList="";


	/** ÿ���Զ�չ��/�����ķ�Χ */
	private int MAX_WIDTH = 0;
	/** ÿ���Զ�չ��/�������ٶ� */
	private final static int SPEED = 30;

	private GestureDetector mGestureDetector;// ����
	public static boolean isScrolling = false;
	private float mScrollX; // ���黬������
	private int window_width;// ��Ļ�Ŀ��

	private String TAG = "edu.sdust.mynote";

	Intent intentView ;
	
	HttpPostRequest request = new HttpPostRequest();
	Lists list = new Lists(MyApplication.getInstance());
	
	
	/***
	 * ��ʼ��view
	 */
	void InitView() {
		
		
		
		//����б����
		SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
		listCnt=preferences.getInt("listCount", 3);
		
		title=new String[listCnt];		
		
		
		
		//�����ݿ��л�ȡlist����
		Lists listsDB=new Lists(MyApplication.getInstance());
		listsDB.open();
		Cursor c=listsDB.getAllLists();
		if(c.moveToFirst())
		{
			title[0]=c.getString(1);
			for(int i=1;c.moveToNext();++i){
				title[i]=c.getString(1);
			}
		}
		c.close();
		listsDB.close();
		
		intentView  = new Intent(MainActivity.this,StoreUpActivity.class);
		rightContent = (MyLinearLayout) findViewById(R.id.mylaout);
		
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		
		iv_set = (ImageView) findViewById(R.id.iv_set);
		lv_set = (ListView) findViewById(R.id.lv_set);
		
		lv_set.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_layout,
				R.id.tv_item, title));
		
		
		lv_set.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				rightContent.removeAllViews();
				
//				intentView.putExtra("position", position);
//				listView = getLocalActivityManager().startActivity("listView", intentView).getDecorView();
//				
				SharedPreferences pre = MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_WRITEABLE);
				Editor editor  = pre.edit();
				editor.putInt("lastPosition", position);
				editor.commit();
				
				dataBinding(position);
				
				Log.v("position click",""+ position);
				intentView.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);//���ﱣ֤StoreUpActivity�ǵ�����ģʽ
				
				listView = getLocalActivityManager().startActivity("listview", intentView).getDecorView();
				
				
				rightContent.addView(listView, 0);
				
				rightContent.bringChildToFront(listView);
				
				new AsynMove().execute(-SPEED);
			}
		});
		

//		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
//		int lastPosition = pref.getInt("lastPosition", 0);
//		if (lastPosition==0)
//			dataBinding(lastPosition);
//		listView = getLocalActivityManager().startActivity("listview", intentView).getDecorView();
//		
//		rightContent.removeAllViews();
//		rightContent.addView(listView, 0);
		
		
		//ע��setOnCreateContextMenuListener��������onContextItemSelected����ʹ�õ�ʵ�ֳ����ǵĲ˵�
		lv_set.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
		       public void onCreateContextMenu(ContextMenu menu, View v,
		              ContextMenuInfo menuInfo) {
		                  menu.add(0, 0, 0, "����");
		                  menu.add(0, 1, 0, "ɾ��");
		                  menu.add(0, 2, 0, "������");
		              }
		});

		

		rightContent.setOnScrollListener(new OnScrollListener() {

			@Override
			public void doScroll(float distanceX) {
				// TODO Auto-generated method stub
				if(isFinish)
					doScrolling(distanceX);
			}

			@Override
			public void doLoosen(boolean suduEnough) {
				
				doCloseScroll(suduEnough);
			}
		});
		iv_set.setOnTouchListener(this);
		layout_right.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this);
		// ���ó�������
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();
	}

	/***
	 * ��ȡ�ƶ����� �ƶ��ľ�����ʵ����layout_left�Ŀ��
	 */
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_right.getViewTreeObserver();
		// ��ȡ�ؼ����
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
							.getLayoutParams();
					layoutParams.width = window_width;
					layout_right.setLayoutParams(layoutParams);
					MAX_WIDTH = layout_left.getWidth();
					Log.v(TAG, "MAX_WIDTH=" + MAX_WIDTH + "width="
							+ window_width);
					hasMeasured = true;
				}
				return true;
			}
		});

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		
	   	InitView();
		mainFunction();

	}
	
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		InitView();
		mainFunction();
		
		
//		intentView.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);//���ﱣ֤StoreUpActivity�ǵ�����ģʽ
//		
//		intentView = new Intent(MainActivity.this,StoreUpActivity.class);
//		
//		listView = getLocalActivityManager().startActivity("listview", intentView).getDecorView();
//		
//		rightContent.removeView(listView);
//		rightContent.addView(listView, 0);
//		
//		rightContent.bringChildToFront(listView);
	}

	@Override
	public Activity getCurrentActivity() {
		// TODO Auto-generated method stub
		return super.getCurrentActivity();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {	
		
		Log.d("onTouch ", "onTouch "+event.getAction());
		
//		if(isScrolling && event.getAction() == MotionEvent.ACTION_UP){
//			
//			Log.e("onTouch ", "action up");
//			
//			doCloseScroll(false);
			
//		}

		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		mScrollX = 0;
		isScrolling = false;
		// ��֮��Ϊtrue����Ȼ�¼��������´���.
		
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.d("main ", "onShowPress...");
	}

	/***
	 * ����ɿ�ִ��
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		Log.d("main ", "onSingleTapUp...");
		
		if(isFinish){
		
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// ���ƶ�
			if (layoutParams.leftMargin >= MAX_WIDTH) {
				new AsynMove().execute(-SPEED);
			} else {
				// ���ƶ�
				new AsynMove().execute(SPEED);
			}
		}
		return true;
	}

	/***
	 * �������� ����һ�����ƶ�������һ����. distanceX=�����x-ǰ���x���������0��˵���������ǰ�����ұ߼����һ���
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.d("main ", "onScroll...");
		if(isFinish)
			doScrolling(distanceX);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.d("main ", "onLongPress...");
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int currentX = (int) e2.getX();
		
		int lastX = (int) e1.getX();
		
		Log.d("action up ", currentX +"");
		
		Log.d("isMenuOpen ", isMenuOpen +"");
		
		if(isMenuOpen){
			
			Log.d("currentX-lastX ", currentX-lastX +"����������  isScrolling "+isScrolling );
			
			if(!isScrolling && currentX-lastX>=0 ){
				
				return false;
			}
		}else {
			Log.d("currentX-lastX ", currentX-lastX +"С��������");
			
			if(!isScrolling && currentX-lastX<=0){
				
				return false;
				
			}
		}
		
		boolean suduEnough = false;	
		
		System.out.println("velocityX=" + velocityX);
		
		if (velocityX > MainActivity.SNAP_VELOCITY || velocityX < -MainActivity.SNAP_VELOCITY) {
			
			suduEnough = true;
			
		} else {
			
			suduEnough = false;
			
		}
		
		doCloseScroll(suduEnough);
		
		return false;
	}
	
	public static  boolean isFinish = true;
	
	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			isFinish = false;
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// ����
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// ������

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(Math.abs(params[0]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			isFinish = true;
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right.getLayoutParams();
			if(layoutParams.leftMargin >= MAX_WIDTH){
					isMenuOpen = true;
			}else{
				    isMenuOpen = false;
			}
			super.onPostExecute(result);
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// ���ƶ�
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], MAX_WIDTH);
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						- values[0], -MAX_WIDTH);
				Log.v(TAG, "�ƶ���" + layoutParams.leftMargin + "  rightMargin="
						+ layoutParams.rightMargin);
			} else {
				// ���ƶ�
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], 0);
				Log.v(TAG, "�ƶ���" + layoutParams.rightMargin);
			}
			layout_right.setLayoutParams(layoutParams);
			layout_left.invalidate();

		}

	}

	public void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:����Ϊ������Ϊ��
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		Log.d("scrolling...mScrollX", mScrollX + "");
		layoutParams.leftMargin -= mScrollX;
		layoutParams.rightMargin += mScrollX;
		Log.d("scrolling...leftmargin", layoutParams.leftMargin + "");
		if (layoutParams.leftMargin <= 0) {
			isScrolling = false;// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			layoutParams.leftMargin = 0;
			layoutParams.rightMargin = 0;

		} else if (layoutParams.leftMargin >= MAX_WIDTH) {
			// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			isScrolling = false;
			layoutParams.leftMargin = MAX_WIDTH;
		}
		layout_right.setLayoutParams(layoutParams);
		layout_left.invalidate();
	}
	
	public void doCloseScroll(boolean suduEnough){
		if(isFinish){
			Log.d("doCloseScroll...", "doCloseScroll");
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();

			int tempSpeed = SPEED;
			

			if(isMenuOpen){
				tempSpeed = -tempSpeed;
			}

			if (suduEnough  || (!isMenuOpen && (layoutParams.leftMargin > window_width / 2))
					|| (isMenuOpen && (layoutParams.leftMargin < window_width / 2))) {
				
				new AsynMove().execute(tempSpeed);
				
			} else {
				
				new AsynMove().execute(-tempSpeed);
				
			}

		}
	}
	
	
	
	//����������ߵķ��ذ����Ƿ��������Ҫ�޸ĳɺ�̨���У���������ѵĵĹ���
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			// �����˳��Ի���
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// ���öԻ������
			isExit.setTitle("ϵͳ��ʾ");
			// ���öԻ�����Ϣ
			isExit.setMessage("ȷ��Ҫ�˳���");
			// ���ѡ��ť��ע�����
			isExit.setButton("ȷ��", listener);
			isExit.setButton2("ȡ��", listener);
			// ��ʾ�Ի���
			isExit.show();

		}
		//���β˵���ť��Ϊʵ���Զ���˵���ť
		if(keyCode==KeyEvent.KEYCODE_MENU)
			return true;
		
		return false;
		
	}//end onKeyDown
    
    
	/**�����Ի��������button����¼�*/
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				System.exit(0);
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};	
	
	// �����˵���Ӧ����
	public boolean onContextItemSelected(MenuItem item) {
		 
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
		                                .getMenuInfo();
		final int pos = (int) info.position;// �����info.id��Ӧ�ľ������ݿ���rowId��ֵ
		                
		Log.v("chang an shijian ", ""+pos+1);
		 
		list.open();
        final Cursor cursor=list.getItem(pos+1);
        cursor.moveToFirst();
        final String curList = cursor.getString(0);
        final String create_time=cursor.getString(2);
        final int total = cursor.getInt(3);
        final String _class = cursor.getString(4);
        		        	               		        
		    switch (item.getItemId()) {
		                case 0:
		                        // �������
			                	Intent shareListIntent = new Intent(MainActivity.this,ShareListActivity.class);
			    				startActivity(shareListIntent);	
		                        break;
		 
		                case 1:
		                        // ɾ������
		                		
	        		        	int result =request.deleteList(curList);
        		        	
		        		        if (result==0){
		        		        	
		        		        	list.deleteItem(pos+1);
		        		        	
		        		        	SharedPreferences prefer = MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_WRITEABLE+Context.MODE_WORLD_READABLE);
		        		        	int listCount=prefer.getInt("listCount", 1);
		        		        	Editor editor = prefer.edit();
		        		        	editor.putInt("listCount", listCount-1);
		        		        	editor.commit();
		        		        	}
		        		        else if(result==11)
		        		        	Toast.makeText(MyApplication.getInstance(), "�ײ�������", Toast.LENGTH_LONG).show();
		        		        else
		        		        	Toast.makeText(MyApplication.getInstance(), "δ֪ԭ��ɾ��ʧ��", Toast.LENGTH_LONG).show();
		        		        cursor.close();
		        		        list.close();
		        		        InitView();
		                        break;
		 
		                case 2:
		                        // ����������
		                	 	AlertDialog builderCreate = null;
		                		builder = new AlertDialog.Builder(this);
		        				builder.setTitle("�б�������");
		        				final View modify = (View)getLayoutInflater().inflate(R.layout.modify_list_name_layout, null);
		        				builder.setView(modify);
		        				builder.setPositiveButton("�޸�", new DialogInterface.OnClickListener() {
		        					
		        					@Override
		        					public void onClick(DialogInterface arg0, int arg1) {
		        						// TODO Auto-generated method stub
		        						
		        						 EditText rename= (EditText)modify.findViewById(R.id.new_list_name);
		        						 String newName = rename.getText().toString();
		        						 
		        						 if(request.modifyListName(newName, curList)==0)
		        							 list.updateItem(pos+1, curList, newName, create_time, total, _class);

		        						 cursor.close();
		        	        		     list.close();
		        	        		     InitView();
		        						 Toast.makeText(MainActivity.this, "����ɹ�", 3000).show();
		        					}
		        				});
		        				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
		        					
		        					@Override
		        					public void onClick(DialogInterface dialog, int which) {
		        						// TODO Auto-generated method stub
		        						cursor.close();
		                		        list.close();
		                		        InitView();
		        					}
		        				});
		        				builderCreate =  builder.create();builderCreate.show();//�ӹ�����ȡ��dialog����

		                	
		                        break;
		 
		                default:
		                        break;
		                }
		                return super.onContextItemSelected(item);
		 
		        }

	
	
	//Ϊ�˴��븴�ã�onCreate and onResumeͬ������
    private void mainFunction() { 
        
    	
    	
    	
        TextView loginLabel=(TextView)this.findViewById(R.id.loginlabel);
      
        SharedPreferences preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String read_username = preferences.getString("username", "");
        String read_password = preferences.getString("password", "");
        if(read_username != "" && read_password != ""){
        	loginLabel.setText(read_username);
        }
        
        
        ImageView add_list_btn=(ImageView)findViewById(R.id.add_list_btn);
        add_list_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	
				Intent add_list_intent=new Intent(MainActivity.this,AddNewListActivity.class);
				startActivity(add_list_intent);
			}
        	
        }); 
          
    	/**�����Ի��������button����¼�*/
    	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    	{
    		public void onClick(DialogInterface dialog, int which)
    		{
    			switch (which)
    			{
    			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
    				System.exit(0);
    				break;
    			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
    				break;
    			default:
    				break;
    			}
    		}
    	};
    	
    	
    	//������event�¼�
    	ImageView add_event=(ImageView)findViewById(R.id.add_event_btn);
    	add_event.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addEventIntent =new Intent(MainActivity.this,AddNewEventActivity.class);
				startActivity(addEventIntent);
			}
    	});
    	
    	
    	TextView toFinish=(TextView)findViewById(R.id.to_finish);
    	toFinish.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toFinishIntent = new Intent(MainActivity.this,FinishActivity.class);
				startActivity(toFinishIntent);
			}		
    	});

    	ImageView shareList = (ImageView)findViewById(R.id.share_list);
    	shareList.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shareListIntent = new Intent(MainActivity.this,ShareListActivity.class);
				startActivity(shareListIntent);			
			}
    		
    	});
    	
    	ImageView setting = (ImageView)findViewById(R.id.main_setting);
    	setting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
				startActivity(settingIntent);
			}
    		
    	});
    	
	}
    
    private void dataBinding(int position) {
		// TODO Auto-generated method stub
    	list.open();
         Cursor cursor=list.getItem(position+1);
         cursor.moveToFirst();
         curList = cursor.getString(0);
         cursor.close();
         
         
         request.getAllEvent(curList);
         list.close();
	}
    
    
	

}