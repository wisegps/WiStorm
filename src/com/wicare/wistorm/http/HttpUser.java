/**
 * HttpUser.java
 * c
 * TODO
 * 2015-12-11
 */
package com.wicare.wistorm.http;

import android.os.Handler;

/**
 * 
 * HttpUser
 * @author
 * c
 * TODO
 * 2015-12-11
 */
public class HttpUser {
	private BaseVolley volley;
	
	public HttpUser(Handler handler) {
		super();
		volley = new BaseVolley(handler);
	}
	
	
}
