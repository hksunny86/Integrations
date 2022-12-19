package com.inov8.agentmate.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.util.Constants;

public abstract class BaseCommunicationActivity extends BaseActivity {
	public AlertDialog.Builder alert;

	public abstract void processRequest();

	public abstract void processResponse(HttpResponseModel response);

	public abstract void processNext();

	public void createAlertDialog(final String reason, final String title) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				alert = new AlertDialog.Builder(BaseCommunicationActivity.this);
				alert.setTitle(title);
				alert.setMessage(reason);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				alert.show();
			}
		});
	}

	public void createAlertDialog(final String reason, final String title,
			final boolean isFinish, final MessageModel message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				alert = new AlertDialog.Builder(BaseCommunicationActivity.this);
				alert.setTitle(title);
				alert.setMessage(reason);				
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								if (message != null
										&& message.getCode() != null
										&& message.getCode().equals(
												Constants.ErrorCodes.INTERNAL)) {
									goToMainMenu();
								}
								if (isFinish) {
									finish();
								}
							}
						});
				alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
			        @Override
			        public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
			            if (keyCode == KeyEvent.KEYCODE_BACK && 
			                event.getAction() == KeyEvent.ACTION_UP && 
			                !event.isCanceled()) {
			                dialog.cancel();
			                if (isFinish) {
								finish();
							}
			                return true;
			            }
			            return false;
			        }
				});
				
				alert.show();
			}
		});
	}

	public void createAlertDialog(final String reason, final String title,
			final boolean isFinish) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				alert = new AlertDialog.Builder(BaseCommunicationActivity.this);
				alert.setTitle(title);
				alert.setMessage(reason);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								if (isFinish) {
									finish();
								}
							}
						});
				alert.show();
			}
		});
	}

	public void createAlertDialog(final String reason, final String title,
			DialogInterface.OnClickListener lisnter) {
		alert = new AlertDialog.Builder(BaseCommunicationActivity.this);
		alert.setTitle(title);
		alert.setCancelable(false);
		alert.setMessage(reason);
		alert.setPositiveButton("OK", lisnter);
		alert.show();
	}
}