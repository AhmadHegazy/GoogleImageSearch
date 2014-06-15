package com.example.gridimagesearch;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.os.Bundle;

public class ImageDisplayActivity extends Activity {
    SmartImageView iView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		iView = (SmartImageView)findViewById(R.id.ivResult);
		  String imageUrl = (String)
	                getIntent().getStringExtra("url");
		  
		  iView.setImageUrl(imageUrl);
	}
}
