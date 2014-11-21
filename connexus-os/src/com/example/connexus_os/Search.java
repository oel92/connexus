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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Search extends ActionBarActivity {
	public JSONArray keys;
	public JSONArray array;
	 final ArrayList<String> size = new ArrayList<String>();
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        Intent myIntent = getIntent(); // gets the previously created intent
        final String text = myIntent.getStringExtra("text");
        
        RequestParams params = new RequestParams();
        params.put("text", text);
        
        final ArrayList<String> images = new ArrayList<String>();
        final ArrayList<String> size = new ArrayList<String>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://connexus-os.appspot.com/mobileSearch", params, new AsyncHttpResponseHandler() {
        	 @Override
 			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
             	String str;
 				try {
 					str = new String(response, "UTF-8");
					JSONObject dict = new JSONObject(str);
					JSONArray numArray = dict.getJSONArray("size");
					size.add(numArray.get(0).toString());
					JSONArray array = dict.getJSONArray("result");
					keys = dict.getJSONArray("streamKeys");
 					for(int i = 0; i < array.length(); i++){
 						
 						images.add(array.get(i).toString());
 					}
 					 ImageButton image1 = (ImageButton)findViewById(R.id.image1);
 				        ImageButton image2 = (ImageButton)findViewById(R.id.image2);
 				        ImageButton image3 = (ImageButton)findViewById(R.id.image3);
 				        ImageButton image4 = (ImageButton)findViewById(R.id.image4);
 				        ImageButton image5 = (ImageButton)findViewById(R.id.image5);
 				        ImageButton image6 = (ImageButton)findViewById(R.id.image6);
 				        ImageButton image7 = (ImageButton)findViewById(R.id.image7);
 				        ImageButton image8 = (ImageButton)findViewById(R.id.image8);
 				        
 				        int count = 0;
 				        System.out.println(images.size());
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image1.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image2.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image3.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image4.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image5.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image6.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image7.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				        if(count < images.size()){
 				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
 				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
 				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
 				        	image8.setBackgroundDrawable(drawable);
 				        	count++;
 				        }
 				       
 				} catch (UnsupportedEncodingException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (JSONException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (MalformedURLException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 				
 				TextView setNum = (TextView)findViewById(R.id.numResults);
 				setNum.setText(size.get(0));
 				TextView setText = (TextView)findViewById(R.id.searchText);
 				setText.setText(text);
        	 }

 			@Override
 			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
 					Throwable arg3) {
 				// TODO Auto-generated method stub
 				
 			}
        });
	}
	
	public void search(View view){
		EditText text = (EditText)findViewById(R.id.editText1);
		Intent intent = new Intent(this, Search.class);
		intent.putExtra("text",text.getText().toString());
		
		startActivity(intent);
	}
	public void search1(View view){
    	if(keys.length() > 0){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(0).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search2(View view){
    	if(keys.length() > 1){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(1).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search3(View view){
    	if(keys.length() > 2){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(2).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search4(View view){
    	if(keys.length() > 3){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(3).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search5(View view){
    	if(keys.length() > 4){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(4).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search6(View view){
    	if(keys.length() > 5){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(5).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search7(View view){
    	if(keys.length() > 6){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(6).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search8(View view){
    	if(keys.length() > 7){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(7).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
}



