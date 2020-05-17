package com.juyijia.mm.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.juyijia.mm.AppConfig;
import com.juyijia.mm.Constants;
import com.juyijia.mm.R;
import com.juyijia.mm.activity.WebViewActivity;
import com.juyijia.mm.adapter.MainHomeShopingGridviewAdapter;
import com.juyijia.mm.adapter.MainHomeShopingRecyclerviewAdapter;
import com.juyijia.mm.adapter.TopicAdapter;
import com.juyijia.mm.bean.SettingBean;
import com.juyijia.mm.bean.TopicBean;
import com.juyijia.mm.bean.UserBean;
import com.juyijia.mm.bean.UserItemBean;
import com.juyijia.mm.bean.frontConfig;
import com.juyijia.mm.bean.shouYeLieBiaoBean;
import com.juyijia.mm.custom.MyGridView;
import com.juyijia.mm.glide.ImgLoader;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.CommonCallback;
import com.juyijia.mm.interfaces.MainAppBarLayoutListener;
import com.juyijia.mm.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 商品页面
 */

public class MainHomeShopingViewHolder extends AbsMainChildViewHolder implements View.OnClickListener, MainHomeShopingRecyclerviewAdapter.backItem {
    private MyGridView myGridView;
    private RecyclerView myRecyclerView;
    private NestedScrollView NestedScrollView;
    RecyclerView topicRecyclerView;
    private RoundedImageView myBanner, avatar;
    private TextView tv_address;

