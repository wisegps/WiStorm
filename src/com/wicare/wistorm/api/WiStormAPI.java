package com.wicare.wistorm.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wicare.wistorm.WiStorm;

/**
 * WToolKit
 * 
 * @author c
 * @date 2015-10-10
 */
public class WiStormAPI extends WiStorm {
	String basePath = "http://o.bibibaba.cn/router/rest?method=";

	/**
	 * raw 把参数排序并进行拼接
	 * 
	 * @param param
	 */
	public String raw(HashMap<String, String> param) {
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
				param.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1,
					Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map.Entry<String, String> entry = list.get(i);
			buffer.append(entry.getKey());
			buffer.append(entry.getValue());
		}

		return buffer.toString();
	}

	/**
	 * 
	 * getCurrentTime 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		return str;
	}

}
