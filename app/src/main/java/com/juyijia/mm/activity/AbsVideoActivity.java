package com.juyijia.mm.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juyijia.mm.AppConfig;
import com.juyijia.mm.Constants;
import com.juyijia.mm.HtmlConfig;
import com.juyijia.mm.R;
import com.juyijia.mm.bean.ConfigBean;
import com.juyijia.mm.bean.VideoBean;
import com.juyijia.mm.bean.VideoCommentBean;
import com.juyijia.mm.dialog.VideoInputDialogFragment;
import com.juyijia.mm.dialog.VideoShareDialogFragment;
import com.juyijia.mm.event.VideoDeleteEvent;
import com.juyijia.mm.event.VideoShareEvent;
import com.juyijia.mm.http.HttpCallback;
import com.juyijia.mm.http.HttpConsts;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.im.ImChatFacePagerAdapter;
import com.juyijia.mm.interfaces.CommonCallback;
import com.juyijia.mm.interfaces.OnFaceClickListener;
import com.juyijia.mm.mob.MobCallback;
import com.juyijia.mm.mob.MobShareUtil;
import com.juyijia.mm.mob.ShareData;
import com.juyijia.mm.utils.DateFormatUtil;
import com.juyijia.mm.utils.DialogUitl;
import com.juyijia.mm.utils.DownloadUtil;
import com.juyijia.mm.utils.L;
import com.juyijia.mm.utils.ProcessResultUtil;
import com.juyijia.mm.utils.StringUtil;
import com.juyijia.mm.utils.ToastUtil;
import com.juyijia.mm.utils.VideoLocalUtil;
import com.juyijia.mm.utils.WordUtil;
import com.juyijia.mm.views.CouponTryOutViewHolder;
import com.juyijia.mm.views.VideoCommentViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * @author : GuZhC
 * @date :  2019/10/6 19:25
 * @description : AbsVideoActivity
 */
public abstract class AbsVideoActivity extends AbsActivity implements View.OnClickListener, OnFaceClickListener {
    protected ClipboardManager mClipboardManager;
    protected MobShareUtil mMobShareUtil;
    protected DownloadUtil mDownloadUtil;
    protected Dialog mDownloadVideoDialog;
    protected ProcessResultUtil mProcessResultUtil;
    protected View mFaceView;//表情面板
    protected int mFaceHeight;//表情面板高度
    protected VideoCommentViewHolder mVideoCommentViewHolder;
    protected CouponTryOutViewHolder mCouponTryOutViewHolder;
    protected VideoInputDialogFragment mVideoInputDialogFragment;
    protected VideoBean mShareVideoBean;
    protected boolean mPaused;
    protected ConfigBean mConfigBean;
    private boolean isWhite;

    //广告视频
    private TextView mBtmAdDetail;
    private TextView tv_ad_use;
    private LinearLayout mBtmAd;
    private LinearLayout ll_btn_coupon;
    private LinearLayout ll_btn_comment;
    private LinearLayout ll_btn_share;
    private VideoBean mVideoBean;
    private TextView coupon_num;
    private TextView share_num;
    private TextView comment_num;
    @Override
    protected void main() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mBtmAd = (LinearLayout) findViewById(R.id.ll_btm_ad);
        mBtmAdDetail = (TextView) findViewById(R.id.tv_ad_detail);
        tv_ad_use = (TextView) findViewById(R.id.tv_ad_use);
        ll_btn_coupon = (LinearLayout) findViewById(R.id.ll_btn_coupon);
        ll_btn_comment = (LinearLayout) findViewById(R.id.ll_btn_comment);
        ll_btn_share = (LinearLayout) findViewById(R.id.ll_btn_share);
        coupon_num = (TextView) findViewById(R.id.coupon_num);
        share_num = (TextView) findViewById(R.id.share_num);
        comment_num = (TextView) findViewById(R.id.comment_num);
        mBtmAdDetail.setOnClickListener(this);
        tv_ad_use.setOnClickListener(this);
        ll_btn_coupon.setOnClickListener(this);
        ll_btn_comment.setOnClickListener(this);
        ll_btn_share.setOnClickListener(this);

