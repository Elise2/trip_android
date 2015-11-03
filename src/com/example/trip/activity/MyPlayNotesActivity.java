package com.example.trip.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.example.trip.R;
import com.example.trip.adapter.NotesAdapter;
import com.example.trip.entity.PlayNote;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyPlayNotesActivity extends Activity implements OnClickListener {
	private ListView mymainNoteList;
	private List<PlayNote> mdata;
	private NotesAdapter adapter;
	private int user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_playnotes);
		findViewById(R.id.myMainNoteImg).setOnClickListener(this);
		mymainNoteList = (ListView) findViewById(R.id.mymainNoteList);
		user_id = getIntent().getIntExtra("user_id", 0);
		mdata = new ArrayList<PlayNote>();
		mymainNoteList.setOnItemClickListener(new OnItemClickListener() {

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
		adapter = new NotesAdapter(mdata, getApplicationContext());
		mymainNoteList.setAdapter(adapter);
		if (user_id != 0) {
			initNotes();
		}
	}

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
							mdata.clear();
							mdata.addAll(playNotes);
							adapter.notifyDataSetChanged();
						} else {
							Toast.makeText(MyPlayNotesActivity.this,
									"还没有游记哦，先添加一个吧", Toast.LENGTH_LONG).show();
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
		postRequest.PutParams("action", "mynote");
		postRequest.PutParams("user_id", user_id + "");
		TripApplication.getInstance().getRequestQueue().add(postRequest);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.myMainNoteImg:
			finish();
			break;

		default:
			break;
		}
	}

}
