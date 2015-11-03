package com.example.trip.activity;

import com.example.trip.R;
import com.example.trip.util.Constant;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class CityDetailsZiyouWebView extends Activity {
	private WebView ziyouWebview;
	private int ziyou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citydetails_ziyouwebview);
		ziyou = getIntent().getIntExtra("ziyou", 0);
		ziyouWebview = (WebView) findViewById(R.id.ziyouWebview);
		switch (ziyou) {
		case Constant.ZIYOU1:
			ziyouWebview.loadUrl("http://m.mafengwo.cn/sales/344516.html");
			break;
		case Constant.ZIYOU2:
			ziyouWebview = (WebView) findViewById(R.id.ziyouWebview);
			ziyouWebview.loadUrl("http://m.mafengwo.cn/sales/342738.html");
			break;
		case Constant.ZIYOU3:
			ziyouWebview = (WebView) findViewById(R.id.ziyouWebview);
			ziyouWebview.loadUrl("http://m.mafengwo.cn/sales/336544.html");
			break;
		}
	}

}
