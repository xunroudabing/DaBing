package com.dabing.emoj.jni;

import java.io.FileDescriptor;

public class JniUtils {
	static
	  {
	    System.loadLibrary("qpicjni99");
	  }

	  protected static native void exifClose(int paramInt);

	  protected static native double[] exifGet3RealValue(int paramInt1, int paramInt2, boolean paramBoolean);

	  protected static native int exifGetDegrees(int paramInt);

	  protected static native long exifGetImageSize(int paramInt);

	  protected static native byte[] exifGetThumbnail(int paramInt);

	  protected static native Object exifGetValue(int paramInt1, int paramInt2, boolean paramBoolean);

	  protected static native int exifOpen(String paramString, boolean paramBoolean);

	  protected static native int exifOpenFD(FileDescriptor paramFileDescriptor);

	  protected static native boolean exifSetDegrees(int paramInt1, int paramInt2);

	  protected static native void fsCancelScan(boolean paramBoolean);

	  protected static native boolean fsCopyFile(String paramString1, String paramString2);

	  protected static native int fsGetFileTime(String paramString);

	  protected static native void fsInitExtensions(String paramString);

	  protected static native String fsScanFolders(String paramString, int paramInt);

	  protected static native String fsScanPictures(String paramString, int paramInt);

	  protected static native int gifAllocBuffer(int paramInt);

	  protected static native void gifClose(int paramInt);

	  protected static native int gifDecodeFrame(int paramInt1, int paramInt2, int paramInt3);

	  protected static native boolean gifDrawFrame(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

	  protected static native void gifFreeBuffer(int paramInt);

	  protected static native int gifGetFrameCount(int paramInt);

	  protected static native long gifGetImageSize(int paramInt);

	  protected static native int gifOpenFD(FileDescriptor paramFileDescriptor);

	  protected static native void gifSetBkColor(int paramInt1, int paramInt2);
}
