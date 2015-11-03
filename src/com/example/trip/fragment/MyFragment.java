package com.example.trip.fragment;

import java.sql.SQLException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.trip.R;
import com.example.trip.activity.BeenActivity;
import com.example.trip.activity.LoginActivity;
import com.example.trip.activity.MyInfoActivity;
import com.example.trip.activity.MyOrderActivity;
import com.example.trip.activity.MyParternActivity;
import com.example.trip.activity.MyPlayNotesActivity;
import com.example.trip.activity.MySettingActivity;
import com.example.trip.activity.MyStrorageActivity;
import com.example.trip.activity.SceneryAskActivity;
import com.example.trip.entity.BeenCityModel;
import com.example.trip.entity.User;
import com.example.trip.util.BaseFragment;
import com.example.trip.util.Constant;
import com.example.trip.util.DatabaseOpenHelper;
import com.example.trip.util.ImageLoaderUtil;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.j256.ormlite.dao.Dao;

public class MyFragment extends BaseFragment implements OnClickListener {
	private boolean isInit = false;
	private User user;
	private TextView loginUserName;
	private TextView cityNums, zujiNums;

	private TextView loginUserlevel;
	private ImageView loginUserImg;
	private ImageLoader loader;
	private View view;
	private Dao<BeenCityModel, Integer> bennDao;

	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.layout_my, null);
		view.findViewById(R.id.myyorder).setOnClickListener(this);
		loginUserName = (TextView) view.findViewById(R.id.userName);
		loginUserlevel = (TextView) view.findViewById(R.id.userlevel);
		loginUserImg = (ImageView) view.findViewById(R.id.loginUserImg);
		cityNums = (TextView) view.findViewById(R.id.cityNums);
		zujiNums = (TextView) view.findViewById(R.id.zujiMum);
		bennDao = DatabaseOpenHelper.getInstance(getActivity()).getBeenDao();
		view.findViewById(R.id.myparenter).setOnClickListener(this);
		view.findViewById(R.id.myPartet).setOnClickListener(this);
		view.findViewById(R.id.plays).setOnClickListener(this);
		view.findViewById(R.id.settingCounter).setOnClickListener(this);
		view.findViewById(R.id.InfoImg).setOnClickListener(this);
		view.findViewById(R.id.loginStart).setOnClickListener(this);
		view.findViewById(R.id.userSave).setOnClickListener(this);
		view.findViewById(R.id.beencity).setOnClickListener(this);
		view.findViewById(R.id.myAsk).setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.settingCounter:
			startActivity(new Intent(getActivity(), MySettingActivity.class));
			break;
		case R.id.myAsk:
			startActivity(new Intent(getActivity(), SceneryAskActivity.class));
			break;
		case R.id.InfoImg:
			startActivity(new Intent(getActivity(), MyInfoActivity.class));
			break;
		case R.id.myPartner:
			startActivity(new Intent(getActivity(), MyParternActivity.class));
			break;
		case R.id.loginStart:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.userSave:
			startActivity(new Intent(getActivity(), MyStrorageActivity.class));
			break;
		case R.id.myPartet:
			startActivity(new Intent(getActivity(), MyParternActivity.class));
			break;
		case R.id.myyorder:
			if (TripApplication.getInstance().getUser() == null) {
				Toast.makeText(getActivity(), "还没登录哦！！！", Toast.LENGTH_LONG)
						.show();
			} else {
				startActivity(new Intent(getActivity(), MyOrderActivity.class));
			}
			break;
		case R.id.beencity:
			startActivity(new Intent(getActivity(), BeenActivity.class));
			break;
		case R.id.plays:
			// 点击我的游记
			if (TripApplication.getInstance().getUser() == null) {
				Toast.makeText(getActivity(), "还没登录哦！！！", Toast.LENGTH_LONG)
						.show();
			} else {
				SharedPreferences sp = getActivity().getSharedPreferences(Constant.SP_NAME,
						getActivity().MODE_PRIVATE);
				int user_id  = sp.getInt("user_id", 1);
				//User user = TripApplication.getInstance().getUser();
				//int user_id = user.getUser_id();
				Intent intent = new Intent(getActivity(),
						MyPlayNotesActivity.class);
				intent.putExtra("user_id", user_id);
				startActivity(intent);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void lazyLoadData() {
		// TODO Auto-generated method stub
		if (isVisible && isInit) {

		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		user = TripApplication.getInstance().getUser();
		if (user != null) {
			view.findViewById(R.id.loginStart).setVisibility(View.GONE);
			view.findViewById(R.id.login).setVisibility(View.VISIBLE);
			loginUserName.setText(user.getUsername());
			loginUserlevel.setText(user.getUserLevel());
			loader = new ImageLoader(TripApplication.getInstance()
					.getRequestQueue(), TripApplication.getInstance()
					.getImageCache());
			String url = user.getUserImg();
			if (!url.equals("")) {
				url = UrlUtil.ROOT_URL + url;
			}
			int num;
			try {
				num = (int) bennDao.countOf();
				cityNums.setText(num + "");
				zujiNums.setText(num + "");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ImageLoaderUtil.display(url, loginUserImg);

		} else {
			view.findViewById(R.id.loginStart).setVisibility(View.VISIBLE);
			view.findViewById(R.id.login).setVisibility(View.GONE);
		}
	}
}
