package edu.sdust.mynote.adapter;


import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.pull.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
public class MyDragListView extends ListView {

	// window���ڹ�����
	private WindowManager mWindowManager;
	// ������ק�����ʾ����
	private WindowManager.LayoutParams mLayoutParams;
	// ��չ���item,��ʵ����һ��ImageView
	private ImageView dragImageView;
	// ��ָ�϶���(item)ԭʼ���б��е�λ��
	private int dragSrcPosition;
	// ��ָ���׼���϶���ʱ�򣬵�ǰ�϶������б��е�λ��
	private int dragPosition;
	// �ڵ�ǰ�������λ��
	private int dragPoint;
	// �Ե�ǰ��ͼ����Ļ�ľ���(����ʹ��y������)
	private int dragOffset;
	// �϶���ʱ�򣬿�ʼ���Ϲ����ı߽�
	private int upScollBounce;
	// �϶���ʱ�򣬿������¹����ı߽�
	private int downScrollBounce;
	// ListView��������
	private final static int step = -1;
	// ��ǰ����
	private int current_step;
	// ��ʱitem������ֵ
	private int ItemIndex;

	private boolean isLongPress = false;

	public boolean isLongClick = false;
	private GestureDetector gestureDetecotr;

	public MotionEvent touchEvent = null;
	
	private ViewGroup itemView = null;
	public MyDragListView(Context context) {
		super(context);

	}

