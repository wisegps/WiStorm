package com.wicare.wistorm.toolkit;

import java.io.File;
import java.io.FileOutputStream;
import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.oss.task.GetObjectTask;
import com.aliyun.android.oss.task.PutObjectTask;
import android.os.Handler;
import android.os.Message;

public class WCloud extends WiStormToolKit {
	/** 在阿里云上url **/
	public static String url = "http://img.bibibaba.cn/";
	// "http://baba-img.oss-cn-hangzhou.aliyuncs.com/photo/";
	/** Bucket是阿里云上的命名空间,存储在阿里云上的每个Object必须都包含在某个Bucket中。 **/
	public static String bucket = "baba-img/photo";

	public String typeAudio = "audio/amr";// 上传内容类型为声音
	public String typeImg = "image/jpg";// 上传内容类型为图片
	/** accessId **/
	public static String accessId = "eJ3GLV07j9DD4LY5";
	/** accessKey **/
	public static String accessKey = "iAxAHoAuG1ZYwjIRLfyYKK2oF9WcCe";

	private Handler uiHandler;
	public int Msg_Get_Img = 10;

	public WCloud(Handler uiHandler) {
		super();
		this.uiHandler = uiHandler;
	}

	/**
	 * 
	 * uploadImg 上传图片
	 * 
	 * @param key
	 *            图片保存在云端的key
	 * @param path
	 *            图片本地路径
	 */
	public void uploadImg(final String key, final String path) {
		upload(key, typeImg, path);
	}

	/**
	 * 
	 * uploadAudio 上传音频
	 * 
	 * @param key
	 *            音频文件保存在云端的key
	 * @param path
	 *            音频文件本地路径
	 */
	public void uploadAudio(final String key, final String path) {
		upload(key, typeAudio, path);
	}

	/**
	 * 
	 * upload
	 * 
	 * @param key
	 *            文件上传到云端的key
	 * @param contentType
	 *            文件类型
	 * @param path
	 *            文件本地路径
	 */
	public void upload(final String key, final String contentType,
			final String path) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 上传图片到阿里云
				PutObjectTask task = new PutObjectTask(bucket, key,
						contentType, path, accessId, accessKey);
				task.getResult();
			}
		}).start();

	}

	/**
	 * 
	 * download
	 * 
	 * @param key
	 *            文件上在云端的key
	 * @param path
	 *            文件上储存在本地的路径
	 */
	public void download(final String key, final String path) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					GetObjectTask task = new GetObjectTask(bucket, key,
							accessId, accessKey);
					OSSObject obj = task.getResult();
					File imageFile = null;
					imageFile = new File(path);
					FileOutputStream fileOutputStream = new FileOutputStream(
							imageFile);
					fileOutputStream.write(obj.getData());
					fileOutputStream.close();
					Message message = new Message();
					message.what = Msg_Get_Img;
					uiHandler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}