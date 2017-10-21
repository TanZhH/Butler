package com.smartbutler.tanhuihui.butler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.smartbutler.tanhuihui.butler.MainActivity;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.tinker.TinkerService;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/6/6
 * 描  述：   引导页
 */

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;
    //容器
    private List<View> itemViwes;
    //小圆点
    private ImageView point1,point2,point3;
    //跳过
    private ImageView mjump;
    //item3进入主页按钮
    private Button mbutton;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        setAdapter();
        setlistener();
    }


    private void setlistener() {
        //设置滑动监听
        mViewPager.addOnPageChangeListener(this);
        //设置点击跳过监听
        mjump.setOnClickListener(this);
        //进入主页监听
        mbutton.setOnClickListener(this);
    }

    //viewpager设置适配器
    private void setAdapter() {
        //为ViewPager加载适配器
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return itemViwes.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager)container).addView(itemViwes.get(position));
                return itemViwes.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager)container).removeView(itemViwes.get(position));
            }
        });


    }

    //初始化
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        View item1 = View.inflate(this,R.layout.item_one,null);
        View item2 = View.inflate(this,R.layout.item_two,null);
        View item3 = View.inflate(this,R.layout.item_three,null);

        itemViwes = new ArrayList<View>();
        itemViwes.add(item1);
        itemViwes.add(item2);
        itemViwes.add(item3);

        point1 = (ImageView) findViewById(R.id.mpoint1);
        point2 = (ImageView) findViewById(R.id.mpoint2);
        point3 = (ImageView) findViewById(R.id.mpoint3);
        setPointSelect(true,false,false);

        mjump = (ImageView) findViewById(R.id.mjump);
        mbutton = (Button) item3.findViewById(R.id.mbutton);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //page选择监听
    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                setPointSelect(true,false,false);
                mjump.setVisibility(View.VISIBLE);
                break;
            case 1:
                setPointSelect(false,true,false);
                mjump.setVisibility(View.VISIBLE);
                break;
            case 2:
                setPointSelect(false,false,true);
                mjump.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //圆点图片设置
    public void setPointSelect(boolean check1 , boolean check2 , boolean check3){
        if (check1){
            point1.setImageResource(R.drawable.point_on);
            point2.setImageResource(R.drawable.point_off);
            point3.setImageResource(R.drawable.point_off);
        }

        if (check2){
            point1.setImageResource(R.drawable.point_off);
            point2.setImageResource(R.drawable.point_on);
            point3.setImageResource(R.drawable.point_off);
        }

        if (check3){
            point1.setImageResource(R.drawable.point_off);
            point2.setImageResource(R.drawable.point_off);
            point3.setImageResource(R.drawable.point_on);
        }
    }

    //点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mjump:
            case R.id.mbutton:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
