package com.apiomat.pushconsumer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.apiomat.frontend.basics.User;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.ImageView;

/**
 * 
 * @author apiomat
 * 
 */
public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 0;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				notification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				notification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				for (int i = 0; i < 5; i++) {
					Log.i("GCMIntentService", "Working... " + (i + 1) + "/5 @ "
							+ SystemClock.elapsedRealtime());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
				Log.i("GCMIntentService",
						"Completed work @ " + SystemClock.elapsedRealtime());
				onMessage(extras);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
	}

	/**
	 * handle the message
	 * 
	 * @param extras
	 */
	private void onMessage(Bundle extras) {
		final String imgId = extras.getString("i");
		final String gcmMessage = extras.getString("payload");
		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {

			@Override
			public void run() {
				try {
					MainActivity.pushText.setText(gcmMessage);
					loadPicture(imgId);
					notification(gcmMessage);
				} catch (Exception exception) {
					Log.e(GcmIntentService.class.getName(), "App isn't active");
				}

			}
		});

	}

	/**
	 * show a nice notification
	 * 
	 * @param gcmMessage
	 */
	public void notification(String gcmMessage) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Notification noti = new Notification.Builder(this)
				.setContentTitle("Neuigkeiten vom Bäcker")
				.setContentText(gcmMessage).setLights(888, 1000, 1000)
				.setSmallIcon(R.drawable.ic_launcher)
				.setWhen(System.currentTimeMillis()).setSound(alarmSound)
				.setContentIntent(pIntent).build();

		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(NOTIFICATION_ID, noti);
	}

	public void loadPicture(String imgId) {
		LoadImageTask loadImageTask = new LoadImageTask(MainActivity.imageView);
		loadImageTask.execute(imgId);
	}

	/**
	 * load image from url async
	 * 
	 * @author apiomat
	 * 
	 */
	public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		ImageView imageView = null;

		public LoadImageTask(ImageView imageView) {
			this.imageView = imageView;

		}

		@Override
		protected Bitmap doInBackground(String... imgId) {

			try {
				URL url = new URL(User.baseURL + "/data/images/" + imgId[0]
						+ ".img?apiKey=" + User.apiKey + "&system="
						+ User.system
						+ "&format=png&height=400&width=400&bgcolor=ffffff");
				Log.i(GcmIntentService.class.getName(),
						"Url: " + url.toString());

				URLConnection connection = url.openConnection();

				int lenghtOfFile = connection.getContentLength();
				Log.i(GcmIntentService.class.getName(),
						"Length of file (" + connection.getContentType() + " "
								+ connection.getContentEncoding() + "): "
								+ lenghtOfFile);

				return BitmapFactory.decodeStream(connection.getInputStream());
			} catch (MalformedURLException e) {
				Log.e(GcmIntentService.class.getName(),
						"Error: " + e.getMessage());
			} catch (IOException e) {
				Log.e(GcmIntentService.class.getName(),
						"Error: " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			MainActivity.imageDownloadProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			MainActivity.imageDownloadProgressDialog.dismiss();
			this.imageView.setImageBitmap(bitmap);
		}

	}

}