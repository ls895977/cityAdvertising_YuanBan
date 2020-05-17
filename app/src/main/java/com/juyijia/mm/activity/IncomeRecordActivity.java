package com.juyijia.mm.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.juyijia.mm.R;
import com.juyijia.mm.adapter.IncomeRecordAdapter;
import com.juyijia.mm.bean.IncomeRecoedBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : GuZhC
 * @date : 2019/10/14 11:11
 * @description : 收入记录
 */
public class IncomeRecordActivity extends AbsActivity {

    private TextView tv_ir_type;
    private TextView tv_ir_time;
    private TextView tv_ir_money;
    private RecyclerView recycler;
    private IncomeRecordAdapter adapter;
    List<IncomeRecoedBean> datas = new ArrayList<>();

    public static void forward(Context context) {
        context.startActivity(new Intent(context, IncomeRecordActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_income_record;
    }

    @Override
    protected void main() {
        tv_ir_type = findViewById(R.id.tv_ir_type);
        tv_ir_time = findViewById(R.id.tv_ir_time);
        tv_ir_money = findViewById(R.id.tv_ir_money);
        recycler = findViewById(R.id.recycler_ir);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        tv_ir_time.setText("2019年10月");
        tv_ir_money.setText("收入￥1231");
        for (int i = 0; i < 10; i++) {
            datas.add(new IncomeRecoedBean());
        }
        adapter = new IncomeRecordAdapter(datas);
        recycler.setAdapter(adapter);
    }
}
