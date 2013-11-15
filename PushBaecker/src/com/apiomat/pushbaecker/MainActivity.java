package com.apiomat.pushbaecker;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.apiomat.frontend.ApiomatRequestException;
import com.apiomat.frontend.Datastore;
import com.apiomat.frontend.basics.User;
import com.apiomat.frontend.callbacks.AOMCallback;
import com.apiomat.frontend.callbacks.AOMEmptyCallback;
import com.apiomat.frontend.push.PushMessage;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author apiomat
 * 
 */
public class MainActivity extends Activity {
	final static User user = new User();
	private static final int SELECT_PHOTO = 100;
	ProgressDialog progressDialog;

	/**
	 * EditText for the PushMessage
	 */
	public static EditText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Processing...");
		progressDialog.setMessage("Please wait.");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();

		text = (EditText) findViewById(R.id.et_main_message);

		user.setUserName("Paul");
		user.setPassword("Paul1");
		Datastore.configure(user);
		// try to load user
		user.loadMeAsync(this.userLoadAsync());
	}

	private final AOMEmptyCallback userLoadAsync() {
		return new AOMEmptyCallback() {

			@Override
			public void isDone(ApiomatRequestException exception) {
				if (exception != null) {
					user.saveAsync(new AOMEmptyCallback() {

						@Override
						public void isDone(ApiomatRequestException exception) {
							Log.i(MainActivity.class.getName(), "User saved");
							progressDialog.dismiss();
						}
					});
				} else {
					// user loaded
					progressDialog.dismiss();
				}

			}
		};
	}

	/**
	 * select a picture from android gallery
	 * 
	 * @param view
	 */
	public void selectPicture(View view) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	/**
	 * will be called when intent finished and returns a result object
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();

				// gets media database
				String[] filePathColumn = { MediaColumns.DATA };

				// cursor searches for selected image
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				// set image to ImageView
				Bitmap bitmap = BitmapFactory.decodeFile(filePath);
				ImageView imageView = (ImageView) findViewById(R.id.img_main_image);
				imageView.setImageBitmap(bitmap);
			}
			break;

		default:
			break;
		}
	}

	public void savePicture(View view) {
		final ImageView imageView = (ImageView) findViewById(R.id.img_main_image);
		Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		final byte[] byteArray = stream.toByteArray();

		pushPhoto(byteArray);
	}

	public void pushPhoto(final byte[] byteArray) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Uploading...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMax(100);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		User.getUsersAsync("", new AOMCallback<List<User>>() {

			@Override
			public void isDone(List<User> resultObject,
					ApiomatRequestException exception) {
				List<String> usernames = new ArrayList<String>();
				for (int i = 0; i < resultObject.size(); i++) {
					usernames.add(resultObject.get(i).getUserName());
				}
				progressDialog.setProgress(50);
				final PushMessage pushMessage = new PushMessage();
				EditText editText = (EditText) findViewById(R.id.et_main_message);
				pushMessage.setPayload(editText.getText().toString());
				pushMessage.setReceiverUserNames(usernames);
				pushMessage.postImageAsync(byteArray, new AOMEmptyCallback() {

					@Override
					public void isDone(ApiomatRequestException exception) {
						if (exception == null) {
							Log.i(MainActivity.class.getName(),
									"Image has been posted");
							progressDialog.setProgress(80);
							pushMessage.sendAsync(new AOMEmptyCallback() {

								@Override
								public void isDone(
										ApiomatRequestException exception) {
									if (exception == null) {
										progressDialog.setProgress(100);
										progressDialog.dismiss();
										Log.i(MainActivity.class.getName(),
												"Push has been sent");
										Toast.makeText(getApplicationContext(),
												"Push has been sent",
												Toast.LENGTH_LONG).show();
									} else {
										progressDialog.dismiss();
										Log.e(MainActivity.class.getName(),
												"Push hasn't been sent");
										Toast.makeText(getApplicationContext(),
												"Push hasn't been sent",
												Toast.LENGTH_LONG).show();
									}

								}
							});
						} else {
							progressDialog.dismiss();
							Log.e(MainActivity.class.getName(),
									"Image hasn't been posted");
							Toast.makeText(getApplicationContext(),
									"Image hasn't been posted",
									Toast.LENGTH_LONG).show();
						}

					}
				});

			}
		});
	}

}
