package com.example.trip.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.trip.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StartActivity extends Activity implements ScrollViewListener {
	private Context mContext;
	private ImageView mGuidePointx2, mGuidePointx3, mGuidePointx4,
			mGuidePointx5, mGuidePointx6, guidepointx7, guidepointx8;
	private TextView guidepointx2title, guidepointx2subtitle,
			guidepointx3title, guidepointx3subtitle, guidepointx4title,
			guidepointx4subtitle, guidepointt5, guidepointt6;
	private ObservableScrollView mScrollView;
	private ArrayList<ShowViews> showViews = new ArrayList<ShowViews>();
	private Handler handler;
	private View goMainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_user_guide);
		init();
	}

	private void init() {
		mContext = StartActivity.this;
		mScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
		mScrollView.setScrollViewListener(this);
		mGuidePointx2 = (ImageView) findViewById(R.id.guidepointx2);
		mGuidePointx3 = (ImageView) findViewById(R.id.guidepointx3);
		mGuidePointx4 = (ImageView) findViewById(R.id.guidepointx4);
		mGuidePointx5 = (ImageView) findViewById(R.id.guidepointx5);
		mGuidePointx6 = (ImageView) findViewById(R.id.guidepointx6);

		guidepointx2title = (TextView) findViewById(R.id.guidepointx2title);
		guidepointx3title = (TextView) findViewById(R.id.guidepointx3title);
		guidepointx4title = (TextView) findViewById(R.id.guidepointx4title);
		guidepointt5 = (TextView) findViewById(R.id.guidepointt5);
		guidepointt6 = (TextView) findViewById(R.id.guidepointt6);

		guidepointx2subtitle = (TextView) findViewById(R.id.guidepointx2subtitle);
		guidepointx3subtitle = (TextView) findViewById(R.id.guidepointx3subtitle);
		guidepointx4subtitle = (TextView) findViewById(R.id.guidepointx4subtitle);
		guidepointx7 = (ImageView) findViewById(R.id.guidepointx7);

		guidepointx8 = (ImageView) findViewById(R.id.guidepointx8);
		goMainActivity = findViewById(R.id.guidegobutton);
		goMainActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
				finish();

				// 跳转到主界面
			}
		});
		for (int i = 0; i < 6; i++) {
			ShowViews views = new ShowViews();
			views.setIn(true);
			views.setOut(true);
			showViews.add(views);
		}
	}

	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		if (scrollView == mScrollView) {

			if (oldy >= y) {
				scrollDown();
			} else {
				scrollUp();
			}
		}
	}

	/**
	 * 帧动画播放
	 */
	private void showNumber() {
		final int[] id = { R.drawable.num_1, R.drawable.num_2,
				R.drawable.num_3, R.drawable.num_4, R.drawable.num_5,
				R.drawable.num_6, R.drawable.num_7, R.drawable.num_8,
				R.drawable.num_9, R.drawable.num_10, R.drawable.num_11,
				R.drawable.num_12, R.drawable.num_13, R.drawable.num_14,
				R.drawable.num_15, R.drawable.num_16, R.drawable.num_17,
				R.drawable.num_18, R.drawable.num_19, R.drawable.num_20,
				R.drawable.num_21, R.drawable.num_22, R.drawable.num_23,
				R.drawable.num_24, R.drawable.num_25, R.drawable.num_26,
				R.drawable.num_27, R.drawable.num_28, R.drawable.num_29,
				R.drawable.num_30, R.drawable.num_31, R.drawable.num_32,
				R.drawable.num_33, R.drawable.num_34, R.drawable.num_35,
				R.drawable.num_36, R.drawable.num_37, R.drawable.num_38,
				R.drawable.num_39, R.drawable.num_40 };

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				guidepointx7.setBackgroundResource(msg.arg1);
			}
		};

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < id.length;) {
					Message msg = new Message();
					msg.arg1 = id[i];
					handler.sendMessage(msg);
					try {
						Thread.sleep(50);
						i++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 0);
	}

	/**
	 * 
	 * @param 判断一个view是否在屏幕中显示
	 * @return
	 */
	public boolean viewIsShow(View view) {
		int[] location = new int[2];
		view.getLocationInWindow(location);
		int height = view.getMeasuredHeight();
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int heightPixels = outMetrics.heightPixels;
		int showCode = (location[1] + height + 300);

		if (showCode >= heightPixels) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 下滑
	 */
	public void scrollDown() {
		if (showViews.get(0).isIn() && !viewIsShow(mGuidePointx2)) {

			mGuidePointx2.setVisibility(View.INVISIBLE);
			mGuidePointx3.setVisibility(View.INVISIBLE);
			mGuidePointx4.setVisibility(View.INVISIBLE);
			mGuidePointx5.setVisibility(View.INVISIBLE);
			mGuidePointx6.setVisibility(View.INVISIBLE);
			guidepointx2title.setVisibility(View.INVISIBLE);
			guidepointx3title.setVisibility(View.INVISIBLE);
			guidepointx4title.setVisibility(View.INVISIBLE);
			guidepointt5.setVisibility(View.INVISIBLE);
			guidepointt6.setVisibility(View.INVISIBLE);

			guidepointx2subtitle.setVisibility(View.INVISIBLE);
			guidepointx3subtitle.setVisibility(View.INVISIBLE);
			guidepointx4subtitle.setVisibility(View.INVISIBLE);
			guidepointx7.setVisibility(View.INVISIBLE);
			Animation animation2 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in1);
			mGuidePointx2.startAnimation(animation2);
			alhpaInAnimation(guidepointx2title);
			alhpaInAnimation(guidepointx2subtitle);

			ShowViews showViews1 = showViews.get(0);
			showViews1.setIn(false);
			showViews1.setOut(true);
		}
		if (showViews.get(1).isIn() && !viewIsShow(mGuidePointx3)) {
			Animation animation3 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in1);
			mGuidePointx3.startAnimation(animation3);
			alhpaInAnimation(guidepointx3title);
			alhpaInAnimation(guidepointx3subtitle);

			mGuidePointx3.setVisibility(View.INVISIBLE);
			mGuidePointx4.setVisibility(View.INVISIBLE);
			mGuidePointx5.setVisibility(View.INVISIBLE);
			mGuidePointx6.setVisibility(View.INVISIBLE);

			guidepointx3title.setVisibility(View.INVISIBLE);
			guidepointx4title.setVisibility(View.INVISIBLE);
			guidepointt5.setVisibility(View.INVISIBLE);
			guidepointt6.setVisibility(View.INVISIBLE);

			guidepointx3subtitle.setVisibility(View.INVISIBLE);
			guidepointx4subtitle.setVisibility(View.INVISIBLE);
			guidepointx7.setVisibility(View.INVISIBLE);
			ShowViews showViews1 = showViews.get(1);
			showViews1.setIn(false);
			showViews1.setOut(true);
		}
		if (showViews.get(2).isIn() && !viewIsShow(mGuidePointx4)) {
			Animation animation4 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in1);
			alhpaInAnimation(guidepointx4title);
			alhpaInAnimation(guidepointx4subtitle);

			mGuidePointx4.startAnimation(animation4);
			mGuidePointx4.setVisibility(View.INVISIBLE);
			mGuidePointx5.setVisibility(View.INVISIBLE);
			mGuidePointx6.setVisibility(View.INVISIBLE);

			guidepointx4title.setVisibility(View.INVISIBLE);
			guidepointt5.setVisibility(View.INVISIBLE);
			guidepointt6.setVisibility(View.INVISIBLE);

			guidepointx4subtitle.setVisibility(View.INVISIBLE);
			guidepointx7.setVisibility(View.INVISIBLE);
			ShowViews showViews1 = showViews.get(2);
			showViews1.setIn(false);
			showViews1.setOut(true);
		}
		if (showViews.get(3).isIn() && !viewIsShow(mGuidePointx5)) {
			Animation animation5 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in1);
			mGuidePointx5.startAnimation(animation5);
			mGuidePointx5.setVisibility(View.INVISIBLE);
			mGuidePointx6.setVisibility(View.INVISIBLE);
			guidepointt5.setVisibility(View.INVISIBLE);
			guidepointt6.setVisibility(View.INVISIBLE);
			alhpaInAnimation(guidepointt5);
			ShowViews showViews1 = showViews.get(3);
			showViews1.setIn(false);
			showViews1.setOut(true);
		}
		if (showViews.get(4).isIn() && !viewIsShow(mGuidePointx6)) {
			Animation animation6 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in1);
			mGuidePointx6.startAnimation(animation6);
			mGuidePointx6.setVisibility(View.INVISIBLE);

			guidepointt6.setVisibility(View.INVISIBLE);

			guidepointx7.setVisibility(View.INVISIBLE);
			alhpaInAnimation(guidepointt6);
			ShowViews showViews1 = showViews.get(4);
			showViews1.setIn(false);
			showViews1.setOut(true);
		}

		if (showViews.get(5).isIn() && !viewIsShow(guidepointx8)) {

			Animation animation8 = AnimationUtils.loadAnimation(mContext,
					R.anim.rotate_up);
			guidepointx8.startAnimation(animation8);
			ShowViews showViews1 = showViews.get(5);
			showViews1.setIn(false);
			showViews1.setOut(true);
		}
	}

	/**
	 * 上滑
	 */
	public void scrollUp() {
		if (showViews.get(0).isOut() && viewIsShow(mGuidePointx2)) {
			mGuidePointx2.setVisibility(View.VISIBLE);

			guidepointx2title.setVisibility(View.VISIBLE);

			guidepointx2subtitle.setVisibility(View.VISIBLE);

			Animation animation2 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in);
			mGuidePointx2.startAnimation(animation2);
			alhpaOutAnimation(guidepointx2title);
			alhpaOutAnimation(guidepointx2subtitle);
			ShowViews showViews2 = showViews.get(0);
			showViews2.setIn(true);
			showViews2.setOut(false);
		}
		if (showViews.get(1).isOut() && viewIsShow(mGuidePointx3)) {
			mGuidePointx2.setVisibility(View.VISIBLE);
			mGuidePointx3.setVisibility(View.VISIBLE);

			guidepointx2title.setVisibility(View.VISIBLE);
			guidepointx3title.setVisibility(View.VISIBLE);

			guidepointx2subtitle.setVisibility(View.VISIBLE);
			guidepointx3subtitle.setVisibility(View.VISIBLE);

			Animation animation3 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in);
			mGuidePointx3.startAnimation(animation3);
			alhpaOutAnimation(guidepointx3subtitle);
			alhpaOutAnimation(guidepointx3title);
			ShowViews showViews2 = showViews.get(1);
			showViews2.setIn(true);
			showViews2.setOut(false);
		}
		if (showViews.get(2).isOut() && viewIsShow(mGuidePointx4)) {
			mGuidePointx2.setVisibility(View.VISIBLE);
			mGuidePointx3.setVisibility(View.VISIBLE);
			mGuidePointx4.setVisibility(View.VISIBLE);

			guidepointx2title.setVisibility(View.VISIBLE);
			guidepointx3title.setVisibility(View.VISIBLE);
			guidepointx4title.setVisibility(View.VISIBLE);

			guidepointx2subtitle.setVisibility(View.VISIBLE);
			guidepointx3subtitle.setVisibility(View.VISIBLE);
			guidepointx4subtitle.setVisibility(View.VISIBLE);

			Animation animation4 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in);
			alhpaOutAnimation(guidepointx4title);
			alhpaOutAnimation(guidepointx4subtitle);
			mGuidePointx4.startAnimation(animation4);
			ShowViews showViews2 = showViews.get(2);
			showViews2.setIn(true);
			showViews2.setOut(false);
		}
		if (showViews.get(3).isOut() && viewIsShow(mGuidePointx5)) {
			mGuidePointx2.setVisibility(View.VISIBLE);
			mGuidePointx3.setVisibility(View.VISIBLE);
			mGuidePointx4.setVisibility(View.VISIBLE);
			mGuidePointx5.setVisibility(View.VISIBLE);

			guidepointx2title.setVisibility(View.VISIBLE);
			guidepointx3title.setVisibility(View.VISIBLE);
			guidepointx4title.setVisibility(View.VISIBLE);
			guidepointt5.setVisibility(View.VISIBLE);

			guidepointx2subtitle.setVisibility(View.VISIBLE);
			guidepointx3subtitle.setVisibility(View.VISIBLE);
			guidepointx4subtitle.setVisibility(View.VISIBLE);

			Animation animation5 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in);
			mGuidePointx5.startAnimation(animation5);

			alhpaOutAnimation(guidepointt5);
			ShowViews showViews2 = showViews.get(3);
			showViews2.setIn(true);
			showViews2.setOut(false);
		}
		if (showViews.get(4).isOut() && viewIsShow(mGuidePointx6)) {
			mGuidePointx2.setVisibility(View.VISIBLE);
			mGuidePointx3.setVisibility(View.VISIBLE);
			mGuidePointx4.setVisibility(View.VISIBLE);
			mGuidePointx5.setVisibility(View.VISIBLE);
			mGuidePointx6.setVisibility(View.VISIBLE);
			guidepointx2title.setVisibility(View.VISIBLE);
			guidepointx3title.setVisibility(View.VISIBLE);
			guidepointx4title.setVisibility(View.VISIBLE);
			guidepointt5.setVisibility(View.VISIBLE);
			guidepointt6.setVisibility(View.VISIBLE);

			guidepointx2subtitle.setVisibility(View.VISIBLE);
			guidepointx3subtitle.setVisibility(View.VISIBLE);
			guidepointx4subtitle.setVisibility(View.VISIBLE);
			guidepointx7.setVisibility(View.VISIBLE);
			alhpaOutAnimation(guidepointt6);
			showNumber();
			Animation animation6 = AnimationUtils.loadAnimation(mContext,
					R.anim.scale_in);
			mGuidePointx6.startAnimation(animation6);
			ShowViews showViews2 = showViews.get(4);
			showViews2.setIn(true);
			showViews2.setOut(false);
		}

		if (showViews.get(5).isOut() && viewIsShow(guidepointx8)) {
			mGuidePointx2.setVisibility(View.VISIBLE);
			mGuidePointx3.setVisibility(View.VISIBLE);
			mGuidePointx4.setVisibility(View.VISIBLE);
			mGuidePointx5.setVisibility(View.VISIBLE);
			mGuidePointx6.setVisibility(View.VISIBLE);

			guidepointx2title.setVisibility(View.VISIBLE);
			guidepointx3title.setVisibility(View.VISIBLE);
			guidepointx4title.setVisibility(View.VISIBLE);
			guidepointt5.setVisibility(View.VISIBLE);
			guidepointt6.setVisibility(View.VISIBLE);

			guidepointx2subtitle.setVisibility(View.VISIBLE);
			guidepointx3subtitle.setVisibility(View.VISIBLE);
			guidepointx4subtitle.setVisibility(View.VISIBLE);
			guidepointx7.setVisibility(View.VISIBLE);

			Animation animation8 = AnimationUtils.loadAnimation(mContext,
					R.anim.rotate_down);
			guidepointx8.startAnimation(animation8);
			ShowViews showViews2 = showViews.get(5);
			showViews2.setIn(true);
			showViews2.setOut(false);
		}

	}

	public void alhpaInAnimation(View view) {
		Animation alhpaIn = AnimationUtils.loadAnimation(mContext,
				R.anim.fade_in);
		view.startAnimation(alhpaIn);
	}

	public void alhpaOutAnimation(View view) {
		Animation alhpaOut = AnimationUtils.loadAnimation(mContext,
				R.anim.fade_out_exit);
		view.startAnimation(alhpaOut);
	}
}
