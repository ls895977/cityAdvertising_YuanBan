package com.juyijia.mm.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juyijia.mm.Constants;
import com.juyijia.mm.R;
import com.juyijia.mm.activity.SearchActivity;
import com.juyijia.mm.adapter.ViewPagerAdapter;
import com.juyijia.mm.bean.VideoBean;
import com.juyijia.mm.custom.MyViewPager;
import com.juyijia.mm.custom.ViewPagerIndicator;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.LifeCycleAdapter;
import com.juyijia.mm.interfaces.LifeCycleListener;
import com.juyijia.mm.interfaces.MainAppBarLayoutListener;
import com.juyijia.mm.interfaces.VideoScrollDataHelper;
import com.juyijia.mm.utils.ScreenDimenUtil;
import com.juyijia.mm.utils.ToastUtil;
import com.juyijia.mm.utils.VideoStorge;
import com.juyijia.mm.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 短视频主页
 */

public class MainHomeViewHolder2 extends AbsMainViewHolder implements View.OnClickListener,VideoScrollViewHolder.OnVideoitemChangeListener {

    private VideoScrollViewHolder[] mViewHolders;
    private ViewPagerIndicator mIndicator;
    private MyViewPager mViewPager;
    private int mScreenWidth;
    private VideoScrollDataHelper mVideoScrollDataHelper;
    private static final int TOTAL = 60;
    private int mCount = TOTAL;
    private TextView mSearch;
    private TextView mTadyMoney;
    private TextView mAllMoney;
    private TextView mCountDown;
    private Handler mHandler;
    private Handler mAdHandler;
    private RelativeLayout mNormalTop;
    private TextView mAdTadyMoney;
    private TextView mAdAllMoney;
    private TextView mAdAllStone;
    private LinearLayout mAdTop;
    private VideoBean selectVideoBean;
    private boolean isNewUser = true;

    public MainHomeViewHolder2(Context context, ViewGroup parentView,boolean isNewUser) {
        super(context, parentView);
        this.isNewUser = isNewUser;
        setCountDown();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home_two;
    }

    @Override
    public void init() {
        mNormalTop = (RelativeLayout) findViewById(R.id.rl_home_video_normal_top);
        mSearch = (TextView) findViewById(R.id.tv_home_searcher);
        mCountDown = (TextView) findViewById(R.id.tv_home_countdown);
        mTadyMoney = (TextView) findViewById(R.id.tv_home_today);
        mAllMoney = (TextView) findViewById(R.id.tv_home_all);
        mViewPager = (MyViewPager) findViewById(R.id.viewPager);

        mAdTadyMoney = (TextView) findViewById(R.id.tv_home_ad_today);
        mAdAllMoney = (TextView) findViewById(R.id.tv_home_ad_all);
        mAdAllStone = (TextView) findViewById(R.id.tv_home_ad_stone);
        mAdTop = (LinearLayout) findViewById(R.id.rl_home_video_ad_top);

        mSearch.setOnClickListener(this);
        mVideoScrollDataHelper = new VideoScrollDataHelper() {
            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getHomeVideoList(p, callback);
            }
        };
        VideoStorge.getInstance().putDataHelper(Constants.VIDEO_HOME, mVideoScrollDataHelper);
        mViewPager.setOffscreenPageLimit(3);
        mViewHolders = new VideoScrollViewHolder[2];
        mViewHolders[0] = new VideoScrollViewHolder(mContext, mViewPager, 0, Constants.VIDEO_HOME, 1);
        mViewHolders[1] = new VideoScrollViewHolder(mContext, mViewPager, 0, Constants.VIDEO_HOME, 1);
        mViewHolders[0].setOnVideoitemChangeListener(this);
        mViewHolders[1].setOnVideoitemChangeListener(this);
        List<View> list = new ArrayList<>();
        for (VideoScrollViewHolder vh : mViewHolders) {
            list.add(vh.getContentView());
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mScreenWidth = ScreenDimenUtil.getInstance().getScreenWdith();
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mIndicator.setTitles(new String[]{
                WordUtil.getString(R.string.main_home_video_recommand),
                WordUtil.getString(R.string.main_home_video_nearby)
        });
        mIndicator.setViewPager(mViewPager);
        mIndicator.setListener(new ViewPagerIndicator.OnPageChangeListener() {
            @Override
            public void onTabClick(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewHolders[mViewPager.getCurrentItem()].loadData();
                mViewHolders[0].setShow(position == 0);
                mViewHolders[1].setShow(position == 1);
                if (position == 0) {
                    mViewHolders[1].pausePlay();
                    mViewHolders[0].resumePlay();
                } else {
                    mViewHolders[0].pausePlay();
                    mViewHolders[1].resumePlay();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTadyMoney.setText(26+"");
        mAllMoney.setText(2666+"");
        mAdTadyMoney.setText(26 + "");
        mAdAllMoney.setText(2666 + "");
        mAdAllStone.setText(12345 + "");

        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onCreate() {
//                EventBus.getDefault().register(MainHomeViewHolder2.this);
            }

            @Override
            public void onDestroy() {
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler = null;
                }
                if (mAdHandler != null) {
                    mAdHandler.removeCallbacksAndMessages(null);
                    mAdHandler = null;
                }
            }
        };
        mViewHolders[mViewPager.getCurrentItem()].loadData();
        mViewHolders[mViewPager.getCurrentItem()].setShow(true);
    }


    @Override
    public void setShowed(boolean showed) {
        super.setShowed(showed);
        if (showed){
            mViewHolders[mViewPager.getCurrentItem()].setShow(true);
            mViewHolders[mViewPager.getCurrentItem()].resumePlay();
        }else {
            mViewHolders[0].setShow(false);
            mViewHolders[1].setShow(false);
        }
    }

    private void setCountDown() {
        if (!isNewUser) {
            mCountDown.setVisibility(View.GONE);
            return;
        }
        mCountDown.setVisibility(View.VISIBLE);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mCount--;
                if (mCount > 0) {
                    if (mCountDown != null) {
                        mCountDown.setText(String.valueOf(mCount));
                    }
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                } else {
                    mCount = TOTAL;
                    isNewUser = false;
                    if (mCountDown != null) {
                        mCountDown.setVisibility(View.GONE);
                    }
                }
            }
        };
        if (mHandler != null) {
            mHandler.sendEmptyMessage(0);
        }
    }

