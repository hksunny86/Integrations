package com.inov8.jsblconsumer.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewExpanded extends GridView {

    public GridViewExpanded(Context context) {
        super(context);
    }

    public GridViewExpanded(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewExpanded(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                MEASURED_SIZE_MASK, MeasureSpec.AT_MOST));
        getLayoutParams().height = getMeasuredHeight();
    }
}