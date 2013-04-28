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

	// window窗口管理类
	private WindowManager mWindowManager;
	// 控制拖拽项的显示参数
	private WindowManager.LayoutParams mLayoutParams;
	// 拓展想的item,其实就是一个ImageView
	private ImageView dragImageView;
	// 手指拖动项(item)原始在列表中的位置
	private int dragSrcPosition;
	// 手指点击准备拖动的时候，当前拖动项在列表中的位置
	private int dragPosition;
	// 在当前数据项的位置
	private int dragPoint;
	// 以当前视图和屏幕的距离(这里使用y方向上)
	private int dragOffset;
	// 拖动的时候，开始向上滚动的边界
	private int upScollBounce;
	// 拖动的时候，开箱向下滚动的边界
	private int downScrollBounce;
	// ListView滑动步伐
	private final static int step = -1;
	// 当前步伐
	private int current_step;
	// 临时item的索引值
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
	 * event.getX()和event.getRawX()的区别 getX()是表示Widget相对于自身左上角的x坐标,
	 * 而getRawX()是表示相对于屏幕左上角的x坐标值 getX()是标示Widget相对于自身左上角x坐标(如果不懂请查看源码)
	 * getRawX()是标示相对于屏幕左上角左上角,不管activity是否又titleBar或者是否全屏幕
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
		// 按下

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
			// 获取按下的x坐标(相对于父容器)
			int x = (int) touchEvent.getX();
			// 获取按下的y坐标(相对于父容器)
			int y = (int) touchEvent.getY();
			/**
			 * AbsListView里面有pointToPosition(x, y)方法根据x,y轴坐标
			 * 返回这个坐标对应的选中项的号码，所以把这个序号给原数据源的序号dragSrcPosition， 把这个序号给当前是拖动项
			 */
			ItemIndex = dragPosition = dragSrcPosition = this.pointToPosition(
					x, y);
			// 无效不处理
			
			// 获取当前位置的视图(可见状态)
			/**
			 * 这里要先说明getChildAt是返回*显示*(当前显示界面)在当前ViewGroup中坐标的item
			 * viewGroup.getFirstVisiblePosition()是当前显示页面中第一个item的在数据源的position
			 * 由于当前屏幕不一定是第一个屏幕所以必须用dragPoint
			 * -this.getFirstVisiblePosition()是当前页面的第几个item 比如
			 * dragPoint是50，而this
			 * .getFirstVisiblePosition是45(当前可以看到的第一个item就是45),你选中的
			 * (50)就是当前界面的第五个item
			 */
			 itemView = (ViewGroup) this.getChildAt(dragPosition
					- this.getFirstVisiblePosition());
			/**
			 * view.getTop()方法返回的是view相对于父容器顶部的距离，单位是像素。 而itemView的父容器是listview
			 */
			dragPoint = y - itemView.getTop();
			dragOffset = (int) (touchEvent.getRawY() - y);
			// 获取可拖拽的图标
			View dragger = itemView.findViewById(R.id.iv);
			// x>dragger.getLeft()-20这句话为了更好的触摸(-20可以省略)
			if (dragger != null && x > dragger.getLeft() - 20) {
				// 取得向上滚动的边界，大概为该控件的1/3
				upScollBounce = this.getHeight() / 3;
				// 取得向下滚动的边界，大概为该控件的2/3
				downScrollBounce = this.getHeight() * 2 / 3;
				itemView.setBackgroundColor(Color.BLUE);
				// 开启cache
				itemView.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache(true));
				// 初始化影像
				startDrag(bm, y);
			}

		}
	}
	private void startDrag(Bitmap bm, int y) {
		// 初始化window
		mLayoutParams = new WindowManager.LayoutParams();
		// 设置定布局
		mLayoutParams.gravity = Gravity.TOP;
		mLayoutParams.x = 0;
		mLayoutParams.y = y - dragPoint + dragOffset;
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不需要焦点
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE // 不需要接触事件
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON // 保持设备常开，并保证高度不变
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN; // 窗口占满整个屏幕，忽略周围装饰边框(例如边界框，此窗口考虑到装饰边框的内容)
		// 窗口所使用的动画设置
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
				// 清楚影像
				stopDrag();
				// 补充说明这里不用再次进行替换了。因为已经替换完成了
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) ev.getY();
				// 带着影像移动
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
	 * 拖动执行逻辑
	 * 
	 * @param y
	 */
	private void onDrag(int y) {
		// 拖拽的item的值不能小于0，如果小于0说明处于无效区域
		int drag_top = y - dragPoint;
		if (dragImageView != null && drag_top >= 0) {
			// 设置透明度
			mLayoutParams.alpha = 0.5f;
			// 移动y值//记得要加上dragOffset,windowManager是计算整个屏幕的
			mLayoutParams.y = y - dragPoint + dragOffset;
			mWindowManager.updateViewLayout(dragImageView, mLayoutParams);
		}
		// 避免拖动项在分割线的时position返回-1
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
		// 获取你拖拽滑动位置及显示item相对应的view上
		View view = this.getChildAt(dragPosition
				- this.getFirstVisiblePosition());
		// 真正的滚动方法setSelectionFromTop
		this.setSelectionFromTop(dragPosition, view.getTop() + current_step);
	}

	/**
	 * 拖动放下的时候
	 * 
	 * @param y
	 */
	private void onDrog(int y) {
		// 为了避免滑动到分割线的时候，返回-1的时候
		int tempPosition = this.pointToPosition(0, y);
		if (tempPosition != AdapterView.INVALID_POSITION) {
			dragPosition = tempPosition;
		}
		// 超出边界处理
		if (y < this.getChildAt(0).getTop()) {
			// 超出上边界
			dragPosition = 0;
		} else if (y > this.getChildAt(this.getChildCount() - 1).getTop()) {
			dragPosition = this.getChildCount() - 1;
		}
		// 数据交换
		if (dragPosition < this.getAdapter().getCount()) {
			DragListAdapter adapter = (DragListAdapter) this.getAdapter();
			adapter.exchange(dragSrcPosition, dragPosition);
		}

	}

	/**
	 * 停止拖动，删除影像
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
