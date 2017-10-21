package com.smartbutler.tanhuihui.butler.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.adapter.PictureAdapter;
import com.smartbutler.tanhuihui.butler.entity.PictureEntity;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.PicassoUtil;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.smartbutler.tanhuihui.butler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.fragment
 * 创建日期： 2017/6/5
 * 描  述：
 */

public class PictFragment extends Fragment {

    private GridView gv_picture;
    private List<PictureEntity> pList = new ArrayList<>();
    //适配器
    private PictureAdapter adapter;

    //是否到达了底部
    private boolean isScrollBottom = false;
    //是否进行刷新
    private boolean isfreshing = false;
    //当前页数
    private int curPage = (int) (Math.random()*50);
    private String URL = StaticClass.GRIL_PICTURE + curPage;
    //弹出框
    private Dialog dialog;
    //弹出框照片
    private PhotoView iv_gril;
    //缩放功能
    private PhotoViewAttacher attacher;
    //进度
    private ProgressBar pb_picture;
    //屏幕长宽
    private int wide;
    private int height;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, null);
        initView(view);
        setListener();
        return view;
    }

    private void setListener() {
        //上拉刷新,监听
        gv_picture.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动结束或者手指离开屏幕
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    //当滑到最底，且没在刷新
                    if (isScrollBottom && !isfreshing) {
                        //页数加1
                        curPage = (int) (Math.random()*50);
                        URL = StaticClass.GRIL_PICTURE + curPage;
                        isfreshing = true;
                        gv_picture.setSelection(gv_picture.getCount());
                        //加载图片
                        RxVolley.get(URL, new HttpCallback() {
                            @Override
                            public void onSuccessInAsync(byte[] s) {
                                String t = new String(s);
                                //解析
                                parsingJSON(t);

                            }
                            @Override
                            public void onFinish() {
                                adapter.notifyDataSetChanged();
                                isfreshing = false;
                            }
                        });
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //判断是否滑到了最后
                if (view.getLastVisiblePosition() == totalItemCount - 1) {
                    isScrollBottom = true;
                } else
                    isScrollBottom = false;
            }
        });


        gv_picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wide = getActivity().getResources().getDisplayMetrics().widthPixels;
                height = getActivity().getResources().getDisplayMetrics().heightPixels;
                PicassoUtil.loadSizeImage(getActivity(),pList.get(position).getImageURL(),iv_gril,
                        wide,height);
                //刷新控件
                attacher.update();
                //弹出
                dialog.show();
            }
        });

        attacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                dialog.dismiss();
            }
        });
    }

    private void initView(View view) {
        gv_picture = (GridView) view.findViewById(R.id.gv_picture);
        pb_picture = (ProgressBar) view.findViewById(R.id.pb_picture);
        //加载图片
        RxVolley.get(URL, new HttpCallback() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                pb_picture.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccessInAsync(byte[] s) {
                String t = new String(s);
                //解析
                parsingJSON(t);
            }

            @Override
            public void onFinish() {
                adapter = new PictureAdapter(getActivity(), pList);
                gv_picture.setAdapter(adapter);
                pb_picture.setVisibility(View.GONE);
            }
        });

        //弹出框
//        dialog = new CustomDialog(getActivity(),0,0,R.layout.dialog_picture,R.style.Theme_dialog2, Gravity.CENTER,R.style.pop_anin_style);
//        dialog.setContentView(R.layout.dialog_picture);

        dialog = new Dialog(getActivity(),R.style.Theme_dialog2);
        dialog.setContentView(R.layout.dialog_picture);
        iv_gril = (PhotoView) dialog.findViewById(R.id.iv_gril);

        //iv_gril上具有缩放功能
        attacher = new PhotoViewAttacher(iv_gril);
    }

    private void parsingJSON(String t) {
        JSONObject object = null;
        JSONArray result = null;
        PictureEntity p = null;
        String url = null;
        try {
            object = new JSONObject(t);
            result = object.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject o = (JSONObject) result.get(i);
                url = o.getString("url");
                p = new PictureEntity(url);
                pList.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
