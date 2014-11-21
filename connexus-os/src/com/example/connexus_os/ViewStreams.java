package com.example.connexus_os;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
 
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.*;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;


@SuppressLint("NewApi")
public class ViewStreams extends ActionBarActivity {
	public JSONArray keys;
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_streams);
        
        final ArrayList<String> images = new ArrayList<String>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://connexus-os.appspot.com/mobileView", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
            	System.out.println("works");
            	String str;
				try {
					str = new String(response, "UTF-8");
					JSONObject dict = new JSONObject(str);
					JSONArray urls = dict.getJSONArray("urls");
					keys = dict.getJSONArray("keys");
					
					
					System.out.println(urls.get(0).toString());
					for(int i = 0; i < urls.length(); i++){
						System.out.println("added");
						images.add(urls.get(i).toString());
					}
					
					 ImageButton image1 = (ImageButton)findViewById(R.id.view1);
				        ImageButton image2 = (ImageButton)findViewById(R.id.view2);
				        ImageButton image3 = (ImageButton)findViewById(R.id.view3);
				        ImageButton image4 = (ImageButton)findViewById(R.id.view4);
				        ImageButton image5 = (ImageButton)findViewById(R.id.view5);
				        ImageButton image6 = (ImageButton)findViewById(R.id.view6);
				        ImageButton image7 = (ImageButton)findViewById(R.id.view7);
				        ImageButton image8 = (ImageButton)findViewById(R.id.view8);
				        ImageButton image9 = (ImageButton)findViewById(R.id.view9);
				        ImageButton image10 = (ImageButton)findViewById(R.id.view10);
				        ImageButton image11 = (ImageButton)findViewById(R.id.view11);
				        ImageButton image12 = (ImageButton)findViewById(R.id.view12);
				        ImageButton image13 = (ImageButton)findViewById(R.id.view13);
				        ImageButton image14 = (ImageButton)findViewById(R.id.view14);
				        ImageButton image15 = (ImageButton)findViewById(R.id.view15);
				        ImageButton image16 = (ImageButton)findViewById(R.id.view16);
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
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image9.setBackgroundDrawable(drawable);
				        	count++;
				        }
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image10.setBackgroundDrawable(drawable);
				        	count++;
				        }
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image11.setBackgroundDrawable(drawable);
				        	count++;
				        }
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image12.setBackgroundDrawable(drawable);
				        	count++;
				        }
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image13.setBackgroundDrawable(drawable);
				        	count++;
				        }
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image14.setBackgroundDrawable(drawable);
				        	count++;
				        }
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image15.setBackgroundDrawable(drawable);
				        	count++;
				        }
				        if(count < images.size()){
				        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(images.get(count)).getContent());
				        	Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				        	Drawable drawable = new BitmapDrawable(bitmapScaled);
				        	image16.setBackgroundDrawable(drawable);
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
            	
//            	try {
//            		JSONObject o = new JSONObject(new String(response));
//                    
//					System.out.print(o.optJSONObject(0));
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
            	
            	
            }
           

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
        	}


			
			
        });
        
       
         
	}
	public void singleStream1(View view){
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
	public void singleStream2(View view){
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
	public void singleStream3(View view){
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
	public void singleStream4(View view){
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
	public void singleStream5(View view){
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
	public void singleStream6(View view){
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
	public void singleStream7(View view){
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
	public void singleStream8(View view){
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
	public void singleStream9(View view){
    	if(keys.length() > 8){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(8).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void singleStream10(View view){
    	if(keys.length() > 9){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(9).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void singleStream11(View view){
    	if(keys.length() > 10){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(10).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void singleStream12(View view){
    	if(keys.length() > 11){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(11).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void singleStream13(View view){
    	if(keys.length() > 12){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(12).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void singleStream14(View view){
    	if(keys.length() > 13){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(13).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void singleStream15(View view){
    	if(keys.length() > 14){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(14).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void singleStream16(View view){
    	if(keys.length() > 15){
    		Intent myIntent = new Intent(this,SingleStream.class);
    		try {
    			myIntent.putExtra("key",keys.get(15).toString());
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		startActivity(myIntent);
    	}
    }
	public void search(View view){
		EditText text = (EditText)findViewById(R.id.searchStreams);
		Intent intent = new Intent(this, Search.class);
		intent.putExtra("text",text.getText().toString());
		
		startActivity(intent);
	}
	public void nearby(View view){
		
		Intent intent = new Intent(this, NearbyStreams.class);
	
		startActivity(intent);
	}

}