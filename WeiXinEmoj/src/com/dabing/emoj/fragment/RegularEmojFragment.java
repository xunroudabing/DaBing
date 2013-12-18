package com.dabing.emoj.fragment;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.activity.EmojBrowseViewActivity;
import com.dabing.emoj.activity.EmojSearchActivity;
import com.dabing.emoj.activity.EmojViewActivity;
import com.dabing.emoj.activity.UserDefineEmojViewActivity;
import com.dabing.emoj.adpater.RegularEmojGridViewAdapter;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.RegularEmojManager;
import com.dabing.emoj.utils.Util;
import com.dabing.emoj.wxapi.WeiXinHelper;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 常用表情
 * @author DaBing
 *
 */
public class RegularEmojFragment extends BaseEmojFragment implements OnItemClickListener {
	String action = "send";
	JSONArray mArray;
	IWXAPI api;
	RegularEmojGridViewAdapter adapter;
	GridView gridView;
	ImageView alertView;
	static final int COLUM = 4;
	static final String TAG = RegularEmojFragment.class.getSimpleName();
	public RegularEmojFragment(){
		this(null);
	}
	public RegularEmojFragment(JSONObject object){
		super(object);
	}
	public static BaseEmojFragment getInstance(JSONObject obj){
		BaseEmojFragment fragment = new RegularEmojFragment(obj);
		return fragment;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		gridView = (GridView) getView().findViewById(R.id.regular_emoj_fragment_gridview);
		alertView = (ImageView) getView().findViewById(R.id.regular_emoj_fragment_alertImg);
		RegularEmojManager manager = new RegularEmojManager(getActivity());
		JSONArray array = manager.get();
		SetupAction();
		//无记录
		if(array == null){
			gridView.setVisibility(View.GONE);
			alertView.setVisibility(View.VISIBLE);
		}else {
			Log.d(TAG, "array:"+array.toString());
			mArray = makeArray(array);
			adapter = new RegularEmojGridViewAdapter(getActivity(), array, COLUM);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(this);
		}
		api = WXAPIFactory.createWXAPI(getActivity(), AppConstant.WEIXIN_APPID);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.regular_emoj_fragment, container, false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	private JSONArray makeArray(JSONArray array){
		JSONArray data = new JSONArray();
		for(int i =0;i<array.length();i++){
			try {
				JSONObject item = array.getJSONObject(i);
				String name = item.getString("name");
				data.put(name);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		return data;
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			JSONObject item =adapter.getPic(position);
			Log.d(TAG, "onItemClick item:"+item.toString());
			String filename = item.getString("name");
			String title = item.getString("p");
			String filepath = null;
			String mFileName = null;
			if (filename.startsWith("file:")){
				filepath = filename.replace("file:", "");
				mFileName = Util.getNameFromFilepath(filepath);
			}else {
				FileHelper helper = new FileHelper();
				mFileName = helper.find(AppConfig.getEmoj(), filename);
				if(mFileName != null){
					filepath = AppConfig.getEmoj() + mFileName;
				}
			}
			Log.d(TAG, "filepath:"+filepath+" mFileName:"+mFileName);
			String url = AppConstant.PIC_SERVER_URL+filename+AppConstant.PIC_ITEM_FULL_PREFIX;
			//直接发送
			if(action.equals("get")){
				if(filename != ""){
					//sd卡无文件，直接访问url
					if(filepath == null){
						SendIntent(filename, url, title);
						return;
					}
					File file = new File(filepath);
					//表情文件不存在
					if(!file.exists()){
						Toast.makeText(getActivity(), R.string.alert_regular_notexist, Toast.LENGTH_SHORT).show();
						return;
					}
					String prefix = mFileName.substring(mFileName
							.lastIndexOf(".") + 1);
					WeiXinHelper wx = new WeiXinHelper(getActivity(), api);
					// Log.d(TAG, "prefix:"+prefix);
					if (prefix.toLowerCase().equals("gif")) {
						wx.sendEmoj(filepath);
					} else {
						wx.sendPng(filepath);
					}
					UmengEvent("action012", filename);
					getActivity().getParent().finish();
				}
			}
			else if (action.equals("pick")) {				
				if(filename != ""){
					if(filename.startsWith("file:")){
						File file = new File(filepath);
						if(file.exists()){
							Intent intent = new Intent();
							intent.setData(Uri.fromFile(file));
							getActivity().getParent().setResult(Activity.RESULT_OK, intent);
							getActivity().getParent().finish();
						}else {
							Toast.makeText(getActivity(), R.string.alert_regular_notexist, Toast.LENGTH_SHORT).show();
							return;
						}
					}else {
						//有缓存
						if(mFileName != null){		
							File file = new File(filepath);
							Intent intent = new Intent();
							intent.setData(Uri.fromFile(file));
							getActivity().getParent().setResult(Activity.RESULT_OK, intent);
							getActivity().getParent().finish();
						}else {
							//无缓存
							SendIntent(filename, url, title);
						}
					}
					
				}
			}
			else if(action.equals("send")){		
				//自定义表情
				if(filename.startsWith("file:")){
					File file = new File(filepath);
					if(file.exists()){
						Intent intent = new Intent(getActivity(), UserDefineEmojViewActivity.class);
						intent.putExtra(AppConstant.INTENT_PIC_NAME, filepath);
						intent.putExtra(AppConstant.INTENT_TITLE, "最近使用");
						startActivityForResult(intent, AppConstant.REQUEST_COMMON_EMOJ);
					}else {
						Toast.makeText(getActivity(), R.string.alert_regular_notexist, Toast.LENGTH_SHORT).show();
					}
				}else {
					SendIntent(filename, url, title);
				}				
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d(TAG, e.toString());
		}
	}
	private void SendIntent(String filename,String url,String title){
		Intent intent = new Intent(getActivity(), EmojBrowseViewActivity.class);
		intent.putExtra(AppConstant.INTENT_PIC_NAME, filename);
		intent.putExtra(AppConstant.INTENT_PIC_URL, url);
		intent.putExtra(AppConstant.INTENT_TITLE, "最近使用");
//		if(mArray != null){
//			intent.putExtra(AppConstant.INTENT_PIC_ARRAY, mArray.toString());
//		}
		intent.putExtras(getActivity().getIntent());
		startActivityForResult(intent, AppConstant.REQUEST_COMMON_EMOJ);
	}
	public void SetupAction(){
		Intent data = getActivity().getIntent();
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);			
		}
		Log.d(TAG, "action:"+action);
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
	
	private void UmengEvent(String eventid,String key){
		MobclickAgent.onEvent(getActivity(),eventid,key);
	}
}
