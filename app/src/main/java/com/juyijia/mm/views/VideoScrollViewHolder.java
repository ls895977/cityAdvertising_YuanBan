package com.juyijia.mm.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.juyijia.mm.R;
import com.juyijia.mm.activity.AbsVideoActivity;
import com.juyijia.mm.activity.VideoPlayActivity;
import com.juyijia.mm.adapter.VideoScrollAdapter;
import com.juyijia.mm.bean.VideoBean;
import com.juyijia.mm.custom.VideoLoadingBar;
import com.juyijia.mm.event.FollowEvent;
import com.juyijia.mm.event.VideoCommentEvent;
import com.juyijia.mm.event.VideoDeleteEvent;
import com.juyijia.mm.event.VideoLikeEvent;
import com.juyijia.mm.event.VideoScrollPageEvent;
import com.juyijia.mm.event.VideoShareEvent;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpConsts;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.LifeCycleAdapter;
import com.juyijia.mm.interfaces.VideoScrollDataHelper;
import com.juyijia.mm.utils.ToastUtil;
import com.juyijia.mm.utils.VideoStorge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/11/26.
 * 视频滑动
 */

public class VideoScrollViewHolder extends AbsViewHolder implements
        VideoScrollAdapter.ActionListener, SwipeRefreshLayout.OnRefreshListener,
        VideoPlayViewHolder.ActionListener, View.OnClickListener {

    private VideoPlayViewHolder mVideoPlayViewHolder;
    private View mPlayView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private VideoScrollAdapter mVideoScrollAdapter;
    private int mPosition;
    private String mVideoKey;
    private VideoPlayWrapViewHolder mVideoPlayWrapViewHolder;
    private VideoLoadingBar mVideoLoadingBar;
    private int mPage;
    private HttpCallback mRefreshCallback;//下拉刷新回调
    private HttpCallback mLoadMoreCallback;//上拉加载更多回调
    private VideoScrollDataHelper mVideoDataHelper;
    private VideoBean mVideoBean;
    private boolean mPaused;//生命周期暂停
    private List<VideoBean> list;
    private boolean isFirstLode = true;
    private boolean isShow = false; //此页面是否显示

    public VideoScrollViewHolder(Context context, ViewGroup parentView, int position, String videoKey, int page) {
        super(context, parentView, position, videoKey, page);
    }

    @Override
    protected void processArguments(Object... args) {
        mPosition = (int) args[0];
        mVideoKey = (String) args[1];
        mPage = (int) args[2];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_scroll;
    }

    @Override
    public void init() {
        list = VideoStorge.getInstance().get(mVideoKey);
        if (list == null) {
            list = new ArrayList<>();
        }
        mVideoPlayViewHolder = new VideoPlayViewHolder(mContext, null);
        mVideoPlayViewHolder.setActionListener(this);
        mPlayView = mVideoPlayViewHolder.getContentView();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.global);
        mRefreshLayout.setEnabled(false);//产品不让使用刷新
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mVideoScrollAdapter = new VideoScrollAdapter(mContext, list, mPosition);
        mVideoScrollAdapter.setActionListener(this);
        mRecyclerView.setAdapter(mVideoScrollAdapter);
//        mVideoLoadingBar = (VideoLoadingBar) findViewById(R.id.video_loading);
//        findViewById(R.id.input_tip).setOnClickListener(this);
        findViewById(R.id.btn_face).setOnClickListener(this);
        EventBus.getDefault().register(this);

        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onResume() {
                mPaused = false;
                if (mVideoPlayViewHolder != null) {
                    resumePlay();
                }
            }

            @Override
            public void onPause() {
                mPaused = true;
                if (mVideoPlayViewHolder != null) {
                    pausePlay();
                }
            }

        };
        mRefreshCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<VideoBean> list = JSON.parseArray(Arrays.toString(info), VideoBean.class);
                    if (mVideoScrollAdapter != null) {
//                        for (int i = 0; i < list.size(); i++) {
//                            VideoBean bean = list.get(i);
//                            bean.setAd(i%2==0);
//                        }
                        mVideoScrollAdapter.setList(list);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        };
        mLoadMoreCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<VideoBean> list = JSON.parseArray(Arrays.toString(info), VideoBean.class);
                    if (list.size() > 0) {
                        if (mVideoScrollAdapter != null) {
                            mVideoScrollAdapter.insertList(list);
                        }
                        EventBus.getDefault().post(new VideoScrollPageEvent(mVideoKey, mPage));
                    } else {
                        ToastUtil.show(R.string.video_no_more_video);
                        mPage--;
                    }
                } else {
                    mPage--;
                }
            }
        };
        mVideoDataHelper = VideoStorge.getInstance().getDataHelper(mVideoKey);
    }

    public void loadData() {
        if (isFirstLode) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
            isFirstLode = false;
        }
    }

    public void pausePlay() {
        mVideoPlayViewHolder.pausePlay();
    }

    public void resumePlay() {
        if (isShow){
            mVideoPlayViewHolder.resumePlay();
        }
    }


    @Override
    public void onPageSelected(VideoPlayWrapViewHolder videoPlayWrapViewHolder, boolean needLoadMore) {
        if (videoPlayWrapViewHolder != null) {
            VideoBean videoBean = videoPlayWrapViewHolder.getVideoBean();
            if (videoBean != null) {
                mVideoBean = videoBean;
                mVideoPlayWrapViewHolder = videoPlayWrapViewHolder;
                ((AbsVideoActivity)mContext).setVideoBtm(videoBean);
                mVideoPlayWrapViewHolder.setRightBtnVisibility(!videoBean.isAd());
                if (onVideoitemChangeListener!=null){
                    onVideoitemChangeListener.onPageSelected(videoBean);
                }
                videoPlayWrapViewHolder.addVideoView(mPlayView);
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.startPlay(videoBean);
                }
                if (mVideoLoadingBar != null) {
                    mVideoLoadingBar.setLoading(true);
                }
            }
            if (needLoadMore) {
                onLoadMore();
            }
        }
    }

    @Override
    public void onPageOutWindow(VideoPlayWrapViewHolder vh) {
        if (mVideoPlayWrapViewHolder != null && mVideoPlayWrapViewHolder == vh && mVideoPlayViewHolder != null) {
            mVideoPlayViewHolder.stopPlay();
        }
    }

    @Override
    public void onVideoDeleteAll() {
        ((VideoPlayActivity) mContext).onBackPressed();
    }

    public void release() {
        HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO_LIST);
        EventBus.getDefault().unregister(this);
        if (mVideoPlayViewHolder != null) {
            mVideoPlayViewHolder.release();
        }
        mVideoPlayWrapViewHolder = null;
        if (mVideoLoadingBar != null) {
            mVideoLoadingBar.endLoading();
        }
        mVideoLoadingBar = null;
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(null);
        }
        mRefreshLayout = null;
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.release();
        }
        mVideoScrollAdapter = null;
        mVideoDataHelper = null;
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mPage = 1;
        if (mVideoDataHelper != null) {
            mVideoDataHelper.loadData(mPage, mRefreshCallback);
        }
    }

    /**
     * 加载更多
     */
    private void onLoadMore() {
        mPage++;
        if (mVideoDataHelper != null) {
            mVideoDataHelper.loadData(mPage, mLoadMoreCallback);
        }
    }

    @Override
    public void onPlayBegin() {
        if (mVideoLoadingBar != null) {
            mVideoLoadingBar.setLoading(false);
        }
    }

    @Override
    public void onPlayStateChange(boolean isPlay) {
        if (onVideoitemChangeListener!=null){
            onVideoitemChangeListener.onVideoStateChange(isPlay);
        }
    }

    @Override
    public void onPlayLoading() {
        if (mVideoLoadingBar != null) {
            mVideoLoadingBar.setLoading(true);
        }
    }

    @Override
    public void onFirstFrame() {
        if (mVideoPlayWrapViewHolder != null) {
            mVideoPlayWrapViewHolder.onFirstFrame();
        }
    }

    /**
     * 关注发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (mVideoScrollAdapter != null && mVideoPlayWrapViewHolder != null) {
            VideoBean videoBean = mVideoPlayWrapViewHolder.getVideoBean();
            if (videoBean != null) {
                mVideoScrollAdapter.onFollowChanged(!mPaused, videoBean.getId(), e.getToUid(), e.getIsAttention());
            }
        }
    }

    /**
     * 点赞发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoLikeEvent(VideoLikeEvent e) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onLikeChanged(!mPaused, e.getVideoId(), e.getIsLike(), e.getLikeNum());
        }
    }

    /**
     * 分享数发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoShareEvent(VideoShareEvent e) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onShareChanged(e.getVideoId(), e.getShareNum());
        }
    }

    /**
     * 评论数发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoCommentEvent(VideoCommentEvent e) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onCommentChanged(e.getVideoId(), e.getCommentNum());
        }
    }

    /**
     * 删除视频
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoDeleteEvent(VideoDeleteEvent e) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.deleteVideo(e.getVideoId());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.input_tip:
//                openCommentInputWindow(false);
//                break;
            case R.id.btn_face:
                openCommentInputWindow(true);
                break;
        }
    }

    /**
     * 打开评论输入框
     */
    private void openCommentInputWindow(boolean openFace) {
        if (mVideoBean != null) {
            ((AbsVideoActivity) mContext).openCommentInputWindow(openFace, mVideoBean, null);
        }
    }

    /**
     * VideoBean 数据发生变化
     */
    public void onVideoBeanChanged(String videoId) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onVideoBeanChanged(videoId);
        }
    }
    private OnVideoitemChangeListener onVideoitemChangeListener;

    public OnVideoitemChangeListener getOnVideoitemChangeListener() {
        return onVideoitemChangeListener;
    }

    public void setOnVideoitemChangeListener(OnVideoitemChangeListener onVideoitemChangeListener) {
        this.onVideoitemChangeListener = onVideoitemChangeListener;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public interface OnVideoitemChangeListener {
        void onPageSelected(VideoBean bean);
        void onVideoStateChange(boolean isPlay);
    }

}
