package com.example.trip.activity;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.example.trip.R;
import com.example.trip.adapter.NoteDetailsAdapter;
import com.example.trip.entity.Discuss;
import com.example.trip.entity.PlayNote;
import com.example.trip.manager.ShareManager;
import com.example.trip.util.Constant;
import com.example.trip.util.DatabaseOpenHelper;
import com.example.trip.util.ImageLoaderUtil;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDeatailActivity extends Activity implements OnClickListener {
	private List<PlayNote> notes;
	private PlayNote playnote;
	private ImageLoader loader;
	private ImageView userImg;
	private TextView title;
	private TextView userName;
	private TextView publishTime;
	private TextView gotime;
	private TextView cost;
	private TextView person;
	private TextView days;
	private TextView types;
	private ListView detailList;
	private View hearderView;
	private NoteDetailsAdapter adapter;
	private ImageView detailsNotesImg;
	private CheckBox notedetailsstorageImg;
	private Dao<PlayNote, Integer> noteDao;
	private ShareManager manager;
	private EditText content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notedetails_listview);
		findViewById(R.id.notedetailBackImg).setOnClickListener(this);
		findViewById(R.id.notedetailsshareImg).setOnClickListener(this);
		findViewById(R.id.lookDiscuss).setOnClickListener(this);

		manager = new ShareManager(this);
		playnote = (PlayNote) getIntent().getSerializableExtra("playNote");
		notedetailsstorageImg = (CheckBox) findViewById(R.id.notedetailsstorageImg);
		noteDao = DatabaseOpenHelper.getInstance(this).getPlayNoteDao();
		try {
			if (noteDao.idExists(playnote.getNote_id()) == true) {
				notedetailsstorageImg.setChecked(true);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		notedetailsstorageImg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1 == false) {
							try {
								if (noteDao.idExists(playnote.getNote_id()) == true) {
									noteDao.delete(playnote);
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {

							try {
								noteDao.createIfNotExists(playnote);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				});
		initheaderView();
		initHeaderData();
		detailList.addHeaderView(hearderView);

		initNotes();
		notes = new ArrayList<PlayNote>();
		adapter = new NoteDetailsAdapter(notes, getApplicationContext());
		detailList.setAdapter(adapter);
	}

	// 初始化固定布局
	private void initHeaderData() {
		String url = playnote.getUserimg();
		if (url != null) {
			url = UrlUtil.ROOT_URL + url;
			ImageLoaderUtil.display(url, userImg);

		}
		String headImgurl = playnote.getTop_img();
		if (headImgurl != null) {
			headImgurl = UrlUtil.ROOT_URL + headImgurl;
			ImageLoaderUtil.display(headImgurl, detailsNotesImg);

		}
		title.setText(playnote.getNotes_title());
		publishTime.setText(playnote.getNotes_publish_time());
		userName.setText(playnote.getUsername());
		gotime.setText(playnote.getNotes_go_time());
		cost.setText(playnote.getNotes_cost());
		person.setText(playnote.getNotes_type());
		days.setText(playnote.getNotes_days());
	}

	// find所有组件
	private void initheaderView() {
		hearderView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.notedetails_headerview, null);
		detailsNotesImg = (ImageView) hearderView
				.findViewById(R.id.detailsNotesImg);
		detailList = (ListView) findViewById(R.id.notedetalsList);
		userImg = (ImageView) hearderView
				.findViewById(R.id.detailsNotesUserImg);
		title = (TextView) hearderView.findViewById(R.id.detailsnotes_title);
		userName = (TextView) hearderView
				.findViewById(R.id.detailsNotesUserName);
		publishTime = (TextView) hearderView.findViewById(R.id.mainNotesTime);
		gotime = (TextView) hearderView.findViewById(R.id.gotime);
		cost = (TextView) hearderView.findViewById(R.id.cost);
		person = (TextView) hearderView.findViewById(R.id.person);
		days = (TextView) hearderView.findViewById(R.id.days);
		types = (TextView) hearderView.findViewById(R.id.types);
	}

	// 加载数据
	public void initNotes() {
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_NOTES_URL, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						List<PlayNote> playNotes = new ArrayList<PlayNote>();
						Gson gson = new Gson();
						playNotes = gson.fromJson(arg0,
								new TypeToken<ArrayList<PlayNote>>() {
								}.getType());
						if (playNotes.size() > 0) {
							notes.clear();
							notes.addAll(playNotes);
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
		postRequest.PutParams("action", "all");
		postRequest.PutParams("note_id", playnote.getNote_id() + "");
		TripApplication.getInstance().getRequestQueue().add(postRequest);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.notedetailBackImg:
			finish();
			break;
		case R.id.lookDiscuss:
			Intent intent = new Intent(this, NotesDiscussActivity.class);
			intent.putExtra("playNote", playnote);
			startActivity(intent);
			break;
		case R.id.notedetailsshareImg:
			manager.create();
			manager.postShare();
			break;

		default:
			break;
		}
	}

	public void sendData(String content) {
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_DISCUSS_URL, new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						JSONObject object;
						try {
							object = new JSONObject(arg0);
							String s = object.getString("info");
							if ("suc".equals(s)) {
								initNotes();
							} else if ("error".equals(s)) {
								Toast.makeText(NoteDeatailActivity.this,
										"回复失败", Toast.LENGTH_LONG).show();
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
						Toast.makeText(NoteDeatailActivity.this, "网络连接错误",
								Toast.LENGTH_LONG).show();
					}
				});
		postRequest.PutParams("action", "addNote");
		SharedPreferences sp = getSharedPreferences(Constant.SP_NAME,
				MODE_PRIVATE);
		String id = sp.getInt("user_id", 1) + "";
		postRequest.PutParams("userId", id);
	
		postRequest.PutParams("content", content);
		postRequest.PutParams("id", playnote.getNote_id() + "");
		TripApplication.getInstance().getRequestQueue().add(postRequest);
	}

	public void initView() {
		StringPostRequest stringPost = new StringPostRequest(
				UrlUtil.TRIP_DISCUSS_URL, new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						List<Discuss> discusses = new ArrayList<Discuss>();
						discusses = gson.fromJson(arg0,
								new TypeToken<ArrayList<Discuss>>() {
								}.getType());
						if (discusses.size() != 0) {
							// datas.clear();
							// datas.addAll(discusses);
						}
						adapter.notifyDataSetChanged();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "加载失败",
								Toast.LENGTH_LONG).show();
					}
				});
		stringPost.PutParams("action", "selectNote");
		stringPost.PutParams("id", playnote.getNote_id() + "");
		TripApplication.getInstance().getRequestQueue().add(stringPost);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.onDestroy();
	}

}