    public void setPlayVideo(boolean isPlay) {
        if (isPlay) {
            mViewHolders[mViewPager.getCurrentItem()].resumePlay();
        } else {
            mViewHolders[0].pausePlay();
            mViewHolders[1].pausePlay();
        }
    }

    @Override
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
//        if (mViewHolders != null) {
//            for (AbsMainListViewHolder vh : mViewHolders) {
//                vh.setAppBarLayoutListener(appBarLayoutListener);
//            }
//        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home_searcher:
                SearchActivity.forward(mContext);
                break;
        }
    }

    @Override
    public List<LifeCycleListener> getLifeCycleListenerList() {
        List<LifeCycleListener> list = new ArrayList<>();
        if (mLifeCycleListener != null) {
            list.add(mLifeCycleListener);
        }
        for (VideoScrollViewHolder vh : mViewHolders) {
            LifeCycleListener listener = vh.getLifeCycleListener();
            if (listener != null) {
                list.add(listener);
            }
        }
        return list;
    }


    @Override
    public void onVideoStateChange(boolean isPlay) {
        if (!selectVideoBean.isAd()){
            return;
        }
        if (isPlay) {
//            ToastUtil.show("播放");
            if (mAdHandler != null  && mCount > 0) {
                mCount++;
                mAdHandler.removeCallbacksAndMessages(null);
                mAdHandler.sendEmptyMessage(0);
            }
        } else {
//            ToastUtil.show("暂停");
            if (mAdHandler != null && selectVideoBean.isAd()) {
                mAdHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    @Override
    public void onPageSelected(VideoBean bean) {
        selectVideoBean = bean;
        if (mAdHandler != null) {
            mAdHandler.removeCallbacksAndMessages(null);
            mCount = 16;
        }
        mViewPager.setCanScroll(!bean.isAd());
        if (bean.isAd()) {
            mNormalTop.setVisibility(View.GONE);
            mAdTop.setVisibility(View.VISIBLE);
            setAdCountDown(bean);
        } else {
            if (!isNewUser) {
                mCountDown.setVisibility(View.GONE);
            }
            mNormalTop.setVisibility(View.VISIBLE);
            mAdTop.setVisibility(View.GONE);
        }
    }

    private void setAdCountDown(VideoBean bean) {
        if (isNewUser) return;
        mCountDown.setVisibility(View.VISIBLE);
        if (mAdHandler == null) {
            mCount = 15;
            mAdHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    mCount--;
                    if (mCount > 0) {
                        if (mCountDown != null) {
                            mCountDown.setText(String.valueOf(mCount));
                        }
                        if (mAdHandler != null) {
                            mAdHandler.sendEmptyMessageDelayed(0, 1000);
                        }
                    } else {
                        ToastUtil.show(selectVideoBean.getTitle());
                        if (mAdTadyMoney != null) {
                            mAdTadyMoney.setText((Integer.valueOf(mAdTadyMoney.getText().toString().trim()) + 1) + "");
                        }
                        mCount = 16;
                        if (mCountDown != null) {
                            mCountDown.setVisibility(View.GONE);
                        }
                    }
                }
            };
        }
        mAdHandler.sendEmptyMessage(0);
    }
    //    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onFollowEvent(FollowEvent e) {
//        if (e.getFrom() != Constants.FOLLOW_FROM_LIST) {
//            for (AbsMainChildTopViewHolder vh : mViewHolders) {
//                vh.onFollowEvent(e.getToUid(), e.getIsAttention());
//            }
//        }
//    }


}
