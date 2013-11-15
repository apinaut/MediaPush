package com.apiomat.pushconsumer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.apiomat.frontend.ApiomatRequestException;
import com.apiomat.frontend.Datastore;
import com.apiomat.frontend.basics.User;
import com.apiomat.frontend.callbacks.AOMEmptyCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author apiomat
 *
 */
public class MainActivity extends Activity {
	static final String TAG = MainActivity.class.getName();

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "187367817043";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;
	ProgressDialog progressDialog;
	ProgressDialog playServicesProgressDialog;

	String regid;

	final static User user = new User();
	public static TextView pushText;
	public static ImageView imageView;
	public static ProgressDialog imageDownloadProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.context = getApplicationContext();

		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			this.gcm = GoogleCloudMessaging.getInstance(this);
			this.regid = getRegistrationId(this.context);

			if (this.regid.isEmpty()) {
				registerInBackground();
			} 
		} else {
			Log.e(TAG, "No valid Google Play Services APK found.");
			Toast.makeText(getApplicationContext(), "No valid Google Play Services APK found.", Toast.LENGTH_LONG);
		}

		TextView open = (TextView) findViewById(R.id.txt_main_open);
		open.setText("An allen Tagen 24h geöffnet. Sonntag RuheTag");
		pushText = (TextView) findViewById(R.id.txt_main_message);
		imageView = (ImageView) findViewById(R.id.img_main_image);
		imageDownloadProgressDialog=new ProgressDialog(this);
		imageDownloadProgressDialog.setTitle("Downloading...");
		imageDownloadProgressDialog.setIndeterminate(false);
		imageDownloadProgressDialog.setCancelable(false);

		user.setUserName("USERNAME");
		user.setPassword("PASSWORD");
		Datastore.configure(user);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("User processing...");
		progressDialog.setMessage("Please wait.");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
		user.loadMeAsync(this.userLoadAsync());

	}

	private final AOMEmptyCallback userLoadAsync() {
		return new AOMEmptyCallback() {

			@Override
			public void isDone(ApiomatRequestException exception) {
				final String regId = getRegistrationId(MainActivity.this.context);
				if (exception != null) {
					user.setRegistrationId(regId);
					user.saveAsync(userSaveAsync());
				} else {

					if (regId.equals("")) {
						Log.e(TAG, "Registration id is empty. Bad.");
					} else {
						Log.i(TAG, "Already registered");
						MainActivity.user.setRegistrationId(regId);
						MainActivity.user.saveAsync(userSaveAsync());
					}
				}

			}

		};
	}

	private final AOMEmptyCallback userSaveAsync() {
		return new AOMEmptyCallback() {

			@Override
			public void isDone(ApiomatRequestException exception) {
				progressDialog.dismiss();

			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("MainActivity", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but how you store the regID in your app is up to you.
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (MainActivity.this.gcm == null) {
						MainActivity.this.gcm = GoogleCloudMessaging
								.getInstance(MainActivity.this.context);
					}
					MainActivity.this.regid = MainActivity.this.gcm
							.register(MainActivity.this.SENDER_ID);
					msg = "Device registered, registration ID="
							+ MainActivity.this.regid;
					Log.i(MainActivity.class.getName(), "RegId" + MainActivity.this.regid);

					sendRegistrationIdToBackend();

					
					storeRegistrationId(MainActivity.this.context,
							MainActivity.this.regid);
					Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_LONG).show();
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_LONG).show();
				}
				return msg;
			}
			
			
			@Override
			protected void onPreExecute() {
				playServicesProgressDialog = new ProgressDialog(MainActivity.this.context);
				playServicesProgressDialog.setTitle("Processing...");
				playServicesProgressDialog.setMessage("Please wait.");
				playServicesProgressDialog.setCancelable(false);
				playServicesProgressDialog.setIndeterminate(true);
				playServicesProgressDialog.show();
			}


			@Override
			protected void onPostExecute(String msg) {
				playServicesProgressDialog.dismiss();
			};
		}.execute(null, null, null);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	protected void sendRegistrationIdToBackend() {
		
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

}
