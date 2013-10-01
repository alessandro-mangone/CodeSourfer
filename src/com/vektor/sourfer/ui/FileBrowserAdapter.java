package com.vektor.sourfer.ui;

import java.util.ArrayList;

import com.vektor.model.VektorSerialization.fs;
import com.vektor.model.VektorSerialization.fsElement;
import com.vektor.sourfer.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileBrowserAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<fsElement> files;
	public FileBrowserAdapter(Context context,fs fs){
		this.context=context;
		this.files=fs.getFiles();
	}
	
	public void updateFs(fs fs){
		files.clear();
		files.addAll(fs.getFiles());
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return files.size();
	}

	@Override
	public fsElement getItem(int position) {
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		fsElement element = files.get(position);
		if (null == v) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.file_browser_row, null);
		}
		if(position%2==0) v.setBackgroundColor(Color.parseColor(Theme.getRow1()));
		else v.setBackgroundColor(Color.parseColor(Theme.getRow2()));
		ImageView filetype = (ImageView) v.findViewById(R.id.fs_type);
		TextView filename = (TextView) v.findViewById(R.id.fs_name);
		if(element.isDir()){
			filetype.setImageDrawable(context.getResources().getDrawable(R.drawable.folder));
			filename.setText(element.getName());
		}
		else{ 
			filetype.setImageDrawable(context.getResources().getDrawable(R.drawable.javaicon));
			filename.setText(element.getName()+".java");
		}
		return v;
	}
	
	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}
}
