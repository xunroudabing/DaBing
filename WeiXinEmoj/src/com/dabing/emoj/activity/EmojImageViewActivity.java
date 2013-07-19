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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.ant.liao.GifView;
import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.activity.EmojViewActivity.MyHandler;
import com.dabing.emoj.activity.EmojViewActivity.downloadTask;
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
import com.dabing.emoj.utils.QStr;
import com.dabing.emoj.utils.RegularEmojManager;
import com.dabing.emoj.widget.EmojImageView;
import com.dabing.emoj.wxapi.WeiXinHelper;
import com.tencent.exmobwin.banner.TAdView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.uikit.MMAlert;
import com.tencent.mm.sdk.uikit.MMAlert.OnAlertSelectId;
import com.umeng.analytics.MobclickAgent;


public class EmojImageViewActivity extends BaseActivity implements OnClickListener {
	 /** Constant used as menu item id for setting zoom control type */
    private static final int MENU_ID_ZOOM = 0;
    /** Constant used as menu item id for setting pan control type */
    private static final int MENU_ID_PAN = 1;
    /** Constant used as menu item id for resetting zoom state */
    private static final int MENU_ID_RESET = 2;
    /** Image zoom view */
    private ImageZoomView mZoomView;
    private GifView mGifView;
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
    //-------
    Button btnok;
    TAdView adView;
    String action = "send";
    LinearLayout layout;
    LinearLayout zoomlayout;
    LinearLayout layoutBottom;
    TextView textView;
	ProgressBar bar;
	TextView rateView;
	MyHandler mHandler = new MyHandler();
	FileType mFileType = FileType.PNG;
	boolean cache = false;
	boolean finish = false;
	String mUrl = "";
	String mFileName = "";
	String mFilePath="";
	String mText = "";
	IWXAPI api;
	boolean wxInstall = true;
	static final String TAG = EmojImageViewActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mGifView = (GifView) findViewById(R.id.gifview);
		textView = (TextView) findViewById(R.id.emoj_img_txt);
		layout = (LinearLayout) findViewById(R.id.emoj_detail_l1);
		zoomlayout = (LinearLayout) findViewById(R.id.emoj_zoom_layout);
		layoutBottom = (LinearLayout) findViewById(R.id.emoj_detail_bottom);
		bar = (ProgressBar) findViewById(R.id.image_zoom_view_progress);
		rateView = (TextView) findViewById(R.id.image_zoom_view_rate);
		mZoomView = (ImageZoomView)findViewById(R.id.emoj_zoomview);
		adView = (TAdView) findViewById(R.id.adview);
		btnok = (Button) findViewById(R.id.emoj_detail_btnok);
		btnok.setOnClickListener(btnListener);
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Initialize();
		SetupWxAction();
		api = WXAPIFactory.createWXAPI(EmojImageViewActivity.this, AppConstant.WEIXIN_APPID);
		wxInstall = api.isWXAppInstalled();
		InitAd();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.emoj_img_detail;
	}
	@Override
	protected void onDestroy() {
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
		 super.onDestroy();
	};
	
	//发送至微信
	private OnClickListener btnListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				if(finish){
					WeiXinHelper helper = new WeiXinHelper(EmojImageViewActivity.this, api);
					if(mFileType == FileType.GIF){
						helper.sendEmoj(mFilePath);
					}else {
						helper.sendIMGPath(mFilePath);
					}
					UmengEvent("action007");
					setResult(RESULT_OK);
					finish();
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
		}else if (action.equals("get")) {
			layoutBottom.setVisibility(View.VISIBLE);
		}
	}
	//广告处理
	private void InitAd(){
		if(AppConfig.getAdvertise_on(getApplicationContext())){
			adView.setVisibility(View.VISIBLE);
		}else {
			adView.setVisibility(View.GONE);
		}
	}
	private void Initialize(){
		Intent data = getIntent();
		if(data.getStringExtra(AppConstant.INTENT_PIC_URL) != null){
			mUrl = data.getStringExtra(AppConstant.INTENT_PIC_URL);
			mFileName = QStr.getPicNameFromURL(mUrl);
		}
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);
			Log.d(TAG, "action:"+action);
		}
		if(data.getStringExtra(AppConstant.INTENT_TEXT) != null){
			mText = data.getStringExtra(AppConstant.INTENT_TEXT);
			textView.setText(mText);
		}
		Log.d(TAG, "mURL:"+mUrl+" mFileName:"+mFileName);
		String filename = null;
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
				mFileType = FileType.PNG;
				initializeControls(mFilePath);
				show(2);
				finish = true;
			}
		}else {
			//下载
			if(!mUrl.equals("")){
				AsyncDownload(mUrl);
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
				textView.setVisibility(View.GONE);
			}else if (i== 1) {
				layout.setVisibility(View.GONE);
				mGifView.setVisibility(View.VISIBLE);
				textView.setVisibility(View.VISIBLE);
				mZoomView.setVisibility(View.GONE);
				zoomlayout.setVisibility(View.GONE);
			}else if (i==2) {
				layout.setVisibility(View.GONE);
				mGifView.setVisibility(View.GONE);
				textView.setVisibility(View.GONE);
				mZoomView.setVisibility(View.VISIBLE);
				zoomlayout.setVisibility(View.VISIBLE);
			}
		}
	private void AsyncDownload(String url){		
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
					InputStream is = new URL(downloadURL).openStream();
					mFileType = FileTypeJudge.getType(is);
					Log.d(TAG, "filetype:"+mFileType.toString());
					mFilePath = AppConfig.getTemp()+mFileName+"."+mFileType.toString();
					Log.d(TAG, "mFilePath:"+mFilePath);
					URL imgUrl=new URL(downloadURL);
					URLConnection connection = imgUrl.openConnection();
					int size=connection.getContentLength();
					byte buffer[] = new byte[1024*4];
					int len=0;
					int hasRead=0;
					int rate=0;
					is = connection.getInputStream();
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
					String msg = "图片无法写入,请检查您的设备是否已插入SD卡";
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
	 class MyHandler extends Handler{

			/* (non-Javadoc)
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				// TODO 1-下载中 2-下载完毕 3-出错
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
							is = new FileInputStream(file);
							mGifView.setGifImage(is);
							show(1);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
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
						Toast.makeText(EmojImageViewActivity.this, txt, Toast.LENGTH_SHORT).show();
					}
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
				default:
					break;
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
			
		default:
			break;
		}
	}
	
	protected void share(){
		String[] items = {"发送给微信好友","分享至微信朋友圈","其它分享"};
		String[] types = {"TYPE_EXIT","TYPE_BLUE","TYPE_BUTTON"};
		MMAlert.showAlert(EmojImageViewActivity.this, "分享", items, types, new OnAlertSelectId() {			
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
			DialogFactory.createCommonDialog(EmojImageViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		if(!finish){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojImageViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
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
			DialogFactory.createCommonDialog(EmojImageViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		if(!finish){
			String string = getResources().getString(R.string.wx_not_install);
			DialogFactory.createCommonDialog(EmojImageViewActivity.this, string,"确定", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

			}).show();
			return;
		}
		try {
			WeiXinHelper helper = new WeiXinHelper(EmojImageViewActivity.this, api);
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
			WeiXinHelper helper = new WeiXinHelper(EmojImageViewActivity.this, api);
			helper.shareToOther(mFilePath, mText, "来自微信表情包的分享");
			UmengEvent("action011");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private void UmengEvent(String eventid){
		MobclickAgent.onEvent(EmojImageViewActivity.this, eventid);
	}
}
