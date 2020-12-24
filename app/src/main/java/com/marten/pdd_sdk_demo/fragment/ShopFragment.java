package com.marten.pdd_sdk_demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marten.pdd_sdk_demo.adapter.GoodsAdapter;
import com.marten.pdd_sdk_demo.databinding.FragmentShopBinding;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class ShopFragment extends Fragment {

    private Context context;
    private int page = 0;
    private final int pageSize = 30;
    private FragmentShopBinding binding;
    private GoodsAdapter adapter;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> list = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
            if (list != null && list.size() > 0) {
                if (page == 1) {
                    adapter = new GoodsAdapter(context, list);
                    binding.rvGoods.setAdapter(adapter);
                } else {
                    if (adapter != null) {
                        adapter.addAllGoods(list);
                    }
                }
                if (list.size() < pageSize) {
                    binding.srlShop.setEnableLoadMore(false); //数据已经请求完，则停止加载
                }
            } else {
                binding.srlShop.setEnableLoadMore(false); //没有加载到list，则停止加载
            }
            binding.srlShop.finishLoadMore();
            binding.srlShop.finishRefresh();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(getLayoutInflater(), container, false);
        initData();
        context = getContext();

        //下拉刷新
        binding.srlShop.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                initData();
            }
        });
        //上拉加载
        binding.srlShop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        binding.rvGoods.setLayoutManager(new LinearLayoutManager(context));

        return binding.getRoot();
    }

    private void initData() {
        //子线程不能访问网络，这里新建一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
                String clientId = "4f8ed144e98d4e7b9868136b14b68b43";
                String clientSecret = "deb2747ad6c918f87d5fc99071e3e57e5df46f24";
                PopClient client = new PopHttpClient(clientId, clientSecret);
                PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
                request.setPage(page);
                request.setPageSize(pageSize);
                PddDdkGoodsSearchResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String string = JsonUtil.transferToJson(response);
                List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
                Message message = new Message();
                message.what = 1; //what判断哪里发的message
                message.obj = goodsList;
                handler.sendMessage(message);
            }
        }).start();
    }
}
