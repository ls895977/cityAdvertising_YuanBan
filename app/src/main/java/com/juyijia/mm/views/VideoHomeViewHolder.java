package com.juyijia.mm.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.juyijia.mm.AppConfig;
import com.juyijia.mm.Constants;
import com.juyijia.mm.R;
import com.juyijia.mm.activity.AddAdActivity;
import com.juyijia.mm.activity.VideoPlayActivity;
import com.juyijia.mm.adapter.RefreshAdapter;
import com.juyijia.mm.adapter.VideoHomeAdapter;
import com.juyijia.mm.bean.VideoBean;
import com.juyijia.mm.custom.ItemDecoration;
import com.juyijia.mm.custom.RefreshView;
import com.juyijia.mm.event.VideoDeleteEvent;
import com.juyijia.mm.event.VideoScrollPageEvent;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpConsts;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.LifeCycleAdapter;
import com.juyijia.mm.interfaces.OnItemClickListener;
import com.juyijia.mm.interfaces.OnItemLongClickListener;
import com.juyijia.mm.interfaces.VideoScrollDataHelper;
import com.juyijia.mm.utils.VideoStorge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/12/14.
 * 用户个人中心发布的视频列表
 */

public class VideoHomeViewHolder extends AbsViewHolder implements OnItemClickListener<VideoBean>, OnItemLongClickListener<VideoBean> {

    private RefreshView mRefreshView;
    private VideoHomeAdapter mAdapter;
    private String mToUid;
    private VideoScrollDataHelper mVideoScrollDataHelper;
    private ActionListener mActionListener;
    private String mKey;

    public VideoHomeViewHolder(Context context, ViewGroup parentView, String toUid) {
        super(context, parentView, toUid);
    }

    @Override
    protected void processArguments(Object... args) {
        mToUid = (String) args[0];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_home;
    }

    @Override
    public void init() {
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        mKey = Constants.VIDEO_USER + this.hashCode();
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        if (mToUid.equals(AppConfig.getInstance().getUid())) {
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_video_home);
        } else {
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_video_home_2);
        }
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 2, 2);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoBean>() {
            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new VideoHomeAdapter(mContext);
                    mAdapter.setOnItemClickListener(VideoHomeViewHolder.this);
                    mAdapter.setOnItemLongClickListener(VideoHomeViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getHomeVideo(mToUid, p, callback);
            }

            @Override
            public List<VideoBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), VideoBean.class);
            }

            @Override
            public void onRefresh(List<VideoBean> list) {
                VideoStorge.getInstance().put(mKey, list);
            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 20) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        mVideoScrollDataHelper = new VideoScrollDataHelper() {

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getHomeVideo(mToUid, p, callback);
            }
        };
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onCreate() {
                EventBus.getDefault().register(VideoHomeViewHolder.this);
            }

            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO);
                EventBus.getDefault().unregister(VideoHomeViewHolder.this);
            }
        };

    }

    public void setRefreshEnable(boolean enable) {
        if (mRefreshView != null) {
            mRefreshView.setRefreshEnable(enable);
        }
    }

    public void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    public void release() {
        HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO);
        mActionListener = null;
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoScrollPageEvent(VideoScrollPageEvent e) {
        if (!TextUtils.isEmpty(mKey) && mKey.equals(e.getKey()) && mRefreshView != null) {
            mRefreshView.setPage(e.getPage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoDeleteEvent(VideoDeleteEvent e) {
        if (mAdapter != null) {
            mAdapter.deleteVideo(e.getVideoId());
            if (mAdapter.getItemCount() == 0 && mRefreshView != null) {
                mRefreshView.showNoData();
            }
        }
        if (mActionListener != null) {
            mActionListener.onVideoDelete(1);
        }
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        int page = 1;
        if (mRefreshView != null) {
            page = mRefreshView.getPage();
        }
        VideoStorge.getInstance().putDataHelper(mKey, mVideoScrollDataHelper);
        VideoPlayActivity.forward(mContext, position, mKey, page);
    }

    @Override
    public void onItemLongClick(VideoBean bean, int position) {
        if (!bean.isAd()){
            AddAdActivity.startAct(mContext,bean);
        }
    }

    public interface ActionListener {
        void onVideoDelete(int deleteCount);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

}
