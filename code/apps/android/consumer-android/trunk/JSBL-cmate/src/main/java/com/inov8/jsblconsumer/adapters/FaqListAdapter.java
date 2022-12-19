package com.inov8.jsblconsumer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.util.TextJustification;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.List;
import java.util.Map;

public class FaqListAdapter extends BaseExpandableListAdapter {

    static Point size;
    static float density;
    private Activity context;
    private Map<String, List<String>> faqsCollections;
    private List<String> listQuestions;

    public FaqListAdapter(Activity context, List<String> listQuestions,
                          Map<String, List<String>> faqsCollections) {
        this.context = context;
        this.faqsCollections = faqsCollections;
        this.listQuestions = listQuestions;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return faqsCollections.get(listQuestions.get(groupPosition)).get(
                childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String answer = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.lblAnswer);
        item.setText(answer);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return faqsCollections.get(listQuestions.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return listQuestions.get(groupPosition);
    }

    public int getGroupCount() {
        return listQuestions.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String question = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
        }
//        Display display = context.getWindowManager().getDefaultDisplay();
//        size=new Point();
//        DisplayMetrics dm=new DisplayMetrics();
//        display.getMetrics(dm);
//        density=dm.density;
//        display.getSize(size);
        TextView item = (TextView) convertView.findViewById(R.id.lblQuestion);
        item.setTypeface(null, Typeface.BOLD);


        item.setText(question);

//        TextJustification.justify(item,size.x);
        ImageView indicator = (ImageView) convertView
                .findViewById(R.id.imageViewindicator);
        if (isExpanded) {
            indicator.setBackgroundResource(R.drawable.faq_arrow_up);
        } else {
            indicator.setBackgroundResource(R.drawable.faq_arrow_down);
        }

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}