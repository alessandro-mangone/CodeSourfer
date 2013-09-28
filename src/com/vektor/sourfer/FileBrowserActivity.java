package com.vektor.sourfer;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.vektor.model.VektorSerialization.fs;
import com.vektor.model.VektorSerialization.fsElement;
import com.vektor.sourfer.ui.FileBrowserAdapter;
import com.vektor.sourfer.webapi.client.SourferWebAPIClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FileBrowserActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private TextView status;
	private ListView filebrowser;
	private Button refresh;
	private fsElement elem;
	private AsyncHttpResponseHandler resHandlerDir = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String response) {
			Log.i("Success", response);
			status.setText(getResources().getString(R.string.conn_status)
					+ " OK");
			try {
				filebrowser.setAdapter(new FileBrowserAdapter(
						getApplicationContext(), new Gson().fromJson(response,
								fs.class)));
			} catch (JsonSyntaxException ise) {
			}
			refresh.setEnabled(true);
		}

		@Override
		public void onFailure(Throwable error, String response) {
			Log.i("Failure", response);
			status.setText(getResources().getString(R.string.conn_status)
					+ " FAIL");
			refresh.setEnabled(true);
		}
	};
	private AsyncHttpResponseHandler resHandlerCode = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String response) {
			Log.i("Success", response);
			status.setText(getResources().getString(R.string.conn_status)
					+ " OK");
			final String theCode=response;
			SourferWebAPIClient.get(elem.getCurDir().substring(1)+File.separator+elem.getName()+"-structure.json", null, new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(String response){
					Bundle data = new Bundle();
					data.putString("code", theCode);
					data.putString("structure", response);
					Intent i = new Intent(FileBrowserActivity.this,SourceActivity.class);
					i.putExtra("javaclass", data);
					startActivity(i);
				}
				@Override
				public void onFailure(Throwable error, String response){
					
				}
			});
			refresh.setEnabled(true);
		}

		@Override
		public void onFailure(Throwable error, String response) {
			Log.i("Failure", response);
			status.setText(getResources().getString(R.string.conn_status)
					+ " FAIL");
			refresh.setEnabled(true);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filebrowser_layout);
		status = (TextView) findViewById(R.id.status_text);
		filebrowser = (ListView) findViewById(R.id.file_list);
		refresh = (Button) findViewById(R.id.button_refresh);
		refresh.setOnClickListener(this);
		filebrowser.setOnItemClickListener(this);
		SourferWebAPIClient.get("dir.json", null, resHandlerDir);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_refresh: {
			refresh.setEnabled(false);
			status.setText(getResources().getString(R.string.conn_status)
					+ " Refresh...");
			SourferWebAPIClient.get("dir.json", null, resHandlerDir);
		}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0.getId() == filebrowser.getId()) {
			FileBrowserAdapter fba = (FileBrowserAdapter) arg0.getAdapter();
			elem = fba.getItem(arg2);
			if(elem.isDir()){
				SourferWebAPIClient.get(elem.getCurDir().substring(1)+File.separator+"dir.json", null, resHandlerDir);
			}
			else{
				SourferWebAPIClient.get(elem.getCurDir().substring(1)+File.separator+elem.getName()+"-code.json", null, resHandlerCode);
			}
			Log.i("DBG",elem.getCurDir()+" ");
		}
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
	}
}
