package com.dabing.emoj.widget;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionWidget;

public class QuickActionGrid extends QuickActionWidget {
	public interface OnQuickActionItemClickListener{
		void onClick(View v,JSONObject item);
	}
	OnQuickActionItemClickListener listener;
	QuickGridAdapter adapter;
	JSONArray mData;
	GridView mGridView;
	static final String TAG = QuickActionGrid.class.getSimpleName();
	public QuickActionGrid(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.gd_quick_action_grid);
		View view = getContentView();
		mGridView = (GridView) view.findViewById(R.id.gd_quick_action_gridview);
	}

	@Override
	protected void onMeasureAndLayout(Rect anchorRect, View contentView) {
		// TODO Auto-generated method stub
		contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        contentView.measure(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        int rootHeight = contentView.getMeasuredHeight();

        int offsetY = getArrowOffsetY();
        int dyTop = anchorRect.top;
        int dyBottom = getScreenHeight() - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom);
        //int popupY = (onTop) ? anchorRect.top - rootHeight + offsetY : anchorRect.bottom - offsetY;
        int popupY = anchorRect.bottom - offsetY;
        setWidgetSpecs(popupY, false);
	}
	public void setOnQuickActionItemClickListener(OnQuickActionItemClickListener l){
		this.listener = l;
	}
	public void setData(JSONArray data){
		this.mData =data;
	}
	private void populate(JSONArray data){
		adapter = new QuickGridAdapter(data);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(itemClickListener);
	}
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(listener != null){
				try {
					JSONObject object = adapter.getData(position);
					listener.onClick(view, object);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
			}
		}
	};
	@Override
	protected void populateQuickActions(List<QuickAction> quickActions) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void show(View anchor) {
		// TODO Auto-generated method stub
		 final View contentView = getContentView();

	        if (contentView == null) {
	            throw new IllegalStateException("You need to set the content view using the setContentView method");
	        }

	        // Replaces the background of the popup with a cleared background
	        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	        final int[] loc = mLocation;
	        anchor.getLocationOnScreen(loc);
	        mRect.set(loc[0], loc[1], loc[0] + anchor.getWidth(), loc[1] + anchor.getHeight());
	        if(adapter == null){
	        	populate(mData);
	        }
	        int popX = loc[0] + (anchor.getWidth()/2);
	        onMeasureAndLayout(mRect, contentView);

	        if ((mPrivateFlags & MEASURE_AND_LAYOUT_DONE) != MEASURE_AND_LAYOUT_DONE) {
	            throw new IllegalStateException("onMeasureAndLayout() did not set the widget specification by calling"
	                    + " setWidgetSpecs()");
	        }
	        prepareAnimationStyle();
	        showAtLocation(anchor, Gravity.NO_GRAVITY, popX, mPopupY);
	}
	
	class QuickGridAdapter extends BaseAdapter{
		JSONArray mArray;
		public QuickGridAdapter(JSONArray data){
			mArray = data;
		}
		private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
			View root = null;
			if(convertView != null){
				root = convertView;
			}else {
				root = LayoutInflater.from(getContext()).inflate(R.layout.gd_quick_action_grid_item, parent, false);
			}
			
			TextView txtView = (TextView) root.findViewById(R.id.gd_quick_aciton_grid_item_txt);
			JSONObject obj = mArray.getJSONObject(position);
			Log.d(TAG, obj.toString());
			String name = obj.getString("name");
			txtView.setText(name);
			return root;
		}
		public JSONObject getData(int position) throws JSONException{
			JSONObject item = mArray.getJSONObject(position);
			return item;
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.length();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			try {
				return makeView(position, convertView, parent);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			return null;
		}
		
	}
	

}