	public MyDragListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDetecotr = new GestureDetector(new LearnGestureListener());

	}

	public MyDragListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/**
	 * event.getX()��event.getRawX()������ getX()�Ǳ�ʾWidget������������Ͻǵ�x����,
	 * ��getRawX()�Ǳ�ʾ�������Ļ���Ͻǵ�x����ֵ getX()�Ǳ�ʾWidget������������Ͻ�x����(���������鿴Դ��)
	 * getRawX()�Ǳ�ʾ�������Ļ���Ͻ����Ͻ�,����activity�Ƿ���titleBar�����Ƿ�ȫ��Ļ
	 * 
	 */

	class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.v("debug", "onLongPress");
			isLongPress = true;
			super.onLongPress(e);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// ����

		Log.v("debug", "intercept");
		// gestureDetecotr.onTouchEvent(ev);
		if(isLongClick)
		{
			return false;
		}
		touchEvent = ev;
		return super.onInterceptTouchEvent(ev);
	}

	
	public void startDragSet()
	{
		if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
			// ��ȡ���µ�x����(����ڸ�����)
			int x = (int) touchEvent.getX();
			// ��ȡ���µ�y����(����ڸ�����)
			int y = (int) touchEvent.getY();
			/**
			 * AbsListView������pointToPosition(x, y)��������x,y������
			 * ������������Ӧ��ѡ����ĺ��룬���԰������Ÿ�ԭ����Դ�����dragSrcPosition�� �������Ÿ���ǰ���϶���
			 */
			ItemIndex = dragPosition = dragSrcPosition = this.pointToPosition(
					x, y);
			// ��Ч������
			
			// ��ȡ��ǰλ�õ���ͼ(�ɼ�״̬)
			/**
			 * ����Ҫ��˵��getChildAt�Ƿ���*��ʾ*(��ǰ��ʾ����)�ڵ�ǰViewGroup�������item
			 * viewGroup.getFirstVisiblePosition()�ǵ�ǰ��ʾҳ���е�һ��item��������Դ��position
			 * ���ڵ�ǰ��Ļ��һ���ǵ�һ����Ļ���Ա�����dragPoint
			 * -this.getFirstVisiblePosition()�ǵ�ǰҳ��ĵڼ���item ����
			 * dragPoint��50����this
			 * .getFirstVisiblePosition��45(��ǰ���Կ����ĵ�һ��item����45),��ѡ�е�
			 * (50)���ǵ�ǰ����ĵ����item
			 */
			 itemView = (ViewGroup) this.getChildAt(dragPosition
					- this.getFirstVisiblePosition());
			/**
			 * view.getTop()�������ص���view����ڸ����������ľ��룬��λ�����ء� ��itemView�ĸ�������listview
			 */
			dragPoint = y - itemView.getTop();
			dragOffset = (int) (touchEvent.getRawY() - y);
			// ��ȡ����ק��ͼ��
			View dragger = itemView.findViewById(R.id.iv);
			// x>dragger.getLeft()-20��仰Ϊ�˸��õĴ���(-20����ʡ��)
			if (dragger != null && x > dragger.getLeft() - 20) {
				// ȡ�����Ϲ����ı߽磬���Ϊ�ÿؼ���1/3
				upScollBounce = this.getHeight() / 3;
				// ȡ�����¹����ı߽磬���Ϊ�ÿؼ���2/3
				downScrollBounce = this.getHeight() * 2 / 3;
				itemView.setBackgroundColor(Color.BLUE);
				// ����cache
				itemView.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache(true));
				// ��ʼ��Ӱ��
				startDrag(bm, y);
			}

		}
	}
	private void startDrag(Bitmap bm, int y) {
		// ��ʼ��window
		mLayoutParams = new WindowManager.LayoutParams();
		// ���ö�����
		mLayoutParams.gravity = Gravity.TOP;
		mLayoutParams.x = 0;
		mLayoutParams.y = y - dragPoint + dragOffset;
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // ����Ҫ����
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE // ����Ҫ�Ӵ��¼�
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON // �����豸����������֤�߶Ȳ���
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN; // ����ռ��������Ļ��������Χװ�α߿�(����߽�򣬴˴��ڿ��ǵ�װ�α߿������)
		// ������ʹ�õĶ�������
		mLayoutParams.windowAnimations = 0;
		ImageView iv = new ImageView(this.getContext());
		iv.setImageBitmap(bm);
		mWindowManager = (WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE);
		mWindowManager.addView(iv, mLayoutParams);
		dragImageView = iv;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.v("debug",ev.toString());
		if (dragImageView != null
				&& dragPosition != AdapterView.INVALID_POSITION && isLongClick) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				Log.v("debug","action_up");
				int upY = (int) ev.getY();
				// ���Ӱ��
				stopDrag();
				// ����˵�����ﲻ���ٴν����滻�ˡ���Ϊ�Ѿ��滻�����
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) ev.getY();
				// ����Ӱ���ƶ�
				onDrag(moveY);
				break;
			case MotionEvent.ACTION_DOWN:
				break;
			default:
				break;
			}
			return true;
		}
		
		return super.onTouchEvent(ev);
	}

	/**
	 * �϶�ִ���߼�
	 * 
	 * @param y
	 */
	private void onDrag(int y) {
		// ��ק��item��ֵ����С��0�����С��0˵��������Ч����
		int drag_top = y - dragPoint;
		if (dragImageView != null && drag_top >= 0) {
			// ����͸����
			mLayoutParams.alpha = 0.5f;
			// �ƶ�yֵ//�ǵ�Ҫ����dragOffset,windowManager�Ǽ���������Ļ��
			mLayoutParams.y = y - dragPoint + dragOffset;
			mWindowManager.updateViewLayout(dragImageView, mLayoutParams);
		}
		// �����϶����ڷָ��ߵ�ʱposition����-1
		int tempPosition = this.pointToPosition(0, y);
		if (tempPosition != AdapterView.INVALID_POSITION) {
			dragPosition = tempPosition;
		}
		this.update(dragPosition);
		doScroll(y);
	}

	private void update(int position) {
		DragListAdapter adapter = (DragListAdapter) this.getAdapter();
		if (position < adapter.getCount() && dragImageView != null) {
			if (ItemIndex != position) {
				adapter.exchange(ItemIndex, position);
				ItemIndex = position;
			}
		}
	}

	private void doScroll(int y) {
		if (y < upScollBounce) {
			current_step = step + (upScollBounce - y) / 10;
		} else if (y > downScrollBounce) {
			current_step = -(step + (y - downScrollBounce)) / 10;
		} else {
			current_step = 0;
		}
		// ��ȡ����ק����λ�ü���ʾitem���Ӧ��view��
		View view = this.getChildAt(dragPosition
				- this.getFirstVisiblePosition());
		// �����Ĺ�������setSelectionFromTop
		this.setSelectionFromTop(dragPosition, view.getTop() + current_step);
	}

	/**
	 * �϶����µ�ʱ��
	 * 
	 * @param y
	 */
	private void onDrog(int y) {
		// Ϊ�˱��⻬�����ָ��ߵ�ʱ�򣬷���-1��ʱ��
		int tempPosition = this.pointToPosition(0, y);
		if (tempPosition != AdapterView.INVALID_POSITION) {
			dragPosition = tempPosition;
		}
		// �����߽紦��
		if (y < this.getChildAt(0).getTop()) {
			// �����ϱ߽�
			dragPosition = 0;
		} else if (y > this.getChildAt(this.getChildCount() - 1).getTop()) {
			dragPosition = this.getChildCount() - 1;
		}
		// ���ݽ���
		if (dragPosition < this.getAdapter().getCount()) {
			DragListAdapter adapter = (DragListAdapter) this.getAdapter();
			adapter.exchange(dragSrcPosition, dragPosition);
		}

	}

	/**
	 * ֹͣ�϶���ɾ��Ӱ��
	 */
	private void stopDrag() {
		if (dragImageView != null) {
			mWindowManager.removeView(dragImageView);
			dragImageView = null;
			isLongClick = false;
			ImageView iv_star= (ImageView)findViewById(R.id.iv_star);
			
			 Memo memo = ((Memo)itemView.getTag());
			 if(memo.getStarrted() == 1)
			 {
				 iv_star.setBackgroundResource(R.drawable.star_important);
			 }else if(memo.getStarrted() == 2)
			 {
				 iv_star.setBackgroundResource(R.drawable.star_normal);
			 }else 
			 {
				 iv_star.setBackgroundResource(R.drawable.star_nothing);
			 }
			Log.v("debug", "stopDrag");
		}

	}

}
