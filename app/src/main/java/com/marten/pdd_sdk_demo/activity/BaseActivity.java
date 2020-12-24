package com.marten.pdd_sdk_demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.marten.pdd_sdk_demo.R;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvRight1, mIvRight2;
    private TextView mTvTitle, mTvRight1, mTvRight2;
    private LinearLayout mLlMain;
    private View view;
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        context = this;
        mIvBack = findViewById(R.id.back_img);
        mIvRight1 = findViewById(R.id.right_image1);
        mIvRight2 = findViewById(R.id.right_image2);
        mTvTitle = findViewById(R.id.title);
        mTvRight1 = findViewById(R.id.right_text1);
        mTvRight2 = findViewById(R.id.right_text2);
        mLlMain = findViewById(R.id.ll_base);
        mIvBack.setOnClickListener(this);
        initView();

    }

    public void setMyTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setRightImage1(int imgId, View.OnClickListener onClickListener) {
        mIvRight1.setVisibility(View.VISIBLE);
        mIvRight1.setImageResource(imgId);
        mIvRight1.setOnClickListener(onClickListener);
    }

    public void setRightImage2(int imgId, View.OnClickListener onClickListener) {
        mIvRight2.setVisibility(View.VISIBLE);
        mIvRight2.setImageResource(imgId);
        mIvRight2.setOnClickListener(onClickListener);
    }

    public void setRightText1(String str, View.OnClickListener onClickListener) {
        mTvRight1.setVisibility(View.VISIBLE);
        mTvRight1.setText(str);
        mTvRight1.setOnClickListener(onClickListener);
    }

    public void setRightText2(String str, View.OnClickListener onClickListener) {
        mTvRight2.setVisibility(View.VISIBLE);
        mTvRight2.setText(str);
        mTvRight2.setOnClickListener(onClickListener);
    }

    protected void contentView(int layoutId) {
        view = getLayoutInflater().inflate(layoutId, null);
        if (mLlMain.getChildCount() > 0) {
            mLlMain.removeAllViews();
        }
        if (view != null) {
            mLlMain.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.right_image1:
                break;
            case R.id.right_image2:
                break;
        }
    }

    public abstract void initView();
}
