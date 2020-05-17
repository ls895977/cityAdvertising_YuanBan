package com.juyijia.mm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.juyijia.mm.R;
import com.juyijia.mm.bean.shouYeLieBiaoBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MainHomeShopingGridViewItemAdapter extends RecyclerView.Adapter<MainHomeShopingGridViewItemAdapter.ViewHolder> {
    private Context context;
    private List<shouYeLieBiaoBean.MessageBeanX.MessageBean.StoresBeanX.StoresBean.HotGoodsBeanXXXXXX> data;
    public MainHomeShopingGridViewItemAdapter(Context context, List<shouYeLieBiaoBean.MessageBeanX.MessageBean.StoresBeanX.StoresBean.HotGoodsBeanXXXXXX> data) {
        this.context = context;
        this.data = data;

    }

    @Override
    public MainHomeShopingGridViewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shouye_griditem, parent, false);
        return new MainHomeShopingGridViewItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHomeShopingGridViewItemAdapter.ViewHolder holder, final int position) {
        shouYeLieBiaoBean.MessageBeanX.MessageBean.StoresBeanX.StoresBean.HotGoodsBeanXXXXXX item = data.get(position);
        Glide.with(context).load(item.getThumb()).into(holder.im_logo);
        holder.tv_title.setText(item.getTitle());
        holder.sailed.setText("￥"+item.getPrice());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView im_logo;
        private TextView tv_title, sailed;
        public ViewHolder(View itemView) {
            super(itemView);
            im_logo = itemView.findViewById(R.id.im_logo);
            tv_title = itemView.findViewById(R.id.tv_title);
            sailed = itemView.findViewById(R.id.tv_sailed);
        }
    }
}
