package com.dabing.emoj.activity;

import greendroid.util.GDUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
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

import com.dabing.ads.*;

import com.ant.liao.GifView;
import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.advertise.AdManager;
import com.dabing.emoj.advertise.MixAdView;
import com.dabing.emoj.advertise.WAPS_CustomAd;
import com.dabing.emoj.advertise.AdManager.AdType;
import com.dabing.emoj.exception.ExceptionManager;
import com.dabing.emoj.imagezoomview.ImageZoomView;
import com.dabing.emoj.imagezoomview.SimpleZoomListener;
import com.dabing.emoj.imagezoomview.ZoomState;
import com.dabing.emoj.imagezoomview.SimpleZoomListener.ControlType;
import com.dabing.emoj.provider.BaseRequest;
import com.dabing.emoj.provider.IRequest;
import com.dabing.emoj.provider.MyRequest;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileType;
import com.dabing.emoj.utils.FileTypeJudge;
import com.dabing.emoj.utils.JsonHelper;
import com.dabing.emoj.utils.QStr;
import com.dabing.emoj.wxapi.WeiXinHelper;
import com.tencent.exmobwin.banner.TAdView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.uikit.MMAlert;
import com.tencent.mm.sdk.uikit.MMAlert.OnAlertSelectId;
import com.umeng.analytics.MobclickAgent;
/**
 * 趣图 
 * @author DaBing
 *
 */
