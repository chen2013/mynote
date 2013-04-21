package edu.sdust.mynote;



import edu.sdust.mynote.MyApplication;
import edu.sdust.mynote.MyLinearLayout.OnScrollListener;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.pull.PullToRefreshExpandableListActivity;
import edu.sdust.mynote.pull.PullToRefreshGridActivity;
import edu.sdust.mynote.pull.PullToRefreshListActivity;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

	private MyLinearLayout rightContent;
	
	private View listView ;


	/** ÿ���Զ�չ��/�����ķ�Χ */
	private int MAX_WIDTH = 0;
	/** ÿ���Զ�չ��/�������ٶ� */
	private final static int SPEED = 30;

	private GestureDetector mGestureDetector;// ����
	public static boolean isScrolling = false;
	private float mScrollX; // ���黬������
	private int window_width;// ��Ļ�Ŀ��

	private String TAG = "edu.sdust.mynote";

	Intent [] intentView ;
	
	/***
	 * ��ʼ��view
	 */
	void InitView() {
		
		
		//����б����
		SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
		listCnt=preferences.getInt("listCount", 3);
		
		title=new String[listCnt];		
		Log.v("listCount_menu",""+listCnt+",,"+title.length);
		
		
		
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
		
		intentView  = new Intent []{
				new Intent(MainActivity.this,PullToRefreshListActivity.class),
				new Intent(MainActivity.this,PullToRefreshGridActivity.class),
				new Intent(MainActivity.this,PullToRefreshExpandableListActivity.class),
				};
		
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		iv_set = (ImageView) findViewById(R.id.iv_set);
		lv_set = (ListView) findViewById(R.id.lv_set);
		lv_set.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tv_item, title));
		lv_set.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				rightContent.removeAllViews();
				switch (position) {
				case 0:
					
					listView = getLocalActivityManager().startActivity("listview", intentView[0]).getDecorView();
					
					break;
				case 1:
					
					listView = getLocalActivityManager().startActivity("listview", intentView[1]).getDecorView();
					
					break;
				case 2:
					
					listView = getLocalActivityManager().startActivity("listview", intentView[2]).getDecorView();
					
					break;

				default:
					break;
				}
				rightContent.addView(listView, 0);
				
				rightContent.bringChildToFront(listView);
				
				new AsynMove().execute(-SPEED);
			}
		});

		rightContent = (MyLinearLayout) findViewById(R.id.mylaout);
		
		listView = getLocalActivityManager().startActivity("listview", intentView[0]).getDecorView();
		
		rightContent.removeAllViews();
		
		rightContent.addView(listView, 0);

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
		layout_right.setOnTouchListener(this);
		iv_set.setOnTouchListener(this);
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
		setContentView(R.layout.main);
		

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

		mainFunction();
		
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
	
	
	//Ϊ�˴��븴�ã�onCreate and onResumeͬ������
    private void mainFunction() { 
        
    	InitView();
    	
        TextView loginBtn=(TextView)this.findViewById(R.id.loginlabel);
        
        SharedPreferences preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String read_username = preferences.getString("username", "");
        String read_password = preferences.getString("password", "");
        if(read_username != "" && read_password != ""){
        	loginBtn.setText(read_username);
        }
        else{
        	loginBtn.setText("�û���½");
        }
        
        loginBtn.setOnClickListener(new OnClickListener(){
        	
        	@Override
        	public void onClick(View v){
        		Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        		startActivity(loginIntent);
        	}
        });
        
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

	

}