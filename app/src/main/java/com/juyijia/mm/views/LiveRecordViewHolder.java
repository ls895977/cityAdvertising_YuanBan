package com.juyijia.mm.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juyijia.mm.AppConfig;
import com.juyijia.mm.R;
import com.juyijia.mm.activity.LiveRecordPlayActivity;
import com.juyijia.mm.adapter.LiveRecordAdapter;
import com.juyijia.mm.adapter.RefreshAdapter;
import com.juyijia.mm.bean.LiveRecordBean;
import com.juyijia.mm.bean.UserBean;
import com.juyijia.mm.custom.RefreshView;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpConsts;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.LifeCycleAdapter;
import com.juyijia.mm.interfaces.OnItemClickListener;
import com.juyijia.mm.utils.L;
import com.juyijia.mm.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/10/18.
 */

public class LiveRecordViewHolder extends AbsViewHolder {

    private RefreshView mRefreshView;
    private LiveRecordAdapter mAdapter;
    private UserBean mUserBean;
    private String mToUid;

    public LiveRecordViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_record;
    }

    @Override
    public void init() {
        mRefreshView = (RefreshView) mContentView;
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<LiveRecordBean>() {
            @Override
            public RefreshAdapter<LiveRecordBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new LiveRecordAdapter(mContext);
                    mAdapter.setOnItemClickListener(new OnItemClickListener<LiveRecordBean>() {
                        @Override
                        public void onItemClick(LiveRecordBean bean, int position) {
                            fowardLiveRecord(bean.getId());
                        }
                    });
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getLiveRecord(mToUid, p, callback);
            }

            @Override
            public List<LiveRecordBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), LiveRecordBean.class);
            }

            @Override
            public void onRefresh(List<LiveRecordBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
//                if (dataCount < 50) {
//                    mRefreshView.setLoadMoreEnable(false);
//                } else {
//                    mRefreshView.setLoadMoreEnable(true);
//                }
            }
        });
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_LIVE_RECORD);
            }
        };
    }

    public void loadData(UserBean userBean) {
        if (userBean == null) {
            return;
        }
        mUserBean = userBean;
        mToUid = userBean.getId();
        if (mToUid.equals(AppConfig.getInstance().getUid())) {
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_live_record);
        } else {
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_live_record_2);
        }
        mRefreshView.initData();
    }

    /**
     * 获取直播回放的url并跳转
     */
    private void fowardLiveRecord(String recordId) {
        HttpUtil.getAliCdnRecord(recordId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject object = JSON.parseObject(info[0]);
                    String url = object.getString("url");
                    L.e("直播回放的url--->" + url);
                    LiveRecordPlayActivity.forward(mContext, url, mUserBean);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }
}
