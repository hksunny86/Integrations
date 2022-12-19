package com.inov8.agentmate.activities.myAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.jsblmfs.R;

public class MyAccountChangePinMenuActivity extends BaseActivity {
	private TextView lblHeading;

	// private Byte flow_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_list_view_new);

		try {
			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Pin Change");

			int[] icons = new int[] { R.drawable.arrow, R.drawable.arrow };

			String[] values = new String[] { "Change Login PIN",
					"Change Transactional PIN" };

			List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < values.length; i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("icon", Integer.toString(icons[i]));
				hm.put("txtOption", values[i]);
				aList.add(hm);
			}

			final String[] from = { "txtOption", "icon" };
			final int[] to = { R.id.txtOption, R.id.icon };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
					R.layout.listview_layout_payment_type, from, to);

			ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(
							MyAccountChangePinMenuActivity.this,
							MyAccountChangePinActivity.class);
					switch (position) {
					case Constants.CHANGE_LOGIN_PIN:

						intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE,
								"0");

						startActivity(intent);
						break;
					case Constants.CHANGE_TRANSACTIONAL_PIN:

						intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE,
								"1");

						startActivity(intent);

						break;
					default:
						Toast.makeText(getApplicationContext(),
								"Feature is not implemented.",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		headerImplementation();
	}

	@Override
	public void onBackPressed() {

		this.finish();

		super.onBackPressed();
	}
}
