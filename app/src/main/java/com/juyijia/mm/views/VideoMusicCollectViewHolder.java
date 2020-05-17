package com.juyijia.mm.views;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.juyijia.mm.R;
import com.juyijia.mm.adapter.MusicAdapter;
import com.juyijia.mm.adapter.RefreshAdapter;
import com.juyijia.mm.bean.MusicBean;
import com.juyijia.mm.custom.RefreshView;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.VideoMusicActionListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/12/7.
 * 视频收藏音乐
 */

public class VideoMusicCollectViewHolder extends VideoMusicChildViewHolder {

    public VideoMusicCollectViewHolder(Context context, ViewGroup parentView, VideoMusicActionListener actionListener) {
        super(context, parentView, actionListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_music_collect;
    }

    @Override
    public void init() {
        super.init();
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_music_collect);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<MusicBean>() {
            @Override
            public RefreshAdapter<MusicBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MusicAdapter(mContext);
                    mAdapter.setActionListener(mActionListener);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getMusicCollectList(p, callback);
            }

            @Override
            public List<MusicBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), MusicBean.class);
            }

            @Override
            public void onRefresh(List<MusicBean> list) {
                if(mActionListener!=null){
                    mActionListener.onStopMusic();
                }
            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }
        });

    }


}
