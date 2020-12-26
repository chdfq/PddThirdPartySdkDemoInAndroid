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
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.adapter.PddCatsAdapter;
import com.marten.pdd_sdk_demo.adapter.ViewPagerAdapter;
import com.marten.pdd_sdk_demo.domain.PddGoodsCat;
import com.marten.pdd_sdk_demo.tools.JsonTool;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddGoodsCatsGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddGoodsCatsGetResponse;

import java.util.ArrayList;
import java.util.List;

import static com.marten.pdd_sdk_demo.contants.Contants.PDD_CLIENT_ID;
import static com.marten.pdd_sdk_demo.contants.Contants.PDD_CLIENT_SECRET;

public class ShopFragment extends Fragment {

    private Context context;
    private RecyclerView mRvCatsList;
    private ViewPager mVpShop;
    private View view;
    private PddCatsAdapter catsAdapter;
    private List<Fragment> fragments;
    private ShopGoodsCatFragment shopGoodsCatFragment1, shopGoodsCatFragment2,
            shopGoodsCatFragment3, shopGoodsCatFragment4,
            shopGoodsCatFragment5, shopGoodsCatFragment6,
            shopGoodsCatFragment7, shopGoodsCatFragment8;
    private ViewPagerAdapter viewPagerAdapter;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    List<PddGoodsCat> catList = (List<PddGoodsCat>) msg.obj;
                    if (catList.size() > 8) {
                        List<PddGoodsCat> catList2 = catList.subList(0, 8);
                        PddGoodsCat cat = catList2.get(0);
                        cat.setSelected(true);
                        catList2.set(0, cat);
                        catsAdapter = new PddCatsAdapter(context, catList2);
                        catsAdapter.setPddCatsOnClickListener(new PddCatsAdapter.PddCatsOnClickListener() {
                            @Override
                            public void catOnClick(int position, PddGoodsCat pddGoodsCat) {
                                catsAdapter.updateItem(position);
                                mVpShop.setCurrentItem(position);
                            }
                        });
                        initView(catList2);
                        mRvCatsList.setAdapter(catsAdapter);
                    }

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
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        mRvCatsList = view.findViewById(R.id.rv_cats_list);
        mVpShop = view.findViewById(R.id.vp_shop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRvCatsList.setLayoutManager(linearLayoutManager);

        getCatData();
        context = getContext();

        return view;
    }

    private void initView(List<PddGoodsCat> catList) {
        fragments = new ArrayList<>();

        shopGoodsCatFragment1 = new ShopGoodsCatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cat", catList.get(0));
        shopGoodsCatFragment1.setArguments(bundle);

        shopGoodsCatFragment2 = new ShopGoodsCatFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("cat", catList.get(1));
        shopGoodsCatFragment2.setArguments(bundle2);

        shopGoodsCatFragment3 = new ShopGoodsCatFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("cat", catList.get(2));
        shopGoodsCatFragment3.setArguments(bundle3);

        shopGoodsCatFragment4 = new ShopGoodsCatFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("cat", catList.get(3));
        shopGoodsCatFragment4.setArguments(bundle4);

        shopGoodsCatFragment5 = new ShopGoodsCatFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putSerializable("cat", catList.get(4));
        shopGoodsCatFragment5.setArguments(bundle5);

        shopGoodsCatFragment6 = new ShopGoodsCatFragment();
        Bundle bundle6 = new Bundle();
        bundle6.putSerializable("cat", catList.get(5));
        shopGoodsCatFragment6.setArguments(bundle6);

        shopGoodsCatFragment7 = new ShopGoodsCatFragment();
        Bundle bundle7 = new Bundle();
        bundle7.putSerializable("cat", catList.get(6));
        shopGoodsCatFragment7.setArguments(bundle7);

        shopGoodsCatFragment8 = new ShopGoodsCatFragment();
        Bundle bundle8 = new Bundle();
        bundle8.putSerializable("cat", catList.get(7));
        shopGoodsCatFragment8.setArguments(bundle8);

        fragments.add(shopGoodsCatFragment1);
        fragments.add(shopGoodsCatFragment2);
        fragments.add(shopGoodsCatFragment3);
        fragments.add(shopGoodsCatFragment4);
        fragments.add(shopGoodsCatFragment5);
        fragments.add(shopGoodsCatFragment6);
        fragments.add(shopGoodsCatFragment7);
        fragments.add(shopGoodsCatFragment8);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),fragments);
        mVpShop.setAdapter(viewPagerAdapter);
        mVpShop.setCurrentItem(0);
        mVpShop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                catsAdapter.updateItem(position);
                mVpShop.setCurrentItem(position);
                mRvCatsList.scrollToPosition(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getCatData() {
        //子线程不能访问网络，这里新建一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientId = PDD_CLIENT_ID;
                String clientSecret = PDD_CLIENT_SECRET;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddGoodsCatsGetRequest request = new PddGoodsCatsGetRequest();
                request.setParentCatId(0L);
                PddGoodsCatsGetResponse response = null;
                try {
                    response = client.syncInvoke(request);
                    String data = JsonUtil.transferToJson(response);
                    JSONObject jsonObject = JSON.parseObject(data);
                    String listData = jsonObject.getJSONObject("goods_cats_get_response").getString("goods_cats_list");
                    List<PddGoodsCat> catList = JsonTool.jsonToList(listData, PddGoodsCat.class);
                    Message message = new Message();
                    message.what = 2; //what判断哪里发的message
                    message.obj = catList;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