        mProcessResultUtil = new ProcessResultUtil(this);
        AppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean bean) {
                mConfigBean = bean;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send://发表评论
                if (mVideoInputDialogFragment != null) {
                    mVideoInputDialogFragment.sendComment();
                }
                break;
            case R.id.tv_ad_detail://详情
                openAdDetailActivity();
                break;
            case R.id.tv_ad_use://试用
                openUseActivity();
                break;
            case R.id.ll_btn_coupon://优惠券
                openCouponActivity();
                break;
            case R.id.ll_btn_comment://评论
                openCommentWindow(mVideoBean);
                break;
            case R.id.ll_btn_share://转发
                clickShare(mVideoBean);
                break;
        }
    }



    protected void isWhiteStateBar(boolean isWhite){
       this.isWhite = isWhite;
        setStatusBar();
    }

    @Override
    protected boolean isStatusBarWhite() {
        return isWhite;
    }

    @Override
    public void onBackPressed() {
        release();
        super.onBackPressed();
    }

    protected void release() {
        HttpUtil.cancel(HttpConsts.SET_VIDEO_SHARE);
        HttpUtil.cancel(HttpConsts.VIDEO_DELETE);
        if (mDownloadVideoDialog != null && mDownloadVideoDialog.isShowing()) {
            mDownloadVideoDialog.dismiss();
        }

        if (mProcessResultUtil != null) {
            mProcessResultUtil.release();
        }
        if (mMobShareUtil != null) {
            mMobShareUtil.release();
        }
        if (mVideoCommentViewHolder != null) {
            mVideoCommentViewHolder.release();
        }
        if (mCouponTryOutViewHolder != null) {
            mCouponTryOutViewHolder.release();
        }
        mDownloadVideoDialog = null;
        mProcessResultUtil = null;
        mMobShareUtil = null;
        mVideoCommentViewHolder = null;
        mCouponTryOutViewHolder = null;
        mVideoInputDialogFragment = null;
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
        L.e("VideoPlayActivity------->onDestroy");
    }


    /**
     * 复制视频链接
     */
    public void copyLink(VideoBean videoBean) {
        if (videoBean == null) {
            return;
        }
        if (mClipboardManager == null) {
            mClipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clipData = ClipData.newPlainText("text", videoBean.getHref());
        mClipboardManager.setPrimaryClip(clipData);
        ToastUtil.show(WordUtil.getString(R.string.copy_success));
    }

    /**
     * 分享页面链接
     */
    public void shareVideoPage(String type, VideoBean videoBean) {
        if (videoBean == null || mConfigBean == null) {
            return;
        }
        mShareVideoBean = videoBean;
        ShareData data = new ShareData();
        data.setTitle(mConfigBean.getVideoShareTitle());
        data.setDes(mConfigBean.getVideoShareDes());
        data.setImgUrl(videoBean.getThumbs());
        String webUrl = HtmlConfig.SHARE_VIDEO + videoBean.getId();
        data.setWebUrl(webUrl);
        if (mMobShareUtil == null) {
            mMobShareUtil = new MobShareUtil();
        }
        mMobShareUtil.execute(type, data, mMobCallback);
    }

    private MobCallback mMobCallback = new MobCallback() {

        @Override
        public void onSuccess(Object data) {
            if (mShareVideoBean == null) {
                return;
            }
            HttpUtil.setVideoShare(mShareVideoBean.getId(), new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0 && info.length > 0 && mShareVideoBean != null) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        EventBus.getDefault().post(new VideoShareEvent(mShareVideoBean.getId(), obj.getString("shares")));
                    } else {
                        ToastUtil.show(msg);
                    }
                }
            });
        }

        @Override
        public void onError() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onFinish() {

        }
    };

    /**
     * 下载视频
     */
    public void downloadVideo(final VideoBean videoBean) {
        if (mProcessResultUtil == null || videoBean == null || TextUtils.isEmpty(videoBean.getHref())) {
            return;
        }
        mProcessResultUtil.requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        }, new Runnable() {
            @Override
            public void run() {
                mDownloadVideoDialog = DialogUitl.loadingDialog(mContext);
                mDownloadVideoDialog.show();
                if (mDownloadUtil == null) {
                    mDownloadUtil = new DownloadUtil();
                }
                String fileName = "YB_VIDEO_" + videoBean.getTitle() + "_" + DateFormatUtil.getCurTimeString() + ".mp4";
                mDownloadUtil.download(videoBean.getTag(), AppConfig.VIDEO_PATH, fileName, videoBean.getHref(), new DownloadUtil.Callback() {
                    @Override
                    public void onSuccess(File file) {
                        ToastUtil.show(R.string.video_download_success);
                        if (mDownloadVideoDialog != null && mDownloadVideoDialog.isShowing()) {
                            mDownloadVideoDialog.dismiss();
                        }
                        mDownloadVideoDialog = null;
                        String path = file.getAbsolutePath();
                        try {
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            mmr.setDataSource(path);
                            String d = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            if (StringUtil.isInt(d)) {
                                long duration = Long.parseLong(d);
                                VideoLocalUtil.saveVideoInfo(mContext, path, duration);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(R.string.video_download_failed);
                        if (mDownloadVideoDialog != null && mDownloadVideoDialog.isShowing()) {
                            mDownloadVideoDialog.dismiss();
                        }
                        mDownloadVideoDialog = null;
                    }
                });
            }
        });
    }

    /**
     * 删除视频
     */
    public void deleteVideo(final VideoBean videoBean) {
        HttpUtil.videoDelete(videoBean.getId(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                        EventBus.getDefault().post(new VideoDeleteEvent(videoBean.getId()));
                }
            }
        });
    }

    /**
     * 初始化表情控件
     */
    private View initFaceView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.view_chat_face, null);
        v.measure(0, 0);
        mFaceHeight = v.getMeasuredHeight();
        v.findViewById(R.id.btn_send).setOnClickListener(this);
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(10);
        ImChatFacePagerAdapter adapter = new ImChatFacePagerAdapter(mContext, this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0, pageCount = adapter.getCount(); i < pageCount; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_chat_indicator, radioGroup, false);
            radioButton.setId(i + 10000);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }
        return v;
    }


    @Override
    public void onFaceClick(String str, int faceImageRes) {
        if (mVideoInputDialogFragment != null) {
            mVideoInputDialogFragment.onFaceClick(str, faceImageRes);
        }
    }

    @Override
    public void onFaceDeleteClick() {
        if (mVideoInputDialogFragment != null) {
            mVideoInputDialogFragment.onFaceDeleteClick();
        }
    }




    /**
     * 打开评论输入框
     */
    public void openCommentInputWindow(boolean openFace, VideoBean videoBean, VideoCommentBean bean) {
        if (mFaceView == null) {
            mFaceView = initFaceView();
        }
        VideoInputDialogFragment fragment = new VideoInputDialogFragment();
        fragment.setVideoBean(videoBean);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.VIDEO_FACE_OPEN, openFace);
        bundle.putInt(Constants.VIDEO_FACE_HEIGHT, mFaceHeight);
        bundle.putParcelable(Constants.VIDEO_COMMENT_BEAN, bean);
        fragment.setArguments(bundle);
        mVideoInputDialogFragment = fragment;
        fragment.show(getSupportFragmentManager(), "VideoInputDialogFragment");
    }


    public View getFaceView() {
        if (mFaceView == null) {
            mFaceView = initFaceView();
        }
        return mFaceView;
    }

    /**
     * 显示试用
     */
    private void openUseActivity() {
        if (mVideoBean == null) {
            ToastUtil.show("获取数据失败");
            return;
        }
        opentryOutWindow(mVideoBean);
    }

    public void opentryOutWindow(VideoBean videoBean) {
        if (mCouponTryOutViewHolder == null) {
            mCouponTryOutViewHolder = new CouponTryOutViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
            mCouponTryOutViewHolder.addToParent();
        }
        mCouponTryOutViewHolder.setVideoBean(videoBean);
        mCouponTryOutViewHolder.showBottom();
    }

    /**
     * 显示评论
     */
    public void openCommentWindow(VideoBean videoBean) {
        if (mVideoCommentViewHolder == null) {
            mVideoCommentViewHolder = new VideoCommentViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
            mVideoCommentViewHolder.addToParent();
        }
        mVideoCommentViewHolder.setVideoBean(videoBean);
        mVideoCommentViewHolder.showBottom();
    }
    /**
     * 点击分享按钮
     */
    public void clickShare(VideoBean videoBean) {
        if (mVideoBean == null) {
            return;
        }
        VideoShareDialogFragment fragment = new VideoShareDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.VIDEO_BEAN, videoBean);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "VideoShareDialogFragment");
    }

    public  void openCouponActivity(VideoBean mVideoBean) {
        if (mVideoBean == null) return;
        //todo open coupon
        ToastUtil.show("打开优惠券页面");
    }

    public  void openCouponActivity() {
        openCouponActivity(mVideoBean);
    }

     public  void openAdBuyActivity(){
         if (mVideoBean == null) return;
        ToastUtil.show("打开购买页面");
    }

    public void openAdDetailActivity() {
        if (mVideoBean == null) return;
        ToastUtil.show("打开详情页面");
    }

    public void setVideoBtm(VideoBean mVideoBean){
        this.mVideoBean = mVideoBean;
        if (mVideoBean == null) return;
        if (mVideoBean.isAd()){
            mBtmAd.setVisibility(View.VISIBLE);
            comment_num.setText(mVideoBean.getCommentNum());
            //todo  add coupon num
            coupon_num.setText("");
            share_num.setText(mVideoBean.getShareNum());
        }else {
            mBtmAd.setVisibility(View.GONE);
        }
    }
    /**
     * 隐藏评论
     */
    public void hideCommentWindow() {
        if (mVideoCommentViewHolder != null) {
            mVideoCommentViewHolder.hideBottom();
        }
        mVideoInputDialogFragment = null;
    }


    @Override
    protected void onPause() {
        mPaused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
    }

    public boolean isPaused() {
        return mPaused;
    }
}