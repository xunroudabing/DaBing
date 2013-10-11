package com.dabing.emoj.adpater;

import java.util.List;

import com.dabing.emoj.R;
import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.SimpleFile;
import com.dabing.emoj.utils.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 用于展现自定义表情添加文件夹详细
 * @author DaBing
 *
 */
public class UserDefineAddFilesAdapter extends BaseAdapter {
	/**
	 * 添加为自定义表情回调
	 * @author DaBing
	 *
	 */
	public interface IAddFileCallBack{
		void addFile(SimpleFile file);
	}
	IAddFileCallBack mCallBack;
	Context mContext;
	List<SimpleFile> mList;
	UserDefineDataBaseHelper mHelper;
	static final String TAG = UserDefineAddFilesAdapter.class.getSimpleName();
	public UserDefineAddFilesAdapter(Context c,List<SimpleFile> list,IAddFileCallBack callback){
		mContext = c;
		mList = list;
		mHelper = new UserDefineDataBaseHelper(mContext);
		mCallBack = callback;
	}
	protected View makeView(int position, View convertView, ViewGroup parent){
		View view = null;
		if(convertView == null){
			view = LayoutInflater.from(mContext).inflate(R.layout.user_define_add_file_item, parent, false);
		}else {
			view = convertView;
		}
		TextView nameTextView = (TextView) view.findViewById(R.id.user_define_add_file_item_txtName);
		TextView sizeTextView = (TextView) view.findViewById(R.id.user_define_add_file_item_txtChildSize);
		ImageView addBtn = (ImageView) view.findViewById(R.id.user_define_add_file_item_btnAdd);
		final SimpleFile file = mList.get(position);
		nameTextView.setText(file.name);
		if(file.count > 0){
			sizeTextView.setText(String.format("(%d张)", file.count));
		}else {
			sizeTextView.setText(null);
		}
		boolean b = mHelper.exist(Util.makeStandardPath(file.path));
		if(!b){
			if(file.count > 0){
				addBtn.setVisibility(View.VISIBLE);
				addBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String txt = mContext.getString(R.string.alert_confirm_add_file);
						txt = txt.replace("{file}", "\""+file.name+"\"");
						Dialog confirmDialog = DialogFactory.createTwoButtonDialog(mContext, txt, null, null, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(mCallBack != null){
									mCallBack.addFile(file);
								}
								dialog.dismiss();
							}
						}, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						
						confirmDialog.show();
					}
				});
			}
			else {
				addBtn.setVisibility(View.GONE);
			}
		}
		return view;
	}
	public SimpleFile getFile(int position){
		return mList.get(position);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			return makeView(position, convertView, parent);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
