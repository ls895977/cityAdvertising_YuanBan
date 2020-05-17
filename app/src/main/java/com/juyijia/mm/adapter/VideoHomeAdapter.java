package com.juyijia.mm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juyijia.mm.R;
import com.juyijia.mm.bean.VideoBean;
import com.juyijia.mm.glide.ImgLoader;

/**
 * Created by cxf on 2018/12/14.
 */

public class VideoHomeAdapter extends RefreshAdapter<VideoBean> {

    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;

    public VideoHomeAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                VideoBean bean = mList.get(position);
                if (bean != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };

        mOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return false;
                }
                int position = (int) tag;
                VideoBean bean = mList.get(position);
                if (bean != null && mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(bean, position);
                }
                return false;
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_video_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    /**
     * 删除视频
     */
    public void deleteVideo(String videoId) {
        if (TextUtils.isEmpty(videoId)) {
            return;
        }
        for (int i = 0, size = mList.size(); i < size; i++) {
            if (videoId.equals(mList.get(i).getId())) {
                notifyItemRemoved(i);
                break;
            }
        }
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mNum;
        TextView mAD;

        public Vh(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img);
            mNum = (TextView) itemView.findViewById(R.id.num);
            mAD = (TextView) itemView.findViewById(R.id.tv_isAd);
            itemView.setOnClickListener(mOnClickListener);
            itemView.setOnLongClickListener(mOnLongClickListener);
        }

        void setData(VideoBean bean, int position) {
            itemView.setTag(position);
            ImgLoader.display(bean.getThumb(), mImg);
            mNum.setText(bean.getViewNum());
            if (bean.isAd()){
                mAD.setText("AD");
            }else {
                mAD.setText("长按设置为广告");
            }
        }

    }
}
