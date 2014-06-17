package com.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingsActivity extends Activity {

	Spinner spinner_color,spinner_size,spinner_type;
	EditText siteFilter;
	Settings setting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setting = (Settings) getIntent().getSerializableExtra(SearchActivity.SETTINGS_KEY);
		setContentView(R.layout.settings_activity);
		View view = this.findViewById(R.id.settings_view);
		spinner_size = provision_spinner(view,R.id.size_spinner,R.array.image_sizes);
		spinner_color = provision_spinner(view,R.id.color_spinner,R.array.colors);
		spinner_type = provision_spinner(view,R.id.type_spinner,R.array.image_types);
		siteFilter = (EditText) this.findViewById(R.id.siteFilter);
        Toast.makeText(this, setting.getQueryString(), Toast.LENGTH_SHORT).show();
	}
	
	
	public Spinner provision_spinner(View view, int SpinnerResourceId, int ArrayResourceId){
		Spinner spinner = (Spinner) view.findViewById(SpinnerResourceId);
		String[] stringArray = getResources().getStringArray(ArrayResourceId);
		spinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, stringArray));
		spinner.setSelection(0);
		return spinner;
	}
	
	public void onSubmit(View v){
		Intent result = new Intent();
		setting.setColor(spinner_color.getSelectedItem().toString());
		setting.setSize(spinner_size.getSelectedItem().toString());
		setting.setType(spinner_type.getSelectedItem().toString());
		setting.setSiteFilter(siteFilter.getText().toString());
		result.putExtra(SearchActivity.SETTINGS_KEY, setting);
		setResult(RESULT_OK, result);
		Log.d("QUERY_SENT",setting.getQueryString());
		finish();
		
	}
}
