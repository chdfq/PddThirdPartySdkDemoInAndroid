package com.marten.pdd_sdk_demo.activity;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.adapter.ViewPagerAdapter;
import com.marten.pdd_sdk_demo.databinding.ActivityBaseBinding;
import com.marten.pdd_sdk_demo.databinding.ActivityNewMainBinding;
import com.marten.pdd_sdk_demo.fragment.JokeFragment;
import com.marten.pdd_sdk_demo.fragment.MineFragment;
import com.marten.pdd_sdk_demo.fragment.NewsFragment;
import com.marten.pdd_sdk_demo.fragment.ShopFragment;
import com.marten.pdd_sdk_demo.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class NewMainActivity extends BaseActivity {

    private List<Fragment> fragments = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private ShopFragment shopFragment;
    private NewsFragment newsFragment;
    private JokeFragment jokeFragment;
    private MineFragment mineFragment;
    private RadioGroup mRg;
    private RadioButton mRb1, mRb2, mRb3, mRb4;
    private NoScrollViewPager mVp;

    @Override
    public void initView() {
        contentView(R.layout.activity_new_main);
        setMyTitle("优惠券商城");
        mVp = findViewById(R.id.vp_main);
        mRg = findViewById(R.id.rg1);
        mRb1 = findViewById(R.id.rb1);
        mRb2 = findViewById(R.id.rb2);
        mRb3 = findViewById(R.id.rb3);
        mRb4 = findViewById(R.id.rb4);
        initData();
        mVp.setCurrentItem(0);
        mRg.check(R.id.rb1);

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRg.check(R.id.rb1);
                        break;
                    case 1:
                        mRg.check(R.id.rb2);
                        break;
                    case 2:
                        mRg.check(R.id.rb3);
                        break;
                    case 3:
                        mRg.check(R.id.rb4);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        mVp.setCurrentItem(0);
                        break;
                    case R.id.rb2:
                        mVp.setCurrentItem(1);
                        break;
                    case R.id.rb3:
                        mVp.setCurrentItem(2);
                        break;
                    case R.id.rb4:
                        mVp.setCurrentItem(3);
                        break;
                }
            }
        });
    }

    private void initData() {
        shopFragment = new ShopFragment();
        newsFragment = new NewsFragment();
        jokeFragment = new JokeFragment();
        mineFragment = new MineFragment();
        fragments.add(shopFragment);
        fragments.add(newsFragment);
        fragments.add(jokeFragment);
        fragments.add(mineFragment);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mVp.setAdapter(viewPagerAdapter);
    }
}
