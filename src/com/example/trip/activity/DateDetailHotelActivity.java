package com.example.trip.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.trip.R;
import com.example.trip.adapter.HotelMainAdapter;
import com.example.trip.adapter.StrategySceneryAdapter;
import com.example.trip.entity.CityModel;
import com.example.trip.entity.Date;
import com.example.trip.entity.Hotel;
import com.example.trip.entity.Scenery;
import com.example.trip.manager.ShareManager;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DateDetailHotelActivity extends Activity {
	private List<Hotel> mdata;
	// 新增属性
	private List<Scenery> topData;
	private ListView myList;
	private HotelMainAdapter adapter;
	private Date date;
	private int isType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_datedetailscenery);
		Intent intent = getIntent();
		date = (Date) intent.getSerializableExtra("datedetail");
		TextView title = (TextView) findViewById(R.id.title);
		date = (Date) intent.getSerializableExtra("datedetail");
		((TextView) findViewById(R.id.addScenery)).setText("+添加酒店");
		((TextView) findViewById(R.id.title)).setText("酒店住宿");

		myList = (ListView) findViewById(R.id.sceneryList);
		mdata = new ArrayList<Hotel>();

		topData = new ArrayList<Scenery>();
		findViewById(R.id.addScenery).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DateDetailHotelActivity.this,
						HotelListActivity.class);
				intent.putExtra("datedetail", date);
				startActivity(intent);

			}
		});
		findViewById(R.id.sceneryImgback).setOnClickListener(
				new OnClickListener() {

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

				Hotel hotel = (Hotel) adapter.getItem(position);
				Intent intent = new Intent(getApplicationContext(),
						OrderHotelDetailActivity.class);
				intent.putExtra("hotel", hotel);
				startActivity(intent);

			}
		});
		myList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DateDetailHotelActivity.this);
				builder.setTitle("删除");
				builder.setMessage("确定要删除该酒店吗?");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Hotel hotel = (Hotel) adapter.getItem(arg2);
								deleteData(hotel.getOrder_id());
								// mdata.remove(scenery);
								// adapter.notifyDataSetChanged();

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						});
				builder.create().show();
				return true;
			}
		});
		adapter = new HotelMainAdapter(mdata, this);
		myList.setAdapter(adapter);

		// adapter = new StrategySceneryAdapter(topData,
		// getApplicationContext());
		// myList.setAdapter(adapter);
		// getData(date, isType);
	}

	public void deleteData(int typeid) {
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
								initHotel();
							} else if ("error".equals(s)) {
								Toast.makeText(DateDetailHotelActivity.this,
										"删除失败", Toast.LENGTH_LONG).show();
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

		stringPost.PutParams("action", "deleteDate");
		stringPost.PutParams("type", 2 + "");
		stringPost.PutParams("type_id", typeid + "");
		stringPost.PutParams("datedetailid", date.getDateDetailId() + "");
		TripApplication.getInstance().getRequestQueue().add(stringPost);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initHotel();// (date, isType);
	}

	// private void getData(Date date, int isType) {
	// mdata.clear();
	// StringPostRequest stringPost = new StringPostRequest(
	// UrlUtil.TRIP_DATE_URL, new Listener<String>() {
	// @Override
	// public void onResponse(String arg0) {
	// // TODO Auto-generated method stub
	// try {
	// JSONArray jsonArray = new JSONArray(arg0);
	// for (int i = 0; i < jsonArray.length(); i++) {
	// JSONObject jsonObject = jsonArray
	// .getJSONObject(i);
	// Scenery scenery = new Scenery();
	// scenery.setSceneryType(jsonObject
	// .getString("scenery_type"));
	// scenery.setRecommendContent(jsonObject
	// .getString("comment"));
	// scenery.setUsername(jsonObject
	// .getString("username"));
	// scenery.setImg(jsonObject
	// .getString("scenery_img"));
	// scenery.setTalkNum(jsonObject
	// .getInt("scenery_talkNum"));
	// scenery.setTitle(jsonObject
	// .getString("scenery_title"));
	// scenery.setTalkNum(jsonObject
	// .getInt("scenery_talkNum"));
	// scenery.setName(jsonObject
	// .getString("sceneryname"));
	// scenery.setIsTop(jsonObject.getInt("isTop"));
	// // 新增属性
	// scenery.setUserImg(jsonObject
	// .getString("userimg"));
	// mdata.add(scenery);
	// }
	// topData.clear();
	// for (int j = 0; j < mdata.size(); j++) {
	// if (mdata.get(j).getIsTop() == 1) {
	// topData.add(mdata.get(j));
	// }
	// }
	// adapter.notifyDataSetChanged();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }, new ErrorListener() {
	//
	// @Override
	// public void onErrorResponse(VolleyError arg0) {
	// // TODO Auto-generated method stub
	// Toast.makeText(getApplicationContext(), "网络加载失败",
	// Toast.LENGTH_LONG).show();
	// }
	// });
	// stringPost.PutParams("action", "selectdetailscenery");
	// stringPost.PutParams("id", date.getDateDetailId() + "");
	// TripApplication.getInstance().getRequestQueue().add(stringPost);
	// }

	public void initHotel() {
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_DATE_URL, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						List<Hotel> hotels = new ArrayList<Hotel>();
						Gson gson = new Gson();
						hotels = gson.fromJson(arg0,
								new TypeToken<ArrayList<Hotel>>() {
								}.getType());
						mdata.clear();

						if (hotels.size() > 0) {
							mdata.addAll(hotels);
						}
						adapter.notifyDataSetChanged();

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "网络连接错误",
								Toast.LENGTH_LONG).show();
					}
				});
		postRequest.PutParams("action", "selectdetailhotel");
		postRequest.PutParams("id", date.getDateDetailId() + "");
		TripApplication.getInstance().getRequestQueue().add(postRequest);
	}

}
