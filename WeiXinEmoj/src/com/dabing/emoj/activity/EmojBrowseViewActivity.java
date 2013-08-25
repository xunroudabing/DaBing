package com.dabing.emoj.activity;

import greendroid.util.GDUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.RelativeLayout.LayoutParams;

import cn.waps.AdView;
import cn.waps.AppConnect;
import cn.waps.MiniAdView;

import com.ant.liao.GifView;
import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.activity.DownloadGridViewActivity.DownloadPackageTask;
import com.dabing.emoj.activity.EmojViewActivity.MyHandler;
import com.dabing.emoj.activity.EmojViewActivity.downloadTask;
import com.dabing.emoj.advertise.AdManager;
import com.dabing.emoj.advertise.WAPS_CustomAd;
import com.dabing.emoj.advertise.AdManager.AdType;
import com.dabing.emoj.imagezoomview.ImageZoomView;
import com.dabing.emoj.imagezoomview.SimpleZoomListener;
import com.dabing.emoj.imagezoomview.ZoomState;
import com.dabing.emoj.imagezoomview.SimpleZoomListener.ControlType;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileType;
import com.dabing.emoj.utils.FileTypeJudge;
import com.dabing.emoj.utils.PackageDownloader;
import com.dabing.emoj.utils.RegularEmojManager;
import com.dabing.emoj.widget.DownloadProgressDialog;
import com.dabing.emoj.wxapi.WeiXinHelper;
import com.tencent.exmobwin.banner.TAdView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.uikit.MMAlert;
import com.tencent.mm.sdk.uikit.MMAlert.OnAlertSelectId;
import com.umeng.analytics.MobclickAgent;

