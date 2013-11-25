package com.dabing.emoj.service;

import greendroid.util.GDUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.TokenStore;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.UmengOnlineConfigureListener;

import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
/**
 * 应用启动服务
 * @author DaBing
 *
 */
public class WeiXinEmojService extends Service implements UmengOnlineConfigureListener {
	private IWXAPI api;
	EmojHandler mHandler = new EmojHandler();
	static final String TAG = WeiXinEmojService.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		api = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APPID, false);
		api.registerApp(AppConstant.WEIXIN_APPID);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent.setOnlineConfigureListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "WeiXinEmojService 服务启动....");
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		//queue.add(new Task1());
		queue.add(new ConfigTask());
		queue.add(new AdvertiseConfig());
		queue.add(new MenuTask());
		//queue.add(new Task2());
		//queue.add(new OauthTask());
		//queue.add(new PriOauthVerify());
		queue.add(new ListenTask());
		queue.add(new ApiCheckTask());
		BlockQueueTask task = new BlockQueueTask(queue);
		GDUtils.getExecutor(getApplicationContext()).execute(task);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onDataReceived(JSONObject arg0) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onDateReceived:"+arg0.toString());
	}
	
	//同步队列
	class BlockQueueTask implements Runnable{
		BlockingQueue<Runnable> mQueue;
		public BlockQueueTask(BlockingQueue<Runnable> queue){
			mQueue = queue;
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(!mQueue.isEmpty()){
					mQueue.take().run();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				stopSelf();
			}
		}
		
	}
	class ApiCheckTask implements Runnable{

		public void run() {
			// TODO 4.0-553713665 4.2-553779201 4.3-553844737
			try {
				boolean support = api.isWXAppSupportAPI();
				int lvl = api.getWXAppSupportAPI();
				Log.d(TAG, "support:"+support+" lvl:"+lvl);
				if(!support && lvl != 0){
					mHandler.sendEmptyMessageDelayed(1, 5000);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	//更新oauth
	class OauthTask implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			try {
				String parms = MobclickAgent.getConfigParams(WeiXinEmojService.this, "oauth");
				if(parms != null && !parms.equals("") && !parms.equals("null")){
					//Log.d(TAG, "oauth:"+parms);
					if(!parms.equals(TokenStore.get(getApplicationContext()))){
						TokenStore.store(getApplicationContext(), parms);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	class Task1 implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			try {
				//广告
				String advertise = MobclickAgent.getConfigParams(WeiXinEmojService.this, "advertise_on");				
				if(advertise != null && !advertise.equals("") && !advertise.equals("null")){
					if(advertise.equals("true")){
						AppConfig.setAdvertise_on(getApplicationContext(), true);
					}else if (advertise.equals("false")) {
						AppConfig.setAdvertise_on(getApplicationContext(), false);
					}
					//Log.d(TAG, "广告:"+advertise);
				}
				//广告类型
				String adtype = MobclickAgent.getConfigParams(WeiXinEmojService.this, "AdType");
				if(adtype != null && !adtype.equals("") && !adtype.equals("null")){
					AppConfig.setAdType(getApplicationContext(), adtype);
				}
				//广告条
				String WAPS_INDEX1_AD_TYPE = MobclickAgent.getConfigParams(WeiXinEmojService.this, "WAPS_INDEX1_AD_TYPE");
				if(WAPS_INDEX1_AD_TYPE != null && !WAPS_INDEX1_AD_TYPE.equals("") && !WAPS_INDEX1_AD_TYPE.equals("null")){
					AppConfig.setWAPS_INDEX1_AD_TYPE(getApplicationContext(), WAPS_INDEX1_AD_TYPE);
				}
				String WAPS_INDEX2_AD_TYPE = MobclickAgent.getConfigParams(WeiXinEmojService.this, "WAPS_INDEX2_AD_TYPE");
				if(WAPS_INDEX2_AD_TYPE != null && !WAPS_INDEX2_AD_TYPE.equals("") && !WAPS_INDEX2_AD_TYPE.equals("null")){
					AppConfig.setWAPS_INDEX2_AD_TYPE(getApplicationContext(), WAPS_INDEX2_AD_TYPE);
				}
				String WAPS_INDEX3_AD_TYPE = MobclickAgent.getConfigParams(WeiXinEmojService.this, "WAPS_INDEX3_AD_TYPE");
				if(WAPS_INDEX3_AD_TYPE != null && !WAPS_INDEX3_AD_TYPE.equals("") && !WAPS_INDEX3_AD_TYPE.equals("null")){
					AppConfig.setWAPS_INDEX3_AD_TYPE(getApplicationContext(), WAPS_INDEX3_AD_TYPE);
				}
				//关于
				String about = MobclickAgent.getConfigParams(WeiXinEmojService.this, "notice");
				if(about != null && !about.equals("") && !about.equals("null")){
					AppConfig.setNotic(getApplicationContext(), about);
					//Log.d(TAG, "关于:"+about);
				}
				//微博分享
				String weibo = MobclickAgent.getConfigParams(WeiXinEmojService.this, "add_weibo_content");
				if(weibo != null && !weibo.equals("") && !weibo.equals("null")){
					AppConfig.setWeibo(getApplicationContext(), weibo);
					//Log.d(TAG, "分享:"+weibo);
				}
				//vip
				String vip = MobclickAgent.getConfigParams(WeiXinEmojService.this, "vip");
				if(vip != null && !vip.equals("") && !vip.equals("null")){
					AppConfig.setVip(getApplicationContext(), vip);
				}
				//趣图分类
				String category = MobclickAgent.getConfigParams(WeiXinEmojService.this, "category1");
				if(category != null && !category.equals("") && !category.equals("null")){
					AppConfig.setCategory(getApplicationContext(), category);
				}
				//公共账号
				String account = MobclickAgent.getConfigParams(WeiXinEmojService.this, "weixin_public_account");
				if(account != null && !account.equals("") && !account.equals("null")){
					AppConfig.setPublicAccount(getApplicationContext(), account);
				}
				//微博分享图片url
				String picUrl = MobclickAgent.getConfigParams(WeiXinEmojService.this, "share_pic_url");
				if(picUrl != null && !picUrl.equals("") && !picUrl.equals("null")){
					AppConfig.setSharePicUrl(getApplicationContext(), picUrl);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	//2013.11.18 更新全局配置项
	class ConfigTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				//关于
				String about = MobclickAgent.getConfigParams(WeiXinEmojService.this, "notice");
				if(about != null && !about.equals("") && !about.equals("null")){
					AppConfig.setNotic(getApplicationContext(), about);
					//Log.d(TAG, "关于:"+about);
				}
				//微博分享
				String weibo = MobclickAgent.getConfigParams(WeiXinEmojService.this, "add_weibo_content");
				if(weibo != null && !weibo.equals("") && !weibo.equals("null")){
					AppConfig.setWeibo(getApplicationContext(), weibo);
					//Log.d(TAG, "分享:"+weibo);
				}
				//vip
				String vip = MobclickAgent.getConfigParams(WeiXinEmojService.this, "vip");
				if(vip != null && !vip.equals("") && !vip.equals("null")){
					AppConfig.setVip(getApplicationContext(), vip);
				}
				
				//公共账号
				String account = MobclickAgent.getConfigParams(WeiXinEmojService.this, "weixin_public_account");
				if(account != null && !account.equals("") && !account.equals("null")){
					AppConfig.setPublicAccount(getApplicationContext(), account);
				}
				//微博分享图片url
				String picUrl = MobclickAgent.getConfigParams(WeiXinEmojService.this, "share_pic_url");
				if(picUrl != null && !picUrl.equals("") && !picUrl.equals("null")){
					AppConfig.setSharePicUrl(getApplicationContext(), picUrl);
				}
				//常见问题
				String setting_problem = MobclickAgent.getConfigParams(WeiXinEmojService.this, "setting_problem_content");
				if(setting_problem != null && !setting_problem.equals("") && !setting_problem.equals("null")){
					AppConfig.setProblem(getApplicationContext(), setting_problem);
				}
				//积分开关 是否开启积分 所有需要积分的都免费
				String bonus_enable = MobclickAgent.getConfigParams(WeiXinEmojService.this, "bonus_enable");
				if(bonus_enable != null && !bonus_enable.equals("") && !bonus_enable.equals("null")){
					if(bonus_enable.equals("true")){
						AppConfig.setBonusEnable(getApplicationContext(), true);
					}else if (bonus_enable.equals("false")) {
						AppConfig.setBonusEnable(getApplicationContext(), false);
					}
				}
				//是否隐藏积分 和积分有关的都不显示
				String bonus_hide = MobclickAgent.getConfigParams(getApplicationContext(), "bonus_hide");
				if(bonus_hide != null && !bonus_hide.equals("") && !bonus_hide.equals("null")){
					if(bonus_hide.equals("true")){
						AppConfig.setBonusHide(getApplicationContext(), true);
					}else if (bonus_hide.equals("false")) {
						AppConfig.setBonusHide(getApplicationContext(), false);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	//2013.10.21 更新广告项配置
	class AdvertiseConfig implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {				
				//广告
				String advertise = MobclickAgent.getConfigParams(WeiXinEmojService.this, "advertise_onV2");				
				if(advertise != null && !advertise.equals("") && !advertise.equals("null")){
					if(advertise.equals("true")){
						AppConfig.setAdvertise_onV2(getApplicationContext(), true);
					}else if (advertise.equals("false")) {
						AppConfig.setAdvertise_onV2(getApplicationContext(), false);
					}
					//Log.d(TAG, "广告:"+advertise);
				}
				String ad1 = MobclickAgent.getConfigParams(WeiXinEmojService.this, "ad_index1");
				if(ad1 != null && !ad1.equals("") && !ad1.equals("null")){
					AppConfig.setAdvertiseTAG(getApplicationContext(), "ad_index1", ad1);
				}
				
				String ad2 = MobclickAgent.getConfigParams(WeiXinEmojService.this, "ad_index2");
				if(ad2 != null && !ad2.equals("") && !ad2.equals("null")){
					AppConfig.setAdvertiseTAG(getApplicationContext(), "ad_index2", ad2);
				}
				
				String ad3 = MobclickAgent.getConfigParams(WeiXinEmojService.this, "ad_index3");
				if(ad3 != null && !ad3.equals("") && !ad3.equals("null")){
					AppConfig.setAdvertiseTAG(getApplicationContext(), "ad_index3", ad3);
				}
				
				String ad4 = MobclickAgent.getConfigParams(WeiXinEmojService.this, "ad_index4");
				if(ad4 != null && !ad4.equals("") && !ad4.equals("null")){
					AppConfig.setAdvertiseTAG(getApplicationContext(), "ad_index4", ad4);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	class MenuTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			try {
				String menu = MobclickAgent.getConfigParams(WeiXinEmojService.this, "menu");
				if(menu != null && !menu.equals("") && !menu.equals("null")){
					AppConfig.setEmojIndex(getApplicationContext(), menu);
					//Log.d(TAG, "菜单:"+menu);
				}
				String menu_extend = MobclickAgent.getConfigParams(WeiXinEmojService.this, "menu_extend");
				if(menu_extend != null && !menu_extend.equals("") && !menu_extend.equals("null")){
					AppConfig.setMenuExtend(getApplicationContext(), menu_extend);
					//Log.d(TAG, "扩展菜单:"+menu_extend);
				}
				String hotemoj = MobclickAgent.getConfigParams(getApplicationContext(), "hotemoj");
				if(hotemoj != null && !hotemoj.equals("") && !hotemoj.equals("null")){
					AppConfig.setHotEmoj(getApplicationContext(), hotemoj);
					//Log.d(TAG, "热门表情:"+hotemoj);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	
	class Task2 implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			try {
				String listen = MobclickAgent.getConfigParams(WeiXinEmojService.this, "listen");
				if(listen != null && !listen.equals("") && !listen.equals("null")){
					//Log.d(TAG, "listen:"+listen);
					AppConfig.setListen(getApplicationContext(), listen);
				}
				String unlisten = MobclickAgent.getConfigParams(WeiXinEmojService.this, "unlisten");
				if(unlisten != null && !unlisten.equals("") && !unlisten.equals("null")){
					//Log.d(TAG, "unlisten:"+unlisten);
					AppConfig.setUnListen(getApplicationContext(), unlisten);
				}
				String listenSwitch = MobclickAgent.getConfigParams(WeiXinEmojService.this, "listen_switch");
				if(listenSwitch != null && !listenSwitch.equals("") && !listenSwitch.equals("null")){
					//Log.d(TAG, "listenSwitch:"+listenSwitch);
					if(listenSwitch.equals("true")){
						AppConfig.setListenSwitch(getApplicationContext(), true);
					}else if (listenSwitch.equals("false")) {
						AppConfig.setListenSwitch(getApplicationContext(), false);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.d(TAG, e.toString());
			}
		}
		
	}
	
	//判读当前用户oauth是否可用
	class PriOauthVerify implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			OAuth priOAuth = TokenStore.fetchPrivate(getApplicationContext());
			if(priOAuth == null){
				return;
			}
			DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);
			try {							
				String response = request.getUserInfo(priOAuth);
				JSONObject object = new JSONObject(response);
				String ret = object.getString("ret");
				Log.d(TAG, "ret:"+ret);
				//鉴权失败
				if(ret.equals("3")){
					AppConfig.setUserEnable(getApplicationContext(), false);
				}else if(ret.equals("0")){
					AppConfig.setUserEnable(getApplicationContext(), true);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				request.shutdownConnection();
			}
		}
		
	}
	
	class ListenTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			try {
				if(!AppConfig.getListenSwitch(getApplicationContext())){
					return;
				}
				OAuth oAuth = TokenStore.fetchPrivate(getApplicationContext());
				if (oAuth == null) {
					return;
				}
				if(!AppConfig.getUserEnable(getApplicationContext())){
					return;
				}
				String listen = AppConfig.getListen(getApplicationContext());
				if(listen != null){
					DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);
					try {
						String response = request.add(oAuth, listen);
						//Log.d(TAG, "收听结果:"+response);
					} catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}finally{
						request.shutdownConnection();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	
	class EmojHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				String txt = getResources().getString(R.string.nosupport);
				Dialog mDialog = DialogFactory.createFailDialog(WeiXinEmojService.this, txt, new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);   
				mDialog.show();

				break;

			default:
				break;
			}
		}
		
	}

}
