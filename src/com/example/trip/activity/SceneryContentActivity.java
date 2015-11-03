package com.example.trip.activity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.android.volley.toolbox.ImageLoader;
import com.example.trip.R;
import com.example.trip.entity.Scenery;
import com.example.trip.manager.ShareManager;
import com.example.trip.util.ImageLoaderUtil;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SceneryContentActivity extends Activity implements OnClickListener {
	private List<Scenery> mdata;
	private List<Scenery> newData;
	private TextView sceneryName;
	private TextView scenerySupport;
	private TextView recommendContent;
	private TextView mTextView;
	private LinearLayout strategyContainer;
	private int maxLine = 3;
	private String scename;
	private Scenery scenery1;
	private int position;
	private ShareManager manager;

	// 关于评论
	private TextView recommentorName1;
	private ImageView recommentorImg1;
	private TextView comment1;
	private TextView recommentorName2;
	private ImageView recommentorImg2;
	private TextView comment2;
	private TextView recommentorName3;
	private ImageView recommentorImg3;
	private TextView comment3;
	private ImageLoader imgLoader;

	// 新增旅游攻略
	private TextView scenery_cost;
	private TextView openTime;
	private TextView telPhone;
	private Button sendOrder;
	// 图片的改变
	private ImageView headerImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_scenery_content);
		manager = new ShareManager(this);
		sendOrder = (Button) findViewById(R.id.sendOrder);
		findViewById(R.id.travelImg).setOnClickListener(this);
		findViewById(R.id.shareImg).setOnClickListener(this);
		// 图片的改变
		headerImg = (ImageView) findViewById(R.id.headerImg);
		sendOrder.setOnClickListener(this);
		scenery1 = (Scenery) getIntent().getSerializableExtra("scenery");
		mdata = (List<Scenery>) getIntent().getSerializableExtra("scenerys");
		initView();
		newData = new ArrayList<Scenery>();
		setDescipte();
		setData();
		setRecommentor();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.onDestroy();
	}

	private void initView() {
		sceneryName = (TextView) findViewById(R.id.sceneryName);
		scenerySupport = (TextView) findViewById(R.id.scenerySupport);
		recommendContent = (TextView) findViewById(R.id.recommendContent);
		mTextView = (TextView) findViewById(R.id.adjust_text);
		strategyContainer = (LinearLayout) findViewById(R.id.strategyContainer);
		// 关于评论者
		recommentorName1 = (TextView) findViewById(R.id.orderName1);
		recommentorImg1 = (ImageView) findViewById(R.id.hotelItem1);
		comment1 = (TextView) findViewById(R.id.comment1);

		recommentorName2 = (TextView) findViewById(R.id.orderName2);
		recommentorImg2 = (ImageView) findViewById(R.id.hotelItem2);
		comment2 = (TextView) findViewById(R.id.comment2);

		recommentorName3 = (TextView) findViewById(R.id.orderName3);
		recommentorImg3 = (ImageView) findViewById(R.id.hotelItem3);
		comment3 = (TextView) findViewById(R.id.comment3);

		// 关于旅游攻略
		
		telPhone = (TextView) findViewById(R.id.telPhone);
		scenery_cost = (TextView) findViewById(R.id.scenery_cost);
		openTime = (TextView) findViewById(R.id.openTime);
	}

	public void setRecommentor() {
		if (newData.size() > 0) {
			Scenery scenery = newData.get(0);
			recommentorName1.setText(newData.get(0).getUsername());
			String url = newData.get(0).getUserImg();
			if (!"".equals(url)) {
				url = UrlUtil.TRIP_LOGIN_URL + url;
				imgLoader = new ImageLoader(TripApplication.getInstance()
						.getRequestQueue(), TripApplication.getInstance()
						.getImageCache());
				ImageLoaderUtil.display(url, recommentorImg1);

			}

			recommentorName2.setText(newData.get(1).getUsername());
			comment2.setText(newData.get(1).getRecommendContent());
			url = newData.get(1).getUserImg();
			if (!url.equals("")) {
				url = UrlUtil.TRIP_LOGIN_URL + url;
				imgLoader = new ImageLoader(TripApplication.getInstance()
						.getRequestQueue(), TripApplication.getInstance()
						.getImageCache());
				ImageLoaderUtil.display(url, recommentorImg2);

			}

			recommentorName3.setText(newData.get(2).getUsername());
			comment3.setText(newData.get(2).getRecommendContent());
			url = newData.get(2).getUserImg();
			if (!url.equals("")) {
				url = UrlUtil.TRIP_LOGIN_URL + url;
				imgLoader = new ImageLoader(TripApplication.getInstance()
						.getRequestQueue(), TripApplication.getInstance()
						.getImageCache());
				ImageLoaderUtil.display(url, recommentorImg3);

			}
		}
	}

	public void setData() {
		for (int i = 0; i < mdata.size(); i++) {
		//	if (mdata.get(i).getIsTop() != 1) {
				newData.add(mdata.get(i));
		//	}
		}
	}

	private void setDescipte() {
	
		sceneryName.setText(scenery1.getScenery_title());
		recommendContent.setText(scenery1.getTitle_comment());
		mTextView.setText(scenery1.getStrategy_descript());
		telPhone.setText(scenery1.getContact_tel());
		scenery_cost.setText(scenery1.getScenery_cost());
		openTime.setText(scenery1.getOpen_time());
		// 图片的改变
		String url = scenery1.getImg() + "";
		if (url != null) {
			url = UrlUtil.ROOT_URL + url;
			ImageLoaderUtil.display(url, headerImg);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.travelImg:
			finish();
			break;
		case R.id.shareImg:
			manager.create();
			manager.postShare();
			break;
		case R.id.sendOrder:
			if (TripApplication.getInstance().getUser() == null) {
				Toast.makeText(this, "还没登录哦！！！", Toast.LENGTH_LONG).show();
			} else {
				Intent intent = new Intent(this, OrderForHotelActivity.class);
				intent.putExtra("scenery", scenery1);
				intent.putExtra("isTop", 3);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

}
