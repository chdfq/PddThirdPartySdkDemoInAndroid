package com.marten.pdd_sdk_demo.activity;

import android.content.Intent;
import android.widget.GridView;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.adapter.AllCatsAdapter;
import com.marten.pdd_sdk_demo.domain.PddGoodsCat;

import java.util.ArrayList;
import java.util.List;

public class PddCatsActivity extends BaseActivity {

    private GridView mGv;
    List<PddGoodsCat> cats = new ArrayList<>();
    private AllCatsAdapter adapter;
    private LabelsView mLv;

    @Override
    public void initView() {
        contentView(R.layout.page_pdd_cats_list);
        setMyTitle("所有分类");
        setBackImage();
        mGv = findViewById(R.id.gv_cats);
        mLv = findViewById(R.id.lv_cats);
        cats = (List<PddGoodsCat>) getIntent().getSerializableExtra("cats");

//        adapter = new AllCatsAdapter(context, cats);
//        mGv.setAdapter(adapter);

        mLv.setLabels(cats, new LabelsView.LabelTextProvider<PddGoodsCat>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, PddGoodsCat data) {
                return data.getCat_name();
            }
        });

        mLv.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                Intent intent = new Intent(context, PddGoodsListActivity.class);
                intent.putExtra("cat", (PddGoodsCat) data);
                startActivity(intent);
            }
        });
    }
}
