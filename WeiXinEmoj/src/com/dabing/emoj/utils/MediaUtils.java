package com.dabing.emoj.utils;

import android.content.Context;
import android.media.MediaPlayer;
/**
 * 声音播放工具类
 * @author DaBing
 *
 */
public class MediaUtils {
	static MediaUtils instance;
	Context mContext;
	MediaPlayer mCurrentMediaPlayer;
	private MediaUtils(Context context){
		mContext = context;
	}
	
	public static MediaUtils getInstance(Context context){
		if(instance == null){
			instance = new MediaUtils(context);
		}
		return instance;
	}
	/**
	 * 播放声音
	 * @param resId
	 */
	public void playSound(int resId) {
		// Stop current player, if there's one playing
		if (null != mCurrentMediaPlayer) {
			mCurrentMediaPlayer.stop();
			mCurrentMediaPlayer.release();
		}

		mCurrentMediaPlayer = MediaPlayer.create(mContext, resId);
		if (null != mCurrentMediaPlayer) {
			mCurrentMediaPlayer.start();
		}
	}
}
