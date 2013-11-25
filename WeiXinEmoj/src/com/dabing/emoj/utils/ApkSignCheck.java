package com.dabing.emoj.utils;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import com.tencent.mm.algorithm.MD5;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
/**
 * 获取应用签名
 * @author DaBing
 *
 */
public class ApkSignCheck {
	Context mContext;
	static final String TAG = ApkSignCheck.class.getSimpleName();
	public ApkSignCheck(Context context){
		mContext = context;
	}
	private byte[] getSign() {
		PackageManager pm = mContext.getPackageManager();
		List<PackageInfo> apps = pm
				.getInstalledPackages(PackageManager.GET_SIGNATURES);
		Iterator<PackageInfo> iter = apps.iterator();

		while (iter.hasNext()) {
			PackageInfo info = iter.next();
			String packageName = info.packageName;
			// 按包名 取签名
			if (packageName.equals(mContext.getPackageName())) {
				return info.signatures[0].toByteArray();

			}
		}
		return null;
	}	
	public String getPublicKey() {
        try {

            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(getSign()));

            String publickey = cert.getPublicKey().toString();
//            publickey = publickey.substring(publickey.indexOf("modulus: ") + 9,
//                    publickey.indexOf("\n", publickey.indexOf("modulus:")));
            String md5 = MD5.getMessageDigest(getSign());
            //Log.d("TAG", publickey);
            Log.d(TAG, "md5:"+md5);
            return md5;
        } catch (CertificateException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
