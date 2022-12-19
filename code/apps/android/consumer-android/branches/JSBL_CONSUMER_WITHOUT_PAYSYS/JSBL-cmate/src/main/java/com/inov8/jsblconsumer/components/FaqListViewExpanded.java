package com.inov8.jsblconsumer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class FaqListViewExpanded extends ExpandableListView {
    public FaqListViewExpanded(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDividerHeight(0);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}