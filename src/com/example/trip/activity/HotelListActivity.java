package com.example.trip.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.example.trip.R;
import com.example.trip.adapter.HotelMainAdapter;
import com.example.trip.adapter.StrategySceneryAdapter;
import com.example.trip.entity.Date;
import com.example.trip.entity.Hotel;
import com.example.trip.entity.Scenery;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HotelListActivity extends Activity {
	private List<Hotel> mdata;
	private ListView myList;
	private HotelMainAdapter adapter;
	private Date date;
	private int isType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		Intent intent = getIntent();
		date = (Date) intent.getSerializableExtra("datedetail");
		TextView title = (TextView) findViewById(R.id.title);
		myList = (ListView) findViewById(R.id.list);
		mdata = new ArrayList<Hotel>();
		((TextView) findViewById(R.id.title)).setText("添加酒店");
		findViewById(R.id.settingImg).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 新增功能，新增每个Item
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Hotel scenery = (Hotel) adapter.getItem(position);
				addData(scenery.getOrder_id());
			}
		});
		adapter = new HotelMainAdapter(mdata, getApplicationContext());
		myList.setAdapter(adapter);
		initHotel();
	}

	private void addData(int typeid) {
		StringPostRequest stringPost = new StringPostRequest(
				UrlUtil.TRIP_DATE_URL, new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						JSONObject object;
						try {
							object = new JSONObject(arg0);
							String s = object.getString("info");
							if ("suc".equals(s)) {
								finish();
							} else if ("error".equals(s)) {
								Toast.makeText(HotelListActivity.this, "添加失败",
										Toast.LENGTH_LONG).show();
								finish();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "网络加载失败",
								Toast.LENGTH_LONG).show();
					}
				});

		stringPost.PutParams("action", "adddetailscenery");
		stringPost.PutParams("type", 2 + "");
		stringPost.PutParams("type_id", typeid + "");
		stringPost.PutParams("datedetailid", date.getDateDetailId() + "");
		TripApplication.getInstance().getRequestQueue().add(stringPost);
	}

	public void initHotel() {
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_HOTEL_URL, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						List<Hotel> hotels = new ArrayList<Hotel>();
						Gson gson = new Gson();
						hotels = gson.fromJson(arg0,
								new TypeToken<ArrayList<Hotel>>() {
								}.getType());
						if (hotels.size() > 0) {
							mdata.clear();
							mdata.addAll(hotels);
							adapter.notifyDataSetChanged();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "网络连接错误",
								Toast.LENGTH_LONG).show();
					}
				});
		postRequest.PutParams("action", "tophotel");
		postRequest.PutParams("city_name", "青岛");
		TripApplication.getInstance().getRequestQueue().add(postRequest);
	}
}
