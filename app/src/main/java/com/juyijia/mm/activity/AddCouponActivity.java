package com.juyijia.mm.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juyijia.mm.R;

public class AddCouponActivity extends AbsActivity {
    private RelativeLayout rlAddCpPic;
    private EditText etCpTitle;
    private RelativeLayout rlCpType;
    private EditText etCpDes;
    private EditText etCpPrice;
    private EditText etCpNum;
    private EditText etCpTime;
    private TextView tvCpPay;
    private View tv_add_cp_stone_price;
    private View tv_add_cp_stone_pay;
    private View tv_add_cp_price;
    private View btnAddcpOk;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_coupon;
    }

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, AddCouponActivity.class));
    }

    @Override
    protected void main() {
        setTitle("添加券");
        rlAddCpPic = findViewById(R.id.rl_add_cp_pic);
        etCpTitle = findViewById(R.id.et_cp_title);
        rlCpType = findViewById(R.id.rl_cp_type);
        etCpDes = findViewById(R.id.et_cp_des);
        etCpPrice = findViewById(R.id.et_cp_price);
        etCpNum = findViewById(R.id.et_cp_num);
        etCpTime = findViewById(R.id.et_cp_time);
        tvCpPay = findViewById(R.id.tv_cp_pay);
        tv_add_cp_stone_price = findViewById(R.id.tv_add_cp_stone_price);
        tv_add_cp_stone_pay = findViewById(R.id.tv_add_cp_stone_pay);
        tv_add_cp_price = findViewById(R.id.tv_add_cp_price);
        btnAddcpOk = findViewById(R.id.add_cp_ok);
    }
}
