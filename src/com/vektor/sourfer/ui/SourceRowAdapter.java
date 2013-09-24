package com.vektor.sourfer.ui;

import java.util.ArrayList;
import java.util.List;

import com.vektor.model.VektorSerialization.codeLine;
import com.vektor.model.VektorSerialization.parsedLine;
import com.vektor.model.VektorSerialization.sourceCode;
import com.vektor.sourfer.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SourceRowAdapter extends BaseAdapter {
	private Context context;
	private sourceCode code;
	private ArrayList<parsedLine> lines;
	private boolean highlighted = false;
	private Integer from, to;

	public SourceRowAdapter(Context context, sourceCode code) {
		this.context = context;
		this.code = code;
		lines = CodeRenderer.parse(code.getCode());
		Log.i("Lines", "Sizeparsed " + lines.size());
		code.getCode().clear();
		from = null;
		to = null;
	}

	public void setHighlight(int from, int to) {
		Log.i("Highlight",from+" "+to);
		if ((this.from != null && this.from == from)
				&& (this.to != null && this.to == to)) {
			highlighted = false;
			this.from = null;
			this.to = null;
		} else {
			highlighted = true;
			this.from = from;
			this.to = to;
		}
	}

	@Override
	public int getCount() {
		return lines.size();
	}

	@Override
	public parsedLine getItem(int position) {
		return lines.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int pos, View v, ViewGroup vg) {
		// Log.i("GetView","POS "+pos);
		parsedLine entry = lines.get(pos);
		if (null == v) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.source_line, null);
		}
		if (highlighted && pos>=from && pos<=to) {
			v.setBackgroundColor(Color.parseColor(Theme.getHighlightColor()));
		} else {
			if (pos % 2 == 0)
				v.setBackgroundColor(Color.parseColor(Theme.getRow1()));
			else
				v.setBackgroundColor(Color.parseColor(Theme.getRow2()));
		}
		LinearLayout row = (LinearLayout) v.findViewById(R.id.sourcerow);
		TextView linenumber = (TextView) v.findViewById(R.id.linenumber);
		TextView linecode = (TextView) v.findViewById(R.id.linecode);
		linenumber.setText(Integer.toString(pos + 1));
		linenumber.setTextColor(Color.parseColor(Theme.getLineNumColor()));
		linenumber.setTypeface(null, Typeface.BOLD);
		linecode.setText(entry.getCode(), TextView.BufferType.SPANNABLE);

		return v;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
