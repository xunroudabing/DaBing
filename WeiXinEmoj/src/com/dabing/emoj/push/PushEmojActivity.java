package com.dabing.emoj.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.CacheWrapperImageView;

public class PushEmojActivity extends BaseActivity {
	int indexImgWidth = 100;
	PushEmojThumbAdapter mAdapter;
	GridView mGridView;
	CacheWrapperImageView mIndexImg;
	static final int COLUM_NUM = 5;
	static final String TAG = PushEmojActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mIndexImg = (CacheWrapperImageView) findViewById(R.id.push_emoj_indexImg);
		mGridView = (GridView) findViewById(R.id.push_emoj_gridview);
		caculateWidth();
		BindUI();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.push_emoj_layout;
	}
	
	protected void caculateWidth(){
		WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = manager.getDefaultDisplay().getWidth();
		//Log.d(TAG, "screenWidth:"+screenWidth);
		//图片宽度
		indexImgWidth = (int) ((screenWidth - (COLUM_NUM + 1)*5) / COLUM_NUM);
	}
	protected JSONArray getTopArray(JSONArray array,int top){
		JSONArray topArray = new JSONArray();
		for(int i =0;i<array.length();i++){
			if(i < top){
				try {
					String item = array.getString(i);
					topArray.put(item);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
				}
			}
		}
		return topArray;
	}
	protected void BindUI(){
		mIndexImg.setWidth(100);
		mIndexImg.setImage(AppConstant.PIC_SERVER_URL + "4db8342fe86e779650f2" + AppConstant.PIC_ITEM_FULL_PREFIX);
		
		final String emoj005 = "{\"data\":[\"e51c18e3e93b75f8fd80\",\"39c38e793d51eac21bc0\",\"1ec9984daad8aa344f80\",\"7f230617577474a1a130\",\"83b7d076a372531b4172\",\"b375af210e374cd86abc\",\"1d92dc3be6be717b59c2\",\"90495831047f15bae286\",\"57ec780f9b4222f4ee2e\",\"fe19fd0cd3be456a6360\",\"75de2e7826f5f9419198\",\"18a03b56bb502c411c18\",\"516a34a52d9e4bb86d42\",\"36b1f93c3f265d8c6d18\",\"e8fe979b7aeda3068eda\",\"609bf93cd367dd778c5e\",\"8fab51a4f40de7b5878c\",\"972aa9ebc2bff955eb1c\",\"7826ad91baa81ff710e2\",\"52b3b0c48ce67cc54a92\",\"9106326361facf0358ce\",\"ca1a1d8badd9882a2b4c\",\"b4b4bbc92dc8e6ee3bfe\",\"3380bbe15d00902f4f1a\",\"b95b503fb3a9f760d35a\",\"d4653438abaf4f3a9cbe\",\"7adcfbdcb8c1117221ea\",\"d6a80a2895b744678ea4\",\"127727ade158fd663856\",\"84d46c712b5a617740e8\",\"255357821980ecdff1d8\",\"d0b9c002d67f9b51c6d0\",\"a15ff947ba6a9c117d2e\",\"cf3197ce3bca169f44b0\",\"4760d4b73c9a337690bc\",\"217dcf1ca1131dcafd52\",\"0155a9181f3b450b993e\",\"d66688663a17135b3d56\",\"f87c6b1b089d959fc68c\",\"d71ccefd6716c478021e\",\"f01471231c05f948b2e0\",\"8170af7a8a80bbe666c4\",\"a5855b65370c59957a38\",\"c1e809e3bcbcb47061c0\",\"b725575ebc4796d60198\",\"681f937e6b9681bad044\",\"3f2bd5a767255f22ddda\",\"fbafe7d5fba74c8f7520\",\"6ad6a6d0e731e294fc9a\",\"2a5ff9faab6cd9a03454\",\"c471f9a2efc67fc134fa\",\"f820ba511994952a38b0\",\"9d49a53b64b7b4a83972\",\"01166194efe7227615e6\",\"cac96704bc2dcbc0cf86\",\"a4b07110c5eb22191c40\",\"1668daa913af1477050e\",\"c2c572dab2206862dad0\",\"fd0360d20a72f238719e\",\"b268888e8bdd4d02a43c\",\"4d941e83075707512f44\",\"bff6a78ee70a13475c86\",\"75bd5b43f2bc62ab301c\",\"67d6c93d4000afc99354\",\"79b1797cd98067e68816\",\"e6336f8bda167005f778\",\"e34e7815ca2414e6dac6\",\"2f73398726923ed3488c\",\"b05d960a9467f4b0c660\",\"c61bc2773f51444ef524\",\"b7d98788fcc7ccd0c52c\",\"8f4a27d888d06c7388ec\",\"f5ad98f6aa9df18f6e40\",\"7cd925553a0667a343c4\",\"c7febf443a795bef18bc\",\"2c2c57da1dc33d50d6fa\",\"bde4600382be13f9a412\",\"e7dd986d9b099cf35c36\",\"34abac5f7feb295c221e\",\"b39c526891404e00c6a4\",\"ebba33560cde8af30b68\",\"d4aaf9547224026b2014\",\"f1d3ac9ae1c4852cf9ee\",\"062b17b7855ba28b49e6\",\"a58fdb0cdd7ad845156a\",\"6aa328d1c4dcc29e260c\",\"0ee2a5a5a6af92c73894\",\"2d1c3f32e5fff46f658c\",\"ca0b71d6bb8635005718\",\"0c6d671629a03f208508\",\"cd61f20525fe070e42ec\",\"5baae8596d9120c99616\",\"41a3340f7af5fe18cd9a\",\"11ed961edfdd368c77c2\",\"bfb0223aa6ca7a3635d2\",\"95cb63b392c8fa6a7548\",\"24a88e628c1b2d6319be\",\"82a6e0b212a889c499c2\",\"8f1deaee7e269e36e8ca\",\"240dd308f03b9c971e3a\",\"15dae2337ef0cccd680c\",\"70597d6738306bf7a74c\",\"2ce2f908f136ef4d3246\",\"a18decf8b26d27c6ec06\",\"57b5fce9ba4bfcc24018\",\"ee6aa6508d07c8f3eae0\",\"bc5f45f7f006344ac2fc\",\"ef7fdc8df8331e38cb06\",\"f819928fcb4521852c02\",\"5f7fb4a82a9ebcfe1900\",\"639cea47fa97771b71b8\",\"cc099309660a865e1a44\",\"50c54ea829b21aac4f82\"],\"name\":\"碎碎猫\",\"p\":\"wxemoj005\"}";	
		try {
			JSONObject object = new JSONObject(emoj005);
			JSONArray array = object.getJSONArray("data");
			mAdapter = new PushEmojThumbAdapter(getTopArray(array, 15));
			mGridView.setAdapter(mAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	class PushEmojThumbAdapter extends BaseAdapter{
		JSONArray mArray;
		public PushEmojThumbAdapter(JSONArray array){
			mArray = array;
		}
		private View makeView(int arg0, View arg1, ViewGroup arg2) throws JSONException{
			View root = null;
			if(arg1 == null){
				root = LayoutInflater.from(getApplicationContext()).inflate(R.layout.push_emoj_layout_item, arg2, false);
			}else {
				root = arg1;
			}
			CacheWrapperImageView imageView = (CacheWrapperImageView) root.findViewById(R.id.push_emoj_item_img);
			imageView.setWidth(indexImgWidth);
			String pic = mArray.getString(arg0);
			String url = AppConstant.PIC_SERVER_URL + pic + AppConstant.PIC_ITEM_FULL_PREFIX;
			imageView.setImage(url);
			return root;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.length();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			try {
				return makeView(arg0, arg1, arg2);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			return null;
		}
		
	}
}
