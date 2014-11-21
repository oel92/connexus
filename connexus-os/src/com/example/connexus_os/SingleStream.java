package com.example.connexus_os;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import android.os.Bundle;

@SuppressLint("NewApi")
public class SingleStream extends ActionBarActivity {
	public JSONArray array;
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_stream);
        
        Intent myIntent = getIntent(); // gets the previously created intent
        String key = myIntent.getStringExtra("key");
        
        RequestParams params = new RequestParams();
        params.put("key", key);
        
        final ArrayList<String> images = new ArrayList<String>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://connexus-os.appspot.com/mobileSingle", params, new AsyncHttpResponseHandler() {

            @Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            	String str;
				try {
					str = new String(response, "UTF-8");
					array = new JSONArray(str);
				
					
					for(int i = 0; i < array.length(); i++){
						System.out.println("added");
						images.add(array.get(i).toString());
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
				
            }

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
        });
    }
	public void picture1(View view){
		if(array.length() > 0){
			Uri uri;
			try {
				uri = Uri.parse((array.get(0)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void picture2(View view){
		if(array.length() > 1){
			Uri uri;
			try {
				uri = Uri.parse((array.get(1)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture3(View view){
		if(array.length() > 2){
			Uri uri;
			try {
				uri = Uri.parse((array.get(2)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture4(View view){
		if(array.length() > 3){
			Uri uri;
			try {
				uri = Uri.parse((array.get(3)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture5(View view){
		if(array.length() > 4){
			Uri uri;
			try {
				uri = Uri.parse((array.get(4)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture6(View view){
		if(array.length() > 5){
			Uri uri;
			try {
				uri = Uri.parse((array.get(5)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture7(View view){
		if(array.length() > 6){
			Uri uri;
			try {
				uri = Uri.parse((array.get(6)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture8(View view){
		if(array.length() > 7){
			Uri uri;
			try {
				uri = Uri.parse((array.get(7)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture9(View view){
		if(array.length() > 8){
			Uri uri;
			try {
				uri = Uri.parse((array.get(8)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture10(View view){
		if(array.length() > 9){
			Uri uri;
			try {
				uri = Uri.parse((array.get(9)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture11(View view){
		if(array.length() > 10){
			Uri uri;
			try {
				uri = Uri.parse((array.get(10)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture12(View view){
		if(array.length() > 11){
			Uri uri;
			try {
				uri = Uri.parse((array.get(11)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture13(View view){
		if(array.length() > 12){
			Uri uri;
			try {
				uri = Uri.parse((array.get(12)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture14(View view){
		if(array.length() > 13){
			Uri uri;
			try {
				uri = Uri.parse((array.get(13)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture15(View view){
		if(array.length() > 14){
			Uri uri;
			try {
				uri = Uri.parse((array.get(14)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void picture16(View view){
		if(array.length() > 15){
			Uri uri;
			try {
				uri = Uri.parse((array.get(15)).toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void viewStreams(View view){
		Intent intent = new Intent(this, ViewStreams.class);
		startActivity(intent);
	}
	public void upload(View view){
		Intent myIntent = getIntent(); // gets the previously created intent
        String key = myIntent.getStringExtra("key");
        
        
        
		Intent intent = new Intent(this, Upload.class);
		intent.putExtra("key", key);
		startActivity(intent);
	}
}
