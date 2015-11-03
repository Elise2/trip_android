package com.example.trip.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.trip.R;
import com.example.trip.entity.User;
import com.example.trip.util.Constant;
import com.example.trip.util.DatabaseOpenHelper;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.j256.ormlite.dao.Dao;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText username;
	private EditText password;
	private Button submit;
	private User user;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private ImageView zou;
	private ImageView zhi;
	private ImageView zou1;
	private ImageView zhi1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		findViewById(R.id.loginBackImg).setOnClickListener(this);
		sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
		editor = sp.edit();
		initView();
		handlerbtn();

	}

	public void initView() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.userpwd);
		submit = (Button) findViewById(R.id.submit);
		zou = (ImageView) findViewById(R.id.zou);
		zhi = (ImageView) findViewById(R.id.zhi);
		zou1 = (ImageView) findViewById(R.id.zou1);
		zhi1 = (ImageView) findViewById(R.id.zhi1);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.login);
		zou.setAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.loginback);
		zhi.setAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.login);
		zou1.setAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.loginback);
		zhi1.setAnimation(animation);

	}

	public void handlerbtn() {
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String usernameString = username.getText().toString();
				final String passwordString = password.getText().toString();
				StringPostRequest stringPost = new StringPostRequest(
						UrlUtil.TRIP_LOGIN_URL, new Listener<String>() {

							@Override
							public void onResponse(String arg0) {
								// TODO Auto-generated method stub
								if (arg0 != null) {
									try {
										JSONArray jsonArray = new JSONArray(
												arg0);
										JSONObject obj = jsonArray
												.getJSONObject(0);
										user = new User();
										if (obj.has("info")) {
											Toast.makeText(LoginActivity.this,
													"账号或密码错误",
													Toast.LENGTH_LONG).show();
										} else {
											user.setUsername(obj
													.getString("username"));
											user.setUserImg(obj
													.getString("userimg"));
											user.setUserLevel(obj
													.getString("user_level"));
											user.setUser_id(obj
													.getInt("user_id"));
											TripApplication.getInstance()
													.setUser(user);

											editor.putString("username",
													usernameString);
											editor.putString("userpwd",
													passwordString);
											editor.putInt("user_id",
													obj.getInt("user_id"));
											editor.commit();
											finish();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError arg0) {
								// TODO Auto-generated method stub
								Toast.makeText(LoginActivity.this,
										"网络好像走丢了，请稍后再试", Toast.LENGTH_LONG)
										.show();
							}

						});
				stringPost.PutParams("username", usernameString);
				stringPost.PutParams("userpwd", passwordString);
				TripApplication.getInstance().getRequestQueue().add(stringPost);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.loginBackImg:
			finish();
			break;
		default:
			break;
		}
	}
}
