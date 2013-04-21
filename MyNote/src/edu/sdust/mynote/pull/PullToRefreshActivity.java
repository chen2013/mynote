package edu.sdust.mynote.pull;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PullToRefreshActivity extends Activity implements OnClickListener {
	private Button lvBtn;
	private Button expBtn;
	private Button gridBtn;
	private Button webBtn;
	private View scrollBtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		lvBtn = (Button) findViewById(R.id.sample_listview);
//		lvBtn.setOnClickListener(this);
//		expBtn = (Button) findViewById(R.id.sample_expandview);
//		expBtn.setOnClickListener(this);
//		gridBtn = (Button) findViewById(R.id.sample_girdview);
//		gridBtn.setOnClickListener(this);
//		webBtn = (Button) findViewById(R.id.sample_webview);
//		webBtn.setOnClickListener(this);
//		scrollBtn = (Button) findViewById(R.id.sample_scrollview);
		scrollBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent jump = new Intent();
		switch (v.getId()) {
//		case R.id.sample_expandview:
//			jump.setClass(this, PullToRefreshExpandableListActivity.class);
//			break;
//		case R.id.sample_girdview:
//			jump.setClass(this, PullToRefreshGridActivity.class);
//			break;
//		case R.id.sample_listview:
//			jump.setClass(this, PullToRefreshListActivity.class);
//			break;
//		case R.id.sample_webview:
//			jump.setClass(this, PullToRefreshWebViewActivity.class);
//			break;
//		case R.id.sample_scrollview:
//			jump.setClass(this, PullToRefreshScrollViewActivity.class);
//			break;
		default:
			break;
		}
		startActivity(jump);
	}
}