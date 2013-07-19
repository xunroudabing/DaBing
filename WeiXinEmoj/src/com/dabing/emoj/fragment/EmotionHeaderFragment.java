package com.dabing.emoj.fragment;

import greendroid.widget.PageIndicator;
import greendroid.widget.PagedView;
import greendroid.widget.PagedView.OnPagedViewChangeListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.adpater.CrystalHeaderPageViewAdpater;
import com.dabing.emoj.adpater.EmotionHeaderPageViewAdpater;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.ColorButton;
import com.dabing.emoj.widget.CrystalButton;
import com.dabing.emoj.widget.EmotionPanel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.GridView;
import android.widget.LinearLayout;

public class EmotionHeaderFragment extends LinearLayout implements OnPagedViewChangeListener {
	List<JSONArray> mList;
	int mWidth,mHeight;
	Context mContext;
	EmotionHeaderPageViewAdpater adpater;
	PagedView pagedView;
	PageIndicator pageIndicator;
	IEmojItemClickListener listener;
	String selected = "e1";
	int currentPage = 0;
	boolean sortByClick = false;//按活跃度排序
	//每页显示条数
	static final int colums = 4;
	static final int spaceing = 15;
	static final int pagesize = 15;
	static final String TAG = EmotionHeaderFragment.class.getSimpleName();
	public EmotionHeaderFragment(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.header_fragment, this, true);
		pagedView = (PagedView) findViewById(R.id.header_pageview);
		pageIndicator = (PageIndicator) findViewById(R.id.header_indicator);
		final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screen_width = windowManager.getDefaultDisplay().getWidth();
		int screen_height = windowManager.getDefaultDisplay().getHeight();
		mWidth = (screen_width - spaceing * (colums+1))/colums;
		mHeight = mWidth * 2 + 8;
		mList = getData();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG,"widthSize:"+widthSize+"widthMode:"+widthMode+ "onMeasure heightsize:"+heightSize+ " " + heightMode);
	    int heightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightSpec);
	};
	
	public void show(){
		adpater = new EmotionHeaderPageViewAdpater(mContext, mList);
		adpater.setItemClickListener(itemClickListener);
		pagedView.setAdapter(adpater);
		pagedView.setOnPageChangeListener(this);
		pageIndicator.setDotCount(adpater.getCount());
		setActivePage(pagedView.getCurrentPage());
		postDelayed(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				Init();
			}
		},500);
	}
	public void Init(){
		setItemSeleted(currentPage, selected, true);
	}
	public void setOnItemClickListener(IEmojItemClickListener l){
		this.listener = l;
	}
	private void setActivePage(int page){
		pageIndicator.setActiveDot(page);
	}
	private List<JSONArray> getData(){
		List<JSONArray> list = new LinkedList<JSONArray>();
		String string = AppConstant.EMOJ_EMOTION_INDEX;
		//Log.d(TAG, "表情分类:"+string);
		try {
			JSONArray array_nosort = new JSONArray(string);
			JSONArray array = sort(array_nosort);
			if(sortByClick){
				array = sortByClick(array);
			}
			//Log.d(TAG, "sorted:"+array.toString());
			//分页处理
			JSONArray temp = new JSONArray();
			int j =0;
			for(int i=0;i<array.length();i++){
				JSONObject item = array.getJSONObject(i);
				temp.put(item);
				if(j<pagesize-1){
					if(i == array.length() - 1){
						list.add(temp);
					}
					j++;
				}else {
					list.add(temp);
					j = 0;
					temp = new JSONArray();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return list;
	}
	
	private JSONArray sort(JSONArray array){
		if(array == null){
			return null;
		}
		for(int i=0;i<array.length();i++){
			for(int j=i+1;j<array.length();j++){
				try {
					JSONObject item_i = array.getJSONObject(i);
					JSONObject item_j = array.getJSONObject(j);
					int o_i = item_i.getInt("o");
					int o_j = item_j.getInt("o");
					if(o_j < o_i){
						array.put(i, item_j);
						array.put(j, item_i);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
				}
				
			}
		}
		return array;
	}
	//按点击量排序
	private JSONArray sortByClick(JSONArray array){
		if(array == null){
			return null;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0;i<array.length();i++){
			try {
				JSONObject item_i = array.getJSONObject(i);
				String id_i = item_i.getString("id");
				int o_i = AppConfig.getHeaderClickCount(mContext, id_i);
				map.put(id_i, o_i);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		for(int i=0;i<array.length();i++){
			for(int j=i+1;j<array.length();j++){
				try {
					JSONObject item_i = array.getJSONObject(i);
					JSONObject item_j = array.getJSONObject(j);
					String id_i = item_i.getString("id");
					String id_j = item_j.getString("id");
					
					int o_i = map.get(id_i);
					int o_j = map.get(id_j);
					if(o_j > o_i){
						array.put(i, item_j);
						array.put(j, item_i);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
				}
				
			}
		}
		return array;
	}
	public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
		// TODO Auto-generated method stub
		setActivePage(newPage);
		currentPage = newPage;		
		setItemSeleted(currentPage, selected, true);
	}
	public void onStartTracking(PagedView pagedView) {
		// TODO Auto-generated method stub
		
	}
	public void onStopTracking(PagedView pagedView) {
		// TODO Auto-generated method stub
		
	}
	//点击item
	IEmojItemClickListener itemClickListener = new IEmojItemClickListener() {		
		public void onItemClick(View view, JSONObject item) {
			// TODO Auto-generated method stub
			String id = "";
			try {
				id = item.getString("id");
				if(!id.equals(selected)){					
					setItemSeleted(currentPage, selected, false);
					ColorButton crystalButton = (ColorButton) view;
					crystalButton.setSelected(true);
					selected = id;
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			if(listener != null){
				listener.onItemClick(view, item);
			}
			
			Log.d(TAG, "item:"+item.toString());
			AppConfig.setNewEmojArray(mContext, id);
		}
	};
	
	private void setItemSeleted(int index,String id,boolean selected){
		View child = pagedView.getChildView(index);
		if(child == null){
			Log.d(TAG, "child is null");
			return;
		}
		EmotionPanel gridView = (EmotionPanel) child.findViewById(R.id.header_fragment_panel);
		for(int i=0;i<gridView.getChildCount();i++){
			View view = gridView.getChildAt(i);
			ColorButton crystalButton = (ColorButton) view;
			if(crystalButton.getID().equals(id)){
				crystalButton.setSelected(selected);
				return;
			}
		}
	}
}
