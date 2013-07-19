package com.dabing.emoj.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.adpater.EmojGridViewAdapter;
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
	QuickActionGrid mGrid;
	String action="send";
	boolean flag = true;
	boolean firstLoad = true;
	Dialog dialog;
	Button btnSearch;
	EditText editText;
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
		editText = (EditText) findViewById(R.id.emoj_index2_edittext);
		btnSearch = (Button) findViewById(R.id.emoj_index2_btnSearch);
		gridView = (GridView) findViewById(R.id.emoj_index2_gridview);
		btnSearch.setOnClickListener(clickListener);
		setTitleBtn1("微频道", categoryListener);
		SetupAction();
		Bind();
		PrepareActionGrid();
		gridView.setOnScrollListener(this);
		gridView.setOnItemClickListener(this);
	}
	private OnClickListener clickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String key = editText.getText().toString();
			if(key == null || key.equals("")){
				Toast.makeText(EmojSearchActivity.this, "请输入搜索条件", Toast.LENGTH_SHORT).show();
				return;
			}
			mRequest = new MyRequest(EmojSearchActivity.this, key);
			mRequest.setOnRequestListener(EmojSearchActivity.this);
			mRequest.beginRequest();
			UmengEvent("action010", key);
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
		if(requestCode == AppConstant.REQUEST_COMMON_EMOJ){
			if(resultCode == RESULT_OK){
				getParent().setResult(RESULT_OK, data);
				finish();
			}
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
			mGrid.show(v);
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
}
