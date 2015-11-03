package com.example.trip.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.trip.R;
import com.example.trip.util.Constant;
import com.example.trip.util.TripApplication;

public class MySettingActivity extends Activity implements OnClickListener {
	private CheckBox lost;
	private SharedPreferences spPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_setting);
		lost = (CheckBox) findViewById(R.id.lost);
		findViewById(R.id.settingImg).setOnClickListener(this);
		findViewById(R.id.quite).setOnClickListener(this);
		spPreferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
		lost.setChecked(spPreferences.getBoolean("lost", true));
		lost.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = spPreferences.edit();
				editor.putBoolean("lost", arg1);
				editor.commit();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.settingImg:
			finish();
			break;
		case R.id.quite:
			if (TripApplication.getInstance().getUser() != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("退出登录").setMessage("确定退出当前登录用户?");
				builder.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(
										getApplicationContext(),
										StartActivity.class));
								// 结束进程
								android.os.Process
										.killProcess(android.os.Process.myPid());
								onDestroy();
							}
						});

				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
			} else {
				Toast.makeText(getApplicationContext(), "请先登录",
						Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
}
