package com.juyijia.mm.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.juyijia.mm.AppConfig;
import com.juyijia.mm.Constants;
import com.juyijia.mm.R;
import com.juyijia.mm.adapter.RefreshAdapter;
import com.juyijia.mm.adapter.SearchAdapter;
import com.juyijia.mm.bean.SearchUserBean;
import com.juyijia.mm.custom.RefreshView;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpConsts;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.interfaces.OnItemClickListener;
import com.juyijia.mm.utils.WordUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/9/29.
 * 我的粉丝 TA的粉丝
 */

public class FansActivity extends AbsActivity {

    private RefreshView mRefreshView;
    private SearchAdapter mAdapter;
    private String mToUid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    protected void main() {
        mToUid = getIntent().getStringExtra(Constants.TO_UID);
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        mRefreshView = (RefreshView)findViewById(R.id.refreshView);
        if (mToUid.equals(AppConfig.getInstance().getUid())) {
            setTitle(WordUtil.getString(R.string.fans_my_fans));
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_fans);
        } else {
            setTitle(WordUtil.getString(R.string.fans_ta_fans));
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_fans_2);
        }
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<SearchUserBean>() {
            @Override
            public RefreshAdapter<SearchUserBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new SearchAdapter(mContext, Constants.FOLLOW_FROM_FANS);
                    mAdapter.setOnItemClickListener(new OnItemClickListener<SearchUserBean>() {
                        @Override
                        public void onItemClick(SearchUserBean bean, int position) {
                            UserHomeActivity.forward(mContext, bean.getId());
                        }
                    });
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getFansList(mToUid, p, callback);
            }

            @Override
            public List<SearchUserBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), SearchUserBean.class);
            }

            @Override
            public void onRefresh(List<SearchUserBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshView.initData();
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.GET_FANS_LIST);
        super.onDestroy();
    }
}