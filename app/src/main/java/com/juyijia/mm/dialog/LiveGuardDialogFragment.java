package com.juyijia.mm.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juyijia.mm.Constants;
import com.juyijia.mm.R;
import com.juyijia.mm.activity.LiveActivity;
import com.juyijia.mm.adapter.GuardAdapter;
import com.juyijia.mm.adapter.RefreshAdapter;
import com.juyijia.mm.bean.GuardUserBean;
import com.juyijia.mm.bean.LiveGuardInfo;
import com.juyijia.mm.custom.RefreshView;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpConsts;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.utils.DpUtil;
import com.juyijia.mm.utils.WordUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/11/6.
 */

public class LiveGuardDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private RefreshView mRefreshView;
    private TextView mGuardNum;
    private View mBottom;
    private TextView mTip;
    private TextView mBtnBuy;
    private GuardAdapter mGuardAdapter;
    private String mLiveUid;
    private boolean mIsAnchor;//是否是主播
    private LiveGuardInfo mLiveGuardInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_list;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(280);
        params.height = DpUtil.dp2px(360);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public void setLiveGuardInfo(LiveGuardInfo info) {
        mLiveGuardInfo = info;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mIsAnchor = bundle.getBoolean(Constants.ANCHOR, false);
        mLiveUid = bundle.getString(Constants.LIVE_UID);
        mGuardNum = (TextView) mRootView.findViewById(R.id.guard_num);
        mBottom = mRootView.findViewById(R.id.bottom);
        if (mIsAnchor) {
            mBottom.setVisibility(View.GONE);
            if (mLiveGuardInfo != null) {
                mGuardNum.setText(WordUtil.getString(R.string.guard_guard) + "(" + mLiveGuardInfo.getGuardNum() + ")");
            }
        } else {
            mTip = (TextView) mRootView.findViewById(R.id.tip);
            mBtnBuy = (TextView) mRootView.findViewById(R.id.btn_buy);
            mBtnBuy.setOnClickListener(this);
            if (mLiveGuardInfo != null) {
                mGuardNum.setText(WordUtil.getString(R.string.guard_guard) + "(" + mLiveGuardInfo.getGuardNum() + ")");
                int guardType = mLiveGuardInfo.getMyGuardType();
                if (guardType == Constants.GUARD_TYPE_NONE) {
                    mTip.setText(R.string.guard_tip_0);
                } else if (guardType == Constants.GUARD_TYPE_MONTH) {
                    mTip.setText(WordUtil.getString(R.string.guard_tip_1) + mLiveGuardInfo.getMyGuardEndTime());
                    mBtnBuy.setText(R.string.guard_buy_3);
                } else if (guardType == Constants.GUARD_TYPE_YEAR) {
                    mTip.setText(WordUtil.getString(R.string.guard_tip_2) + mLiveGuardInfo.getMyGuardEndTime());
                    mBtnBuy.setText(R.string.guard_buy_3);
                }
            }
        }
        mRefreshView = (RefreshView) mRootView.findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(mIsAnchor ? R.layout.view_no_data_guard_anc : R.layout.view_no_data_guard_aud);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<GuardUserBean>() {
            @Override
            public RefreshAdapter<GuardUserBean> getAdapter() {
                if (mGuardAdapter == null) {
                    mGuardAdapter = new GuardAdapter(mContext, true);
                }
                return mGuardAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getGuardList(mLiveUid, p, callback);
            }

            @Override
            public List<GuardUserBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), GuardUserBean.class);
            }

            @Override
            public void onRefresh(List<GuardUserBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }
        });
        mRefreshView.initData();
    }

    @Override
    public void onClick(View v) {
        dismiss();
        ((LiveActivity) mContext).openBuyGuardWindow();
    }

    @Override
    public void onDestroy() {
        mLiveGuardInfo=null;
        HttpUtil.cancel(HttpConsts.GET_GUARD_LIST);
        super.onDestroy();
    }
}
