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
import androidx.recyclerview.widget.RecyclerView;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.adapter.GoodsAdapter;
import com.marten.pdd_sdk_demo.domain.PddGoodsCat;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import static com.marten.pdd_sdk_demo.contants.Contants.PDD_CLIENT_ID;
import static com.marten.pdd_sdk_demo.contants.Contants.PDD_CLIENT_SECRET;

public class ShopGoodsCatFragment extends Fragment {

    private Context context;
    private int page = 0;
    private final int pageSize = 30;
    private GoodsAdapter adapter;
    private RecyclerView mRvShop;
    private SmartRefreshLayout mSrlShop;
    private View view;
    private PddGoodsCat cat;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> list = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
                    if (list != null && list.size() > 0) {
                        if (page == 1) {
                            adapter = new GoodsAdapter(context, list);
                            mRvShop.setAdapter(adapter);
                        } else {
                            if (adapter != null) {
                                adapter.addAllGoods(list);
                            }
                        }
                        if (list.size() < pageSize) {
                            mSrlShop.setEnableLoadMore(false); //数据已经请求完，则停止加载
                        }
                    } else {
                        mSrlShop.setEnableLoadMore(false); //没有加载到list，则停止加载
                    }
                    mSrlShop.finishLoadMore();
                    mSrlShop.finishRefresh();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parten = (ViewGroup) view.getParent();
            if (parten != null) {
                parten.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment_goods_cat_list, container, false);
        mRvShop = view.findViewById(R.id.rv_goods);
        mSrlShop = view.findViewById(R.id.srl_shop);
        context = getContext();
        assert getArguments() != null;
        cat = (PddGoodsCat) getArguments().getSerializable("cat");

        //下拉刷新
        mSrlShop.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                initData();
            }
        });
        //上拉加载
        mSrlShop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        mRvShop.setLayoutManager(new LinearLayoutManager(context));
        initData();
        return view;
    }

    private void initData() {
        //子线程不能访问网络，这里新建一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
                PopClient client = new PopHttpClient(PDD_CLIENT_ID, PDD_CLIENT_SECRET);
                PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
                request.setCatId(cat.getCat_id());
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
