package com.wicare.wistorm.toolkit;

import java.util.Set;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * WJpush
 * 
 * @author c
 * @date 2015-11-4
 */
public class WJpush {

	private Context context;

	public WJpush(Context context) {
		super();
		this.context = context;
		init();
	}

	/**
	 * 初始化 init
	 */
	private void init() {
		JPushInterface.init(context);
	}

	/**
	 * 恢复推送服务。
	 * 
	 * 调用了此 API 后，极光推送完全恢复正常工作。
	 * 
	 * resume
	 */
	public void resume() {
		JPushInterface.resumePush(context);
	}

	/**
	 * 停止推送服务。
	 * 
	 * JPush 推送服务完全被停止。具体表现为：
	 * 
	 * JPush Service 不在后台运行 收不到推送消息 极光推送所有的其他 API 调用都无效,不能通过 JPushInterface.init
	 * 恢复，需要调用resumePush恢复。
	 * 
	 * stop
	 */
	public void stop() {
		JPushInterface.stopPush(context);
	}

	/**
	 * 
	 * setAlias调用此 API 来设置别名。
	 * 
	 * @param alias
	 */
	public void setAlias(String alias) {
		JPushInterface.setAlias(context, alias, callback);
	}

	/**
	 * 
	 * 调用此 API 来设置标签。
	 * 
	 * @param tags
	 */
	public void setTags(Set tags) {
		JPushInterface.setTags(context, tags, callback);
	}

	
	
	private TagAliasCallback callback = new TagAliasCallback() {
		/**
		 * responseCode 0 表示调用成功。 alias 原设置的别名 tags 原设置的标签
		 */
		@Override
		public void gotResult(int responseCode, String alias, Set<String> tags) {
			String response = responseCode ==0?"成功":"失败";
			Toast.makeText(context, "设置别名"+response, Toast.LENGTH_LONG).show();
			
			Log.i("JpushActivity", response);
		}

	};
}
