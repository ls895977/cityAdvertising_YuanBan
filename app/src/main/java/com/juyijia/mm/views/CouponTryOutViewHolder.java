package com.juyijia.mm.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.juyijia.mm.R;
import com.juyijia.mm.activity.AbsVideoActivity;
import com.juyijia.mm.bean.VideoBean;
import com.juyijia.mm.bean.VideoCommentBean;
import com.juyijia.mm.custom.MyLinearLayout3;
import com.juyijia.mm.utils.ToastUtil;

/**
 * Created by cxf on 2018/12/3.
 * 视频评论相关
 */

public class CouponTryOutViewHolder extends AbsViewHolder implements View.OnClickListener {

    private View mRoot;
    private MyLinearLayout3 mBottom;
    private VideoBean mVideoBean;
    private ObjectAnimator mShowAnimator;
    private ObjectAnimator mHideAnimator;
    private boolean mAnimating;
    private TextView tv_try_out_location;

    public CouponTryOutViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_ad_try_out;
    }

    @Override
    public void init() {
        mRoot = findViewById(R.id.root);
        mBottom = (MyLinearLayout3) findViewById(R.id.bottom);
        int height = mBottom.getHeight2();
        mBottom.setTranslationY(height);
        mShowAnimator = ObjectAnimator.ofFloat(mBottom, "translationY", 0);
        mHideAnimator = ObjectAnimator.ofFloat(mBottom, "translationY", height);
        mShowAnimator.setDuration(200);
        mHideAnimator.setDuration(200);
        TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
        mShowAnimator.setInterpolator(interpolator);
        mHideAnimator.setInterpolator(interpolator);
        AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
                if (animation == mHideAnimator) {
                    if (mRoot != null && mRoot.getVisibility() == View.VISIBLE) {
                        mRoot.setVisibility(View.INVISIBLE);
                    }
                }
//                else if (animation == mShowAnimator) {
//                    if (mRefreshView != null) {
//                        mRefreshView.initData();
//                    }
//                }
            }
        };
        mShowAnimator.addListener(animatorListener);
        mHideAnimator.addListener(animatorListener);

        findViewById(R.id.root).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.input).setOnClickListener(this);
        findViewById(R.id.btn_face).setOnClickListener(this);
        findViewById(R.id.btn_try_out_ok).setOnClickListener(this);
        findViewById(R.id.btn_try_out_no).setOnClickListener(this);

        tv_try_out_location = (TextView) findViewById(R.id.tv_try_out_location);
        String location = "领取地址：" + "<font color='#000000'>烟台市莱山区迎春大街绿色家园1号楼3单元3009</font>";
        tv_try_out_location.setText(Html.fromHtml(location));
        String des = "当您点开后，会弹出否与确认按钮，确认后，用户手机号码会以短信自动发送到广告商手机里。后台统计每个广告商获取短信数量。当月被举报超过" +
                "<font color='#535252'>2</font>" +
                "次，当月所得收入将被锁定。每月至少参与商家活动不低于" +
                "<font color='#535252'>1</font>" +
                "次。<br/><br/>" +
                "<P><font color='#535252'>详细介绍：</font></P>"+
                "尊敬的用户，为了节约商家资源，请您认真考虑后再点击本按钮，点击后，商家会及时跟您联系，为了保障您在本平台的权益，当您接到同城广告商家打来的电话后，请一定不要马上挂断，并及时配合商家的邀请，切忌做以下：打错了、莫名挂断、不是本人、点错了等动作，"+
                "<font color='#ef454e'>否则商家会举报您</font>"+
                "，一个月内被举报超过3次，当月所得收入将被锁定。<br/><br/>"+
                "<P><font color='#535252'>解除办法：</font></P>"+
                "<P>（1）领取该商家优惠券，并在此商家消费超过1次，自动解除。</P>"+
                "<P>（2）与广告商沟通解除，请求广告商解除。广告商如果恶意举报，平台将介入。</P>"+
                "<P>（3）升级成为代理解除锁定。</P>"+
                "<P>（4）推荐人中当月成为代理自动解除锁定</P>"+
                "<P>（5）缴纳300元信用保证金解除限制。如果下次再被举报，信用保证金将每次扣100元。超过三次，平台介入该账户永久锁定。<br/><br/></P>"+
                "<P><font color='#535252'>备注：</font></P>"+
                "用户1个月内至少参与不同商家（解释：3个月内需参与不同商家活动，如始终参与一个商家活动，将被视为作弊，账户将被锁定，并不得提现。）活动超过1次，否则第二个月广告数量将以每月30%的幅度减少广告配给，如广告数量减少后，当月如参与不同商家活动超过3次后，广告配给数量将被还原至100%状态。";
        TextView tv_try_out_des = (TextView) findViewById(R.id.tv_try_out_des);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_try_out_des.setText(Html.fromHtml(des,Html.FROM_HTML_MODE_COMPACT));
        }else {
            tv_try_out_des.setText(Html.fromHtml(des));
        }
    }

    public void setVideoBean(VideoBean videoBean) {
        if (mVideoBean != null) {
            mVideoBean = videoBean;
        }
    }

    public void showBottom() {
        if (mAnimating) {
            return;
        }
        if (mRoot != null && mRoot.getVisibility() != View.VISIBLE) {
            mRoot.setVisibility(View.VISIBLE);
        }
        if (mShowAnimator != null) {
            mShowAnimator.start();
        }
    }

    public void hideBottom() {
        if (mAnimating) {
            return;
        }
        if (mHideAnimator != null) {
            mHideAnimator.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root:
            case R.id.btn_close:
            case R.id.btn_try_out_no:
                hideBottom();
                break;
            case R.id.input:
                openCommentInputWindow(false, mVideoBean, null);
                break;
            case R.id.btn_face:
                openCommentInputWindow(true, mVideoBean, null);
                break;
            case R.id.btn_try_out_ok:
                ToastUtil.show("click ok");
                break;
        }
    }

    private void openCommentInputWindow(boolean openFace, VideoBean videoBean, VideoCommentBean bean) {
        ((AbsVideoActivity) mContext).openCommentInputWindow(openFace, videoBean, bean);
    }


    public void release() {
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        mShowAnimator = null;
        if (mHideAnimator != null) {
            mHideAnimator.cancel();
        }
        mHideAnimator = null;
    }
}