public class EmojBrowseViewActivity extends BaseActivity implements OnTouchListener,OnClickListener {
	 /** Constant used as menu item id for setting zoom control type */
    private static final int MENU_ID_ZOOM = 0;
    /** Constant used as menu item id for setting pan control type */
    private static final int MENU_ID_PAN = 1;
    /** Constant used as menu item id for resetting zoom state */
    private static final int MENU_ID_RESET = 2;
    /** Image zoom view */
    private ImageZoomView mZoomView;
    /** Zoom state */
    private ZoomState mZoomState;
    /** Decoded bitmap image */
    private Bitmap mBitmap;
    /** On touch listener for zoom view */
    private SimpleZoomListener mZoomListener;
    private ZoomControls mZoomControls;
    private Button mZoomIn;
    private Button mZoomOut;
    private float seed=1.2f;
	//************************************
    boolean emotionMode = false;
    RelativeLayout container;
    TAdView adView;
    LinearLayout wapsAdView;//万普广告
    WAPS_CustomAd wapsCustomAd;//万普自定义广告
    LinearLayout wapsMiniAdBackView;
    LinearLayout wapsMiniAdView;//万普迷你广告
	LinearLayout zoomlayout;
	LinearLayout layoutBottom;
	Button btnOK;
	String action = "send";
	LinearLayout layout;
	GifView mGifView;
	ProgressBar bar;
	TextView rateView;
	FileType mFileType = FileType.PNG;
	boolean finish = false;
	String mUrl = "";
	String mFileName = "";
	String mFilePath="";
	String mParms = "";
	String mMenuID = "";
	JSONArray mArray = null;
	int mIndicator = 0;
	IWXAPI api;
	boolean wxInstall = true;
	//*************************************	
	boolean hasException = false;
	LinearLayout exceptionLayout;
	ImageView exceptionImageView;
	Button btn_browse_left,btn_browse_right;
	EmojHandler mHandler = new EmojHandler();
	long flag = 0;
	Animation left_in;
	Animation right_in;
	Animation left_out;
	Animation right_out;
	Animation pop_in;
	static final float ZOOM_RATIO = 0.25f;
	static final long delayMillis = 5000;
	static final String TAG = EmojBrowseViewActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		btn_browse_left = (Button) findViewById(R.id.btn_browse_left);
		btn_browse_right = (Button) findViewById(R.id.btn_browse_right);
		exceptionLayout = (LinearLayout) findViewById(R.id.emoj_detail_exception);
		exceptionImageView = (ImageView) findViewById(R.id.emoj_detail_exception_img);
		btn_browse_left.setOnTouchListener(this);
		btn_browse_right.setOnTouchListener(this);
		btn_browse_left.setOnClickListener(this);
		btn_browse_right.setOnClickListener(this);
		exceptionImageView.setOnClickListener(this);
		left_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in);
		right_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_in);
		left_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_out);
		right_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_out);
		pop_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
		Initialize();
	}
	protected void Initialize(){
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
		adView = (TAdView) findViewById(R.id.adview);
		wapsAdView = (LinearLayout) findViewById(R.id.wapsAdview);
		wapsCustomAd = (WAPS_CustomAd) findViewById(R.id.wapsCustomAdview);
		wapsMiniAdBackView = (LinearLayout) findViewById(R.id.miniAdviewBackView);
		wapsMiniAdView = (LinearLayout) findViewById(R.id.miniAdview);
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
		api = WXAPIFactory.createWXAPI(EmojBrowseViewActivity.this, AppConstant.WEIXIN_APPID);
		wxInstall = api.isWXAppInstalled();
		InitAd();
	}
	//设置当前图片
	private void SetImage(){
		boolean cache = false;
		finish = false;
		String filename = null;
		if(!mFileName.equals("")){
			FileHelper helper =new FileHelper();
			filename = helper.find(AppConfig.getEmoj(), mFileName);
			if(filename != null){
				cache = true;
				Log.d(TAG, "filename:"+filename);
			}
		}
		//有缓存
		if(cache){
			String prefix = filename.substring(filename.lastIndexOf(".")+1);
			Log.d(TAG, "prefix:"+prefix);
			mFilePath = AppConfig.getEmoj()+filename;
			//gif图
			if(prefix.toLowerCase().equals("gif")){				
				try {
					mFileType = FileType.GIF;
					File file = new File(mFilePath);
					InputStream is = new FileInputStream(file);
					mGifView.setGifImage(is);
					show(1);
					finish = true;
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
			}else {
				//普通图
				mFileType = FileType.PNG;
				initializeControls(mFilePath);
				show(2);
				finish = true;
			}
		}else {
			if(!mUrl.equals("")){
				AsyncDownload(mUrl);
			}
		}
	}
	//返回下一个
	protected String GetNext(){
		if(mArray == null){
			return null;
		}
		try {
			if(mIndicator + 1 < mArray.length()){
				mIndicator++;
				return mArray.getString(mIndicator);
			}else {
				Toast.makeText(EmojBrowseViewActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
				//已经是最后一个了
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG,e.toString());
		}
		return null;
		
	}
	
	//返回上一个
	protected String GetPrev(){
		if(mArray == null){
			return null;
		}
		try {
			if(mIndicator - 1 >= 0){
				mIndicator--;
				Log.d(TAG, "mIndicator:"+mIndicator);
				return mArray.getString(mIndicator);
			}else {
				//已经是第一项了
				Toast.makeText(EmojBrowseViewActivity.this, "已经是第一项了", Toast.LENGTH_SHORT).show();
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}
	protected void showNextImage(){
		if(!finish){
			Toast.makeText(EmojBrowseViewActivity.this, "正在加载,请稍候切换...", Toast.LENGTH_SHORT).show();
			return;
		}
		hideException();
		String name = GetNext();
		if(name != null){
			mFileName = name;
			mUrl = AppConstant.PIC_SERVER_URL + mFileName + AppConstant.PIC_ITEM_FULL_PREFIX;
			RecycleBitmap();
			SetImage();
		}
	}
	protected void showPrevImage(){
		if(!finish){
			Toast.makeText(EmojBrowseViewActivity.this, "正在加载,请稍候切换...", Toast.LENGTH_SHORT).show();
			return;
		}
		hideException();
		String name = GetPrev();
		if(name != null){
			mFileName = name;
			mUrl = AppConstant.PIC_SERVER_URL + mFileName + AppConstant.PIC_ITEM_FULL_PREFIX;
			RecycleBitmap();
			SetImage();
		}
	}
	protected void RecycleBitmap(){
		if(mGifView != null){		
			mGifView.destroy();			
		}
	     if(mZoomView != null){
	        mZoomView.setOnTouchListener(null);
	    }
	     if(mZoomState != null){
	        mZoomState.deleteObservers();
	    }
	}
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.emoj_browse_detail;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:		
			flag = SystemClock.uptimeMillis();
			showBrowseButton();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			hideBrowseButton();
			break;
		case MotionEvent.ACTION_CANCEL:

			break;
		default:
			break;
		}	
		return super.onTouchEvent(event);
	}
	private void showBrowseButton(){
		mHandler.sendEmptyMessage(9);		
	}
	private void hideBrowseButton(){
		mHandler.sendMessageDelayed(Message.obtain(mHandler, 10, flag), delayMillis);
	}	
	private void showException(){
		layout.setVisibility(View.GONE);
		exceptionLayout.setVisibility(View.VISIBLE);
	}
	private void hideException(){
		exceptionLayout.setVisibility(View.GONE);
	}
	class EmojHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO 1-下载中 2-下载完毕 3-出错 9-显示browse 10-隐藏browse 11-加载广告
			switch (msg.what) {
			case 1:
				int rate = (Integer) msg.obj;
				bar.setProgress(rate);
				rateView.setText(rate+"%");
				break;
			case 2:
				finish = true;
				if(mFileType == FileType.GIF){
					File file = new File(mFilePath);
					InputStream is;
					try {
						if(mGifView != null){
						is = new FileInputStream(file);
						mGifView.setGifImage(is);
						show(1);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, e.toString());
					}catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}						
				}else {
					try{
						initializeControls(mFilePath);
						show(2);
					}catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}
					
				}
				break;
			case 3:
				String txt = msg.obj.toString();
				if(!txt.equals("")){
					Toast.makeText(EmojBrowseViewActivity.this, txt, Toast.LENGTH_SHORT).show();
				}
				hasException = true;
				finish = true;
				showException();
				//删除file
				try {
					File file = new File(mFilePath);
					if(file.exists()){
						file.delete();
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
				break;
			case 9:
				if(btn_browse_left.getVisibility() != View.VISIBLE){
				btn_browse_left.setVisibility(View.VISIBLE);				
				btn_browse_right.setVisibility(View.VISIBLE);
				btn_browse_left.startAnimation(left_in);
				btn_browse_right.startAnimation(right_in);
				}
				break;
			case 10:
				long l = (Long) msg.obj;
				Log.d(TAG, "l:"+l+" flag:"+flag);
				if(flag == l){
				btn_browse_left.setVisibility(View.INVISIBLE);
				btn_browse_right.setVisibility(View.INVISIBLE);
				btn_browse_left.startAnimation(left_out);
				btn_browse_right.startAnimation(right_out);
				}
				break;
			default:
				break;
			}
		}
		
	}
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:		
			flag = SystemClock.uptimeMillis();
			showBrowseButton();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "btnup");
			hideBrowseButton();
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.d(TAG, "btncancel");
			break;
		default:
			break;
		}	
		return false;
	}
	
	//****************************************************
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
					WeiXinHelper helper = new WeiXinHelper(EmojBrowseViewActivity.this, api);
					if(mFileType == FileType.GIF){
						//发动态图
						helper.sendEmoj(mFilePath);
					}
					else {
						//发静态图
						helper.sendPng(mFilePath);
					}
					UpdateHeaderClickCount();
					UmengEvent("action002", mFileName, mParms);
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
						UpdateHeaderClickCount();
						UmengEvent("action019", mFileName, mParms);
						setResult(RESULT_OK, intent);
						finish();
					}
					//-----更新事件-----
					if(emotionMode){
						UmengEvent("action021", mFileName, mParms);
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
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			recycle();
		}
		return super.onKeyDown(keyCode, event);
	};
	private void recycle(){
		if(mGifView != null){	
			mGifView.interrupt();
		}
	}
	@Override
	protected void onDestroy() {

		try {
			Log.d(TAG, "onDestroy");
			if(mGifView != null){			
				mGifView.destroy();
			}
			if(mBitmap != null){
		        mBitmap.recycle();
		    }
		     if(mZoomView != null){		    	
		        mZoomView.setOnTouchListener(null);
		    }
		     if(mZoomState != null){
		        mZoomState.deleteObservers();
		    }
		     if(wapsCustomAd != null){
		    	 wapsCustomAd.stop();
		     }
		    mGifView = null;
		    mBitmap = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		super.onDestroy();
	};
	private void SetupWxAction(){
		//直接打开应用
		if(action.equals("send")){
			//隐藏确定按钮
			layoutBottom.setVisibility(View.GONE);			
			setTitleBtn1(R.drawable.mm_title_btn_share_normal, this);			
		}else if (action.equals("get") || action.equals("pick")) {
			//从微信打开
			layoutBottom.setVisibility(View.VISIBLE);
			setTitleBtn1(R.drawable.mm_title_btn_menu_normal, new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openMenu();
				}
			});
		}
	}
	//广告处理
	private void InitAd(){
		AdManager ad = new AdManager(EmojBrowseViewActivity.this);
		if(!ad.getEnable()){
			return;
		}
		AdType adType = ad.getAdType();
		switch (adType) {
		case QQ:
			adView.setVisibility(View.VISIBLE);
			break;
		case WAPS:
			String waps_ad_type = null;
			if(emotionMode){
				waps_ad_type = AppConfig.getWAPS_INDEX2_AD_TYPE(getApplicationContext());				
			}else {
				waps_ad_type = AppConfig.getWAPS_INDEX1_AD_TYPE(getApplicationContext());
			}
			//String waps_ad_type = AppConfig.getWAPS_AD_Type(getApplicationContext());
			if(waps_ad_type.equals("BANNER")){
				wapsAdView.setVisibility(View.VISIBLE);
				new AdView(EmojBrowseViewActivity.this, wapsAdView).DisplayAd();
			}else if (waps_ad_type.equals("MINI")) {
				wapsMiniAdBackView.setVisibility(View.VISIBLE);
				AppConnect.getInstance(this).setAdBackColor(getResources().getColor(R.color.app_panel_bg)); 
				//设置迷你广告广告语颜色
				AppConnect.getInstance(this).setAdForeColor(Color.WHITE); 
				new MiniAdView(this, wapsMiniAdView).DisplayAd(10); 
			}else if (waps_ad_type.equals("CUSTOM")) {
				wapsCustomAd.setVisibility(View.VISIBLE);
				wapsCustomAd.start();
			}
			break;
		default:
			break;
		}
	}
	private void Init(){
		Intent data = getIntent();
//		if(data.getStringExtra(AppConstant.INTENT_PIC_URL) != null){
//			mUrl = data.getStringExtra(AppConstant.INTENT_PIC_URL);
//		}
		if(data.getStringExtra(AppConstant.INTENT_PIC_NAME) != null){
			mFileName = data.getStringExtra(AppConstant.INTENT_PIC_NAME);
			mUrl = AppConstant.PIC_SERVER_URL + mFileName + AppConstant.PIC_ITEM_FULL_PREFIX;
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
		if(data.getStringExtra(AppConstant.INTENT_PIC_ARRAY) != null){
			String json = data.getStringExtra(AppConstant.INTENT_PIC_ARRAY);
			try {
				mArray = new JSONArray(json);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		//来自心情模式
		if(data.getBooleanExtra(AppConstant.INTENT_EMOTION_MODE, false)){
			emotionMode = true;
		}
		SetImage();
		//设置初始指针
		if(mArray != null){
			for(int i=0;i<mArray.length();i++){
				try {
					String item = mArray.getString(i);
					if(item.equals(mFileName)){
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
	/**
	 * 0-加载中 1-gif 2-png
	 * @param i
	 */
	private void show(int i){
		if(i== 0){
			exceptionLayout.setVisibility(View.GONE);
			layout.setVisibility(View.VISIBLE);
			mGifView.setVisibility(View.GONE);
			mZoomView.setVisibility(View.GONE);
			zoomlayout.setVisibility(View.GONE);
		}else if (i== 1) {
			layout.setVisibility(View.GONE);
			mGifView.setVisibility(View.VISIBLE);
			mZoomView.setVisibility(View.GONE);
			zoomlayout.setVisibility(View.GONE);
			if(mGifView != null){
				mGifView.startAnimation(pop_in);
			}
		}else if (i==2) {
			layout.setVisibility(View.GONE);
			mGifView.setVisibility(View.GONE);
			mZoomView.setVisibility(View.VISIBLE);
			zoomlayout.setVisibility(View.VISIBLE);
			if(mZoomView != null){
				mZoomView.startAnimation(pop_in);
			}
		}
	}
	private void AsyncDownload(String url){
		hasException = false;
		bar.setProgress(0);
		rateView.setText("");
		show(0);
		GDUtils.getExecutor(getApplicationContext()).execute(new downloadTask(url));
	}
	class downloadTask implements Runnable{
		private String downloadURL;
		public downloadTask(String url){
			downloadURL = url;
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				//文件类型预判断
				mFileType = FileTypeJudge.getTypeFromURL(downloadURL);
				Log.d(TAG, "filetype:"+mFileType.toString());
				mFilePath = AppConfig.getEmoj()+mFileName+"."+mFileType.toString();
				Log.d(TAG, "mFilePath:"+mFilePath);
				URL imgUrl=new URL(downloadURL);
				URLConnection connection = imgUrl.openConnection();
				byte buffer[] = new byte[1024*10];
				int len=0;
				int hasRead=0;
				int rate=0;
				InputStream is = connection.getInputStream();
				int size=connection.getContentLength();
				File file = new File(mFilePath);
				OutputStream os=new FileOutputStream(file);
				while((len=is.read(buffer))!=-1){  
	                os.write(buffer, 0, len);  
	                hasRead+=len;  
	                rate = (int)(hasRead*100)/size;  
	                //加载中	                
	                mHandler.sendMessage(Message.obtain(mHandler, 1, rate));
	                if(rate >= 100){
	                	mHandler.sendEmptyMessage(2);
	                }
	            }
				is.close();
				os.close();
				
			} 
			catch (MalformedURLException e){
				String msg = "图片地址无法访问...";
				mHandler.sendMessage(Message.obtain(mHandler, 3, msg));
				Log.e(TAG, e.toString());
			}
			catch (IOException e){
				String msg = "表情加载失败,请确认您的设备网络连接正常并且已经插入SD卡";
				mHandler.sendMessage(Message.obtain(mHandler, 3, msg));
				Log.e(TAG, e.toString());
			}
			catch (Exception e) {
				// TODO: handle exception
				mHandler.sendMessage(Message.obtain(mHandler, 3, ""));
				Log.e(TAG, e.toString());
			}
		}
		
	}
	protected void initializeControls(String path){
		mZoomIn = (Button) findViewById(R.id.zoomin);
		mZoomOut = (Button) findViewById(R.id.zoomout);
        mZoomState = new ZoomState();
        File picFile=new File(path);
        if(picFile.exists()){
        	mBitmap = BitmapFactory.decodeFile(path);
        }

        mZoomListener = new SimpleZoomListener();
        mZoomListener.setZoomState(mZoomState);
        mZoomListener.setControlType(ControlType.PAN);
        mZoomListener.setOnTouchListener(this);
       
        mZoomView.setZoomState(mZoomState);
        mZoomView.setImage(mBitmap);       
        mZoomView.setOnTouchListener(mZoomListener);
        mZoomIn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				float zoom=mZoomState.getZoom();
				mZoomState.setZoom(mZoomState.getZoom() * seed);
				mZoomState.notifyObservers();
			}
		});
        
        mZoomOut.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				float zoom=mZoomState.getZoom();
				mZoomState.setZoom(mZoomState.getZoom() / seed);
				mZoomState.notifyObservers();
			}
		});

        resetZoomState();
	}
	 private void resetZoomState() {
	        mZoomState.setPanX(0.5f);
	        mZoomState.setPanY(0.5f);
	        mZoomState.setZoom(1.0f);
	        mZoomState.notifyObservers();
	    }
	//***********分享******************
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//分享
		case R.id.title_btn1:
			share();
			break;
		//下一个	
		case R.id.btn_browse_right:
			showNextImage();
			break;
		//上一个	
		case R.id.btn_browse_left:
			showPrevImage();
			break;
		//重新下载
		case R.id.emoj_detail_exception_img:
			if(mUrl != null){
				hideException();
				AsyncDownload(mUrl);
			}
			break;
		default:
			break;
		}
	}

	protected void openMenu(){
		String[] items = {"分享至微信朋友圈","打包下载本系列表情","其它分享"};
		String[] types = {"TYPE_EXIT","TYPE_BLUE","TYPE_BUTTON"};
		Log.d(TAG, "menuid:"+mMenuID);
		if(mMenuID == null || mMenuID.equals("") || mMenuID.equals("000")){
			items = new String[] {"分享至微信朋友圈","其它分享"};
			types = new String[] {"TYPE_EXIT","TYPE_BUTTON"};
			MMAlert.showAlert(EmojBrowseViewActivity.this, "操作", items, types, new OnAlertSelectId() {			
				public void onClick(int whichButton) {
					// TODO Auto-generated method stub
					if(whichButton == 0){
						ShareToWx_Circle();
						
					}else if (whichButton == 1) {
						//其它分享
						shareOther();
						
					}
				}
			}, new OnCancelListener() {
				
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			return;
		}
		MMAlert.showAlert(EmojBrowseViewActivity.this, "操作", items, types, new OnAlertSelectId() {			
			public void onClick(int whichButton) {
				// TODO Auto-generated method stub
				if(whichButton == 0){
					ShareToWx_Circle();
					
				}else if (whichButton == 1) {
					downloadPackage();					
					
				}else if (whichButton == 2) {
					//其它分享
					shareOther();
				}
			}
		}, new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		
	}
	protected void share(){
		String[] items = {"发送给微信好友","分享至微信朋友圈","其它分享","打包下载本系列表情"};
		String[] types = {"TYPE_EXIT","TYPE_BLUE","TYPE_BUTTON","TYPE_BUTTON"};
		if(mMenuID == null || mMenuID.equals("") || mMenuID.equals("000")){
			items = new String[] {"发送给微信好友","分享至微信朋友圈","其它分享"};
			types = new String[] {"TYPE_EXIT","TYPE_BLUE","TYPE_BUTTON"};
		}
		MMAlert.showAlert(EmojBrowseViewActivity.this, "分享", items, types, new OnAlertSelectId() {			
			public void onClick(int whichButton) {
				// TODO Auto-generated method stub
				if(whichButton == 0){
					//分享到微信好友
					ShareToWX_Friends();
					
				}else if (whichButton == 1) {
					//分享到微信朋友圈
					ShareToWx_Circle();
				}else if(whichButton == 2){
					//其它分享
					shareOther();
				}else if(whichButton == 3){
					//打包下载本系列表情
					downloadPackage();
				}
			}
		}, new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
	}
	
	//分享至微信好友
	protected void ShareToWX_Friends(){
		if(!wxInstall){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowseViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		if(!finish){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowseViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		try {
			WeiXinHelper helper = new WeiXinHelper(getApplicationContext(), api);
			if(mFileType == FileType.GIF){
				helper.shareEmoj(mFilePath);
			}else {
				helper.shareIMG(mFilePath);
			}	
			RegularEmojManager manager = new RegularEmojManager(getApplicationContext());
			manager.add(mFileName, mParms);
			UpdateHeaderClickCount();
			UmengEvent("action001", mFileName, mParms);
			//-----更新事件-----
			if(emotionMode){
				UmengEvent("action021", mFileName, mParms);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG,e.toString());
		}
		
	}
	
	//分享到微信朋友圈
	protected void ShareToWx_Circle(){
		if(!wxInstall){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowseViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		if(!finish){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowseViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		try {
			WeiXinHelper helper = new WeiXinHelper(EmojBrowseViewActivity.this, api);
			if(mFileType == FileType.GIF){
				helper.shareWebpageToCircle(mFilePath, mUrl);
			}else {
				helper.shareIMGToCircle(mFilePath);
			}
			RegularEmojManager manager = new RegularEmojManager(getApplicationContext());
			manager.add(mFileName, mParms);
			UpdateHeaderClickCount();
			UmengEvent("action003", mFileName,mParms);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	
	private void shareOther(){
		try {
			WeiXinHelper helper = new WeiXinHelper(EmojBrowseViewActivity.this, api);
			helper.shareToOther(mFilePath, "#微信表情包#这个表情是不是很有趣?", "来自微信表情包的分享");
			UpdateHeaderClickCount();
			UmengEvent("action008", mFileName, mParms);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	//点击计数
	private void UpdateHeaderClickCount(){
		Log.d(TAG, "mMenuID:"+mMenuID);
		if(mMenuID != ""){
			AppConfig.setHeaderClickCount(getApplicationContext(), mMenuID);
			int count = AppConfig.getHeaderClickCount(getApplicationContext(), mMenuID);
			//Log.d(TAG, mParms + ":" + count);
		}
	}
	private void UmengEvent(String eventid,String filename,String parms){
		Map<String, String> map = new HashMap<String, String>();
		map.put("parms", parms);
		map.put("filename", filename);
		MobclickAgent.onEvent(EmojBrowseViewActivity.this, eventid, map);
	}
	
	//***********表情打包下载*************
	//表情打包下载
	protected void downloadPackage(){
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(EmojBrowseViewActivity.this, R.string.download_sdcard_mounted, Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			final PackageDownloader downloader = new PackageDownloader(EmojBrowseViewActivity.this);
			String text = String.format("打包下载\"%s\"表情?", mParms);
			final String menuid = mMenuID;
			Dialog dialog = DialogFactory.createTwoButtonDialog(EmojBrowseViewActivity.this, text, "确定", "取消", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();					
					downloader.download(menuid);
				}
			}, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	
}
