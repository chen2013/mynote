package edu.sdust.mynote;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.mynote.R;
import edu.sdust.mynote.adapter.GuidePagerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GuideActivity extends Activity {

	private static final int TO_THE_END = 0;// �������һ��
	private static final int LEAVE_FROM_END = 1;// �뿪���һ��

	private int[] ids = { R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3, R.drawable.guide_4, 
			R.drawable.guide_5, R.drawable.guide_6 };
			
	private List<View> guides = new ArrayList<View>();
	private ViewPager pager;
	private ImageView openImage;
	private ImageView curDot;
	private int offset;// λ����
	private int curPos = 0;// ��¼��ǰ��λ��

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_layout);

		for (int i = 0; i < ids.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(ids[i]);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.FIT_XY);
			guides.add(iv);
		}

		curDot = (ImageView) findViewById(R.id.cur_dot);
		curDot.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {
					public boolean onPreDraw() {
						offset = curDot.getWidth();
						return true;
					}
				});

		GuidePagerAdapter adapter = new GuidePagerAdapter(guides);
		pager = (ViewPager) findViewById(R.id.contentPager);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				moveCursorTo(arg0);
				if (arg0 == ids.length - 1) {// �����һ����
					handler.sendEmptyMessageDelayed(TO_THE_END, 500);
				} else if (curPos == ids.length - 1) {
					handler.sendEmptyMessageDelayed(LEAVE_FROM_END, 100);
				}
				curPos = arg0;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
			
		});
		
		openImage = (ImageView)this.findViewById(R.id.open);
		openImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent openIntent=new Intent(GuideActivity.this,LoginActivity.class);
				startActivity(openIntent);
			}
			});
	}

	/**
	 * �ƶ�ָ�뵽���ڵ�λ��
	 * 
	 * @param position
	 * 		  		ָ�������ֵ
	 * */
	private void moveCursorTo(int position) {
		TranslateAnimation anim = new TranslateAnimation(offset*curPos, offset*position, 0, 0);
		anim.setDuration(300);
		anim.setFillAfter(true);
		curDot.startAnimation(anim);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == TO_THE_END)
				openImage.setVisibility(View.VISIBLE);
			else if (msg.what == LEAVE_FROM_END)
				openImage.setVisibility(View.GONE);
		}
	};
}