package com.example.trip.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.example.trip.R;
import com.example.trip.adapter.SceneryNoteAdapter;
import com.example.trip.entity.CityModel;
import com.example.trip.entity.Date;
import com.example.trip.entity.PlayNote;
import com.example.trip.entity.Scenery;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DateDetailNoteActivity extends Activity {
	private ListView noteList;
	private List<PlayNote> notes;
	private SceneryNoteAdapter adapter;
	private Date date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_datedetailscenery);
		Intent intent = getIntent();
		date = (Date) intent.getSerializableExtra("datedetail");
		noteList = (ListView) findViewById(R.id.sceneryList);
		((TextView) findViewById(R.id.addScenery)).setText("+添加游记");
		((TextView) findViewById(R.id.title)).setText("旅行日记");
		findViewById(R.id.addScenery).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DateDetailNoteActivity.this,
						AddPlayNotesActivity.class);
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
		noteList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PlayNote playNote = (PlayNote) adapter.getItem(position);
				Intent intent = new Intent(getApplicationContext(),
						NoteDeatailActivity.class);
				intent.putExtra("playNote", playNote);
				startActivity(intent);
			}
		});
		notes = new ArrayList<PlayNote>();
		adapter = new SceneryNoteAdapter(notes, this, 1);
		noteList.setAdapter(adapter);
		noteList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DateDetailNoteActivity.this);
				builder.setTitle("删除");
				builder.setMessage("确定要删除该游记吗?");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								PlayNote playNote = (PlayNote) adapter
										.getItem(arg2);

								deleteData(playNote.getNote_id());
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
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initData();

	}

	public void initData() {
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_DATE_URL, new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						List<PlayNote> playNotes = new ArrayList<PlayNote>();
						Gson gson = new Gson();
						playNotes = gson.fromJson(arg0,
								new TypeToken<ArrayList<PlayNote>>() {
								}.getType());
						notes.clear();
						if (playNotes.size() > 0) {
							notes.addAll(playNotes);
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
		postRequest.PutParams("action", "selectdetailnote");
		postRequest.PutParams("id", date.getDateDetailId() + "");
		TripApplication.getInstance().getRequestQueue().add(postRequest);

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
								initData();
							} else if ("error".equals(s)) {
								Toast.makeText(DateDetailNoteActivity.this,
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
		stringPost.PutParams("type", 3 + "");
		stringPost.PutParams("type_id", typeid + "");
		stringPost.PutParams("datedetailid", date.getDateDetailId() + "");
		TripApplication.getInstance().getRequestQueue().add(stringPost);
	}

}
