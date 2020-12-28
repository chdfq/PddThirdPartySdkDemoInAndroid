package com.marten.pdd_sdk_demo.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MyLinearLayoutManage extends LinearLayoutManager {

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

    public MyLinearLayoutManage(Context context) {
        super(context);
    }

    public MyLinearLayoutManage(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyLinearLayoutManage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
