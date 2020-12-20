package com.marten.pdd_sdk_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marten.pdd_sdk_demo.R;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    private Context context;
    private List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> list = new ArrayList<>();

    public GoodsAdapter(Context context, List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> list) {
        this.context = context;
        this.list = list;
    }

    public void addAllGoods(List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> listUpdate) {
        if (listUpdate != null && listUpdate.size() > 0) {
            list.addAll(listUpdate);
            notifyItemRangeChanged(list.size() - listUpdate.size(), listUpdate.size()); //从哪儿刷新多少条item
        }
    }

    @NonNull
    @Override
    public GoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsAdapter.ViewHolder holder, int position) {
        PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem item = list.get(position);

        //加载图片
//        Picasso.get()
//                .load(item.getGoodsThumbnailUrl())
//                .placeholder(R.color.gray)
//                .into(holder.mIvGoods);
        Glide.with(context)
                .load(item.getGoodsThumbnailUrl())
                .placeholder(R.color.gray)
                .centerCrop()
                .into(holder.mIvGoods);

        holder.mTvGoodsName.setText(item.getGoodsName());
        holder.mTvGoodsDescription.setText(item.getGoodsDesc());

        String coupon = "";
        if (item.getCouponDiscount() != null) {
            coupon = String.valueOf(item.getCouponDiscount() / 100);
        }
        holder.mTvCoupon.setText(String.format("可领 %s元 优惠券", coupon));

        //时间戳转换
        String endTime = "";
        if (item.getCouponEndTime() != null) {
            //SimpleDateFormat类处理时间格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
            //Data类处理时间
            Date date = new Date(item.getCouponEndTime() * 1000); //10位时间戳转13位
            endTime = simpleDateFormat.format(date);
        }
        holder.mTvCouponEndTime.setText(String.format("截止日期：%s", endTime));
        holder.mTvSalesTip.setText(String.format("已售%s 件", item.getSalesTip()));

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
