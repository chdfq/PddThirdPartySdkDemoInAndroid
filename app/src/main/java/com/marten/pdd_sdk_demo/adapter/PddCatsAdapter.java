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
import com.marten.pdd_sdk_demo.domain.PddGoodsCat;

import java.util.ArrayList;
import java.util.List;

public class PddCatsAdapter extends RecyclerView.Adapter<PddCatsAdapter.ViewHolder> {

    private Context context;
    private List<PddGoodsCat> list = new ArrayList<>();
    private PddCatsOnClickListener pddCatsOnClickListener;

    private int lastIndex = 0;

    public void setPddCatsOnClickListener(PddCatsOnClickListener pddCatsOnClickListener) {
        this.pddCatsOnClickListener = pddCatsOnClickListener;
    }

    public PddCatsAdapter(Context context, List<PddGoodsCat> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PddCatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PddCatsAdapter.ViewHolder holder, int position) {
        PddGoodsCat cat = list.get(position);
        if (cat.isSelected()) {
            holder.mLlLine.setVisibility(View.VISIBLE);
        } else {
            holder.mLlLine.setVisibility(View.GONE);
        }
        holder.mTvCat.setText(list.get(position).getCat_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pddCatsOnClickListener.catOnClick(position, cat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvCat;
        private LinearLayout mLlLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCat = itemView.findViewById(R.id.tv_cat);
            mLlLine = itemView.findViewById(R.id.ll_line);
        }
    }

    public void updateItem(int position){
        PddGoodsCat pddGoodsCat = list.get(lastIndex);
        pddGoodsCat.setSelected(false);
        notifyItemChanged(lastIndex, pddGoodsCat);

        PddGoodsCat pddGoodsCatNow = list.get(position);
        pddGoodsCatNow.setSelected(true);
        notifyItemChanged(position, pddGoodsCatNow);
        lastIndex = position;

    }

    public interface PddCatsOnClickListener {
        void catOnClick(int position, PddGoodsCat pddGoodsCat);

    }
}
