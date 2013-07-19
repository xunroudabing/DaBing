package com.dabing.emoj.admin;

import greendroid.util.GDUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.R.string;
import com.dabing.emoj.exception.Check;
import com.dabing.emoj.exception.DBLog;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.QStr;
import com.dabing.emoj.utils.TokenStore;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;

public class uploadPicActivity extends BaseActivity {
	EditText pathView,htView,emojView;
	Button btn,btnReload;
	TextView msgView;
	MyHandler mHandler=new MyHandler();
	int failCount = 0;
	int successCount = 0;
	static final int interval = 2000;
	static final String TAG = uploadPicActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pathView = (EditText) findViewById(R.id.admin_txtPath);
		htView = (EditText) findViewById(R.id.admin_txtKey);
		emojView = (EditText) findViewById(R.id.admin_txtEmojName);
		msgView = (TextView) findViewById(R.id.admin_txtMsg);
		btn = (Button) findViewById(R.id.admin_btnSend);
		btnReload = (Button) findViewById(R.id.admin_btnReload);
		btn.setOnClickListener(btnClickListener);
		btnReload.setOnClickListener(btnReloadListener);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.admin_upload;
	}
	private OnClickListener btnReloadListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String ht = htView.getText().toString();
			//String json = AppConfig.getUploadIds(getApplicationContext(), ht);
			String json = "[\"264696035221303\",\"271513069924452\",\"241957049248440\",\"184888079850101\",\"194531082134016\",\"245457045750336\",\"254895036834059\",\"262354125584924\",\"265152017030508\",\"220548064027185\",\"183488076613918\",\"257456058510054\",\"212795129672712\",\"181288081843362\",\"206917068505835\",\"234916004215265\",\"181088078888576\",\"220448064160768\",\"248957083739648\",\"272109131921641\",\"220949066753836\",\"206917068599960\",\"222133034193043\",\"39913114936369\",\"237709108191946\",\"260356016757914\",\"181688079138388\",\"262354125862912\",\"236309107061134\",\"244158029455852\",\"196331083600428\",\"240883074964953\",\"261056016468107\",\"233810059506131\",\"256056063432114\",\"256295037255602\",\"182888076093950\",\"269413071093350\",\"262257041067902\",\"244757047426154\",\"272109132181607\",\"267252013254567\",\"216296055943267\",\"251855054050785\",\"188830123668112\",\"220849072618568\",\"250357084814907\",\"230870127442413\",\"193031085475076\",\"246157047094637\",\"238788040557683\",\"241957049878358\",\"230670124439207\",\"220048064350993\",\"183288069995644\",\"249153042070267\",\"183288070016500\",\"250553045923372\",\"239960077644056\",\"222707022258906\",\"237709108638004\",\"244158029860078\",\"252095036843028\",\"261895129280476\",\"220949067297191\",\"187630121440010\",\"272913073307058\",\"240660074363189\",\"233610062002127\",\"241957050112232\",\"185288077115694\",\"259095131714493\",\"185488080703384\",\"247557080968458\",\"262957035477170\",\"249055056970639\",\"237860082037162\",\"234210052844326\",\"188830124095112\",\"257695132568009\",\"220949067517061\",\"261654129772473\",\"252095037116535\",\"234010055550006\",\"236688039382986\",\"270113071537196\",\"220949067595778\",\"234316012691448\",\"272913073591230\",\"256056064141066\",\"241358030340189\",\"272913073630720\",\"256295037972969\",\"248957084688933\",\"258854133132464\",\"181688080005785\",\"246857083546074\",\"237709109134848\",\"268652011292008\",\"239488039463992\",\"250455054384773\",\"267952012890308\",\"249853041242391\",\"219633012023945\",\"236545002857592\",\"251757087003802\",\"239260081582903\",\"206517074715607\",\"250357085626759\",\"185488081199022\",\"233810060488868\",\"246157047902380\",\"259095132254637\",\"192431086415630\",\"222707023021749\",\"261654130242733\",\"199816029168236\",\"242758029729384\",\"237009111397661\",\"196331084767070\",\"220849073589379\",\"258854133503166\",\"185888080851125\",\"189430121223419\",\"220748067177131\",\"252556056372721\",\"242983074794938\",\"253857043655019\",\"268652011679007\",\"220148067889001\",\"244383077341751\",\"237860082755933\",\"265396038264234\",\"182888077334425\",\"253256057650412\",\"193331085117851\",\"222507025442681\",\"220448065712541\",\"267496039942567\",\"196931066812694\",\"184888081624491\",\"220448065762053\",\"258154130481691\",\"182488078249277\",\"237009111713854\",\"248257086807005\",\"236688040152148\",\"233610063014257\",\"259656019184970\",\"196331085111998\",\"192431086871759\",\"222607019093162\",\"258854133870884\",\"268196038237566\",\"255957042917302\",\"220348066678955\",\"251155055044678\",\"176209055832456\",\"272109133679471\",\"223007019630270\",\"189430121643233\",\"236460081198987\",\"223107022997476\",\"199916032533696\",\"265396038646716\",\"185488081868268\",\"212495130862868\",\"249153043477111\",\"271409133740892\",\"206717073319424\",\"244858030389903\",\"243683077371079\",\"193931087688192\",\"234410052073864\",\"234410052085760\",\"248355056105163\",\"183088077823937\",\"199916032691080\",\"194231087190733\",\"234210054103686\",\"207017073609162\",\"184288080189802\",\"207117071947207\",\"212495130951087\",\"237160077344812\",\"185888081578421\",\"233916016605329\",\"256995036678530\",\"176209056184828\",\"192131089247341\",\"256056065369967\",\"243357051746075\",\"221349070220676\",\"259457033933842\",\"220648066503680\",\"250455055514917\",\"238788042359556\",\"187630123058548\",\"245783077559405\",\"244383078146920\",\"197831068332268\",\"186888079154205\",\"261756013459460\",\"188830125623350\",\"221549070434339\",\"221549070445323\",\"182488078968792\",\"249055058579289\",\"262595132915889\",\"258395133075181\",\"252457044283921\",\"240660076102088\",\"235010047140178\",\"260356019048579\",\"249853042562617\",\"239260082861891\",\"256756061576699\",\"207117072333660\",\"246857085004779\",\"234410052569697\",\"242758030901945\",\"246955057570402\",\"263296041215785\",\"231070126047347\",\"183688080804736\"]";
			Log.d(TAG, "reload json:"+json);
			try {
				JSONArray array = new JSONArray(json);
				List<String> parms = new ArrayList<String>();
				for(int i = 0;i<array.length();i++){
					String item = array.getString(i);
					parms.add(item);
				}
				GDUtils.getExecutor(getApplicationContext()).execute(new getURLTask(parms,ht));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
	private OnClickListener btnClickListener = new OnClickListener() {		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			failCount = 0;
			successCount = 0;
			String ht = htView.getText().toString();
			String docname = pathView.getText().toString();
			String path = AppConfig.getRoot()+docname+File.separator;
			try {
				File file = new File(path);
				if(!file.exists()){
					Toast.makeText(uploadPicActivity.this, "路径不存在", Toast.LENGTH_SHORT).show();
					return;
				}
				String[] filelist = file.list();
				List<String> picArray = new LinkedList<String>();
				for(int i=0;i<filelist.length;i++){
					String item = path+filelist[i];
					Log.d(TAG, "item:"+item);
					picArray.add(item);
				}
				GDUtils.getExecutor(getApplicationContext()).execute(new uploadTask(picArray, "#"+ht+"#"));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
	
	class uploadTask implements Runnable{
		List<String> picList;
		OAuth oAuth;
		String ht;
		public uploadTask(List<String> array,String key){
			picList = array;
			ht = key;
			oAuth = TokenStore.fetch(getApplicationContext());
			
		}
		public void run() {
			// TODO Auto-generated method stub
			TAPI api = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
			List<String> ids = new LinkedList<String>();
			JSONArray array = new JSONArray();
			try {
				int totalnum = picList.size();
				int count = 0;
				for(String pic : picList){
					String response =  api.addPic(oAuth, "json", ht, oAuth.getClientIP(), pic);
					Log.d(TAG, "response:"+response);
					count++;
					String msg = String.format("%d/%d", count,totalnum);
					mHandler.sendMessage(Message.obtain(mHandler, 1, msg));
					try {
						JSONObject obj = new JSONObject(response);
						//上传成功
						if(Check.check_ret(obj)){
							String id = obj.getJSONObject("data").getString("id");
							String imgurl = obj.getString("imgurl");
							if(!imgurl.equals("")){
								if(id != null && !id.equals("")){
									ids.add(id);
									String img = QStr.getPic(imgurl);
									array.put(img);
									mHandler.sendMessage(Message.obtain(mHandler, 4, pic));
								}	
							}else {
								//失败
								mHandler.sendMessage(Message.obtain(mHandler, 5, pic));
							}
							
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
						mHandler.sendMessage(Message.obtain(mHandler, 5, pic));
					}
					Thread.sleep(interval);
				}
				//mHandler.sendMessage(Message.obtain(mHandler, 2, ids));
				mHandler.sendMessage(Message.obtain(mHandler, 3, array));
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	class getURLTask implements Runnable{
		List<String> mIds;
		OAuth oAuth;
		List<String> parms;
		public getURLTask(List<String> ids,String ht){
			mIds = ids;
			oAuth = TokenStore.fetch(getApplicationContext());
			parms = new LinkedList<String>();
			int i = 0;
			int j =0;
			JSONArray array = new JSONArray();
			String idsString="";
			for(String id:mIds){
				array.put(id);
				idsString += id + ",";
				j++;
				i++;
				if( j < mIds.size()){
					if(i >= 40){
						idsString = idsString.substring(0, idsString.length() -1);
						parms.add(idsString);
						idsString = "";
						i = 0;
					}
				}else {
					idsString = idsString.substring(0, idsString.length() -1);
					parms.add(idsString);
				}
			}
			AppConfig.setUploadIds(getApplicationContext(), ht, array.toString());
			Log.d(TAG, "parms.count:"+parms.size());
		}
		public void run() {
			// TODO Auto-generated method stub
			DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);
			try {
				JSONArray urlArray = new JSONArray();
				for(String p:parms){
					try {
						DBLog.d("ids", "id", p);
						String response = request.getWeiboList(oAuth, p);
						Log.d(TAG, "getUrlTask:"+response);
						JSONObject object = new JSONObject(response);
						JSONArray array = object.getJSONObject("data").getJSONArray("info");

						for(int i=0;i<array.length();i++){
							JSONObject item = array.getJSONObject(i);
							String url = "";
							if(!item.isNull("image") && !item.get("image").equals("null") && item.getJSONArray("image") != null && item.getJSONArray("image").length() > 0){
								url = item.getJSONArray("image").getString(0);
							}	
							if(url != ""){
								urlArray.put(url.replace(AppConstant.PIC_SERVER_URL, ""));
							}
						}
						Thread.sleep(15000);
					} catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}
				}
				
				mHandler.sendMessage(Message.obtain(mHandler, 3, urlArray));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				request.shutdownConnection();
			}
		}
		
	}
	class MyHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO 1-msg 2-ids 3-返回url数组 4-图片上传成功 5-图片上传失败
			switch (msg.what) {
			case 1:
				String txt = msg.obj.toString();
				msgView.setText(txt);
				
				break;
			case 2:
				List<String> ids = (List<String>) msg.obj;
				String ht1 = htView.getText().toString();
				GDUtils.getExecutor(getApplicationContext()).execute(new getURLTask(ids,ht1));
				break;
			case 3:
				JSONArray array = (JSONArray) msg.obj;
				Log.d(TAG, "urlArray:"+array.toString());
				JSONObject result = new JSONObject();
				String emojname = emojView.getText().toString();
				String ht = htView.getText().toString();
				try {
					result.put("data", array);
					result.put("p", ht);
					result.put("name", emojname);
					Log.d(TAG, "result:"+result.toString());
					DBLog.d("Data", "data", result.toString());
					String text = String.format("成功:%d 失败:%d", successCount,failCount);
					msgView.setText(msgView.getText().toString()+"\r\n"+text);
					Toast.makeText(uploadPicActivity.this, "生成成功...", Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					Log.e(TAG, e.toString());
				}
				
				break;
			case 4:
				String path = msg.obj.toString();
				successCount ++;
				try {
					File file = new File(path);
					if(file.exists()){
						Log.d(TAG, "upload success,delete:"+path);
						file.delete();
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
				break;
			case 5:
				failCount++;
				break;
			default:
				break;
			}
		}
		
	}
}
