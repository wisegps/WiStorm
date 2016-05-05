/*package com.wicare.wistorm.http;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class VolleyCallback implements Response.Listener<String>,Response.ErrorListener{
	
	protected abstract void onSuccess(String response);
	protected abstract void onFailure(VolleyError error);
	
	private static Handler mHandler = new Handler(Looper.getMainLooper());
	
	@Override
	public void onResponse(final String response) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				onSuccess(response);
			}
		});
	}
	
	@Override
	public void onErrorResponse(final VolleyError error) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				onFailure(error);
			}
		});
	}
	

}
*/