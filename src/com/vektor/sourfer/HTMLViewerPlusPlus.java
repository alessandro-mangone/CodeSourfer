package com.vektor.sourfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Cosme Zamudio - Android SDK Examples I Grabbed this class from the
 *         SDK Examples, i actually made a lot of changes, so it doesnt look
 *         like the original one. it has the prettify functionality inside the
 *         class, it may look ugly.. but it works fine and its well organized
 *         (thats what i think) Note: Im Leaving the original comments, they
 *         might come in handy for other users reading the code
 * 
 *         Wraps a WebView widget within an Activity. When launched, it uses the
 *         URI from the intent as the URL to load into the WebView. It supports
 *         all URLs schemes that a standard WebView supports, as well as loading
 *         the top level markup using the file scheme. The WebView default
 *         settings are used with the exception of normal layout is set. This
 *         activity shows a loading progress bar in the window title and sets
 *         the window title to the title of the content.
 * 
 */

@SuppressLint("DefaultLocale")
public class HTMLViewerPlusPlus extends Activity {
	boolean first = true;
	class MyJavaScriptInterface {

		@android.webkit.JavascriptInterface
		public void showHTML(final String html) {
			/*
			TextView tv = HTMLViewerPlusPlus.this.tv;
			if (null != tv) {
				Log.i("Inliner",AutomaticCssInliner.inline(html));
				tv.setText(Html.fromHtml(AutomaticCssInliner.inline(html)),
						TextView.BufferType.SPANNABLE);
				tv.invalidate();
			}
			 */
			
			  new AlertDialog.Builder(HTMLViewerPlusPlus.this).setTitle("HTML")
			  .setMessage(AutomaticCssInliner.inline(html))
			  .setPositiveButton(android.R.string.ok, null)
			  .setCancelable(false).create();//.show();
			 new Thread(){
				 public void run(){
					 HTMLViewerPlusPlus.this.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							tv.setText(Html.fromHtml(AutomaticCssInliner.inline(html)));
						}
						 
					 });}
				 }.start();
		}
	}

	/**
	 * The WebView that is placed in this Activity
	 */
	private WebView mWebView;
	private TextView tv;
	/**
	 * As the file content is loaded completely into RAM first, set a limitation
	 * on the file size so we don't use too much RAM. If someone wants to load
	 * content that is larger than this, then a content provider should be used.
	 */
	static final int MAXFILESIZE = 16172;
	static final String LOGTAG = "HTMLViewerPlusPlus";
	private static final int PICK_REQUEST_CODE = 0;
	private boolean isSearcihng = false;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;

	}

	public TextView getTv() {
		return tv;
	}

	/**
	 * Modify the menus according to the searching mode and matches
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isSearcihng) {
			menu.findItem(R.id.next_menu).setEnabled(true);
			menu.findItem(R.id.clear_menu).setEnabled(true);
		} else {
			menu.findItem(R.id.next_menu).setEnabled(false);
			menu.findItem(R.id.clear_menu).setEnabled(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.open_menu) {
			openFileIntent();
		} else if (itemId == R.id.search_menu) {
			showSearchDialog();
		} else if (itemId == R.id.next_menu) {
			nextSearch();
		} else if (itemId == R.id.clear_menu) {
			clearSearch();
		} else if (itemId == R.id.select_menu) {
			selectAndCopyText();
		} else if (itemId == R.id.home_menu) {
			loadHomeScreen();
		} else if (itemId == R.id.help_menu) {
			loadHelpScreen();
		} else if (itemId == R.id.quit_menu) {
			quitApplication();
		}
		return false;

	}

	/**
	 * Added to avoid refreshing the page on orientation change saw it on
	 * stackoverflow, dont remember wich article
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Gets the result from the file picker activity thats the only intent im
	 * actually calling (and expecting results from) right now
	 */
	@SuppressLint("DefaultLocale")
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == PICK_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri uri = intent.getData();
				if (uri != null) {
					String path = uri.toString();
					if (path.toLowerCase().startsWith(
							Locale.getDefault().toString()))
						;
					{
						
					}
				}
			} else {
			}
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CookieSyncManager.createInstance(this);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		// tv = new TextView(this);
		mWebView = new WebView(this);
		setContentView(R.layout.source_browser);
		//setContentView(mWebView);
		tv = (TextView) findViewById(R.id.the_code);

	}


	/**
	 * Call the intent to open files
	 */
	public void openFileIntent() {
		Intent fileIntent = new Intent(HTMLViewerPlusPlus.this,
				FileBrowser.class);
		startActivityForResult(fileIntent, PICK_REQUEST_CODE);
	}

	/**
	 * Function found in the android sdk examples, checks if an action has an
	 * intent associated going to be used to check for other filebrowser intents
	 * 
	 * @param context
	 *            usually this, the context we are working on
	 * @param action
	 *            the action we want to check the intents
	 * @return true if there is at least one intent for that action
	 */
	@SuppressWarnings("unused")
	private boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentServices(intent, 0);

		return list.size() > 0;
	}

	/***
	 * Closes the application
	 */
	public void quitApplication() {
		finish();
	}

	/**
	 * Loads the home screen
	 */
	public void loadHomeScreen() {
		mWebView.getSettings().setUseWideViewPort(false);
		mWebView.loadUrl("file:///android_asset/home.html");

	}

	/**
	 * Loads the help screen
	 */
	public void loadHelpScreen() {
		mWebView.getSettings().setUseWideViewPort(false);
		mWebView.loadUrl("file:///android_asset/home.html");
	}

	/**
	 * Select Text in the webview and automatically sends the selected text to
	 * the clipboard
	 */
	public void selectAndCopyText() {
		try {
			KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
			shiftPressEvent.dispatch(mWebView, null, null);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * Clear all the matches in the search
	 */
	public void clearSearch() {
		isSearcihng = false;
		mWebView.clearMatches();
	}

	/**
	 * Find Next Match in Search
	 */
	public void nextSearch() {
		mWebView.findNext(true);
	}

	/**
	 * Search inside the webview
	 */
	public void showSearchDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Find Text");
		alert.setMessage("Enter text to find:");

		// Set an EditText view to get user input
		final EditText inputText = new EditText(this);
		alert.setView(inputText);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = inputText.getText().toString();
				isSearcihng = true;
				// mWebView.findAll(value);
				mWebView.findAllAsync(value);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alert.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CookieSyncManager.getInstance().startSync();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		mWebView.saveState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();

		CookieSyncManager.getInstance().stopSync();
		mWebView.stopLoading();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
	}

}
