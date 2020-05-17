/*
 * Copyright (c) 2020 - present. All rights reserved.
 *
 * Author : Xianxiang.Hu
 * E-mail : huxianxiang@gmail.com
 * Version: v1.0.0
 * Date   : 2020/01/14 12:55
 * Desc   :
 */

package com.juyijia.mm.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.juyijia.mm.AppConfig;
import com.juyijia.mm.R;
import com.juyijia.mm.bean.TopicBean;
import com.juyijia.mm.bean.frontConfig;
import com.juyijia.mm.http.HttpClient;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<frontConfig.DataBean.InfoBean> mData;
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private int columnCount = 5;

    public TopicAdapter(Context context, List<frontConfig.DataBean.InfoBean> data) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_topic_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, final int position) {
        if (mData.get(position) == null) {
            return;
        }
        final frontConfig.DataBean.InfoBean item = mData.get(position);
        holder.title.setText(item.getModule());
        Glide.with(mContext).load(AppConfig.HOST1+item.getIcon()).into(holder.item_Image);
        holder.item_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onTopicItemClick(item);
                }
            }
        });
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels-40; //屏幕宽度
        params.width = screenWidth / columnCount;
        holder.itemView.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        if(mData==null){
            return 0;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView item_Image;

        public TopicViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            item_Image = view.findViewById(R.id.item_Image);
        }
    }

    public interface OnItemClickListener {

        public void onTopicItemClick(frontConfig.DataBean.InfoBean position);

    }

}
