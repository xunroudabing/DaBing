package com.dabing.emoj.fragment;

import greendroid.util.GDUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.activity.EmojBrowseViewActivity;
import com.dabing.emoj.activity.EmojViewActivity;
import com.dabing.emoj.admin.SelectEmojActivity;
import com.dabing.emoj.adpater.EmojUmGridViewAdapter;
import com.dabing.emoj.provider.BaseRequest;
import com.dabing.emoj.provider.UMengDataProvider;
import com.dabing.emoj.provider.UMengDataProvider.IUMengReceiveListener;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.TokenStore;
import com.dabing.emoj.widget.NormalProgressDialog;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class CommonEmojFragment extends BaseEmojFragment implements IUMengReceiveListener,OnItemClickListener,OnScrollListener {
	NormalProgressDialog dialog;
	boolean flag = true;
	boolean hasnext = true;
	EmojRequest request;
	UMengDataProvider provider;
	EmojUmGridViewAdapter adapter;
	GridView gridView;
	String id;
	int start = 0;
	int end = 0;
	String action = "send";
	static final int CACHE_RANGE = 8;
	static final int COLUM = 4;
	static final int REQ_NUM = 30;
	static final String TAG = CommonEmojFragment.class.getSimpleName();
	public CommonEmojFragment(){
		this(null);
	}
	public CommonEmojFragment(JSONObject obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	public static BaseEmojFragment getInstance(JSONObject obj){
		BaseEmojFragment fragment = new CommonEmojFragment(obj);
		return fragment;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//MobclickAgent.updateOnlineConfig(getActivity());
		gridView = (GridView) getView().findViewById(R.id.common_emoj_fragment_gridview);
		gridView.setOnItemClickListener(this);
		gridView.setOnScrollListener(this);
		try {
			InitParms();
			//JSONArray parms = mObject.getJSONArray("p");
			id = mObject.getString("id");
			String json = AppConfig.getEmoj(getActivity(), id);
			if(json != null){
				//Log.d(TAG, "直接读缓存:"+json);
				JSONObject object = new JSONObject(json);
				JSONArray array = object.getJSONArray("data");
				Bind(array);
			}
//			provider = new UMengDataProvider(getActivity());
//			provider.setUMengReceiveListener(this);
//			provider.execute(parms);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");
		if(mObject != null){
			outState.putString("mObject", mObject.toString());
		}
	};
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if((savedInstanceState != null) && (savedInstanceState.containsKey("mObject"))){
			try {
				String json = savedInstanceState.getString("mObject");
				Log.d(TAG, "onCreate saveInstanceState:"+json);
				mObject = new JSONObject(json);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.common_emoj_fragment, container, false);
	}
	
	private void InitParms(){
		Intent data = getActivity().getIntent();
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);
			Log.d(TAG, "action:"+action);
		}
	}
	class EmojRequest extends BaseRequest{
		OAuth oAuth;
		String ht;
		public EmojRequest(Context context,String parms){
			this(context);
			ht = parms;
			oAuth = TokenStore.fetch(context);
		}
		public EmojRequest(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String execute(String pageflag, String tweetid, String time) {
			// TODO Auto-generated method stub
			DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);		
			try {
				String response = request.getHt_timeline(oAuth, ht, String.valueOf(REQ_NUM), pageflag, tweetid, time, "4");
				return response;
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				request.shutdownConnection();
			}
			return null;
		}
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			
		}
		
	}


	public void Exception(java.lang.Exception exception) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Exception:"+exception.toString());
	}

	public void Finish() {
		// TODO Auto-generated method stub
		try {
			if(dialog != null){
				dialog.dismiss();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	public void Loading() {
		// TODO Auto-generated method stub
		dialog = new NormalProgressDialog(getActivity());
		dialog.show();
	}

	public void Receive(String resposne) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Receive :"+resposne);
		if(resposne != null && !resposne.equals("null")){
			try {
				JSONObject object = new JSONObject(resposne);
				//String parms = object.getString("p");
				JSONArray array = object.getJSONArray("data");
				Bind(array);
				//缓存
				if(array.length() > 0){
					AppConfig.setEmoj(getActivity(), id, object.toString());
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	}
	
	public void Bind(JSONArray array){
		adapter = new EmojUmGridViewAdapter(getActivity(), array, COLUM);
		gridView.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			String name = mObject.getString("t");
			String menuId = mObject.getString("id");
			String picname = adapter.getPicName(position);
			String json = adapter.getData();
			Log.d(TAG, "onItemClick:"+picname);
			//String url = AppConstant.PIC_SERVER_URL+picname+AppConstant.PIC_ITEM_FULL_PREFIX;
			//Intent intent = new Intent(getActivity(), SelectEmojActivity.class);
			Intent intent = new Intent(getActivity(), EmojBrowseViewActivity.class);
			intent.putExtra(AppConstant.INTENT_PIC_NAME, picname);
			//intent.putExtra(AppConstant.INTENT_PIC_URL, url);
			intent.putExtra(AppConstant.INTENT_TITLE, name);
			intent.putExtra(AppConstant.INTENT_PIC_PARMS, name);
			intent.putExtra(AppConstant.INTENT_PIC_ARRAY, json);			
			intent.putExtra(AppConstant.INTENT_MENU_ID, menuId);
			intent.putExtras(getActivity().getIntent());
			//intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
			//startActivity(intent);
			startActivityForResult(intent, AppConstant.REQUEST_COMMON_EMOJ);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "requestCode:"+requestCode+" resultCode:"+resultCode);
		if(requestCode == AppConstant.REQUEST_COMMON_EMOJ){
			if(resultCode == Activity.RESULT_OK){
				getActivity().getParent().setResult(Activity.RESULT_OK, data);
				getActivity().getParent().finish();
			}
		}
	}
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState == SCROLL_STATE_IDLE){			
			System.gc();
		}
	}



}
