package com.dabing.emoj.advertise;

import cn.waps.AdInfo;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class WAPS_ADInfo implements Parcelable {
	public String[] imageUrls;
	public Bitmap adIcon;
	public int adPoint;
	public String adId,adName,adText,description,version,filesize,provider,adPackage,action;
	public WAPS_ADInfo(){
		
	}
	@Override
	public String toString() {
		String string = String.format("%s %s %s %s", adId,adName,adText,imageUrls.length > 0 ?imageUrls[0]:"");
		return string;
	};
	public static WAPS_ADInfo Convert(AdInfo adInfo){
		WAPS_ADInfo info = new WAPS_ADInfo();
		info.adId = adInfo.getAdId();
		info.adName = adInfo.getAdName();
		info.adText = adInfo.getAdText();
		info.description = adInfo.getDescription();
		info.version = adInfo.getVersion();
		info.filesize = adInfo.getFilesize();
		info.provider = adInfo.getProvider();
		info.adPackage = adInfo.getAdPackage();
		info.action = adInfo.getAction();
		info.imageUrls = adInfo.getImageUrls();
		info.adIcon = adInfo.getAdIcon();
		info.adPoint = adInfo.getAdPoints();
		return info;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(adId);
		dest.writeString(adName);
		dest.writeString(adText);
		dest.writeString(description);
		dest.writeString(version);
		dest.writeString(filesize);
		dest.writeString(provider);
		dest.writeString(adPackage);
		dest.writeString(action);
		dest.writeInt(adPoint);
		dest.writeStringArray(imageUrls);
		dest.writeParcelable(adIcon, 0);
	}
	public static final Parcelable.Creator<WAPS_ADInfo> CREATOR = new Creator<WAPS_ADInfo>() {

		public WAPS_ADInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			String _adId = source.readString();
			String _adName = source.readString();
			String _adText = source.readString();
			String _description = source.readString();
			String _version = source.readString();
			String _filesize = source.readString();
			String _provider = source.readString();
			String _adPackage = source.readString();
			String _action = source.readString();
			int _adPoint = source.readInt();
			String[] _imageUrls = new String[2];
			source.readStringArray(_imageUrls);
			Bitmap _adIcon = source.readParcelable(Bitmap.class.getClassLoader());
			WAPS_ADInfo info = new WAPS_ADInfo();
			info.adId = _adId;
			info.adName = _adName;
			info.adText = _adText;
			info.description = _description;
			info.version = _version;
			info.filesize = _filesize;
			info.provider = _provider;
			info.adPackage = _adPackage;
			info.action = _action;
			info.adPoint = _adPoint;
			info.imageUrls = _imageUrls;
			info.adIcon = _adIcon;
			return info;
		}

		public WAPS_ADInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
