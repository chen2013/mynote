package edu.sdust.mynote;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.LinearLayout;
import android.widget.ListView;

/***
 * �Զ��岼���ļ�.
 * 
 * @author zhangjia
 * 
 */
public class MyLinearLayout extends LinearLayout {
	
	private VelocityTracker velocityTracker;
	
	private int velocityX;
	
	private GestureDetector mGestureDetector;
	
	View.OnTouchListener mGestureListener;

	private boolean isLock = false;// �����ƶ���.
	
	private boolean isNeedOpen = false;// �����ƶ���.

	public OnScrollListener onScrollListener;// �Զ��廬���ӿ�

	private boolean b;// ����touch��ʶ

	public MyLinearLayout(Context context) {
		super(context);
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new MySimpleGesture());

	}
	float lastX = 0, currentX = 0;
	
	float lastY = 0, currentY = 0;

	/***
	 * �¼��ַ�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		

		b = mGestureDetector.onTouchEvent(ev);// ��ȡ���Ʒ���ֵ.
		
		if(ev.getAction() == MotionEvent.ACTION_DOWN ){
			
			lastX = ev.getX();
			
			Log.d("action down ", lastX +"");
			
		}
		
		if(ev.getAction() == MotionEvent.ACTION_MOVE ){
			
			if (velocityTracker == null) {
				
				velocityTracker = VelocityTracker.obtain();
			}
			velocityTracker.addMovement(ev);
			
		}
		/***
		 * �ɿ�ʱ�ǵô�������...
		 */
		if (ev.getAction() == MotionEvent.ACTION_UP && isNeedOpen) {
			
			currentX = ev.getX();
			
			Log.d("action up ", currentX +"");
			
			Log.d("isMenuOpen ", MainActivity.isMenuOpen +"");
			
			if(MainActivity.isMenuOpen){
				
				Log.d("currentX-lastX ", currentX-lastX +"����������  MainActivity.isScrolling "+MainActivity.isScrolling );
				
				if(!MainActivity.isScrolling && currentX-lastX>=0 ){
					
					return super.dispatchTouchEvent(ev);
				}
			}else {
				Log.d("currentX-lastX ", currentX-lastX +"С��������");
				
				if(!MainActivity.isScrolling && currentX-lastX<=0){
					
					return super.dispatchTouchEvent(ev);
					
				}
			}
			
			boolean suduEnough = false;
			
			final VelocityTracker tempVelocityTracker = velocityTracker;
			
			tempVelocityTracker.computeCurrentVelocity(1000);
			
			velocityX = (int) tempVelocityTracker.getXVelocity();
			
			System.out.println("velocityX=" + velocityX);
			
			if (velocityX > MainActivity.SNAP_VELOCITY || velocityX < -MainActivity.SNAP_VELOCITY) {
				
				suduEnough = true;
				
			} else {
				
				suduEnough = false;
				
			}

			if (velocityTracker != null) {
				
				velocityTracker.recycle();
				
				velocityTracker = null;
			}
			onScrollListener.doLoosen(suduEnough);
		}
		return super.dispatchTouchEvent(ev);
	}

	/***
	 * �¼����ش���
	 * 
	 * Ҫ���׻��ƣ��������ture�Ļ����Ǿ��ǽ������أ������Լ���ontouch. ����false�Ļ�����ô�ͻ����´���...
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return b;
	}

	/***
	 * �¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		isLock = false;
		return super.onTouchEvent(event);
	}

	/***
	 * �Զ�������ִ��
	 * 
	 * @author zhangjia
	 * 
	 * 
	 */
	class MySimpleGesture extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			
			isLock = true;
			
			return super.onDown(e);
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.d("onScroll", distanceX+"");
			if (!isLock)
				onScrollListener.doScroll(distanceX);

			// ��ֱ����ˮƽ
			if (Math.abs(distanceY) > Math.abs(distanceX) && !MainActivity.isScrolling && !MainActivity.isMenuOpen) {
				isNeedOpen = false;
				return false;
			} else {
				
				isNeedOpen = true;
				
				return true;
			}

		}
	}

	/***
	 * �Զ���ӿ� ʵ�ֻ���...
	 * 
	 * @author zhangjia
	 * 
	 */
	public interface OnScrollListener {
		void doScroll(float distanceX);// ����...

		void doLoosen(boolean suduEnough);// ��ָ�ɿ���ִ��...
	}

}
