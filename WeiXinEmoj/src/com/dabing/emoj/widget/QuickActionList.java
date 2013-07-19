package com.dabing.emoj.widget;

import java.lang.reflect.Field;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionWidget;

public class QuickActionList extends QuickActionWidget {
	LinearLayoutForListView mListView;
	JSONArray mData;
	static final String TAG = QuickActionList.class.getSimpleName();
	public QuickActionList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.gd_quick_action_list);
		View view = getContentView();
		mListView = (LinearLayoutForListView) view.findViewById(R.id.gd_quick_action_list_listview);
		
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

	@Override
	protected void populateQuickActions(List<QuickAction> quickActions) {
		// TODO Auto-generated method stub

	}
	public void setData(JSONArray data){
		this.mData =data;
	}
	private void populate(JSONArray data){
		QuickActionAdapter adapter = new QuickActionAdapter(data);
		mListView.setAdapter(adapter);
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

	        populate(mData);

	        onMeasureAndLayout(mRect, contentView);

	        if ((mPrivateFlags & MEASURE_AND_LAYOUT_DONE) != MEASURE_AND_LAYOUT_DONE) {
	            throw new IllegalStateException("onMeasureAndLayout() did not set the widget specification by calling"
	                    + " setWidgetSpecs()");
	        }
	        prepareAnimationStyle();
	        showAtLocation(anchor, Gravity.NO_GRAVITY, 0, mPopupY);
	}
	
	class QuickActionAdapter extends BaseAdapter{
		JSONArray mArray;
		public QuickActionAdapter(JSONArray data){
			mArray = data;
		}
		private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
			View root = null;
			if(convertView != null){
				root = convertView;
			}else {
				root = LayoutInflater.from(getContext()).inflate(R.layout.gd_quick_action_list_item, parent, false);
			}
			ImageView imageView = (ImageView) root.findViewById(R.id.gd_quick_aciton_list_item_img);
			TextView txtView = (TextView) root.findViewById(R.id.gd_quick_aciton_list_item_txt);
			JSONObject obj = mArray.getJSONObject(position);
			Log.d(TAG, obj.toString());
			String name = obj.getString("name");
			String d = obj.getString("d");
			if(!d.equals("")){
				Drawable drawable = getIcon(d);
				if(drawable != null){
					imageView.setImageDrawable(drawable);
				}
			}
			txtView.setText(name);
			return root;
		}
		private Drawable getIcon(String res){
			Field field;
			try {
				field = R.drawable.class.getDeclaredField(res);
				int resourceId = Integer.parseInt(field.get(null).toString());
				Drawable d = getContext().getResources().getDrawable(resourceId);
				d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
				return d;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
			return null;
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
