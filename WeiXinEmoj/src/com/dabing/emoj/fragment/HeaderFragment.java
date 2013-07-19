package com.dabing.emoj.fragment;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import greendroid.widget.PageIndicator;
import greendroid.widget.PagedView;
import greendroid.widget.PagedView.OnPagedViewChangeListener;

import com.dabing.emoj.R;
import com.dabing.emoj.adpater.HeaderPageViewAdpater;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 表情分类fragment
 * @author DaBing
 *
 */
public class HeaderFragment extends LinearLayout implements OnPagedViewChangeListener{
	public interface IEmojItemClickListener {
		void onItemClick(View view,JSONObject item);
	}
	List<JSONArray> mList;
	int mWidth,mHeight;
	Context mContext;
	HeaderPageViewAdpater adpater;
	PagedView pagedView;
	PageIndicator pageIndicator;
	IEmojItemClickListener listener;
	String selected = "最近使用";
	int currentPage = 0;
	//每页显示条数
	static final int pagesize = 6;
	static final String TAG = HeaderFragment.class.getSimpleName();
	public HeaderFragment(Context context){
		this(context, null);
	}
	public HeaderFragment(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.header_fragment, this, true);
		pagedView = (PagedView) findViewById(R.id.header_pageview);
		pageIndicator = (PageIndicator) findViewById(R.id.header_indicator);
		final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screen_width = windowManager.getDefaultDisplay().getWidth();
		int screen_height = windowManager.getDefaultDisplay().getHeight();
		mWidth = screen_width;
		mHeight = screen_height / 5;
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
		adpater = new HeaderPageViewAdpater(mContext, mList);
		adpater.setOnItemClickListener(itemClickListener);
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
		setItemSeleted(currentPage, selected, View.VISIBLE);
	}
	private void setItemSeleted(int index,String text,int visable){
		View child = pagedView.getChildView(index);
		if(child == null){
			Log.d(TAG, "child is null");
			return;
		}
		GridView gridView = (GridView) child.findViewById(R.id.header_gridview);
		for(int i=0;i<gridView.getChildCount();i++){
			View view = gridView.getChildAt(i);
			TextView textView = (TextView) view.findViewById(R.id.header_txtEmoj);
			if(textView.getText().equals(text)){
				ImageView imageView = (ImageView) view.findViewById(R.id.header_corner);
				imageView.setVisibility(visable);
				break;
			}
		}
	}
	public void setOnItemClickListener(IEmojItemClickListener l){
		this.listener = l;
	}
	private void setActivePage(int page){
		pageIndicator.setActiveDot(page);
	}
	private List<JSONArray> getData(){
		List<JSONArray> list = new LinkedList<JSONArray>();
		String string = AppConfig.getEmojIndex(mContext);
		Log.d(TAG, "表情分类:"+string);
		try {
			JSONArray array_nosort = new JSONArray(string);
			JSONArray array = sort(array_nosort);
			Log.d(TAG, "sorted:"+array.toString());
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
	public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
		// TODO Auto-generated method stub
		setActivePage(newPage);
		currentPage = newPage;		
		setItemSeleted(currentPage, selected, View.VISIBLE);
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
			if(listener != null){
				listener.onItemClick(view, item);
			}
			if(view == null){
				return;
			}
			Log.d(TAG, "item:"+item.toString());
			try {
				String name = item.getString("t");
				if(!name.equals(selected)){
					//隐藏前一个选择过的对象
					setItemSeleted(currentPage, selected, View.GONE);
					selected = name;
					ImageView corner = (ImageView) view.findViewById(R.id.header_corner);
					corner.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
}
