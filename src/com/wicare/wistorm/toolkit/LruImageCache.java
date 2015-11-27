package com.wicare.wistorm.toolkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 
 * LruImageCache  图片内存缓存，文件缓存
 * @author c
 * @date 2015-11-20
 */
public class LruImageCache implements ImageCache {

	private static LruCache<String, Bitmap> mMemoryCache;

	private static LruImageCache lruImageCache;

	private ImageFileCache fileCache;

	private LruImageCache(Context context) {
		// 获取最大内存，并且开辟一块儿来作为缓存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
		//而且本地建立文件存储，作为二级缓存
		fileCache = new ImageFileCache(context);
	}

	
	/**
	 * 
	 *instance 单例模式，实例化缓存，以后从这一个缓存中存取网络图片
	 *@return
	 */
	public static LruImageCache instance(Context context) {
		if (lruImageCache == null) {
			lruImageCache = new LruImageCache(context);
		}
		return lruImageCache;
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = mMemoryCache.get(url);
		if (bitmap == null) {
			Log.i("LruImageCache","缓冲文件不存在");
			bitmap = fileCache.getBitmapFromSD(url);
		} else {
			Log.i("LruImageCache","缓冲存在");
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if (getBitmap(url) == null) {
			mMemoryCache.put(url, bitmap);
			fileCache.putBitmapToSD(url, bitmap);
		}
	}

}