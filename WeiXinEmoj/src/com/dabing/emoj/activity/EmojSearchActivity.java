package com.dabing.emoj.activity;

import org.apache.http.client.RedirectException;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.adpater.EmojGridViewAdapter;
import com.dabing.emoj.db.ChannelDatabaseHelper;
import com.dabing.emoj.exception.DBLog;
import com.dabing.emoj.exception.ExceptionManager;
import com.dabing.emoj.provider.BaseRequest;
import com.dabing.emoj.provider.ChannelRequest;
import com.dabing.emoj.provider.IRequest;
import com.dabing.emoj.provider.MyRequest;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.JsonHelper;
import com.dabing.emoj.utils.TokenStore;
import com.dabing.emoj.widget.NormalProgressDialog;
import com.dabing.emoj.widget.QuickActionGrid;
import com.dabing.emoj.widget.QuickActionGrid.OnQuickActionItemClickListener;
import com.tencent.mm.sdk.uikit.MMImageButton;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;
import com.umeng.analytics.MobclickAgent;
/**
 * 第二页
 * @author DaBing
 *
 */
public class EmojSearchActivity extends BaseActivity implements IRequest,OnScrollListener,OnItemClickListener {
	RadioGroup mHeader;
	QuickActionGrid mGrid;
	String action="send";
	boolean flag = true;
	boolean firstLoad = true;
	Dialog dialog;
	EditText editText;
	ImageView editClear;
	BaseRequest mRequest;
	GridView gridView;
	EmojGridViewAdapter adapter;
	static final int COL_NUM = 4;
	static final String REQ_NUM = "40";
	String DEFAULT = "903";
	static final String TAG = EmojSearchActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle(R.string.title_channel);		
		editText = (EditText) findViewById(R.id.emoj_index2_edittext);
		editClear = (ImageView) findViewById(R.id.emoj_index2_editclear);
		//btnSearch = (Button) findViewById(R.id.emoj_index2_btnSearch);
		gridView = (GridView) findViewById(R.id.emoj_index2_gridview);
		mHeader = (RadioGroup) findViewById(R.id.channel_category_head);
		//btnSearch.setOnClickListener(clickListener);
		editText.setOnEditorActionListener(onEditorActionListener);
		editText.addTextChangedListener(textWatcher);
		editClear.setOnClickListener(clearListener);
		setTitleBtn1(R.drawable.mm_title_btn_channel_normal, categoryListener);
		SetupAction();
		Bind();
		//PrepareActionGrid();
		BindHeader();
		gridView.setOnScrollListener(this);
		gridView.setOnItemClickListener(this);		
	}
	//***搜索相关****
	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				String key = v.getText().toString();
				if (key == null || key.equals("")) {
					Toast.makeText(EmojSearchActivity.this, "请输入搜索条件",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				mRequest = new MyRequest(EmojSearchActivity.this, key);
				mRequest.setOnRequestListener(EmojSearchActivity.this);
				mRequest.beginRequest();
				UmengEvent("action010", key);
			}
			return false;
		}
	};
	private TextWatcher textWatcher = new TextWatcher() {
		int length = 0;
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			length = count;
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(length > 0){
				editClear.setVisibility(View.VISIBLE);
			}else {
				editClear.setVisibility(View.INVISIBLE);
			}
		}
	};
	private OnClickListener clearListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			editText.setText("");
		}
	};
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.emoj_index2;
	}
	public void SetupAction(){
		Intent data = getIntent();
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);			
		}
		Log.d(TAG, "action:"+action);
		if(action.equals("get")){
			MMImageButton button = setTitleBtn4("微信", new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			button.setBackgroundDrawable(getDrawable(R.drawable.mm_title_btn_back));
			
		}
		else if (action.equals("pick")) {
			MMImageButton button = setTitleBtn4("返回", new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			button.setBackgroundDrawable(getDrawable(R.drawable.mm_title_btn_back));
		}
		else {
			
		}
	}
	public void Bind(){
		mRequest = new ChannelRequest(EmojSearchActivity.this, DEFAULT);
		mRequest.setOnRequestListener(this);
		mRequest.beginRequest();
	}
	

	public void onBind(String response) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onBind:"+response);
		try {
			JSONObject object = new JSONObject(response);
			JSONArray array = object.getJSONObject("data").getJSONArray("info");
			JSONArray data = new JsonHelper().Filter(array);
			adapter = new EmojGridViewAdapter(getApplicationContext(), data, COL_NUM);
			gridView.setAdapter(adapter);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		ExceptionManager.handle(EmojSearchActivity.this, ex);
	}

	public void onHasNext(String hasnext) {
		// TODO Auto-generated method stub
		if(hasnext.equals("0")){
			Toast.makeText(EmojSearchActivity.this, "图片全部拉取完毕", Toast.LENGTH_SHORT).show();
		}
	}

	public void onLoading(String pageflag) {
		// TODO Auto-generated method stub
		if(firstLoad && pageflag.equals("0")){
			dialog = new NormalProgressDialog(EmojSearchActivity.this);
			dialog.show();
			firstLoad = false;
		}else if(!firstLoad && pageflag.equals("0")) {
			dialog = DialogFactory.creatRequestDialog(EmojSearchActivity.this, "加载中...");
			dialog.show();
		}
	}

	public void onRefresh(String response) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onRefresh:"+response);
		try {
			JSONObject object = new JSONObject(response);
			JSONArray array = object.getJSONObject("data").getJSONArray("info");
			JSONArray data = new JsonHelper().Filter(array);
			adapter.refresh(data);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	public void onRequestEnd() {
		// TODO Auto-generated method stub
		flag = true;
		try {
			if(dialog != null){
				dialog.dismiss();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onScroll:view.getLastVisiblePosi="+view.getLastVisiblePosition()+" view.getcount="+view.getCount());
		if(view.getLastVisiblePosition() > 0 && view.getLastVisiblePosition() >= (view.getCount() - 2) && flag){
			flag = false;
			mRequest.beginRequest();
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState == SCROLL_STATE_IDLE){			
			System.gc();
		}
		
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = adapter.getData(position);
			Log.d(TAG, "item:"+item.toString());
			String url = "";
			String text = "";
			if(!item.isNull("image") && !item.get("image").equals("null")){
				String json = item.getString("image");
				//微频道
				if(json.startsWith("{")){
					if(item.getJSONObject("image").has("info")){
						JSONArray info = item.getJSONObject("image").getJSONArray("info");
						JSONObject o = info.getJSONObject(0);
						url = o.getJSONArray("url").getString(0);
					}
				}else {
					url = item.getJSONArray("image").getString(0);
				}
				Log.d(TAG, "url:"+url);
			}	
			text = item.getString("origtext");
			String tweetid = item.getString("id");
			String json = adapter.getArray().toString();
			//Log.d(TAG, "json:"+json);
			Intent intent = new Intent(getApplicationContext(), EmojBrowsePictureActivity.class);
			intent.putExtra(AppConstant.INTENT_PIC_URL, url+AppConstant.PIC_ITEM_FULL_PREFIX);
			intent.putExtra(AppConstant.INTENT_TEXT, text);
			intent.putExtra(AppConstant.INTENT_TWEETID, tweetid);
			intent.putExtra(AppConstant.INTENT_PIC_ARRAY, json);
			intent.putExtra(AppConstant.INTENT_REQUESTOBJ, mRequest);
			intent.putExtras(getIntent());
			//startActivity(intent);
			startActivityForResult(intent, AppConstant.REQUEST_COMMON_EMOJ);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onActivityResult:"+requestCode+" "+resultCode);		
		if(requestCode == AppConstant.REQUEST_COMMON_EMOJ){
			if(resultCode == RESULT_OK){
				getParent().setResult(RESULT_OK, data);
				finish();
			}
		}		
		else if (requestCode == AppConstant.REQUEST_ADD_CHANNEL) {
			BindHeader();
		}
	}
	
	private void UmengEvent(String eventid,String key){
		MobclickAgent.onEvent(EmojSearchActivity.this, eventid,key);
	}
	
	private void PrepareActionGrid(){
		mGrid = new QuickActionGrid(getApplicationContext());
		try {
			String json = AppConfig.getCategory(getApplicationContext());
			JSONArray data = new JSONArray(json);
			mGrid.setData(data);
			mGrid.setOnQuickActionItemClickListener(actionItemClickListener);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private OnClickListener categoryListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//mGrid.show(v);
			Intent intent = new Intent(getApplicationContext(), ChannelAddCategoryActivity.class);
			startActivityForResult(intent, AppConstant.REQUEST_ADD_CHANNEL);
		}
	};
	//点击分类
	private OnQuickActionItemClickListener actionItemClickListener = new OnQuickActionItemClickListener() {
		
		public void onClick(View v, JSONObject item) {
			// TODO Auto-generated method stub
			try {
				mGrid.dismiss();
				String parms = item.getString("p");
				String actionID = item.getString("a");
				String id = item.getString("c");
				//Log.d(TAG, "parms:"+parms);
				DEFAULT = id;
				Bind();
				MobclickAgent.onEvent(EmojSearchActivity.this, actionID);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
	
	private void getImages(JSONArray array){
		try {
			JSONArray result = new JSONArray();
			for(int i = 0;i < array.length();i++){
				JSONObject item = array.getJSONObject(i);
				String url = "";
				if(!item.isNull("image") && !item.get("image").equals("null") && item.getJSONArray("image") != null && item.getJSONArray("image").length() > 0){
					url = item.getJSONArray("image").getString(0);
					result.put(url);
				}	
			}
			DBLog.d("Data", "data", result.toString());
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	
	//********header********
	protected String getHeaderJson(){
		ChannelDatabaseHelper helper = new ChannelDatabaseHelper(getApplicationContext());
		Cursor cursor = null;
		try {
			JSONArray array = new JSONArray(AppConstant.CHANNEL_CATEGORY_INDEX);
			cursor = helper.getCursor();
			//获取数据库中的频道集合，构造json
			if(cursor != null && cursor.getCount() > 0){
				cursor.moveToFirst();
				do {
					int channelId = cursor.getInt(cursor.getColumnIndexOrThrow(ChannelDatabaseHelper.FIELD_CHANNLEID));
					String name = cursor.getString(cursor.getColumnIndexOrThrow(ChannelDatabaseHelper.FIELD_NAME));
					JSONObject object = new JSONObject();
					object.put("id", channelId);
					object.put("n", name);
					array.put(object);
				} while (cursor.moveToNext());
				
			}
			return array.toString();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return null;
	}
	protected void BindHeader(){
		try {
			String json = getHeaderJson();
			if(json == null){
				return;
			}
			mHeader.removeAllViews();
			JSONArray array = new JSONArray(json);
			int checkedId = Integer.parseInt(DEFAULT);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String name = obj.getString("n");
				int id = obj.getInt("id");
				String title = String.format("%s", name);
				String btnid = String.format("channel_header_radio_%d", i+1);
				int resId = getResources().getIdentifier(btnid, "id", getPackageName());
				RadioButton rd = (RadioButton) LayoutInflater.from(getApplicationContext()).inflate(R.layout.channel_category_item,mHeader,false);
				rd.setId(resId);
				rd.setText(title);
				rd.setTag(id);
				if(checkedId == id){
					rd.setChecked(true);
				}
				rd.setOnCheckedChangeListener(onHeaderChangeListener);
				mHeader.addView(rd);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
	//点击header时触发
	private OnCheckedChangeListener onHeaderChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				int id = (Integer) buttonView.getTag();
				Log.d(TAG, "onCheckedChanged:"+id);
				DEFAULT = String.valueOf(id);
				Bind();
			}
		}
	};
}
