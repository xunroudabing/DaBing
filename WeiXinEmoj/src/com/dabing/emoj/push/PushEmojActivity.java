package com.dabing.emoj.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.activity.BonusGainActivity;
import com.dabing.emoj.bonus.IBonusChangeListener;
import com.dabing.emoj.bonus.IBouns;
import com.dabing.emoj.bonus.WAPS_Bonus;
import com.dabing.emoj.db.PushEmojDatabaseHelper;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.MediaUtils;
import com.dabing.emoj.widget.CacheWrapperImageView;

public class PushEmojActivity extends BaseActivity implements IBonusChangeListener{

	IBouns mBouns;
	int indexImgWidth = 100;
	JSONObject mData;
	String mEmojID;
	PushEmojThumbAdapter mAdapter;
	GridView mGridView;
	CacheWrapperImageView mIndexImg;
	TextView nameView, desView, countView;
	Button addBtn;
	static final int COLUM_NUM = 5;
	static final String TAG = PushEmojActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		nameView = (TextView) findViewById(R.id.push_emoj_emojTxt);
		desView = (TextView) findViewById(R.id.push_emoj_desTxt);
		countView = (TextView) findViewById(R.id.disan);
		mIndexImg = (CacheWrapperImageView) findViewById(R.id.push_emoj_indexImg);
		mGridView = (GridView) findViewById(R.id.push_emoj_gridview);
		addBtn = (Button) findViewById(R.id.push_emoj_btnAdd);
		addBtn.setOnClickListener(onClickListener);
		mBouns = new WAPS_Bonus(PushEmojActivity.this);
		mBouns.setBonusChangeListener(this);
		caculateWidth();
		BindUI();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.push_emoj_layout;
	}

	protected void caculateWidth() {
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = manager.getDefaultDisplay().getWidth();
		// Log.d(TAG, "screenWidth:"+screenWidth);
		// 图片宽度
		indexImgWidth = (int) ((screenWidth - (COLUM_NUM + 1) * 5) / COLUM_NUM);
	}

	// 返回jsonarray中的前top项
	protected JSONArray getTopArray(JSONArray array, int top) {
		JSONArray topArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			if (i < top) {
				try {
					String item = array.getString(i);
					topArray.put(item);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
				}
			}
		}
		return topArray;
	}

	// 加入表情列表
	protected void AddEmoj() {
		if (mEmojID == null || mEmojID.equals("")) {
			return;
		}
		try {
			ContentValues cv = new ContentValues();
			cv.put(PushEmojDatabaseHelper.FIELD_STATE, "1");
			PushEmojDatabaseHelper helper = new PushEmojDatabaseHelper(
					getApplicationContext());
			helper.update(cv, mEmojID);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}

	}

	protected void BindUI() {

		mEmojID = getIntent().getStringExtra(AppConstant.INTENT_PUSH_EMOJID);
		if (mEmojID == null) {
			return;
		}
		PushEmojDatabaseHelper helper = new PushEmojDatabaseHelper(
				getApplicationContext());
		try {
			Log.d(TAG, "emojId:" + mEmojID);
			JSONObject item = helper.getItem(mEmojID);
			mData = item;
			Log.d(TAG, "item:" + item.toString());

			String thumb = item.getString(PushEmojDatabaseHelper.FIELD_THUMB);
			String name = item.getString(PushEmojDatabaseHelper.FIELD_NAME);
			String des = item.getString(PushEmojDatabaseHelper.FIELD_DES);
			int type = item.getInt(PushEmojDatabaseHelper.FIELD_TYPE);// 0-免费
			int state = item.getInt(PushEmojDatabaseHelper.FIELD_STATE);
			int money = item.getInt(PushEmojDatabaseHelper.FIELD_MONEY);
			JSONObject object = item
					.getJSONObject(PushEmojDatabaseHelper.FIELD_EMOJ);
			JSONArray array = object.getJSONArray("data");

			String btn_txt = getString(R.string.btn_push_emoj_add);
			switch (type) {
			case 0:

				break;
			case 1:
				// 会员
				btn_txt += "(会员可用)";
				break;
			case 2:
				// 铜板
				btn_txt += "(需" + money + "铜板)";
				break;
			default:
				break;
			}
			if (state == 1) {
				btn_txt = getString(R.string.btn_push_emoj_add_finish);
				addBtn.setEnabled(false);
			}
			setMMTitle(name);
			addBtn.setText(btn_txt);
			nameView.setText(name);
			desView.setText(des);
			countView.setText(String.format("共%d个表情", array.length()));
			mIndexImg.setWidth(100);
			mIndexImg.setImage(AppConstant.PIC_SERVER_URL + thumb
					+ AppConstant.PIC_ITEM_FULL_PREFIX);
			mAdapter = new PushEmojThumbAdapter(getTopArray(array, 15));
			mGridView.setAdapter(mAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		} finally {

		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mData == null) {
				return;
			}
			try {
				// 0-免费 1-会员 2-铜板
				int type = mData.getInt(PushEmojDatabaseHelper.FIELD_TYPE);
				final int money = mData.getInt(PushEmojDatabaseHelper.FIELD_MONEY);
				switch (type) {
				case 0:
					AddEmoj();
					addBtn.setText(R.string.btn_push_emoj_add_finish);
					addBtn.setEnabled(false);
					break;
				case 1:
					boolean isVip = AppConfig.getIsVip(getApplicationContext());
					// 已经是会员则添加
					if (isVip) {
						AddEmoj();
						addBtn.setText(R.string.btn_push_emoj_add_finish);
						addBtn.setEnabled(false);
					} else {
						// 提示升级会员
						String msg = getString(R.string.alert_push_emoj_need_vip);
						Dialog mDialog = DialogFactory.createTwoButtonDialog(PushEmojActivity.this, msg, null, null,new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												getApplicationContext(),
												BonusGainActivity.class);
										startActivity(intent);
										dialog.dismiss();
									}
								}, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
						mDialog.show();
					}
					break;
				case 2:
					
					int wealth = mBouns.get();
					//提示购买
					if(wealth >= money){
						String msg = getString(R.string.alert_push_emoj_needbonus).replace("{bonus}", String.valueOf(money));
						Dialog dialog1 = DialogFactory.createTwoButtonDialog(PushEmojActivity.this, msg, null, null, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								mBouns.spend(money);
								AddEmoj();
								addBtn.setText(R.string.btn_push_emoj_add_finish);
								addBtn.setEnabled(false);
								dialog.dismiss();
							}
						},new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						dialog1.show();
						
					}else {
					//余额不足						
						String msg = getString(R.string.alert_push_emoj_getbonus).replace("{bonus}", String.valueOf(money));
						Dialog dialog2 = DialogFactory.createTwoButtonDialog(PushEmojActivity.this, msg, "获取铜板", null, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								mBouns.showOffers();
								dialog.dismiss();
							}
						}, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						dialog2.show();
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};

	class PushEmojThumbAdapter extends BaseAdapter {
		JSONArray mArray;

		public PushEmojThumbAdapter(JSONArray array) {
			mArray = array;
		}

		private View makeView(int arg0, View arg1, ViewGroup arg2)
				throws JSONException {
			View root = null;
			if (arg1 == null) {
				root = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.push_emoj_layout_item, arg2, false);
			} else {
				root = arg1;
			}
			CacheWrapperImageView imageView = (CacheWrapperImageView) root
					.findViewById(R.id.push_emoj_item_img);
			imageView.setWidth(indexImgWidth);
			String pic = mArray.getString(arg0);
			String url = AppConstant.PIC_SERVER_URL + pic
					+ AppConstant.PIC_ITEM_FULL_PREFIX;
			imageView.setImage(url);
			return root;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.length();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			try {
				return makeView(arg0, arg1, arg2);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			return null;
		}

	}
	
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mBouns.reflesh();
	}
	
	@Override
	public void onChange(String t, final int value) {
		// TODO Auto-generated method stub
		if (t.equals("get")) {
			if (value != 0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						MediaUtils.getInstance(getApplicationContext())
								.playSound(R.raw.coin);
						showToast(value);
					}
				});

			}
		} else if (t.equals("spend")) {
			if (value != 0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						MediaUtils.getInstance(getApplicationContext())
								.playSound(R.raw.coin);
						showToast(value);
					}
				});
			}
		}
	}

	@Override
	public void onError(String TAG, String ex) {
		// TODO Auto-generated method stub
		
	}
	
	protected void showToast(int value) {
		View view = LayoutInflater.from(PushEmojActivity.this).inflate(
				R.layout.bonus_alert_toast, null);
		TextView txt = (TextView) view.findViewById(R.id.bonus_alert_toast_txt);
		String s = value > 0 ? String.format("+%d", value) : String
				.valueOf(value);
		txt.setText(s);
		Toast toast = new Toast(PushEmojActivity.this);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
}
