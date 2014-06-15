package com.example.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	String query;
	ArrayList <ImageResult> imageResults = new ArrayList <ImageResult>();
	ImageResultsArrayAdapter imageAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
		setupViews();
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
                // Launch the detail view passing movie as an extra
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult iResult = imageResults.get(position);
                i.putExtra("url", iResult.getFullUrl());
                startActivity(i);
            }
        });
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		        customLoadMoreDataFromApi(page); 
	                // or customLoadMoreDataFromApi(totalItemsCount); 
		    }
	        });
	}
	
	public void setupViews(){
		etQuery = (EditText)findViewById(R.id.etQuery);
		gvResults = (GridView)findViewById(R.id.gvResults);
		btnSearch = (Button)findViewById(R.id.btnSearch);
	}
	
	public void onImageSearch(View v){
		query = etQuery.getText().toString();
		Toast.makeText(this,"query"+query,Toast.LENGTH_SHORT).show();
		imageResults.clear();
		imageAdapter.clear();
		searchWithOffset(0);
	}
	
	public void searchWithOffset(int offset){
		AsyncHttpClient client = new AsyncHttpClient(); 
		client.get("https://ajax.googleapis.com/ajax/services/search/images?" +
                  "start=" + Integer.toString(offset*8) + "&v=1.0&q=" + Uri.encode(query)+"&rsz=8", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response){
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					Log.d("DEBUG",imageResults.toString());
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		});
		
	}
	
	 private void customLoadMoreDataFromApi(int offset) {
		 Toast.makeText(this,"offset"+Integer.toString(offset),Toast.LENGTH_SHORT).show();
		 searchWithOffset(offset); 
	 }
	
}
