package com.marten.pdd_sdk_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.domain.News;
import com.marten.pdd_sdk_demo.tools.ImageLoaderTool;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private List<News> list = new ArrayList<>();

    public NewsAdapter(Context context, List<News> list) {
        this.context = context;
        this.list = list;
    }

    public void addAllGoods(List<News> listUpdate) {
        if (listUpdate != null && listUpdate.size() > 0) {
            list.addAll(listUpdate);
            notifyItemRangeChanged(list.size() - listUpdate.size(), listUpdate.size()); //从哪儿刷新多少条item
        }
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News item = list.get(position);

        //使用封装类，方便维护
        ImageLoaderTool.imageLoader(context, item.getThumbnail_pic_s(), holder.mIvGoods);
        //加载图片
//        Picasso.get()
//                .load(item.getGoodsThumbnailUrl())
//                .placeholder(R.color.gray)
//                .into(holder.mIvGoods);
//        Glide.with(context)
//                .load(item.getGoodsThumbnailUrl())
//                .placeholder(R.color.gray)
//                .centerCrop()
//                .into(holder.mIvGoods);

        holder.mTvGoodsName.setText(item.getTitle());
        holder.mTvGoodsDescription.setText(item.getAuthor_name());
        holder.mTvCoupon.setText(item.getCategory());
        //holder.mTvCouponEndTime.setText(item.getUniquekey());
        holder.mTvCouponEndTime.setText("");
        holder.mTvSalesTip.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvGoods;
        private TextView mTvGoodsName, mTvGoodsDescription, mTvCoupon, mTvCouponEndTime, mTvSalesTip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvGoods = itemView.findViewById(R.id.iv_goods_image);
            mTvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            mTvGoodsDescription = itemView.findViewById(R.id.tv_goods_description);
            mTvCoupon = itemView.findViewById(R.id.tv_coupon);
            mTvCouponEndTime = itemView.findViewById(R.id.tv_coupon_end_time);
            mTvSalesTip = itemView.findViewById(R.id.tv_sales_tip);
        }
    }
}
