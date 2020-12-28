package com.marten.pdd_sdk_demo.fragment;

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
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.adapter.JokeAdapter;
import com.marten.pdd_sdk_demo.domain.Joke;
import com.marten.pdd_sdk_demo.domain.ResultData;
import com.marten.pdd_sdk_demo.tools.JsonTool;
import com.marten.pdd_sdk_demo.utils.HttpUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JokeFragment extends Fragment {

    private Context context;
    private View view;
    private RecyclerView mRvJoke;
    private SmartRefreshLayout mSrlJoke;
    private int page = 0;
    private int pageSize = 20;
    private JokeAdapter adapter;

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
        view = inflater.inflate(R.layout.fragment_joke, container, false);
        context = getContext();

        mRvJoke = view.findViewById(R.id.rv_joke);
        mRvJoke.setLayoutManager(new LinearLayoutManager(context));

        mSrlJoke = view.findViewById(R.id.srl_joke);

        //上拉刷新
        mSrlJoke.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                initData3();
            }
        });

        //下拉加载
        mSrlJoke.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData3();
            }
        });

        initData3();
        return view;
    }

    private void initData3() {
        page++;
        //封装okhttp
        Map<String, Object> map = new HashMap<>();
        map.put("sort", "asc:");
        map.put("page", page);
        map.put("pagesize", pageSize);
        map.put("time", System.currentTimeMillis() / 1000);
        map.put("key", "55be8172f7cc74882f8fbad918baa234");

        //HttpUtils.get("http://v.juhe.cn/joke/content/list.php", map, new HttpUtils.HttpCallBack() {
        HttpUtils.post("http://v.juhe.cn/joke/content/list.php", map, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(ResultData resultData) {
                if (resultData.getError_code() == 0) {
                    JSONObject jsonObject = JSON.parseObject(resultData.getResult());
                    List<Joke> list = JsonTool.jsonToList(jsonObject.getString("data"), Joke.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (list != null) {
                                if (page == 1) {
                                    adapter = new JokeAdapter(context, list);
                                    mRvJoke.setAdapter(adapter);
                                } else {
                                    if (adapter != null) {
                                        adapter.addAllData(list);
                                    }
                                }
                                if (list.size() < pageSize) {
                                    mSrlJoke.setEnableLoadMore(false);
                                }
                            }
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, resultData.getReason(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                mSrlJoke.finishLoadMore();
                mSrlJoke.finishRefresh();
            }

            @Override
            public void onFail(String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
                mSrlJoke.finishLoadMore();
                mSrlJoke.finishRefresh();
            }
        });

    }

    private void initData2() {
        //post方法
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)  //设置超时
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("sort", "asc:")
                .add("page", "1")
                .add("pagesize", "20")
                .add("time", String.valueOf(System.currentTimeMillis() / 1000))   //13位时间戳转换为10位
                .add("key", "55be8172f7cc74882f8fbad918baa234")
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url("http://v.juhe.cn/joke/content/list.php")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //失败
                String error = e.getMessage();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //成功
                String data = response.body().string();
                JSONObject jsonObject = JSON.parseObject(data);
                JSONObject jsonObject2 = JSON.parseObject(jsonObject.getString("result"));
                List<Joke> list = JsonTool.jsonToList(jsonObject2.getString("data"), Joke.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JokeAdapter adapter = new JokeAdapter(context, list);
                        mRvJoke.setAdapter(adapter);
                    }
                });
            }
        });

    }

    private void initData() {
        //get方法
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)  //设置超时
                .build();

        Request request = new Request.Builder()
                .get()
                .url("http://v.juhe.cn/joke/content/list.php?sort=asc&page=&pagesize=&time=1418816972&key=55be8172f7cc74882f8fbad918baa234")
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //失败
                String error = e.getMessage();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //成功
                String data = response.body().string();
                JSONObject jsonObject = JSON.parseObject(data);
                JSONObject jsonObject2 = JSON.parseObject(jsonObject.getString("result"));
                List<Joke> list = JsonTool.jsonToList(jsonObject2.getString("data"), Joke.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JokeAdapter adapter = new JokeAdapter(context, list);
                        mRvJoke.setAdapter(adapter);
                    }
                });
            }
        });

    }


}
