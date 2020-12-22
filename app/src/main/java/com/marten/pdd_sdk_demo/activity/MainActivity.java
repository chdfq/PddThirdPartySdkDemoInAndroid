package com.marten.pdd_sdk_demo.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.adapter.GoodsAdapter;
import com.marten.pdd_sdk_demo.adapter.NewsAdapter;
import com.marten.pdd_sdk_demo.domain.News;
import com.marten.pdd_sdk_demo.tools.HttpTools;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SmartRefreshLayout mSrlMain;
    private RecyclerView mRvGoods;
    private GoodsAdapter adapter;
    private int page = 0;
    private int pageSize = 30;

    //子线程不能更新UI
    //方法2：使用handler，发送空信息
//    private Handler handler = new Handler(Looper.myLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> list = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
//            mTv1.setText(list.get(0).getGoodsName());
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSrlMain = findViewById(R.id.srl_main);
        //下拉刷新
        mSrlMain.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                getGoods();
            }
        });
        //上拉加载
        mSrlMain.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getGoods();
            }
        });

        mRvGoods = findViewById(R.id.rv_goods);
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        //getGoods();
        getNewsData();
    }

    private void getGoods() {

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


                //子线程不能更新UI
                //方法1：返回UI线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (goodsList != null && goodsList.size() > 0) {
                            if (page == 1) {
                                adapter = new GoodsAdapter(MainActivity.this, goodsList);
                                mRvGoods.setAdapter(adapter);
                            } else {
                                if (adapter != null) {
                                    adapter.addAllGoods(goodsList);
                                }
                            }
                            if (goodsList.size() < pageSize) {
                                mSrlMain.setEnableLoadMore(false); //数据已经请求完，则停止加载
                            }
                        } else {
                            mSrlMain.setEnableLoadMore(false); //没有加载到list，则停止加载
                        }
                        mSrlMain.finishLoadMore();
                        mSrlMain.finishRefresh();
                    }
                });

//                //方法2：使用handler，发送信息
//                Message message = new Message();
//                message.what = 1; //what判断哪里发的message
//                message.obj = goodsList;
//                handler.sendMessage(message);


//                Gson gson = new Gson();
//                PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem item =
//                        gson.fromJson("", PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem.class); //Gson转对象
//                List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> items =
//                        gson.fromJson("", new TypeToken<List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>>() {
//                        }.getType()); //Gson转list
            }
        }).start();
    }

    public void getNewsData() {
//        HttpTools.getData("http://v.juhe.cn/toutiao/index?key=c60ab75f8ce6994bc6a06ea5adb617db");
        HttpTools.postData("http://v.juhe.cn/toutiao/index", new HttpTools.HttpBackListener() {
            @Override
            public void onSuccess(String data, int code) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String result = jsonObject.getString("result");
                    JSONObject jsonObject2 = new JSONObject(result);
                    JSONArray jsonArray = jsonObject2.getJSONArray("data");
                    List<News> list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        News news1 = new News();
                        news1.setUniquekey(jsonObject3.getString("uniquekey"));
                        news1.setTitle(jsonObject3.getString("title"));
                        news1.setDate(jsonObject3.getString("date"));
                        news1.setCategory(jsonObject3.getString("category"));
                        news1.setAuthor_name(jsonObject3.getString("author_name"));
                        news1.setUrl(jsonObject3.getString("url"));
                        news1.setThumbnail_pic_s(jsonObject3.getString("thumbnail_pic_s"));
//                        news1.setThumbnail_pic_s02(jsonObject3.getString("thumbnail_pic_s02")); //有些item只有thumbnail_pic_s，不然会跳出循环
//                        news1.setThumbnail_pic_s03(jsonObject3.getString("thumbnail_pic_s03"));
                        list.add(news1);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, list);
                            mRvGoods.setAdapter(newsAdapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMes, int code) {

            }
        });
    }
}