package com.marten.pdd_sdk_demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.domain.PddGoodsCat;

import java.util.ArrayList;
import java.util.List;

public class AllCatsAdapter extends BaseAdapter {

    private Context context;
    private List<PddGoodsCat> list = new ArrayList<>();

    public AllCatsAdapter(Context context, List<PddGoodsCat> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_cats, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.mTv.setText(list.get(position).getCat_name());

        return convertView;
    }

    class ViewHolder {

        TextView mTv;

        public ViewHolder(View view) {
            mTv = view.findViewById(R.id.tv_cat);
        }
    }
}
