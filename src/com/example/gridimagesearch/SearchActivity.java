package com.example.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener{
	public static final String SETTINGS_KEY = "settings";
	GridView gvResults;
	String query;
	ArrayList <ImageResult> imageResults = new ArrayList <ImageResult>();
	ImageResultsArrayAdapter imageAdapter;
    Settings setting;
    SearchView mSearchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
		setupViews();
		setting = new Settings();
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
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
		gvResults = (GridView)findViewById(R.id.gvResults);
	}
	
	public void searchWithOffset(int offset){
		AsyncHttpClient client = new AsyncHttpClient();
		String url= "https://ajax.googleapis.com/ajax/services/search/images?" +
                "start=" + Integer.toString(offset*8) + "&v=1.0&q=" + Uri.encode(query)+"&rsz=8"+setting.getQueryString().toString();
  	    Log.d("QUERY_DONE",setting.getQueryString());

		client.get(url, new JsonHttpResponseHandler(){
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }
	
    public void onSettings(MenuItem mi) {
    	Intent i = new Intent(this,SettingsActivity.class);
    	i.putExtra(SETTINGS_KEY, setting);
    	startActivityForResult(i, 41);
     } 
	
	public void customLoadMoreDataFromApi(int offset) {
		searchWithOffset(offset); 
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);

      if (requestCode ==  41 && resultCode == RESULT_OK) {
    	  setting = (Settings) data.getSerializableExtra(SETTINGS_KEY); 
    	  Log.d("QUERY_RECEIVED", setting.getQueryString());
      }
    }
    
    private void setupSearchView(MenuItem searchItem) {
    	if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
    	
    	 mSearchView.setOnQueryTextListener(this);
    	
    }

	@Override
	public boolean onQueryTextSubmit(String query) {
		this.query = query;
		imageResults.clear();
		imageAdapter.clear();
		searchWithOffset(0);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	} 
	
	public boolean onClose() {
	      return false;
	}
	
	protected boolean isAlwaysExpanded() {
	      return false;
	}
	
}
