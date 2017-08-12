package com.smartbutler.tanhuihui.butler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.smartbutler.tanhuihui.butler.fragment.BulterFragment;
import com.smartbutler.tanhuihui.butler.fragment.PictFragment;
import com.smartbutler.tanhuihui.butler.fragment.UserSettingFragment;
import com.smartbutler.tanhuihui.butler.fragment.WechatFragment;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //名称
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragment;
    //setting按钮，设置页面删除了
//    private FloatingActionButton mfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置ActionBar的阴影度为0
        getSupportActionBar().setElevation(0);

        initData();
        initView();
        setAdapter();
//        setListener();

        //bugly 测试
        //CrashReport.testJavaCrash();
    }

    //设置监听器

    /**
     * 设置页面已经删除了
     */
/*    private void setListener() {
        //设置页面删除了
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        //设置mfab的可见性监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                    mfab.setVisibility(View.GONE);
                else
                    mfab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }*/

    //设置适配器
    private void setAdapter() {
        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //获取item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        //TabLayout绑定ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //初始化View
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        //设置按钮
//        mfab = (FloatingActionButton) findViewById(R.id.mfloatButton);

        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
    }

    //初始化数据
    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add(this.getString(R.string.butler_str));
        mTitle.add(this.getString(R.string.wechat_str));
        mTitle.add(this.getString(R.string.picture_str));
        mTitle.add(this.getString(R.string.setting_str));

        mFragment = new ArrayList<>();
        mFragment.add(new BulterFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new PictFragment());
        mFragment.add(new UserSettingFragment());
    }
}
