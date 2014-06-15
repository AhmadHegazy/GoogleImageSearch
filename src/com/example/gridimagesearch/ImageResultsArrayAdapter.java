package com.example.gridimagesearch;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ImageResultsArrayAdapter extends ArrayAdapter<ImageResult> {
	
	
	private static class ViewHolder {
		public SmartImageView sImage;

		public ViewHolder(View view) {
			sImage = (SmartImageView) view.findViewById(R.id.sImage);
		}

	}
	public ImageResultsArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);  
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);
		 ViewHolder holder;
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_image_result,parent,false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			 holder = (ViewHolder) convertView.getTag();
			 holder.sImage.setImageResource(android.R.color.transparent);
		}
		
		holder.sImage.setImageUrl(imageInfo.getThumbUrl());
		return convertView;
	}
	
}
