package com.juyijia.mm.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.juyijia.mm.AppConfig;
import com.juyijia.mm.R;
import com.juyijia.mm.activity.LiveClassActivity;
import com.juyijia.mm.adapter.MainHomeLiveClassAdapter;
import com.juyijia.mm.adapter.ViewPagerAdapter;
import com.juyijia.mm.bean.ConfigBean;
import com.juyijia.mm.bean.LiveClassBean;
import com.juyijia.mm.http.HttpConsts;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.LifeCycleAdapter;
import com.juyijia.mm.interfaces.LifeCycleListener;
import com.juyijia.mm.interfaces.MainAppBarExpandListener;
import com.juyijia.mm.interfaces.OnItemClickListener;
import com.juyijia.mm.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * MainActivity 首页
 */

public class MainHomeViewHolder extends AbsMainParentViewHolder implements OnItemClickListener<LiveClassBean> {

    private RecyclerView mClassRecyclerView;
    private ObjectAnimator mShowAnimator;
    private ObjectAnimator mHideAnimator;
    private View mShadow;
    private View mBtnDismiss;
    private boolean mNeedShowClassListDialog;//是否需要显示分类列表弹窗
    private boolean mExpand;//AppbarLayout是否展开
    private boolean mPaused;

    public MainHomeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home;
    }

    @Override
    public void init() {
        super.init();
        mViewHolders = new AbsMainChildTopViewHolder[3];
        mViewHolders[0] = new MainHomeFollowViewHolder(mContext, mViewPager);
        mViewHolders[1] = new MainHomeLiveViewHolder(mContext, mViewPager);
        mViewHolders[2] = new MainHomeVideoViewHolder(mContext, mViewPager);
        MainAppBarExpandListener expandListener = new MainAppBarExpandListener() {
            @Override
            public void onExpand(boolean expand) {
                mExpand = expand;
                if (mViewPager != null) {
                    mViewPager.setCanScroll(expand);
                    mViewHolders[mViewPager.getCurrentItem()].setCanRefresh(expand);
                }
                if (mNeedShowClassListDialog) {
                    mNeedShowClassListDialog = false;
                    showClassListDialog();
                }
            }
        };
        List<View> list = new ArrayList<>();
        for (AbsMainChildTopViewHolder vh : mViewHolders) {
            vh.setAppBarExpandListener(expandListener);
            list.add(vh.getContentView());
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mIndicator.setTitles(new String[]{
                WordUtil.getString(R.string.follow),
                WordUtil.getString(R.string.live),
                WordUtil.getString(R.string.video)
        });
        mIndicator.setViewPager(mViewPager);
        //点击分类item的监听
        ((MainHomeLiveViewHolder) mViewHolders[1]).setLiveClassItemClickListener(this);
        mShadow = findViewById(R.id.shadow);
        mBtnDismiss = findViewById(R.id.btn_dismiss);
        mBtnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClick()) {
                    mShowAnimator.cancel();
                    mHideAnimator.start();
                }
            }
        });
        mClassRecyclerView = (RecyclerView) findViewById(R.id.classRecyclerView);
        mClassRecyclerView.setHasFixedSize(true);
        mClassRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5, GridLayoutManager.VERTICAL, false));
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean != null) {
            List<LiveClassBean> liveClassList = configBean.getLiveClass();
            if (liveClassList != null && liveClassList.size() > 0) {
                MainHomeLiveClassAdapter adapter = new MainHomeLiveClassAdapter(mContext, liveClassList, true);
                adapter.setOnItemClickListener(this);
                mClassRecyclerView.setAdapter(adapter);
                mClassRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        initAnim();
                    }
                });
            }
        }
    }

    /**
     * 显示分类列表弹窗
     */
    private void showClassListDialog() {
        if (mBtnDismiss != null && mBtnDismiss.getVisibility() != View.VISIBLE) {
            mBtnDismiss.setVisibility(View.VISIBLE);
        }
        if (mShowAnimator != null) {
            mShowAnimator.start();
        }
    }

    /**
     * 初始化弹窗动画
     */
    private void initAnim() {
        final int height = mClassRecyclerView.getHeight();
        mClassRecyclerView.setTranslationY(-height);
        mShowAnimator = ObjectAnimator.ofFloat(mClassRecyclerView, "translationY", 0);
        mShowAnimator.setDuration(200);
        mHideAnimator = ObjectAnimator.ofFloat(mClassRecyclerView, "translationY", -height);
        mHideAnimator.setDuration(200);
        TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
        mShowAnimator.setInterpolator(interpolator);
        mHideAnimator.setInterpolator(interpolator);
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rate = 1 + ((float) animation.getAnimatedValue() / height);
                mShadow.setAlpha(rate);
            }
        };
        mShowAnimator.addUpdateListener(updateListener);
        mHideAnimator.addUpdateListener(updateListener);
        mHideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mBtnDismiss != null && mBtnDismiss.getVisibility() == View.VISIBLE) {
                    mBtnDismiss.setVisibility(View.INVISIBLE);
                }
            }

        });
    }

    @Override
    public void onItemClick(LiveClassBean bean, int position) {
        if (!canClick()) {
            return;
        }
        if (bean.isAll()) {//全部分类
            if (mExpand) {//如果AppBarLayout展开了，直接显示分类弹窗
                showClassListDialog();
            } else {//否则，先让AppBarLayout展开，然后显示分类弹窗
                mNeedShowClassListDialog = true;
                mViewHolders[1].expand();
            }
        } else {
            LiveClassActivity.forward(mContext, bean.getId(), bean.getName());
        }
    }


    @Override
    public List<LifeCycleListener> getLifeCycleListenerList() {
        List<LifeCycleListener> list = new ArrayList<>();
        list.add(new LifeCycleAdapter() {
            @Override
            public void onResume() {
                if (mPaused) {
                    mPaused = false;
                    if (mViewPager != null && mViewHolders != null) {
                        mViewHolders[mViewPager.getCurrentItem()].loadData();
                    }
                }
            }

            @Override
            public void onPause() {
                mPaused = true;
            }

            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_HOT);
                HttpUtil.cancel(HttpConsts.GET_FOLLOW);
                HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO_LIST);
            }
        });
        for (AbsMainChildTopViewHolder vh : mViewHolders) {
            LifeCycleListener lifeCycleListener = vh.getLifeCycleListener();
            if (lifeCycleListener != null) {
                list.add(lifeCycleListener);
            }
        }
        return list;
    }

    public void setCurrentPage(int position) {
        if (mViewHolders != null) {
            for (int i = 0, length = mViewHolders.length; i < length; i++) {
                if (position == i) {
                    mViewHolders[i].addTopView(mTopView);
                    mViewHolders[i].setNeedDispatch(true);
                } else {
                    mViewHolders[i].setNeedDispatch(false);
                }
            }
            if (position == 0) {
                mViewHolders[0].loadData();
            }
        }
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }
    }

}
