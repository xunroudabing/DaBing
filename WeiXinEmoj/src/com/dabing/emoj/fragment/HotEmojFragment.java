package com.dabing.emoj.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.activity.EmojBrowseViewActivity;
import com.dabing.emoj.activity.EmojViewActivity;
import com.dabing.emoj.adpater.HotEmojGridViewAdapter;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 热门表情
 * @author DaBing
 *
 */
public class HotEmojFragment extends BaseEmojFragment implements OnItemClickListener {
	HotEmojGridViewAdapter adapter;
	GridView gridView;
	static final int COLUM = 4;
	static final String TAG = HotEmojFragment.class.getSimpleName();
	public HotEmojFragment(){
		this(null);
	}
	public HotEmojFragment(JSONObject object){
		super(object);
	}
	public static BaseEmojFragment getInstance(JSONObject obj){
		BaseEmojFragment fragment = new HotEmojFragment(obj);
		return fragment;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		gridView = (GridView) getView().findViewById(R.id.hot_emoj_fragment_gridview);
		String json = AppConfig.getHotEmoj(getActivity());
		if(json != null){
			try {
				JSONArray array = new JSONArray(json);
				adapter = new HotEmojGridViewAdapter(getActivity(), array, COLUM);
				gridView.setAdapter(adapter);
				gridView.setOnItemClickListener(this);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
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
		return inflater.inflate(R.layout.hot_emoj_frargment, container, false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			String item = adapter.getPic(position);
			Log.d(TAG, "item:"+item.toString());
			String filename = item;
			String title = "热门表情";
			String url = AppConstant.PIC_SERVER_URL+filename+AppConstant.PIC_ITEM_FULL_PREFIX;
			SendIntent(filename, url, title);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
	private void SendIntent(String filename,String url,String title){
		try {
			String menuId = mObject.getString("id");
			String json = adapter.getData();		
			Intent intent = new Intent(getActivity(), EmojBrowseViewActivity.class);
			intent.putExtra(AppConstant.INTENT_PIC_NAME, filename);
			intent.putExtra(AppConstant.INTENT_PIC_URL, url);
			intent.putExtra(AppConstant.INTENT_TITLE, title);
			intent.putExtra(AppConstant.INTENT_PIC_PARMS, "热门");
			intent.putExtra(AppConstant.INTENT_MENU_ID, menuId);
			intent.putExtra(AppConstant.INTENT_PIC_ARRAY, json);
			intent.putExtras(getActivity().getIntent());
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
}
