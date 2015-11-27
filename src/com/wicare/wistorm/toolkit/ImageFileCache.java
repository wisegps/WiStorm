/**
 * 
 */
package com.wicare.wistorm.toolkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author cyy
 * @desc 图片文件缓存
 * @date 2015-11-20
 * 
 */
public class ImageFileCache {
	public Context context;
	public static String externalCacheDir = null;

	public ImageFileCache(Context context) {
		this.context = context;
		externalCacheDir = getSDCacheDir();
	}

	public ImageFileCache() {
		super();
	}

	/**
	 * 
	 * getBitmapFromSD 对本地缓存的查找
	 * 
	 * @param url
	 *            图片网络地址
	 * @return Bitmap
	 */
	public Bitmap getBitmapFromSD(String url) {
		String fileName = getFileName(url);
		if (isExist(fileName)) {
			Log.i("FileCache", "对本地缓存的查找 ");
			return BitmapFactory.decodeFile(getFilePath(url));
		} else {
			Log.i("FileCache", "本地文件不存在");
			return null;
		}

	}

	/**
	 * 保存文件到本地缓存
	 */
	public void putBitmapToSD(String url, Bitmap bitmap) {
		Log.i("FileCache", "保存文件到本地");
		// MD5加密后保存

		String fileName = getFileName(url);

		if (isExist(fileName)) {
			Log.i("FileCache", "保存文件到本地，但是已经存在");
			return;
		}

		//
		Log.i("FileCache", "对本地缓存的保存 " + fileName);
		File file = new File(getFilePath(url));
		// 保存
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			if (url.endsWith(".png") || url.endsWith(".PNG")) {
				Log.i("FileCache", "保存文件到本地，png");
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			} else {
				Log.i("FileCache", "保存文件到本地，jpg");
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			}

			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return 获取外部存储器缓存目录
	 */
	private String getSDCacheDir() {
		if (externalCacheDir != null) {
			return externalCacheDir;
		}
		String sdState = Environment.getExternalStorageState();
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File cacheDir = context.getExternalCacheDir();
			externalCacheDir = cacheDir.getAbsolutePath();
			Log.i("FileCache", "externalCacheDir " + externalCacheDir);
		}
		return externalCacheDir;
	}

	/**
	 * @desc 文件是否存在
	 * @param url
	 * @return
	 */
	private boolean isExist(String fileName) {
		// SD卡不可用
		if (externalCacheDir == null) {
			return false;
		}

		File cacheDir = new File(externalCacheDir);
		File[] cacheFiles = cacheDir.listFiles();
		if (cacheFiles == null) {
			return false;
		}
		// 查找本地文件
		for (int i = 0; i < cacheFiles.length; i++) {
			String name = cacheFiles[i].getName();
			if (name.equals(fileName)) {
				Log.i("FileCache", "文件本地存在");
				return true;
			}
		}
		return false;

	}

	/**
	 * 
	 *getFilePath 根据url返回文件本地路径
	 *@param url  图片网络地址
	 *@return  本地路径
	 */
	private String getFilePath(String url) {
		return externalCacheDir + "/" + getFileName(url);
	}

	/**
	 * 
	 * getFileName url转换成本地存储的文件名
	 * 
	 * @param url
	 *            图片网络地址
	 * @return 本地文件名
	 */
	private String getFileName(String url) {
		// 获取后缀名 .png
		int begin = url.lastIndexOf(".");
		String suffix = url.substring(begin);
		// URL加密后最为文件名
		// 文件名为，加密+后缀格式
		String fileName = WEncrypt.MD5(url) + suffix;
		return fileName;
	}

}
