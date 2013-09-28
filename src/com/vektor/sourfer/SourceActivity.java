package com.vektor.sourfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import pl.polidea.treeview.InMemoryTreeStateManager;
import pl.polidea.treeview.TreeViewList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vektor.model.VektorSerialization.classDocument;
import com.vektor.model.VektorSerialization.sourceCode;
import com.vektor.sourfer.ui.ClassStructAdapter;
import com.vektor.sourfer.ui.SourceRowAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SourceActivity extends Activity implements OnClickListener {

	private ListView thecode;
	private TreeViewList treeView;
	private InMemoryTreeStateManager<Long> manager;
	private String currentPath;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		Bundle data = i.getBundleExtra("javaclass");
		String code = data.getString("code");
		String structure = data.getString("structure");
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.source_browser);
		thecode = (ListView) findViewById(R.id.source_code_list);
		try {
			LinearLayout ttt = (LinearLayout) findViewById(R.id.taptotop);
			ttt.setOnClickListener(this);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					getAssets().open("KeyElement-code.json")));
			long start = System.currentTimeMillis();
			thecode.setAdapter(new SourceRowAdapter(getApplicationContext(),
					new Gson().fromJson(code, sourceCode.class)));
			Log.i("RenderTime", (System.currentTimeMillis() - start)
					+ " ms, rendered " + thecode.getAdapter().getCount()
					+ " lines.");
			treeView = (TreeViewList) findViewById(R.id.mainTreeView);
			classDocument doc = new Gson().fromJson(structure,
					classDocument.class);
			manager = new InMemoryTreeStateManager<Long>();
			ClassStructAdapter structAdapter = new ClassStructAdapter(this,
					manager, doc.getNestlevel(), doc, thecode);
			treeView.setAdapter(structAdapter);
			Button b = (Button) findViewById(R.id.button1);
			b.setOnClickListener(this);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button1) {
			if (thecode.getCount() > 0) {
				final EditText input = new EditText(SourceActivity.this);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				new AlertDialog.Builder(SourceActivity.this)
						.setTitle(
								"Go to line (Accepted values between 1 and "
										+ thecode.getCount() + "):")
						.setView(input)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										String value = input.getText()
												.toString();
										thecode.setSelection(Integer
												.parseInt(value) - 1);
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								}).create().show();
			}
		} else if (id == R.id.taptotop) {
			thecode.setSelection(0);
		}
	}

}
