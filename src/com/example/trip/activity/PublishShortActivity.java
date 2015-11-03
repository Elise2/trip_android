package com.example.trip.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.example.trip.R;
import com.example.trip.entity.Community;
import com.example.trip.locationselect.ActivitySelectCity;
import com.example.trip.locationselect.MainActivity;
import com.example.trip.util.Constant;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.stat.common.p;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PublishShortActivity extends Activity implements OnClickListener {
	private ImageView img;
	private TextView commit, area;
	private EditText content;
	private String imgString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_publicshort);
		findViewById(R.id.back).setOnClickListener(this);
		img = (ImageView) findViewById(R.id.img);
		content = (EditText) findViewById(R.id.content);
		area = (TextView) findViewById(R.id.location);
		findViewById(R.id.goLocation).setOnClickListener(this);

		findViewById(R.id.commit).setOnClickListener(this);
		Intent intent = getIntent();
		if (intent != null) {
			// byte[] bis = intent.getByteArrayExtra("bitmap");
			// Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0,
			// bis.length);
			// img.setImageBitmap(bitmap);
			// imgString = convertBitmap(bitmap);
			String path = intent.getStringExtra("bitmap");
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			img.setImageBitmap(bitmap);

			bitmap = comp(bitmap);
			imgString = convertBitmap(bitmap);
		}

	}

	private Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public String convertBitmap(Bitmap bitmap) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// 将图片进行压缩
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		try {
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buffer = outputStream.toByteArray();
		byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
		return new String(encode);
	}

	public void initData(String location, String content) {
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_SHORT_URL, new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						finish();

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(PublishShortActivity.this, "网络连接错误",
								Toast.LENGTH_LONG).show();
					}
				});
		postRequest.PutParams("action", "add");
		SharedPreferences sp = getSharedPreferences(Constant.SP_NAME,
				MODE_PRIVATE);
		String id = sp.getInt("user_id", 1) + "";
		postRequest.PutParams("userId", id);

		postRequest.PutParams("img", imgString);
		postRequest.PutParams("location", location);
		postRequest.PutParams("content", content);
		TripApplication.getInstance().getRequestQueue().add(postRequest);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.commit:
			String contents = content.getText().toString();
			String location = area.getText().toString();
			initData(location, contents);
			break;
		case R.id.goLocation:
			startActivityForResult(new Intent(this, ActivitySelectCity.class),
					99);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			switch (resultCode) {
			case 99:
				area.setText(data.getStringExtra("lngCityName"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
