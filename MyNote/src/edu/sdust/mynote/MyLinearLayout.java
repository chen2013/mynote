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
 * 自定义布局文件.
 * 
 * @author zhangjia
 * 
 */
public class MyLinearLayout extends LinearLayout {
	
	private VelocityTracker velocityTracker;
	
	private int velocityX;
	
	private GestureDetector mGestureDetector;
	
	View.OnTouchListener mGestureListener;

	private boolean isLock = false;// 左右移动锁.
	
	private boolean isNeedOpen = false;// 左右移动锁.

	public OnScrollListener onScrollListener;// 自定义滑动接口

	private boolean b;// 拦截touch标识

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
	 * 事件分发
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		

		b = mGestureDetector.onTouchEvent(ev);// 获取手势返回值.
		
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
		 * 松开时记得处理缩回...
		 */
		if (ev.getAction() == MotionEvent.ACTION_UP && isNeedOpen) {
			
			currentX = ev.getX();
			
			Log.d("action up ", currentX +"");
			
			Log.d("isMenuOpen ", MainActivity.isMenuOpen +"");
			
			if(MainActivity.isMenuOpen){
				
				Log.d("currentX-lastX ", currentX-lastX +"大于零往右  MainActivity.isScrolling "+MainActivity.isScrolling );
				
				if(!MainActivity.isScrolling && currentX-lastX>=0 ){
					
					return super.dispatchTouchEvent(ev);
				}
			}else {
				Log.d("currentX-lastX ", currentX-lastX +"小于零往左");
				
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
	 * 事件拦截处理
	 * 
	 * 要明白机制，如果返回ture的话，那就是进行拦截，处理自己的ontouch. 返回false的话，那么就会向下传递...
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return b;
	}

	/***
	 * 事件处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		isLock = false;
		return super.onTouchEvent(event);
	}

	/***
	 * 自定义手势执行
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

			// 垂直大于水平
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
	 * 自定义接口 实现滑动...
	 * 
	 * @author zhangjia
	 * 
	 */
	public interface OnScrollListener {
		void doScroll(float distanceX);// 滑动...

		void doLoosen(boolean suduEnough);// 手指松开后执行...
	}

}
