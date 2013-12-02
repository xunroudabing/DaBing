package com.dabing.emoj.demo;

import java.util.ArrayList;
import java.util.List;

import com.dabing.emoj.activity.ChannelAddCategoryActivity;
import com.dabing.emoj.activity.DownloadGridViewActivity;
import com.dabing.emoj.activity.EmojBrowseViewActivity;
import com.dabing.emoj.activity.EmojContainerActivity;
import com.dabing.emoj.activity.EmojEmotionActivity;
import com.dabing.emoj.activity.MainTab1Activity;
import com.dabing.emoj.activity.MainTab2Activity;
import com.dabing.emoj.activity.MainTabActivity;
import com.dabing.emoj.activity.UserDefineActivity;
import com.dabing.emoj.activity.WelcomeActivity;
import com.dabing.emoj.admin.MakeEmotionActivity;
import com.dabing.emoj.admin.WebLoginActivity;
import com.dabing.emoj.admin.uploadPicActivity;
import com.dabing.emoj.push.PushControllerActivity;
import com.dabing.emoj.push.PushEmojActivity;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class demolist extends ListActivity {
	ListView listView;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listView = getListView();
		Bind();
	}
	
	public void Bind(){
		String[] names = {"demo1",
						  "EmojContainerActivity",
						  "democall",
						  "uploadPicActivity",
						  "WebLoginActivity",
						  "MainTabActivity",
						  "WelcomeActivity",
						  "demoshow",
						  "EmojBrowseViewActivity",
						  "DownloadGridViewActivity",
						  "demoOOM",
						  "MakeEmotionActivity",
						  "demoPanel",
						  "EmojEmotionActivity",
						  "MainTab1Activity",
						  "MainTab2Activity",
						  "UserDefineActivity",
						  "demoFile",
						  "ChannelAddCategoryActivity",
						  "PushControllerActivity",
						  "PushEmojActivity"
						};
		
		final Class<?>[] classes = {demo1.class,
									EmojContainerActivity.class,
									democall.class,
									uploadPicActivity.class,
									WebLoginActivity.class,
									MainTabActivity.class,
									WelcomeActivity.class,
									demoshow.class,
									EmojBrowseViewActivity.class,
									DownloadGridViewActivity.class,
									demoOOM.class,
									MakeEmotionActivity.class,
									demoPanel.class,
									EmojEmotionActivity.class,
									MainTab1Activity.class,
									MainTab2Activity.class,
									UserDefineActivity.class,
									demoFile.class,
									ChannelAddCategoryActivity.class,
									PushControllerActivity.class,
									PushEmojActivity.class
									};
		List<String> nameArray = new ArrayList<String>();
		for(String a : names){
			nameArray.add(a);
		}
		ListAdapter adapter= new ArrayAdapter<String>(demolist.this, android.R.layout.simple_list_item_1, nameArray);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(demolist.this,classes[position]);
				startActivity(intent);
				
			}
		});
	}

}