public class EmojBrowsePictureActivity extends BaseActivity implements OnTouchListener,OnClickListener,IRequest {
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
    boolean hasException = false;
    LinearLayout exceptionLayout;
    ImageView exceptionImageView;
    TextView textView;
    RelativeLayout container;
    TAdView adView;
    MixAdView mAdView;//自定义广告
    LinearLayout wapsAdView;//万普广告
    WAPS_CustomAd wapsCustomAd;//万普自定义广告
    LinearLayout wapsMiniAdBackView;
    LinearLayout wapsMiniAdView;//万普迷你广告
	LinearLayout zoomlayout;
	Button btnTxt;//显示文字按钮
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
	String mText = "";
	String mTweetID = "";
	JSONArray mArray = null;
	int mIndicator = 0;
	IWXAPI api;
	boolean wxInstall = true;
	//*************************************
	boolean requestFlag = true;
	Dialog mDialog;
	BaseRequest mRequest;
	Button btn_browse_left,btn_browse_right;
	EmojHandler mHandler = new EmojHandler();
	long flag = 0;
	Animation left_in;
	Animation right_in;
	Animation left_out;
	Animation right_out;
	Animation pop_in;
	static final float ZOOM_RATIO = 0.75f;
	static final long delayMillis = 5000;
	static final String TAG = EmojBrowsePictureActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		btn_browse_left = (Button) findViewById(R.id.btn_browse_left);
		btn_browse_right = (Button) findViewById(R.id.btn_browse_right);
		btn_browse_left.setOnTouchListener(this);
		btn_browse_right.setOnTouchListener(this);
		btn_browse_left.setOnClickListener(this);
		btn_browse_right.setOnClickListener(this);
		left_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in);
		right_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_in);
		left_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_out);
		right_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_out);
		pop_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
		Initialize();
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.emoj_browse_picture_detail;
	}
	protected void Initialize(){
		mAdView = (MixAdView) findViewById(R.id.mixAdView);
		mAdView.setAdTag("ad_index4");
		mAdView.InitAD();
		exceptionLayout = (LinearLayout) findViewById(R.id.emoj_detail_exception);
		exceptionImageView = (ImageView) findViewById(R.id.emoj_detail_exception_img);
		exceptionImageView.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.emoj_img_txt);
		mGifView = (GifView) findViewById(R.id.gifview);
		mGifView.setZoomRatio(ZOOM_RATIO);
		layout = (LinearLayout) findViewById(R.id.emoj_detail_l1);
		bar = (ProgressBar) findViewById(R.id.image_zoom_view_progress);
		rateView = (TextView) findViewById(R.id.image_zoom_view_rate);
		btnOK = (Button) findViewById(R.id.emoj_detail_btnok);
		layoutBottom = (LinearLayout) findViewById(R.id.emoj_detail_bottom);
		btnTxt = (Button) findViewById(R.id.btn_txt);
		zoomlayout = (LinearLayout) findViewById(R.id.emoj_zoom_layout);
		mZoomView = (ImageZoomView)findViewById(R.id.emoj_zoomview);
		container = (RelativeLayout) findViewById(R.id.emoj_browse_container);
		//adView = (TAdView) findViewById(R.id.adview);
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
		btnTxt.setOnClickListener(this);
		Init();
		SetupWxAction();
		api = WXAPIFactory.createWXAPI(EmojBrowsePictureActivity.this, AppConstant.WEIXIN_APPID);
		wxInstall = api.isWXAppInstalled();
		//InitAd();
	}
	private void Init(){
		Intent data = getIntent();
//		if(data.getStringExtra(AppConstant.INTENT_PIC_URL) != null){
//			mUrl = data.getStringExtra(AppConstant.INTENT_PIC_URL);
//		}
		if(data.getStringExtra(AppConstant.INTENT_PIC_URL) != null){
			mUrl = data.getStringExtra(AppConstant.INTENT_PIC_URL);
			mFileName = QStr.getPicNameFromURL(mUrl);
		}
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);
			Log.d(TAG, "action:"+action);
		}
		if(data.getStringExtra(AppConstant.INTENT_PIC_PARMS) != null){
			mParms = data.getStringExtra(AppConstant.INTENT_PIC_PARMS);
		}
		if(data.getStringExtra(AppConstant.INTENT_TEXT) != null){
			mText = data.getStringExtra(AppConstant.INTENT_TEXT);
			textView.setText(mText);
		}
		if(data.getStringExtra(AppConstant.INTENT_TWEETID) != null){
			mTweetID = data.getStringExtra(AppConstant.INTENT_TWEETID);
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
		if(data.getParcelableExtra(AppConstant.INTENT_REQUESTOBJ) != null){
			mRequest = data.getParcelableExtra(AppConstant.INTENT_REQUESTOBJ);
			mRequest.setContext(getApplicationContext());
			mRequest.setOnRequestListener(this);
		}
		SetImage();
		//设置初始指针
		if(mArray != null){
			for(int i=0;i<mArray.length();i++){
				try {
					JSONObject object = mArray.getJSONObject(i);
					String id = object.getString("id");
					if(id.equals(mTweetID)){
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
	//***********切换************
	//返回下一个
	protected JSONObject GetNext(){
		if(mArray == null){
			return null;
		}
		try {
			if(mIndicator + 1 < mArray.length()){
				mIndicator++;
				return mArray.getJSONObject(mIndicator);
			}else {
				if(mRequest == null){
					Toast.makeText(EmojBrowsePictureActivity.this, "已经是最后一项了", Toast.LENGTH_SHORT).show();
					return null;
				}
				if(requestFlag){
					requestFlag = false;
					mRequest.beginRequest();
				}
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
	protected JSONObject GetPrev(){
		if(mArray == null){
			return null;
		}
		try {
			if(mIndicator - 1 >= 0){
				mIndicator--;
				return mArray.getJSONObject(mIndicator);
			}else {
				//已经是第一项了
				Toast.makeText(EmojBrowsePictureActivity.this, "已经是第一项了", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(EmojBrowsePictureActivity.this, "正在加载,请稍候切换...", Toast.LENGTH_SHORT).show();
			return;
		}
		hideException();
		JSONObject object = GetNext();
		if(object != null){
			try {
				String url = "";
				if(!object.isNull("image") && !object.get("image").equals("null")){
					String json = object.getString("image");
					//微频道
					if(json.startsWith("{")){
						if(object.getJSONObject("image").has("info")){
							JSONArray info = object.getJSONObject("image").getJSONArray("info");
							JSONObject o = info.getJSONObject(0);
							url = o.getJSONArray("url").getString(0);
						}
					}else {
						url = object.getJSONArray("image").getString(0);
					}
				}
				if(!url.equals("")){
					mUrl = url + AppConstant.PIC_ITEM_FULL_PREFIX;
					mFileName = QStr.getPic(url);
					mText = object.getString("origtext");
					RecycleBitmap();
					SetImage();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}		
			
		}
	}
	protected void showPrevImage(){
		if(!finish){
			Toast.makeText(EmojBrowsePictureActivity.this, "正在加载,请稍候切换...", Toast.LENGTH_SHORT).show();
			return;
		}
		hideException();
		JSONObject object = GetPrev();
		if(object != null){
			try {
				String url = "";
				if(!object.isNull("image") && !object.get("image").equals("null")){
					String json = object.getString("image");
					//微频道
					if(json.startsWith("{")){
						if(object.getJSONObject("image").has("info")){
							JSONArray info = object.getJSONObject("image").getJSONArray("info");
							JSONObject o = info.getJSONObject(0);
							url = o.getJSONArray("url").getString(0);
						}
					}else {
						url = object.getJSONArray("image").getString(0);
					}
				}
				if(!url.equals("")){
					mUrl = url + AppConstant.PIC_ITEM_FULL_PREFIX;
					mFileName = QStr.getPic(url);
					mText = object.getString("origtext");
					RecycleBitmap();
					SetImage();
//					mGifView.startAnimation(pop_in);
//					mZoomView.startAnimation(pop_in);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}		
			
		}
	}
	//*************************
	protected void RecycleBitmap(){
		if(mGifView != null){		
			mGifView.destroy();			
		}
		if(mZoomView != null){
			mZoomView.destroy();
		}
		if(mBitmap != null){
			mBitmap.recycle();
			mBitmap = null;
		}
	     if(mZoomView != null){
	        mZoomView.setOnTouchListener(null);
	    }
	     if(mZoomState != null){
	        mZoomState.deleteObservers();
	    }
	}
	//设置当前图片
	private void SetImage(){
		boolean cache = false;
		finish = false;
		String filename = null;
		textView.setText(mText);
		if(!mFileName.equals("")){
			FileHelper helper =new FileHelper();
			filename = helper.find(AppConfig.getTemp(), mFileName);
			if(filename != null){
				cache = true;
				Log.d(TAG, "filename:"+filename);
			}
		}
		//有缓存
		if(cache){
			String prefix = filename.substring(filename.lastIndexOf(".")+1);
			Log.d(TAG, "prefix:"+prefix);
			mFilePath = AppConfig.getTemp()+filename;
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
	private void recycle(){
		if(mGifView != null){	
		   mGifView.interrupt();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			recycle();
		}
		return super.onKeyDown(keyCode, event);
	};
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
		    mGifView = null;
		    mBitmap = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		super.onDestroy();
	};
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
			// TODO 1-下载中 2-下载完毕 3-出错 9-显示browse 10-隐藏browse
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
					Toast.makeText(EmojBrowsePictureActivity.this, txt, Toast.LENGTH_SHORT).show();
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
				//Log.d(TAG, "l:"+l+" flag:"+flag);
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
			hideBrowseButton();
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}	
		return false;
	}
	
	//******************************************
	//发送至微信
	private OnClickListener btnListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				if(finish){
					if(action.equals("get")){
						WeiXinHelper helper = new WeiXinHelper(EmojBrowsePictureActivity.this, api);
						if(mFileType == FileType.GIF){
							helper.sendEmoj(mFilePath);
						}else {
							helper.sendIMGPath(mFilePath);
						}
						UmengEvent("action007");
						setResult(RESULT_OK);
						finish();
					}else if (action.equals("pick")) {
						//其他
						File file = new File(mFilePath);
						Intent intent = new Intent();
						intent.setData(Uri.fromFile(file));
						UmengEvent("action020");
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
	private void SetupWxAction(){
		//直接打开应用
		if(action.equals("send")){
			//隐藏确定按钮
			layoutBottom.setVisibility(View.GONE);
			setTitleBtn1(R.drawable.mm_title_btn_share_normal, this);				
		}else if (action.equals("get") || action.equals("pick")) {
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
		AdManager ad = new AdManager(EmojBrowsePictureActivity.this);
		if(!ad.getEnable()){
			return;
		}
		AdType adType = ad.getAdType();
		switch (adType) {
		case QQ:
			adView.setVisibility(View.VISIBLE);
			break;
		case WAPS:
			String waps_ad_type = AppConfig.getWAPS_INDEX3_AD_TYPE(getApplicationContext());
			//String waps_ad_type = AppConfig.getWAPS_AD_Type(getApplicationContext());
			if(waps_ad_type.equals("BANNER")){
				wapsAdView.setVisibility(View.VISIBLE);
				new AdView(EmojBrowsePictureActivity.this, wapsAdView).DisplayAd();
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
	        mZoomState.setZoom(1f);
	        mZoomState.notifyObservers();
	    }
	 
	 /**
		 * 0-加载中 1-gif 2-png
		 * @param i
		 */
		private void show(int i){
			if(i== 0){
				layout.setVisibility(View.VISIBLE);
				mGifView.setVisibility(View.GONE);
				mZoomView.setVisibility(View.GONE);
				zoomlayout.setVisibility(View.GONE);
				btnTxt.setVisibility(View.GONE);
				textView.setVisibility(View.GONE);
			}else if (i== 1) {
				layout.setVisibility(View.GONE);
				mGifView.setVisibility(View.VISIBLE);
				textView.setVisibility(View.VISIBLE);
				mZoomView.setVisibility(View.GONE);
				zoomlayout.setVisibility(View.GONE);
				btnTxt.setVisibility(View.GONE);
				if(mGifView != null){
					mGifView.startAnimation(pop_in);
				}
			}else if (i==2) {
				layout.setVisibility(View.GONE);
				mGifView.setVisibility(View.GONE);
				textView.setVisibility(View.GONE);
				mZoomView.setVisibility(View.VISIBLE);
				zoomlayout.setVisibility(View.VISIBLE);
				btnTxt.setVisibility(View.VISIBLE);
				if(mZoomView != null){
					mZoomView.startAnimation(pop_in);
				}
			}
		}
	private void AsyncDownload(String url){		
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
					mFilePath = AppConfig.getTemp()+mFileName+"."+mFileType.toString();
					Log.d(TAG, "mFilePath:"+mFilePath);
					URL imgUrl=new URL(downloadURL);
					URLConnection _connection = imgUrl.openConnection();
					HttpURLConnection connection = (HttpURLConnection) _connection; 
					//application/x-www-form-urlencoded;charset=utf-8
					//application/x-java-serialized-object
					//application/octet-stream
					//connection.setRequestProperty("Content-type", "application/octet-stream"); 
					
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
					String msg = "图片加载失败,请确认您的设备网络连接正常并且已经插入SD卡";
					mHandler.sendMessage(Message.obtain(mHandler, 3, msg));
					Log.e(TAG, e.toString());
				}
				catch (Exception e) {
					// TODO: handle exception
					mHandler.sendMessage(Message.obtain(mHandler, 3, "网络连接异常"));
					Log.e(TAG, e.toString());
				}
			}
			
		}
	
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
			//显示文字	
			case R.id.btn_txt:
				showText();
				break;
			default:
				break;
			}
		}
	protected void share(){
		String[] items = {"发送给微信好友","分享至微信朋友圈","其它分享","保存到我的相册"};
		String[] types = {"TYPE_EXIT","TYPE_BLUE","TYPE_BUTTON","TYPE_BUTTON"};
		MMAlert.showAlert(EmojBrowsePictureActivity.this, "分享", items, types, new OnAlertSelectId() {			
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
				}else if (whichButton == 3) {
					//保存到相册
					saveMedia();
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
			DialogFactory.createCommonDialog(EmojBrowsePictureActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		if(!finish){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowsePictureActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
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
			UmengEvent("action005");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG,e.toString());
		}
		
	}
	
	//分享到微信朋友圈
	protected void ShareToWx_Circle(){
		if(!wxInstall){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowsePictureActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		if(!finish){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowsePictureActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		try {
			WeiXinHelper helper = new WeiXinHelper(EmojBrowsePictureActivity.this, api);
			if(mFileType == FileType.GIF){
				helper.shareWebpageToCircle(mFilePath, mUrl,mText,"");
			}else {
				helper.shareIMGToCircle(mFilePath);
			}
			UmengEvent("action006");
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private void shareOther(){
		try {
			WeiXinHelper helper = new WeiXinHelper(EmojBrowsePictureActivity.this, api);
			helper.shareToOther(mFilePath, mText, "来自微信表情包的分享");
			UmengEvent("action011");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private void UmengEvent(String eventid){
		MobclickAgent.onEvent(EmojBrowsePictureActivity.this, eventid);
	}
	//IRequest接口
	public void onBind(String response) {
		// TODO Auto-generated method stub
		
	}

	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		ExceptionManager.handle(EmojBrowsePictureActivity.this, ex);
	}

	public void onHasNext(String hasnext) {
		// TODO Auto-generated method stub
		if(hasnext.equals("0")){
			Toast.makeText(EmojBrowsePictureActivity.this, "图片全部拉取完毕", Toast.LENGTH_SHORT).show();
		}
	}

	public void onLoading(String pageflag) {
		// TODO Auto-generated method stub
		if(mDialog == null){
			mDialog = DialogFactory.creatRequestDialog(EmojBrowsePictureActivity.this, "加载更多...");
		}
		mDialog.show();
	}

	public void onRefresh(String response) {
		// TODO Auto-generated method stub
		Log.d(TAG, "response:"+response);
		try {
			JSONObject object = new JSONObject(response);
			JSONArray array = object.getJSONObject("data").getJSONArray("info");
			JSONArray data = new JsonHelper().Filter(array);
			if(mArray != null){
				for(int i =0;i<data.length();i++){
					mArray.put(data.getJSONObject(i));
				}
				if(data.length() > 0){
					showNextImage();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	public void onRequestEnd() {
		// TODO Auto-generated method stub
		requestFlag = true;
		try {
			if(mDialog != null){
				mDialog.dismiss();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//保存到相册
	protected void saveMedia(){
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String msg = getResources().getString(R.string.download_sdcard_mounted);
			DialogFactory.createCommonDialog(EmojBrowsePictureActivity.this, msg,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		if(!finish){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojBrowsePictureActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		File file = new File(mFilePath);
		String filename = mFilePath.substring(mFilePath.lastIndexOf("/")+1);
		String savepath = AppConfig.getAblum() + filename;
		if(file.exists()){
			try {
				InputStream is = new FileInputStream(file);
				OutputStream os = new FileOutputStream(savepath);
				byte[] buffer = new byte[1024*4];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);					
				}
				is.close();
				os.close();
				Toast.makeText(EmojBrowsePictureActivity.this, String.format("已保存至%s", savepath), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	protected void openMenu(){
		String[] items = {"分享至微信朋友圈","其它分享","保存到我的相册"};
		String[] types = {"TYPE_EXIT","TYPE_BUTTON","TYPE_BUTTON"};	
		MMAlert.showAlert(EmojBrowsePictureActivity.this, "操作", items, types, new OnAlertSelectId() {			
			public void onClick(int whichButton) {
				// TODO Auto-generated method stub
				if(whichButton == 0){
					ShareToWx_Circle();					
				}else if (whichButton == 1) {
					shareOther();										
				}else if (whichButton == 2) {
					saveMedia();
				}
			}
		}, new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		
	}
	/**
	 * 显示文字
	 */
	protected void showText(){
		DialogFactory.createTextTransparentBack(EmojBrowsePictureActivity.this, mText, null);
	}
}
