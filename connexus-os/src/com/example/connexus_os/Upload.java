package com.example.connexus_os;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageButton;
import android.widget.TextView;

public class Upload extends ActionBarActivity{
	
	 final ArrayList<String> name = new ArrayList<String>();
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        
        Intent myIntent = getIntent(); // gets the previously created intent
        final String key = myIntent.getStringExtra("key");
        
        RequestParams params = new RequestParams();
        params.put("key", key);
        
        
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://connexus-os.appspot.com/mobileUpload", params, new AsyncHttpResponseHandler() {
        	 @Override
 			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
             	String str;
 				try {
 					str = new String(response, "UTF-8");
					
					
					name.add(str);
					
 						
 				       
 				} catch (UnsupportedEncodingException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 				
 				TextView setName = (TextView)findViewById(R.id.textView2);
 				setName.setText(name.get(0));
 				
        	 }

 			@Override
 			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
 					Throwable arg3) {
 				// TODO Auto-generated method stub
 				
 			}
        });
	}

}
