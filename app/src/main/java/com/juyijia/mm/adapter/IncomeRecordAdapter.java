package com.juyijia.mm.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juyijia.mm.R;
import com.juyijia.mm.bean.IncomeRecoedBean;

import java.util.List;

/**
 * @author : GuZhC
 * @date : 2019/10/14 11:11
 * @description : IncomeRecordAdapter
 */
public class IncomeRecordAdapter extends BaseQuickAdapter<IncomeRecoedBean, BaseViewHolder> {
    public IncomeRecordAdapter(@Nullable List<IncomeRecoedBean> data) {
        super(R.layout.item_income_recoed, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IncomeRecoedBean item) {
        helper.setText(R.id.tv_ir_item_time,"10月10 12 03");
        helper.setText(R.id.tv_ir_item_title,"微全部信红包-退款");
        helper.setText(R.id.tv_ir_item_money,"+1234");
    }
}
