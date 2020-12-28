package com.marten.pdd_sdk_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.domain.Joke;

import java.util.ArrayList;
import java.util.List;

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {

    private Context context;
    private List<Joke> list = new ArrayList<>();

    public JokeAdapter(Context context, List<Joke> list) {
        this.context = context;
        this.list = list;
    }

    public void addAllData(List<Joke> dataList1) {
        list.addAll(dataList1);
        notifyItemRangeChanged(list.size() - dataList1.size(), dataList1.size());   //从哪刷新多少条item
    }

    @NonNull
    @Override
    public JokeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_joke, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JokeAdapter.ViewHolder holder, int position) {
        holder.mTvJoke.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvJoke;
        private LinearLayout mLlLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvJoke = itemView.findViewById(R.id.tv_joke_content);
            mLlLine = itemView.findViewById(R.id.ll_line);
        }
    }
}
