package com.dabing.emoj.wxapi;

import greendroid.util.GDUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;

import org.apache.http.client.utils.URIUtils;



import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.QStr;
import com.tencent.mm.sdk.channel.MMessage;
import com.tencent.mm.sdk.openapi.GetMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXEmojiObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.URLUtil;

public class WeiXinHelper {
	public interface WeiXinListener{
		void OnMakeThumbSuccess(Bitmap bitmap);
		void onProgress(int rate);
		void onDownload(Bitmap bitmap);
	}
	private static final long MAX_BYTE_SIZE = 30720;
	private static final int THUMB_SIZE = 100;
	private static final int THUMB_MAX_WIDTH = 100;
	private static final int THUMB_MAX_HEIGHT = 150;
	private Context context;
	private IWXAPI mAPI;
	static final String TAG = WeiXinHelper.class.getSimpleName();
	public WeiXinHelper(Context c,IWXAPI api){
		context = c;
		mAPI = api;
	}
	private String getTransaction() {
		final GetMessageFromWX.Req req = new GetMessageFromWX.Req(((Activity)context).getIntent().getExtras());
		String username = req.username;
		Log.d(TAG, "username:"+username);
		return req.transaction;
	}
	public void sendEmoj(String filepath){
		File file = new File(filepath);
		if(!file.exists()){
			return;
		}		 
		WXEmojiObject emoji = new WXEmojiObject();
		emoji.emojiPath = filepath;
		WXMediaMessage msg = new WXMediaMessage(emoji);
		msg.title = "分享自微信表情包";
		msg.description = "";
		Bitmap bitmap  = BitmapFactory.decodeFile(filepath);
		Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);		
		byte[] thumbData = Util.bmpToByteArray(thumbBmp, true);
		Log.d(TAG, "thumbData:"+thumbData.length);
		msg.thumbData = thumbData;	
		bitmap.recycle();
		GetMessageFromWX.Resp resp = new GetMessageFromWX.Resp();
		String trans = getTransaction();
		Log.d(TAG, "trans:"+trans);
		resp.transaction = trans;
		resp.message = msg;
		mAPI.sendResp(resp);
	
	}
	/**
	 * 将静态图片以emoj的形式发送
	 * @param filepath
	 */
	public void sendPng(String filepath){
		File file = new File(filepath);
		if(!file.exists()){
			return;
		}		 
		long filesize = file.length();
		WXEmojiObject emoji = new WXEmojiObject();
		emoji.emojiPath = filepath;
		WXMediaMessage msg = new WXMediaMessage(emoji);
		msg.title = "分享自微信表情包";
		msg.description = "";
		Log.d(TAG, "sendPng filesize:"+filesize);
		//小于30k直接发送
		if(filesize < MAX_BYTE_SIZE){
			Bitmap bitmap  = BitmapFactory.decodeFile(filepath);
			byte[] thumbData = Util.bmpToByteArray(bitmap,true);
			msg.thumbData = thumbData;
			Log.d(TAG, "sendPng thumbData:"+thumbData.length);
		}else {
			Bitmap bitmap  = BitmapFactory.decodeFile(filepath);
			Bitmap thumbBmp = Util.resizeBitmap(bitmap, 200, 300);		
			byte[] thumbData = Util.bmpToByteArray(thumbBmp,true);
			if(thumbData.length > 30000){
				thumbBmp = Util.resizeBitmap(bitmap, 150, 225);
				thumbData = Util.bmpToByteArray(thumbBmp,true);
			}
			Log.d(TAG, "sendPng thumbData:"+thumbData.length);
			msg.thumbData = thumbData;	
			bitmap.recycle();
		}
		
		GetMessageFromWX.Resp resp = new GetMessageFromWX.Resp();
		String trans = getTransaction();
		Log.d(TAG, "trans:"+trans);
		resp.transaction = trans;
		resp.message = msg;
		mAPI.sendResp(resp);
	}
	public void sendIMGPath(String filepath){
		File file = new File(filepath);
		if(!file.exists()){
			return;
		}
		WXImageObject obj = new WXImageObject();
		obj.setImagePath(filepath);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = obj;
		msg.title = "分享自微信表情包";
		Bitmap bitmap  = BitmapFactory.decodeFile(filepath);
		Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
		//Log.d(TAG, "before:"+bitmap.getWidth()+"x"+bitmap.getHeight() + "after:"+thumbBmp.getWidth()+"x"+thumbBmp.getHeight());
		byte[] thumbData = Util.bmpToByteArray(thumbBmp, true);
		if(thumbData.length > 30000){
			thumbBmp = Util.resizeBitmap(bitmap, 50, 100);
			thumbData = Util.bmpToByteArray(thumbBmp, true);
		}
		bitmap.recycle();
		msg.thumbData = thumbData;
		GetMessageFromWX.Resp resp = new GetMessageFromWX.Resp();
		resp.transaction = getTransaction();
		resp.message = msg;
		mAPI.sendResp(resp);
	}
	/**
	 * 用绝对路径的方式分享图片
	 * @param path
	 * @param scene
	 */
	public void shareIMGPath(String path,int scene){
		File file = new File(path);
		if(!file.exists()){
			return;
		}
		WXImageObject obj = new WXImageObject();
		obj.setImagePath(path);
		WXMediaMessage msg=new WXMediaMessage();
		msg.mediaObject = obj;
		msg.title = "分享自微信表情包";
		Bitmap bitmap  = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
		//Log.d(TAG, "before:"+bitmap.getWidth()+"x"+bitmap.getHeight() + "after:"+thumbBmp.getWidth()+"x"+thumbBmp.getHeight());
		byte[] thumbData = Util.bmpToByteArray(thumbBmp, true);
		if(thumbData.length > 30000){
			thumbBmp = Util.resizeBitmap(bitmap, 50, 100);
			thumbData = Util.bmpToByteArray(thumbBmp, true);
		}
		bitmap.recycle();
		Log.d(TAG, "shareIMGPath thumbData size:"+thumbData.length);
		msg.thumbData = thumbData;				
		//Log.d(TAG, "thumbBmp size:"+msg.thumbData.length/1024);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;	
		req.scene = scene;
		mAPI.sendReq(req);
	}
	/**
	 * 分享到微信朋友圈
	 * @param url
	 */
	public void shareIMGFromURL_Circle(String url){
		shareIMGFromURL(url, SendMessageToWX.Req.WXSceneTimeline);
	}
	public void shareIMGFromURL(final String url,final int scene){
		try {
			Log.d(TAG, "----execute");
			GDUtils.getExecutor(context).execute(new WXThumbTask(url, new WeiXinListener() {
				
				public void OnMakeThumbSuccess(Bitmap bitmap) {
					// TODO Auto-generated method stub
					Log.d(TAG, "-----begin");
					WXImageObject imgObj = new WXImageObject();
					imgObj.imageUrl = url + AppConstant.PIC_ITEM_FULL_PREFIX;
					
					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = imgObj;
					Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
					byte[] thumbData = Util.bmpToByteArray(thumbBmp, true);
					Log.d(TAG, "thumbBmp size:"+thumbData.length);
					msg.thumbData = thumbData;					
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("img");
					req.message = msg;
					req.scene = scene;
					mAPI.sendReq(req);
				}

				public void onDownload(Bitmap bitmap) {
					// TODO Auto-generated method stub
					
				}

				public void onProgress(int rate) {
					// TODO Auto-generated method stub
					
				}
			}));
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	public void shareEmoj(String path){
		shareEmoj(path, SendMessageToWX.Req.WXSceneSession);
	}
	public void shareEmojToCircle(String path){
		//--尚不支持
		//shareEmoj(path, SendMessageToWX.Req.WXSceneTimeline);
	}
	public void shareWebpageToCircle(String path,String url){
		shareWebpageToCircle(path, url, "分享一个有趣的表情", "");
	}
	public void shareWebpageToCircle(String path,String url,String title,String description){
		WXWebpageObject obj = new WXWebpageObject();
		obj.webpageUrl = url;
		WXMediaMessage msg=new WXMediaMessage();
		msg.mediaObject = obj;
		msg.description = description;
		msg.title=title;
		Bitmap bitmap  = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);				
		//Log.d(TAG, "thumbBmp size:"+msg.thumbData.length/1024);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;	
		req.scene = SendMessageToWX.Req.WXSceneTimeline;				
		mAPI.sendReq(req);
	}
	public void shareEmoj(String path,int scene){
		WXEmojiObject emoji = new WXEmojiObject();
		emoji.emojiPath = path;
		
		WXMediaMessage msg = new WXMediaMessage(emoji);
		msg.title = "分享自微信表情包";
		msg.description = "";
		Bitmap bitmap  = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
		//Log.d(TAG, "before:"+bitmap.getWidth()+"x"+bitmap.getHeight() + "after:"+thumbBmp.getWidth()+"x"+thumbBmp.getHeight());
		byte[] thumbData = Util.bmpToByteArray(thumbBmp, true);
		bitmap.recycle();
//		if(thumbData.length > 30000){
//			thumbBmp = Util.resizeBitmap(thumbBmp, 50, 75);
//		}
		Log.d(TAG, "thumbData:"+thumbData.length);
		msg.thumbData = thumbData;	
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("emoji");
		req.message = msg;
		req.scene = scene;
		mAPI.sendReq(req);
	}
	/**
	 * 分享图片到微信好友
	 * @param path
	 */
	public void shareIMG(String path){
		shareIMGPath(path, SendMessageToWX.Req.WXSceneSession);
	}
	/**
	 * 分享图片到微信朋友圈
	 * @param path
	 */
	public void shareIMGToCircle(String path){
		shareIMGPath(path, SendMessageToWX.Req.WXSceneTimeline);
	}
	/**
	 * 分享图片到微信好友
	 * @param url
	 * @param listener
	 */
	public void shareIMG(String url,final WeiXinListener listener){
		shareIMG(url, SendMessageToWX.Req.WXSceneSession, listener);
	}
	/**
	 * 分享图片到微信朋友圈
	 * @param url
	 * @param listener
	 */
	public void shareIMGToCircle(String url,final WeiXinListener listener){
		shareIMG(url, SendMessageToWX.Req.WXSceneTimeline, listener);
	}
	/**
	 * 用流方式分享图片
	 * @param url
	 */
	public void shareIMG(String url,final int scene,final WeiXinListener listener){
		String mURL = url + AppConstant.PIC_ITEM_FULL_PREFIX;
		GDUtils.getExecutor(context).execute(new WXImageDownloadTask(mURL, new WeiXinListener() {
			
			public void onProgress(int rate) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onProgress(rate);
				}
			}
			
			public void onDownload(Bitmap bitmap) {
				// TODO Auto-generated method stub
				if(bitmap == null){
					return;
				}
				Log.d(TAG, "下载完成..");
				WXImageObject obj = new WXImageObject(bitmap);
				WXMediaMessage msg=new WXMediaMessage();
				msg.mediaObject = obj;
				msg.title = "这是一个标题";
				Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
				//Log.d(TAG, "before:"+bitmap.getWidth()+"x"+bitmap.getHeight() + "after:"+thumbBmp.getWidth()+"x"+thumbBmp.getHeight());
				bitmap.recycle();
				msg.thumbData = Util.bmpToByteArray(thumbBmp, true);				
				//Log.d(TAG, "thumbBmp size:"+msg.thumbData.length/1024);
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("img");
				req.message = msg;	
				req.scene = scene;
				mAPI.sendReq(req);
				if(listener != null){
					listener.onDownload(null);
				}
			}
			
			public void OnMakeThumbSuccess(Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			}
		}));
	}
	public void shareApp(String url,final String text,final WeiXinListener listener){
		String mURL = url;
		GDUtils.getExecutor(context).execute(new WXImageDownloadTask(mURL, new WeiXinListener() {
			
			public void onProgress(int rate) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onProgress(rate);
				}
			}
			
			public void onDownload(Bitmap bitmap) {
				// TODO Auto-generated method stub
				if(bitmap == null){
					return;
				}
				Log.d(TAG, "下载完成..");
				WXAppExtendObject obj = new WXAppExtendObject();
				obj.extInfo = "你好，大饼看看";
				obj.fileData = Util.bmpToByteArray(bitmap, false);
				WXMediaMessage msg=new WXMediaMessage();
				msg.mediaObject = obj;
				
				Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
				//Log.d(TAG, "before:"+bitmap.getWidth()+"x"+bitmap.getHeight() + "after:"+thumbBmp.getWidth()+"x"+thumbBmp.getHeight());
				bitmap.recycle();
				msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
				msg.title="这是一个标题";
				msg.description = text;
				//Log.d(TAG, "thumbBmp size:"+msg.thumbData.length/1024);
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("appdata");
				req.message = msg;
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
				mAPI.sendReq(req);
				if(listener != null){
					listener.onDownload(null);
				}
			}
			
			public void OnMakeThumbSuccess(Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			}
		}));
	}
	/**
	 * 分享文字内容给微信好友
	 * @param text
	 */
	public void shareTextToFriends(String text){
		shareText(text, SendMessageToWX.Req.WXSceneSession);
	}
	/**
	 * 分享文字内容到微信朋友圈
	 * @param text
	 */
	public void shareTextToCircle(String text){
		shareText(text, SendMessageToWX.Req.WXSceneTimeline);
	}
	/**
	 * 分享文字
	 * @param text
	 * @param scene
	 */
	private void shareText(String text,int scene){
		WXTextObject obj = new WXTextObject();
		obj.text = text;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = obj;
		msg.description = text;
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text");
		req.message = msg;
		req.scene = scene;
		mAPI.sendReq(req);
		
		
	}
	/**
	 * 分享网页至微信好友
	 * @param text
	 * @param url
	 * @param listener
	 */
	public void shareWebpage(String text,String url,WeiXinListener listener){
		shareWebpage(text, url, SendMessageToWX.Req.WXSceneSession, listener);
	}
	/**
	 * 分享网页到微信朋友圈
	 * @param text
	 * @param url
	 * @param listener
	 */
	public void shareWebpageToCircle(String text,String url,WeiXinListener listener){
		shareWebpage(text, url, SendMessageToWX.Req.WXSceneTimeline, listener);
	}
	/**
	 * 分享网页
	 * @param text
	 * @param url
	 * @param scene
	 */
	private void shareWebpage(final String text,String url,final int scene,final WeiXinListener listener){
		final String mURL = url + AppConstant.PIC_ITEM_PREFIX;
		GDUtils.getExecutor(context).execute(new WXImageDownloadTask(mURL, new WeiXinListener() {
			
			public void onProgress(int rate) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onProgress(rate);
				}
			}
			
			public void onDownload(Bitmap bitmap) {
				// TODO Auto-generated method stub
				if(bitmap == null){
					return;
				}
				WXWebpageObject obj = new WXWebpageObject();
				obj.webpageUrl = mURL;
				WXMediaMessage msg=new WXMediaMessage();
				msg.mediaObject = obj;
				msg.description = text;
				msg.title=text;
				Bitmap thumbBmp = Util.resizeBitmap(bitmap, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT);
				//Log.d(TAG, "before:"+bitmap.getWidth()+"x"+bitmap.getHeight() + "after:"+thumbBmp.getWidth()+"x"+thumbBmp.getHeight());
				bitmap.recycle();
				msg.thumbData = Util.bmpToByteArray(thumbBmp, true);				
				//Log.d(TAG, "thumbBmp size:"+msg.thumbData.length/1024);
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("webpage");
				req.message = msg;	
				req.scene = scene;				
				mAPI.sendReq(req);
				if(listener != null){
					listener.onDownload(null);
				}
			}
			
			public void OnMakeThumbSuccess(Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			}
		}));
	}
	public void shareToOther(String text,String path){
		File file = new File(path);
		if(!file.exists()){
			return;
		}
		Intent intent=new Intent(Intent.ACTION_SEND);  
        intent.setType("image/*");  
        intent.putExtra(Intent.EXTRA_SUBJECT, "来自大饼看看的图片分享");  
        intent.putExtra(Intent.EXTRA_TEXT, text);  
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        context.startActivity(Intent.createChooser(intent, "分享"));
		
	}
	//其它分享
	public void shareToOther(final String text,String url,final WeiXinListener listener){
		String mURl = url + AppConstant.PIC_ITEM_FULL_PREFIX;
		final String filename = QStr.getPicName(url);
		GDUtils.getExecutor(context).execute(new WXImageDownloadTask(mURl, new WeiXinListener() {
			
			public void onProgress(int rate) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onProgress(rate);
				}
			}
			
			public void onDownload(Bitmap bitmap) {
				// TODO Auto-generated method stub
				if(bitmap == null){
					Log.d(TAG, "无图片,只分享文字...");
					Intent intent=new Intent(Intent.ACTION_SEND);  
			        intent.setType("text/plain");  
			        intent.putExtra(Intent.EXTRA_SUBJECT, "来自大饼看看的分享");  
			        intent.putExtra(Intent.EXTRA_TEXT, text);  
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			        context.startActivity(Intent.createChooser(intent, "分享"));
			        if(listener != null){
						listener.onDownload(null);
					}
					return;
				}
				File file = new File(AppConfig.getTemp()+filename);
				FileOutputStream outputStream=null;
				try {
					if(!file.exists()){
						file.createNewFile();
					}
					outputStream = new FileOutputStream(file);
					bitmap.compress(CompressFormat.PNG, 100, outputStream);
					bitmap.recycle();
					outputStream.close();
					Intent intent=new Intent(Intent.ACTION_SEND);  
			        intent.setType("image/*");  
			        intent.putExtra(Intent.EXTRA_SUBJECT, "来自大饼看看的图片分享");  
			        intent.putExtra(Intent.EXTRA_TEXT, text);  
			        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			        context.startActivity(Intent.createChooser(intent, "分享"));
					if(listener != null){
						listener.onDownload(null);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
				
			}
			
			public void OnMakeThumbSuccess(Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			}
		}));
	} 
	
	public void shareToOther(String filepath,String text,String title){
		try {
			File file = new File(filepath);
//			boolean gif = filepath.toLowerCase().lastIndexOf(".gif") > -1 ? true:false;	
//			Log.d(TAG, "shareToOther gif:"+gif);
			Intent intent=new Intent(Intent.ACTION_SEND);  
			intent.setType("image/*");
	        intent.putExtra(Intent.EXTRA_SUBJECT, title);  
	        intent.putExtra(Intent.EXTRA_TEXT, text);  
	        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        context.startActivity(Intent.createChooser(intent, "分享"));
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
	/**
	 * 分享到QQ
	 * @param filepath
	 */
	public void shareQQ(String filepath){
		try {
			File file = new File(filepath);
			Intent intent=new Intent(Intent.ACTION_SEND);  
			intent.setType("image/*"); 
	        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        intent.setPackage("com.tencent.mobileqq");
	        context.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	/**
	 * 判断是否安装了某用于分享的应用
	 * @param context
	 * @param packname 包名
	 * @return
	 */
	public static boolean existPackageName(Context context,String packname){
		Intent intent=new Intent(Intent.ACTION_SEND);  
		intent.setType("image/*");
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo info : list){
			ActivityInfo activityInfo = info.activityInfo;
			if(activityInfo.packageName.toLowerCase().equals(packname)){
				return true;
			}
		}
		return false;
	}
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	class WXThumbTask implements Runnable{
		private String mURL;
		private WXHandler mHandler;
		final String prefix ="/160";
		public WXThumbTask(String url,WeiXinListener l){
			this.mURL = url;
			mHandler = new WXHandler(l);
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(new URL(mURL).openStream());				
				mHandler.sendMessage(Message.obtain(mHandler, 1, bitmap));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		
	}
	
	class WXImageDownloadTask implements Runnable{
		String mURL;
		WXHandler mHandler;
		public WXImageDownloadTask(String url,WeiXinListener listener){
			mURL = url;
			mHandler = new WXHandler(listener);
		}
		public void run() {
			// TODO Auto-generated method stub	
			if(!URLUtil.isValidUrl(mURL)){
				mHandler.sendMessage(Message.obtain(mHandler, 3, null));
				return;
			}
			Bitmap bitmap = loadImageFromURL(mURL);
			mHandler.sendMessage(Message.obtain(mHandler, 3, bitmap));
		}
		
		private Bitmap loadImageFromURL(String url){
			Log.d(TAG, "download url:"+url);
			URL m;
			InputStream i = null;
			BufferedInputStream bis = null; 
			ByteArrayOutputStream out =null;  
			 try {
				 m = new URL(url);  
				 URLConnection conn = m.openConnection();
				 int length = conn.getContentLength();
				 i = conn.getInputStream();
				 bis = new BufferedInputStream(i,1024 * 8); 
				 out = new ByteArrayOutputStream();   
				 int len=0;       
				 byte[] buffer = new byte[1024*4]; 
				 int hasRead=0;
				 int rate=0;//下载百分比
				 while((len = bis.read(buffer)) != -1){   					 
					 out.write(buffer, 0, len);   
					 hasRead+=len;  
		             rate = (int)(hasRead*100)/length;  
		             mHandler.sendMessage(Message.obtain(mHandler, 2, rate));
				 }        
				 out.close();  
				 bis.close(); 
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}    
			byte[] data = out.toByteArray(); 
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length); 
			return bitmap;
		}
		
	}
	
	class WXHandler extends Handler{
		private WeiXinListener listener;
		public WXHandler(WeiXinListener l){
			listener = l;
		}
		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				if(listener != null){
					Bitmap bitmap = (Bitmap) msg.obj;
					listener.OnMakeThumbSuccess(bitmap);
				}
				break;
			case 2:
				if(listener != null){
					Integer rate = (Integer) msg.obj;
					listener.onProgress(rate);
				}
				break;
			case 3:
				if(listener != null){
					Bitmap bitmap = (Bitmap) msg.obj;
					listener.onDownload(bitmap);
				}
				break;
			default:
				break;
			}
		}
		
	}
}
