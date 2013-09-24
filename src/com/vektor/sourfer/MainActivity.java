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

public class MainActivity extends Activity implements OnClickListener {

	private boolean isSearching;
	private ListView thecode;
	private TreeViewList treeView;
	private InMemoryTreeStateManager<Long> manager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		isSearching = false;
		CookieSyncManager.createInstance(this);
		setContentView(R.layout.source_browser);
		thecode = (ListView) findViewById(R.id.source_code_list);
		try {
			LinearLayout ttt = (LinearLayout) findViewById(R.id.taptotop);
			ttt.setOnClickListener(this);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					getAssets().open("KeyElement-code.json")));
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				buffer.append(line);
			in.close();
			long start = System.currentTimeMillis();
			thecode.setAdapter(new SourceRowAdapter(getApplicationContext(),
					new Gson().fromJson(buffer.toString(), sourceCode.class)));
			Log.i("RenderTime", (System.currentTimeMillis() - start)
					+ " ms, rendered " + thecode.getAdapter().getCount()
					+ " lines.");
			buffer = new StringBuilder();
			in = new BufferedReader(new InputStreamReader(getAssets().open(
					"KeyElement-structure.json")));
			while ((line = in.readLine()) != null)
				buffer.append(line);
			treeView = (TreeViewList) findViewById(R.id.mainTreeView);
			classDocument doc = new Gson().fromJson(buffer.toString(),
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

	/**
	 * Modify the menus according to the searching mode and matches
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isSearching) {
			menu.findItem(R.id.next_menu).setEnabled(true);
			menu.findItem(R.id.clear_menu).setEnabled(true);
		} else {
			menu.findItem(R.id.next_menu).setEnabled(false);
			menu.findItem(R.id.clear_menu).setEnabled(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button1) {
			if (thecode.getCount() > 0) {
				final EditText input = new EditText(getApplicationContext());
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				new AlertDialog.Builder(MainActivity.this)
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
										// Do something with value!
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// Canceled.
										dialog.dismiss();
									}
								}).create().show();
			}
		} else if (id == R.id.taptotop) {
			thecode.setSelection(0);
		}
	}



	
}
