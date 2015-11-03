package com.example.trip.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.example.trip.R;
import com.example.trip.activity.AddDateActivity;
import com.example.trip.activity.DateDetailActivity;
import com.example.trip.activity.LoginActivity;
import com.example.trip.activity.MyInfoActivity;
import com.example.trip.activity.MySettingActivity;
import com.example.trip.activity.OldDateActivity;
import com.example.trip.entity.Date;
import com.example.trip.util.BaseFragment;
import com.example.trip.util.Constant;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DateFragment extends Fragment implements OnClickListener {
	private boolean isInit = false;
	private ImageView start;

	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_date, null);
		start = (ImageView) view.findViewById(R.id.start);
		start.setOnClickListener(this);
		view.findViewById(R.id.myDate).setOnClickListener(this);
		view.findViewById(R.id.oldDate).setOnClickListener(this);

		// if (isVisible) {
		// new Handler().post(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// }
		return view;
	}

	// @Override
	// public void lazyLoadData() {
	// // TODO Auto-generated method stub
	// if (isVisible && isInit) {
	// }
	// }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.start:
			if (TripApplication.getInstance().getUser() == null) {
				Toast.makeText(getActivity(), "还没登录哦！！！", Toast.LENGTH_LONG)
						.show();
			} else {
				Intent intent = new Intent(getActivity(), AddDateActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.myDate:
			if (TripApplication.getInstance().getUser() == null) {
				Toast.makeText(getActivity(), "还没登录哦！！！", Toast.LENGTH_LONG)
						.show();
			} else {
				initNewData();
			}
			break;
		case R.id.oldDate:
			if (TripApplication.getInstance().getUser() == null) {
				Toast.makeText(getActivity(), "还没登录哦！！！", Toast.LENGTH_LONG)
						.show();
			} else {
				Intent intent = new Intent(getActivity(), OldDateActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	public void initNewData() {
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_DATE_URL, new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						List<Date> date = new ArrayList<Date>();
						Gson gson = new Gson();
						date = gson.fromJson(arg0,
								new TypeToken<ArrayList<Date>>() {
								}.getType());
						if (date.size() > 0) {
							Intent intent = new Intent(getActivity(),
									DateDetailActivity.class);
							intent.putExtra("dates", (Serializable) date);
							startActivity(intent);
						} else {
							Toast.makeText(getActivity(), "还没有行程哦，先添加一个吧",
									Toast.LENGTH_LONG).show();
						}

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "网络连接错误",
								Toast.LENGTH_LONG).show();
					}
				});
		SharedPreferences sp = getActivity().getSharedPreferences(
				Constant.SP_NAME, getActivity().MODE_PRIVATE);
		String id = sp.getInt("user_id", 1) + "";
		postRequest.PutParams("user_id", id);

		postRequest.PutParams("action", "selectNew");
		TripApplication.getInstance().getRequestQueue().add(postRequest);

	}
}
