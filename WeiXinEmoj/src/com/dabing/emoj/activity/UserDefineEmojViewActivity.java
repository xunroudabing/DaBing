package com.dabing.emoj.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;

import com.ant.liao.GifView;
import com.dabing.emoj.R;
import com.dabing.emoj.advertise.MixAdView;
import com.dabing.emoj.advertise.WAPS_CustomAd;
import com.dabing.emoj.imagezoomview.ImageZoomView;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.FileType;
import com.dabing.emoj.utils.RegularEmojManager;
import com.dabing.emoj.utils.Util;
import com.dabing.emoj.wxapi.WeiXinHelper;
import com.tencent.exmobwin.banner.TAdView;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 自定义图片
 * @author DaBing
 *
 */
public class UserDefineEmojViewActivity extends EmojBrowseViewActivity {
	
	String[] mFiles;
	static final String TAG = UserDefineEmojViewActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.activity.EmojBrowseViewActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	/* (non-Javadoc)
	 * @see com.dabing.emoj.activity.EmojBrowseViewActivity#Initialize()
	 */
	@Override
	protected void Initialize() {
		// TODO Auto-generated method stub
		mAdView = (MixAdView) findViewById(R.id.mixAdView);
		mAdView.setAdTag("ad_index3");
		mAdView.InitAD();
		mGifView = (GifView) findViewById(R.id.gifview);
		mGifView.setZoomRatio(ZOOM_RATIO);
		layout = (LinearLayout) findViewById(R.id.emoj_detail_l1);
		bar = (ProgressBar) findViewById(R.id.image_zoom_view_progress);
		rateView = (TextView) findViewById(R.id.image_zoom_view_rate);
		btnOK = (Button) findViewById(R.id.emoj_detail_btnok);
		layoutBottom = (LinearLayout) findViewById(R.id.emoj_detail_bottom);
		zoomlayout = (LinearLayout) findViewById(R.id.emoj_zoom_layout);
		mZoomView = (ImageZoomView)findViewById(R.id.emoj_zoomview);
		mZoomView.setFullScreen(false);
		container = (RelativeLayout) findViewById(R.id.emoj_browse_container);
//		adView = (TAdView) findViewById(R.id.adview);
//		wapsAdView = (LinearLayout) findViewById(R.id.wapsAdview);
//		wapsCustomAd = (WAPS_CustomAd) findViewById(R.id.wapsCustomAdview);
//		wapsMiniAdBackView = (LinearLayout) findViewById(R.id.miniAdviewBackView);
//		wapsMiniAdView = (LinearLayout) findViewById(R.id.miniAdview);
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recycle();
				finish();
			}
		});
		btnOK.setOnClickListener(btnListener);
		Init();
		SetupWxAction();
		api = WXAPIFactory.createWXAPI(UserDefineEmojViewActivity.this, AppConstant.WEIXIN_APPID);
		wxInstall = api.isWXAppInstalled();
	}
	
	
	private void Init(){
		Intent data = getIntent();
		if(data.getStringExtra(AppConstant.INTENT_PIC_NAME) != null){
			mFilePath = data.getStringExtra(AppConstant.INTENT_PIC_NAME);//路径名
		}
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);
		}
		if(data.getStringExtra(AppConstant.INTENT_PIC_PARMS) != null){
			mParms = data.getStringExtra(AppConstant.INTENT_PIC_PARMS);
		}
		if(data.getStringExtra(AppConstant.INTENT_MENU_ID) != null){
			mMenuID = data.getStringExtra(AppConstant.INTENT_MENU_ID);
		}
		if(data.getStringArrayExtra(AppConstant.INTENT_PIC_ARRAY) != null){
			mFiles = data.getStringArrayExtra(AppConstant.INTENT_PIC_ARRAY);//文件路径名集合			
		}
		finish = true;
		//显示图片
		SetImage();
		//设置初始指针
		if(mFiles != null){
			for(int i=0;i<mFiles.length;i++){
				try {
					String item = mFiles[i];
					if(item.equals(mFilePath)){
						mIndicator = i;
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
			}
		}
	}
	
	
	//发送至微信
		private OnClickListener btnListener = new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if(finish){
						//微信请求
						if(action.equals("get")){
						RegularEmojManager manager = new RegularEmojManager(getApplicationContext());
						manager.add(mFileName, mParms);
						WeiXinHelper helper = new WeiXinHelper(UserDefineEmojViewActivity.this, api);
						if(mFileType == FileType.GIF){
							//发动态图
							helper.sendEmoj(mFilePath);
						}
						else {
							//发静态图
							helper.sendPng(mFilePath);
						}						
						UmengEvent("action026");//[自定义表情]从微信发送
						setResult(RESULT_OK);
						finish();
						}else if (action.equals("pick")) {
							//其他
							Log.d(TAG, "onclick action:"+action);
							RegularEmojManager manager = new RegularEmojManager(getApplicationContext());
							manager.add(mFileName, mParms);
							File file = new File(mFilePath);
							Intent intent = new Intent();
							intent.setData(Uri.fromFile(file));
							UmengEvent("action028");//[自定义表情]第三方调用
							setResult(RESULT_OK, intent);
							finish();
						}
						
					}else{
						String text = getResources().getString(R.string.wx_not_finish);
						Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
			}
		};
	/* (non-Javadoc)
	 * @see com.dabing.emoj.activity.EmojBrowseViewActivity#showNextImage()
	 */
	@Override
	protected void showNextImage() {
		// TODO Auto-generated method stub
		hideException();
		String name = showNext();
		if(name != null){
			mFilePath = name;
			RecycleBitmap();
			SetImage();
		}
	}


	/* (non-Javadoc)
	 * @see com.dabing.emoj.activity.EmojBrowseViewActivity#showPrevImage()
	 */
	@Override
	protected void showPrevImage() {
		// TODO Auto-generated method stub
		hideException();
		String name = showPrev();
		if(name != null){
			mFilePath = name;
			RecycleBitmap();
			SetImage();
		}
	}

	// 返回下一个
	private String showNext() {
		if (mFiles == null) {
			return null;
		}
		try {
			if (mIndicator + 1 < mFiles.length) {
				mIndicator++;
				return mFiles[mIndicator];
			} else {
				Toast.makeText(UserDefineEmojViewActivity.this, "已经是最后一项了",
						Toast.LENGTH_SHORT).show();
				// 已经是最后一个了
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;

	}
	
	//返回上一个
	protected String showPrev() {
		if (mFiles == null) {
			return null;
		}
		try {
			if (mIndicator - 1 >= 0) {
				mIndicator--;
				Log.d(TAG, "mIndicator:" + mIndicator);
				return mFiles[mIndicator];
			} else {
				// 已经是第一项了
				Toast.makeText(UserDefineEmojViewActivity.this, "已经是第一项了",
						Toast.LENGTH_SHORT).show();
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}
	//显示图片
	private void SetImage(){
		File file = new File(mFilePath);
		if(!file.exists()){
			return;
		}
		String filename = Util.getNameFromFilepath(mFilePath);
		String prefix = Util.getExtFromFilename(filename);
		Log.d(TAG, "filename:"+filename + " prefix:"+prefix);
		//gif图
		if(prefix.toLowerCase().equals("gif")){
			try {
				mFileType = FileType.GIF;
				InputStream is = new FileInputStream(file);
				mGifView.setGifImage(is);
				show(1);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			
		}
		//静态图
		else {
			mFileType = FileType.PNG;
			initializeControls(mFilePath);
			show(2);
		}
	}
	
	@Override
	protected void AddToRegular(String filename, String parms, String filepath) {
		// TODO Auto-generated method stub
		RegularEmojManager manager = new RegularEmojManager(getApplicationContext());
		Log.d(TAG, "AddToRegular:"+filepath);
		try {
			manager.add(String.format("file:%s", filepath), parms);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
}
