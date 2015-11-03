package com.example.trip.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Trace;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.trip.R;
import com.example.trip.adapter.AddNoteAdapter;
import com.example.trip.entity.AddNote;
import com.example.trip.entity.Date;
import com.example.trip.util.Constant;
import com.example.trip.util.FileUtil;
import com.example.trip.util.StringPostRequest;
import com.example.trip.util.TripApplication;
import com.example.trip.util.UrlUtil;

public class SingleAddPlayNotesActivity extends Activity implements
		OnClickListener {
	private ImageView face;
	private String locations;
	private MenuItem items;

	/* 头像文件 */
	private static final String IMAGE_FILE_NAME = "head_image.jpg";
	/* 请求识别码 */
	private static final int CODE_GALLERY_REQUEST = 0xa0;
	private static final int CODE_CAMERA_REQUEST = 0xa1;
	private static final int CODE_RESULT_REQUEST = 0xa2;
	// 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
	private static int output_X = 480;
	private static int output_Y = 480;
	String photoPath;
	private PopupWindow popupWindow;
	private LinearLayout cost, location, tag;
	private RelativeLayout reserve;
	private TextView addLocation, addCost, addTag, reserveTime, uncontent,
			content;
	private EditText title;
	private ListView listView;
	private List<AddNote> traces;
	private AddNoteAdapter adapter;
	private boolean flag = false;
	private String fengmian;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_addplaynote);
		listView = (ListView) findViewById(R.id.list);
		traces = new ArrayList<AddNote>();
		View view = LayoutInflater.from(this).inflate(
				R.layout.layout_addnote_headview, null);
		listView.addHeaderView(view);
		findViewById(R.id.savenote).setOnClickListener(this);
		findViewById(R.id.settingImg).setOnClickListener(this);

		adapter = new AddNoteAdapter(traces, this);
		title = (EditText) view.findViewById(R.id.title);
		face = (ImageView) view.findViewById(R.id.face);
		face.setOnClickListener(this);
		cost = (LinearLayout) view.findViewById(R.id.cost);
		cost.setOnClickListener(this);
		location = (LinearLayout) view.findViewById(R.id.location);
		location.setOnClickListener(this);
		view.findViewById(R.id.stillAdd).setOnClickListener(this);
		addLocation = (TextView) view.findViewById(R.id.addLocation);
		addCost = (TextView) view.findViewById(R.id.addCost);
		addTag = (TextView) view.findViewById(R.id.addTag);
		tag = (LinearLayout) view.findViewById(R.id.tag);
		tag.setOnClickListener(this);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(
						SingleAddPlayNotesActivity.this);
				builder.setTitle("删除");
				builder.setMessage("确定要删除此条描述吗?");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								AddNote trace = (AddNote) adapter
										.getItem(arg2 - 1);
								traces.remove(trace);
								adapter.notifyDataSetChanged();
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = null;
		View view = null;
		switch (arg0.getId()) {
		case R.id.stillAdd:
			flag = true;
			setImage();
			break;
		case R.id.settingImg:
			finish();
			break;
		case R.id.cost:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("人均花费");
			view = LayoutInflater.from(this).inflate(R.layout.cost, null);
			final EditText text = (EditText) view.findViewById(R.id.cost);
			if (!"添加".equals(addCost.getText())) {
				text.setText(addCost.getText());
			}
			builder.setView(view);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							addCost.setText(text.getText().toString());
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
			break;
		case R.id.location:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("人物");
			view = LayoutInflater.from(this).inflate(R.layout.people, null);
			final EditText text1 = (EditText) view.findViewById(R.id.people);
			if (!"添加".equals(addLocation.getText())) {
				text1.setText(addLocation.getText());
			}
			builder.setView(view);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							addLocation.setText(text1.getText().toString());
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
			break;
		case R.id.tag:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("形式");
			view = LayoutInflater.from(this).inflate(R.layout.cost, null);
			final EditText tags = (EditText) view.findViewById(R.id.cost);
			if (!"添加".equals(addTag.getText())) {
				tags.setText(addTag.getText());
			}
			builder.setView(view);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							addTag.setText(tags.getText().toString());
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
			break;
		case R.id.savenote:
			if (fengmian == null) {
				Toast.makeText(this, "请选择封面", Toast.LENGTH_LONG).show();
			} else if ("".equals(title.getText().toString())) {
				Toast.makeText(this, "请输入标题", Toast.LENGTH_LONG).show();
			} else if ("".equals(addLocation.getText().toString())) {
				Toast.makeText(this, "请输入人物", Toast.LENGTH_LONG).show();
			} else if ("".equals(addCost.getText().toString())) {
				Toast.makeText(this, "请输入人均花费", Toast.LENGTH_LONG).show();
			} else if ("".equals(addTag.getText().toString())) {
				Toast.makeText(this, "请输入形式", Toast.LENGTH_LONG).show();
			} else if (!(traces.size() > 0)) {
				Toast.makeText(this, "请添加描述", Toast.LENGTH_LONG).show();
			} else {
				AddNote();
			}
			break;
		case R.id.face:
			View v = LayoutInflater.from(this).inflate(
					R.layout.facepopupwindow, null);
			if (popupWindow == null) {
				popupWindow = new PopupWindow(v,
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				popupWindow.setBackgroundDrawable(new ColorDrawable());
				popupWindow.showAtLocation(arg0, Gravity.BOTTOM, 0, 0);
			} else if (!popupWindow.isShowing()) {
				popupWindow = new PopupWindow(v,
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				popupWindow.setBackgroundDrawable(new ColorDrawable());
				popupWindow.showAtLocation(arg0, Gravity.BOTTOM, 0, 0);
			}
			Button Gallery = (Button) v.findViewById(R.id.gallery);
			Button camera = (Button) v.findViewById(R.id.camera);
			Button cancel = (Button) v.findViewById(R.id.cancel);
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
			Gallery.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					flag = false;
					Intent picture = new Intent(Intent.ACTION_PICK);
					picture.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(picture, CODE_GALLERY_REQUEST);
				}
			});

			camera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					flag = false;
					String state = Environment.getExternalStorageState();
					if (state.equals(Environment.MEDIA_MOUNTED)) {
						Intent intent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File parent = FileUtil.getInstance(
								getApplicationContext()).makeDir("jredu_head");
						photoPath = parent.getPath() + File.separator
								+ System.currentTimeMillis() + ".png";
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(photoPath)));
						startActivityForResult(intent, CODE_CAMERA_REQUEST);
					} else {
						Toast.makeText(getApplication(), "û��SDCard!",
								Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		default:
			break;
		}
	}

	public void setImage() {
		Intent picture = new Intent(Intent.ACTION_PICK);
		picture.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(picture, CODE_GALLERY_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		// 用户没有进行有效的设置操作，返回
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
			return;
		}
		switch (requestCode) {
		case CODE_GALLERY_REQUEST:
			// cropRawPhoto(intent.getData());
			Cursor cursor = getContentResolver().query(intent.getData(),
					new String[] { MediaStore.Images.Media.DATA }, null, null,
					null);
			cursor.moveToFirst();
			photoPath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			cursor.close();
			// face.setImageURI(Uri.fromFile(new File(photoPath)));
			// cropRawPhoto(Uri.fromFile(new File(photoPath)));
			setImageToHeadView1(photoPath);

			break;
		case CODE_CAMERA_REQUEST:
			if (photoPath != null) {
				// face.setImageURI(Uri.fromFile(new File(photoPath)));
				cropRawPhoto(Uri.fromFile(new File(photoPath)));
			}
			break;
		case CODE_RESULT_REQUEST:
			if (intent != null) {
				setImageToHeadView(intent);
			}
			break;

		}
	}

	/** * 提取保存裁剪之后的图片数据，并设置头像部分的View */
	private void setImageToHeadView1(String path) {
		// Bundle extras = intent.getExtras();
		// if (extras != null) {
		final Bitmap photo = comp(BitmapFactory.decodeFile(path));
		if (flag) {
			traces.add(new AddNote(convertBitmap(photo), "添加描述...", photo));
			adapter.notifyDataSetChanged();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示").setMessage("是否上传封面");
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
							popupWindow.dismiss();

						}
					});
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							face.setImageBitmap(photo);
							fengmian = convertBitmap(photo);
							// 询问是否需要上传图片
							// 如果是开始上传，将图片转换为Base64编码的字符串
							// updatePhoto(convertBitmap(photo));
							popupWindow.dismiss();

						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		}

		// }
	}

	/** * 裁剪原始的图片 */
	public void cropRawPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX , aspectY :宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX , outputY : 裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CODE_RESULT_REQUEST);
	}

	/** * 提取保存裁剪之后的图片数据，并设置头像部分的View */
	private void setImageToHeadView(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			final Bitmap photo = extras.getParcelable("data");
			if (flag) {
				traces.add(new AddNote(convertBitmap(photo), "添加描述...", photo));
				adapter.notifyDataSetChanged();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("提示").setMessage("是否上传封面");
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
								popupWindow.dismiss();

							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								face.setImageBitmap(photo);
								fengmian = convertBitmap(photo);
								// 询问是否需要上传图片
								// 如果是开始上传，将图片转换为Base64编码的字符串
								// updatePhoto(convertBitmap(photo));
								popupWindow.dismiss();

							}
						});
				AlertDialog dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
			}

		}
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

	/** * 检查设备是否存在SDCard的工具方法 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// 有存储的SDCard
			return true;
		} else {
			return false;
		}
	}

	public static String object2json(List<AddNote> obj) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		for (int i = 0; i < obj.size(); i++) {
			json.append("{\"content\":" + "\"" + obj.get(i).getContent()
					+ "\",");
			json.append("\"img\":" + "\"" + obj.get(i).getImg() + "\"}");
			json.append(",");
		}
		json.deleteCharAt(json.length() - 1);
		json.append("]");
		// System.out.print(json.toString());
		return json.toString();
	}

	private void AddNote() {

		// TODO Auto-generated method stub
		StringPostRequest postRequest = new StringPostRequest(
				UrlUtil.TRIP_DATE_URL, new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						finish();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(SingleAddPlayNotesActivity.this,
								"网络好像走丢了，请稍后再试。", Toast.LENGTH_LONG).show();
					}
				});

		postRequest.PutParams("action", "adddetailnoteNo");
		postRequest.PutParams("city_id", 35 + "");
		SharedPreferences sp = getSharedPreferences(Constant.SP_NAME,
				MODE_PRIVATE);
		String id = sp.getInt("user_id", 1) + "";
		postRequest.PutParams("user_id", id);

		postRequest.PutParams("details", object2json(traces));
		postRequest.PutParams("top_img", fengmian);
		postRequest.PutParams("notes_days", 3 + "");
		postRequest.PutParams("notes_go_time", "2015/10/31");
		postRequest.PutParams("notes_cost", addCost.getText().toString());
		postRequest.PutParams("notes_title", title.getText().toString());
		postRequest.PutParams("notes_type", addLocation.getText().toString());
		postRequest.PutParams("notes_travel_type", addTag.getText().toString());
		TripApplication.getInstance().getRequestQueue().add(postRequest);
	}

}