    public MainHomeShopingViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.homedatascrollviewholder;
    }

    private ArrayList<frontConfig.DataBean.InfoBean> mTopicData;
    private TopicAdapter topicAdapter;
    MainHomeShopingGridviewAdapter mainHomeShopingGridviewAdapter;
    List<frontConfig.DataBean.InfoBean> mainHomeShopingGridviewdata = new ArrayList<>();
    private double mLng;
    private double mLat;
    MainHomeShopingRecyclerviewAdapter shopingAdapter;
    List<shouYeLieBiaoBean.MessageBeanX.MessageBean.StoresBeanX.StoresBean> data = new ArrayList<>();

    @Override
    public void init() {
        super.init();
//        tv_address = (TextView) findViewById(R.id.tv_address);
//        tv_address.setText(AppConfig.getInstance().getAddress());
        avatar = (RoundedImageView) findViewById(R.id.avatar);
        ImgLoader.displayAvatar(AppConfig.getInstance().getUserBean().getAvatar(), avatar);
        mLng = AppConfig.getInstance().getLng();
        mLat = AppConfig.getInstance().getLat();
        findViewById(R.id.hongbaotaocan).setOnClickListener(this);
        findViewById(R.id.xianshiqiangjuan).setOnClickListener(this);
        findViewById(R.id.lingyuanzhuanqu).setOnClickListener(this);
        findViewById(R.id.myBanner).setOnClickListener(this);
        myBanner = (RoundedImageView) findViewById(R.id.myBanner);
        NestedScrollView = (android.support.v4.widget.NestedScrollView) findViewById(R.id.myNestedScrollView);
        mAppBarLayoutListener = new MainAppBarLayoutListener() {
            @Override
            public void onOffsetChanged(float rate) {
            }
        };
        myGridView = (MyGridView) findViewById(R.id.myGridview);
        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //菜单
        topicRecyclerView = (RecyclerView) findViewById(R.id.topicRecyclerView);
        final View mIndicatorLayout = findViewById(R.id.parent_layout);
        final View mIndicatorView = findViewById(R.id.main_line);
        mTopicData = new ArrayList<>();
        topicAdapter = new TopicAdapter(mContext, mTopicData);
        topicAdapter.setOnItemClickListener(new TopicAdapter.OnItemClickListener() {
            @Override
            public void onTopicItemClick(frontConfig.DataBean.InfoBean position) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.URL, position.getLink());
                mContext.startActivity(intent);
            }
        });
        topicRecyclerView.setAdapter(topicAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topicRecyclerView.setLayoutManager(gridLayoutManager);
        topicRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //当前RcyclerView显示区域的高度。水平列表屏幕从左侧到右侧显示范围
                int extent = recyclerView.computeHorizontalScrollExtent();

                //整体的高度，注意是整体，包括在显示区域之外的。
                int range = recyclerView.computeHorizontalScrollRange();

                //已经滚动的距离，为0时表示已处于顶部。
                int offset = recyclerView.computeHorizontalScrollOffset();

                //计算出溢出部分的宽度，即屏幕外剩下的宽度
                float maxEndX = range - extent;

                //计算比例
                float proportion = offset / maxEndX;

                int layoutWidth = mIndicatorLayout.getWidth();
                int indicatorViewWidth = mIndicatorView.getWidth();

                //可滑动的距离
                int scrollableDistance = layoutWidth - indicatorViewWidth;

                //设置滚动条移动
                mIndicatorView.setTranslationX(scrollableDistance * proportion);
            }
        });
        lanMu();
        shouYeLieBiao();
    }

    @Override
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
    }


    @Override
    public void loadData() {
        if (isFirstLoadData()) {
            AppConfig appConfig = AppConfig.getInstance();
            UserBean u = appConfig.getUserBean();
            List<UserItemBean> list = appConfig.getUserItemList();
            if (u != null && list != null) {
                showData(u, list);
            }
        }
        HttpUtil.getBaseInfo(mCallback);
    }

    private CommonCallback<UserBean> mCallback = new CommonCallback<UserBean>() {
        @Override
        public void callback(UserBean bean) {
            List<UserItemBean> list = AppConfig.getInstance().getUserItemList();
            if (bean != null) {
                showData(bean, list);
            }
        }
    };

    private void showData(UserBean u, List<UserItemBean> list) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hongbaotaocan:
                if (frontConfig == null) {
                    return;
                }
                for (int i = 0; i < frontConfig.getData().getInfo().size(); i++) {
                    if (frontConfig.getData().getInfo().get(i).getModule().contains("红包套餐")) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(Constants.URL, frontConfig.getData().getInfo().get(i).getLink());
                        mContext.startActivity(intent);
                    }
                }
                break;
            case R.id.xianshiqiangjuan:
                if (frontConfig == null) {
                    return;
                }
                for (int i = 0; i < frontConfig.getData().getInfo().size(); i++) {
                    if (frontConfig.getData().getInfo().get(i).getModule().contains("限时抢")) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(Constants.URL, frontConfig.getData().getInfo().get(i).getLink());
                        mContext.startActivity(intent);
                    }
                }
                break;
            case R.id.lingyuanzhuanqu:
                if (frontConfig == null) {
                    return;
                }
                for (int i = 0; i < frontConfig.getData().getInfo().size(); i++) {
                    if (frontConfig.getData().getInfo().get(i).getModule().contains("0元专区")) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(Constants.URL, frontConfig.getData().getInfo().get(i).getLink());
                        mContext.startActivity(intent);
                    }
                }
                break;
            case R.id.myBanner:
                if (frontConfig == null) {
                    return;
                }
                if (frontConfig.getData().getInfo().size() > 0) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Constants.URL, frontConfig.getData().getInfo().get(0).getLink());
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    frontConfig frontConfig;

    /**
     * 栏目
     */
    public void lanMu() {
        OkGo.<String>get(AppConfig.HOST1 + "/api/public/?service=Activity.frontConfig")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mTopicData.clear();
                        mainHomeShopingGridviewdata.clear();
                        Gson gson = new Gson();
                        frontConfig = gson.fromJson(response.body(), frontConfig.class);
                        if (frontConfig.getData() != null && frontConfig.getData().getInfo().size() > 0) {
                            Glide.with(mContext).load("http://juyijia.wtwx.cn" + frontConfig.getData().getInfo().get(0).getIcon()).into(myBanner);
                        }
                        for (int i = 1; i < frontConfig.getData().getInfo().size(); i++) {
                            if (frontConfig.getData().getInfo().get(i).getModule().contains("招聘求职")
                                    || frontConfig.getData().getInfo().get(i).getModule().contains("名医在线") ||
                                    frontConfig.getData().getInfo().get(i).getModule().contains("家政维修")
                                    ||
                                    frontConfig.getData().getInfo().get(i).getModule().contains("房屋租")) {
                                mainHomeShopingGridviewdata.add(frontConfig.getData().getInfo().get(i));
                            } else {
                                mTopicData.add(frontConfig.getData().getInfo().get(i));
                            }
                        }
                        mainHomeShopingGridviewAdapter = new MainHomeShopingGridviewAdapter(mContext, mainHomeShopingGridviewdata);
                        myGridView.setAdapter(mainHomeShopingGridviewAdapter);
                        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(mContext, WebViewActivity.class);
                                intent.putExtra(Constants.URL, mainHomeShopingGridviewdata.get(i).getLink());
                                mContext.startActivity(intent);
                            }
                        });
                        topicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
    }

    /**
     * 首页列表
     */
    public void shouYeLieBiao() {
        OkGo.<String>get(AppConfig.HOST2)
                .params("i", "2")
                .params("m", "we7_wmall")
                .params("c", "entry")
                .params("do", "mobile")
                .params("lang", "zh-cn")
                .params("ctrl", "diypage")
                .params("ac", "diy")
                .params("from", "vue")
                .params("u", "wap")
                .params("lat", "37.51856")
                .params("lng", "121.3549")
                .params("id", "17")
                .params("menufooter", "1")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        shouYeLieBiaoBean shouYeLieBiaoBean = gson.fromJson(response.body(), shouYeLieBiaoBean.class);
                        data.addAll(shouYeLieBiaoBean.getMessage().getMessage().getStores().getStores());
                        shopingAdapter = new MainHomeShopingRecyclerviewAdapter(mContext, data,MainHomeShopingViewHolder.this);
                        myRecyclerView.setAdapter(shopingAdapter);
                        myRecyclerView.setNestedScrollingEnabled(false);
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
    }

    @Override
    public void onBackItem(int position) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(Constants.URL, "https://waimai.juyijia.cn/addons/we7_wmall/template/vue/index.html?menu=#/pages/store/goods?sid="+
                data.get(position).getId()+"&i=2");
        mContext.startActivity(intent);
    }
}
