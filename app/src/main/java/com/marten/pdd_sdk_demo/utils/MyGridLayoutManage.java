package com.marten.pdd_sdk_demo.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class MyGridLayoutManage extends GridLayoutManager {

    private boolean hScroll;
    private boolean vScroll;

    public boolean ishScroll() {
        return hScroll;
    }

    public void sethScroll(boolean hScroll) {
        this.hScroll = hScroll;
    }

    public boolean isvScroll() {
        return vScroll;
    }

    public void setvScroll(boolean vScroll) {
        this.vScroll = vScroll;
    }

    public MyGridLayoutManage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManage(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManage(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return hScroll;
    }

    @Override
    public boolean canScrollVertically() {
        return vScroll;
    }
}
