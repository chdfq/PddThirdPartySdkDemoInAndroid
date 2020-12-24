package com.marten.pdd_sdk_demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.adapter.NewsAdapter;
import com.marten.pdd_sdk_demo.databinding.FragmentNewsBinding;
import com.marten.pdd_sdk_demo.domain.NewsResultData;
import com.marten.pdd_sdk_demo.tools.HttpTools;
import com.marten.pdd_sdk_demo.tools.JsonTool;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class NewsFragment extends Fragment {

    private int page = 0;
    private final int pageSize = 30;
    private Context context;
    private FragmentNewsBinding binding;
    NewsResultData newsResultData;
    NewsAdapter newsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(getLayoutInflater(), container, false);
        context = getContext();
        initData();
        //下拉刷新
        binding.srlNews.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                initData();
            }
        });
        //上拉加载
        binding.srlNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        binding.rvNews.setLayoutManager(new LinearLayoutManager(context));
        return binding.getRoot();
    }

    public void initData() {
        page++;
//        HttpTools.getData("http://v.juhe.cn/toutiao/index?key=c60ab75f8ce6994bc6a06ea5adb617db");
        HttpTools.postData("http://v.juhe.cn/toutiao/index", new HttpTools.HttpBackListener() {
            @Override
            public void onSuccess(String data, int code) {
                try {
                    newsResultData = JsonTool.jsonToObject(data, NewsResultData.class);

                    if (newsResultData != null && newsResultData.getError_code() == 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (newsResultData != null && newsResultData.getResult().getData().size() > 0) {
                                    if (page == 1) {
                                        newsAdapter = new NewsAdapter(context, newsResultData.getResult().getData());
                                        binding.rvNews.setAdapter(newsAdapter);
                                    } else {
                                        if (newsAdapter != null) {
                                            newsAdapter.addAllGoods(newsResultData.getResult().getData());
                                        }
                                    }

                                    if (newsResultData.getResult().getData().size() < pageSize) {
                                        binding.srlNews.setEnableLoadMore(false);
                                    }
                                } else {
                                    binding.srlNews.setEnableLoadMore(false);
                                }
                            }
                        });
                    } else {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.srlNews.setEnableLoadMore(false);
                                if (newsResultData != null) {
                                    Toast.makeText(context, newsResultData.getReason(), Toast.LENGTH_SHORT).show();
                                } else if (newsResultData.getError_code() == 0) {
                                    Toast.makeText(context, R.string.data_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.srlNews.finishRefresh();
                binding.srlNews.finishLoadMore();
            }

            @Override
            public void onError(String errorMes, int code) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (errorMes != null && !errorMes.isEmpty()) {
                            Toast.makeText(context, errorMes, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, R.string.data_error, Toast.LENGTH_SHORT).show();
                        }
                        binding.srlNews.setEnableLoadMore(false);
                        binding.srlNews.finishRefresh();
                        binding.srlNews.finishLoadMore();
                    }
                });
            }
        });
    }

}
