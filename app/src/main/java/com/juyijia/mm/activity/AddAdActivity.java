package com.juyijia.mm.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juyijia.mm.R;
import com.juyijia.mm.bean.VideoBean;
import com.juyijia.mm.glide.ImgLoader;

public class AddAdActivity extends AbsActivity implements View.OnClickListener {

    private LinearLayout llAddAdVideo;
    private TextView llAddAdVideoHelper;
    private EditText etAdTitle;
    private RelativeLayout rlAdTypeChose;
    private TextView tvVideoType;
    private EditText etAdDes;
    private EditText etAdFree;
    private RelativeLayout rl_ad_add_coupon;
    private RelativeLayout rl_ad_add_act;
    private Button btnNext;
    private ImageView iv_add_ad_video_img;
    private VideoBean mVideoBean = null;

    public static void startAct(Context context, VideoBean bean){
        Intent intent = new Intent(context, AddAdActivity.class);
        intent.putExtra("videoBean",bean);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_ad;
    }

    @Override
    protected void main() {
        Intent intent = getIntent();
        if (intent!=null){
             mVideoBean = intent.getParcelableExtra("videoBean");
        }
        llAddAdVideo = findViewById(R.id.ll_add_ad_video);
        llAddAdVideoHelper = findViewById(R.id.tv_add_ad_video_helper);
        etAdTitle = findViewById(R.id.et_ad_title);
        rlAdTypeChose = findViewById(R.id.rl_ad_type);
        tvVideoType = findViewById(R.id.et_ad_type);
        etAdDes = findViewById(R.id.et_ad_des);
        etAdFree = findViewById(R.id.et_ad_free);
        rl_ad_add_coupon = findViewById(R.id.rl_ad_add_coupon);
        rl_ad_add_act = findViewById(R.id.rl_ad_add_act);
        iv_add_ad_video_img = findViewById(R.id.iv_add_ad_video_img);
        btnNext = findViewById(R.id.add_ad_next);
        setTitle("添加广告");

        llAddAdVideo.setOnClickListener(this);
        llAddAdVideoHelper.setOnClickListener(this);
        rlAdTypeChose.setOnClickListener(this);
        rl_ad_add_coupon.setOnClickListener(this);
        rl_ad_add_act.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        if (mVideoBean!=null){
            ImgLoader.display(mVideoBean.getThumb(), iv_add_ad_video_img);
            iv_add_ad_video_img.setVisibility(View.VISIBLE);
            etAdTitle.setText(mVideoBean.getTitle());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_ad_video:

                break;
            case R.id.tv_add_ad_video_helper:

                break;
            case R.id.rl_ad_type:
                break;
            case R.id.rl_ad_add_coupon:
                AddCouponActivity.startAct(this);
                break;
            case R.id.rl_ad_add_act:

                break;
            case R.id.add_ad_next:

                break;
            default:
                break;
        }

    }
}
