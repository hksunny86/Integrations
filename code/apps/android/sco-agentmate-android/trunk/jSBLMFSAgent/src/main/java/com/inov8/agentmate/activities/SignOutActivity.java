package com.inov8.agentmate.activities;

import java.util.Hashtable;
import java.util.List;

import android.os.Bundle;

import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsbl.sco.R;

public class SignOutActivity extends BaseCommunicationActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_signout);

		processRequest();
	}
	
	@Override
	public void processRequest() {
		if (!haveInternet()) {
			createAlertDialog(Constants.Messages.INTERNET_CONNECTION_PROBLEM, "Alert", true, null);
			loginPage();		
			return;
		}

		try {
			showLoading("Please Wait", "Processing...");
			new HttpAsyncTask(SignOutActivity.this).execute(Constants.CMD_SIGN_OUT+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void processResponse(HttpResponseModel response) {
		try {
			XmlParser xmlParser = new XmlParser();
			Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
					.getXmlResponse());
			if (table.containsKey("errs")) {
				List<?> list = (List<?>) table.get("errs");
				MessageModel message = (MessageModel) list.get(0);
				hideLoading();
				String code = message.getCode();
				AppLogger.i("##Error Code: " + code);

				if (message.getCode().equals(
						Constants.ErrorCodes.SESSION_EXPIRED)) {
					loginPage();
				} else {
					createAlertDialog(message.getDescr(), "Alert", true, null);					
				}
			}
			else
            {
                hideLoading();
				loginPage();
			}
		} catch (Exception e) {
            hideLoading();
			AppLogger.e(e);
			loginPage();
		}
	}

	@Override
	public void processNext() {
	}
}