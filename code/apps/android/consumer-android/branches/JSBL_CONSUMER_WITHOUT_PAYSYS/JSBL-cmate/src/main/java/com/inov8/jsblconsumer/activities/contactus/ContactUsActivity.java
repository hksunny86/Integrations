package com.inov8.jsblconsumer.activities.contactus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.R;


public class ContactUsActivity extends BaseActivity {

    private TextView tvContactNo, tvHeading, tvSubheading, tvContactEmail;
    private ImageView ivIcon;
    private Intent intent;
    private int icon;
    private String strHeading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        addAutoKeyboardHideFunctionScrolling();
        headerImplementation();

        fetchIntents();
        setUI();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fetchIntents() {
        strHeading = getIntent().getExtras().get(Constants.IntentKeys.HEADING)
                .toString();
        icon = getIntent().getExtras().getInt(Constants.IntentKeys.ICON);
    }

    private void setUI() {
        tvHeading = (TextView) findViewById(R.id.lblHeading);
        tvSubheading = (TextView) findViewById(R.id.lblSubHeading);
        ivIcon = (ImageView) findViewById(R.id.icon);
        tvContactNo = (TextView) findViewById(R.id.contactNo);
        tvContactEmail = (TextView) findViewById(R.id.contactEmail);
        btnHome.setVisibility(View.GONE);

        tvHeading.setText(strHeading);
        tvSubheading.setVisibility(View.GONE);
        ivIcon.setImageDrawable(AppCompatResources.getDrawable(this, icon));
//        ivIcon.setBackgroundResource(icon);
        tvContactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{tvContactEmail.getText().toString()});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
//                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent,
                        "Choose an Email Client :"));
            }
        });

        tvContactNo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: ".concat(tvContactNo.getText().toString()
                        .replaceAll("\\D+", ""))));
                startActivity(intent);
            }
        });
    }
}
