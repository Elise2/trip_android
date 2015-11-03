package com.example.trip.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.baidu.location.BDLocation.a;
import com.example.trip.R;
import com.example.trip.adapter.DiscussAdapter;
import com.example.trip.adapter.SceneryAskContentAdapter;
import com.example.trip.entity.Answer;
import com.example.trip.entity.Ask;
import com.example.trip.entity.Community;
import com.example.trip.entity.Discuss;
import com.example.trip.entity.PlayNote;
import com.example.trip.manager.ShareManager;
import com.example.trip.util.ImageLoaderUtil;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class NotesDiscussActivity extends Activity implements OnClickListener {
	private int city_Id;
	private int ask_Id;
	private DiscussAdapter adapter;
	private List<Discuss> datas;
	private ListView list;
	private Community data;
	private EditText content;
	private PlayNote playnote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_note_discuss);
		playnote = (PlayNote) getIntent().getSerializableExtra("playNote");
		findViewById(R.id.backcommunity).setOnClickListener(this);
		findViewById(R.id.sendNotediscuss).setOnClickListener(this);
		content = (EditText) findViewById(R.id.sendcontentNote);
		datas = new ArrayList<Discuss>();
		list = (ListView) findViewById(R.id.discussNoteList);
		adapter = new DiscussAdapter(datas, this);
		list.setAdapter(adapter);
		initView();
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
							datas.clear();
							datas.addAll(discusses);
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
								initView();
							} else if ("error".equals(s)) {
								Toast.makeText(NotesDiscussActivity.this,
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
						Toast.makeText(NotesDiscussActivity.this, "网络连接错误",
								Toast.LENGTH_LONG).show();
					}
				});
		postRequest.PutParams("action", "addNote");
		postRequest.PutParams("userId", TripApplication.getInstance().getUser()
				.getUser_id()
				+ "");
		postRequest.PutParams("content", content);
		postRequest.PutParams("id", playnote.getNote_id() + "");
		TripApplication.getInstance().getRequestQueue().add(postRequest);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.backcommunity:
			finish();
			break;
		case R.id.sendNotediscuss:
			if (TripApplication.getInstance().getUser() == null
					|| "".equals(TripApplication.getInstance().getUser()
							.getUsername())) {
				Toast.makeText(this, "请先登录", Toast.LENGTH_LONG).show();
			} else {
				String contents = content.getText().toString();
				if (contents == null || "".equals(contents)) {
					Toast.makeText(this, "请先将内容填写完整", Toast.LENGTH_LONG).show();
				} else {
					sendData(contents);
				}
			}
			break;
		default:
			break;
		}
	}
}
