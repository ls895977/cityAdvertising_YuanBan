package com.juyijia.mm.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.juyijia.mm.Constants;
import com.juyijia.mm.R;
import com.juyijia.mm.utils.L;
import com.juyijia.mm.utils.VideoStorge;
import com.juyijia.mm.views.VideoScrollViewHolder;

/**
 * Created by cxf on 2018/11/26.
 */

public class VideoPlayActivity extends AbsVideoActivity {


    public static void forward(Context context, int position, String videoKey, int page) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(Constants.VIDEO_POSITION, position);
        intent.putExtra(Constants.VIDEO_KEY, videoKey);
        intent.putExtra(Constants.VIDEO_PAGE, page);
        context.startActivity(intent);
    }

    private String mVideoKey;
    private VideoScrollViewHolder mVideoScrollViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void main() {
        super.main();
        Intent intent = getIntent();
        mVideoKey = intent.getStringExtra(Constants.VIDEO_KEY);
        if (TextUtils.isEmpty(mVideoKey)) {
            return;
        }
        int position = intent.getIntExtra(Constants.VIDEO_POSITION, 0);
        int page = intent.getIntExtra(Constants.VIDEO_PAGE, 1);
        mVideoScrollViewHolder = new VideoScrollViewHolder(mContext, (ViewGroup) findViewById(R.id.container), position, mVideoKey, page);
        addLifeCycleListener(mVideoScrollViewHolder.getLifeCycleListener());
        mVideoScrollViewHolder.addToParent();
        isWhiteStateBar(true);
    }


    @Override
    protected void release() {
        super.release();
        if (mVideoScrollViewHolder != null) {
            mVideoScrollViewHolder.release();
        }
        VideoStorge.getInstance().removeDataHelper(mVideoKey);
        mVideoScrollViewHolder = null;
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
        L.e("VideoPlayActivity------->onDestroy");
    }
}
