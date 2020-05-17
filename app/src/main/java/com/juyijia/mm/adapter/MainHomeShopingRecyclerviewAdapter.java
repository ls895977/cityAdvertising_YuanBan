package com.juyijia.mm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.juyijia.mm.R;
import com.juyijia.mm.bean.shouYeLieBiaoBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MainHomeShopingRecyclerviewAdapter extends RecyclerView.Adapter<MainHomeShopingRecyclerviewAdapter.ViewHolder> {

    private Context context;
    private List<shouYeLieBiaoBean.MessageBeanX.MessageBean.StoresBeanX.StoresBean> data;
    private backItem backItem1;

    public MainHomeShopingRecyclerviewAdapter(Context context, List<shouYeLieBiaoBean.MessageBeanX.MessageBean.StoresBeanX.StoresBean> data, backItem backItem) {
        this.context = context;
        this.data = data;
        backItem1 = backItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        shouYeLieBiaoBean.MessageBeanX.MessageBean.StoresBeanX.StoresBean item = data.get(position);
        Glide.with(context).load(item.getLogo()).into(holder.im_logo);
        holder.sailed.setText("月售：" + item.getSailed());
        holder.tv_title.setText(item.getTitle());
        holder.ra_scores111.setRating(item.getScores().size());
        if (item.getSend_price().equals("0")) {
            holder.tv_send_price.setText("起送价 ￥" + item.getSend_price() + "    免费配送");
        } else {
            holder.tv_send_price.setText("起送价 ￥" + item.getSend_price());
        }
        holder.myRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        MainHomeShopingGridViewItemAdapter adapter = new MainHomeShopingGridViewItemAdapter(context, item.getHot_goods());
        holder.myRecyclerView.setAdapter(adapter);
        if (item.getActivity().getItems().size() > 0) {
            holder.llmian.setVisibility(View.VISIBLE);
            holder.labe_items.setText(item.getActivity().getItems().get(0).getTitle());
        } else {
            holder.llmian.setVisibility(View.GONE);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backItem1.onBackItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView im_logo;
        private TextView tv_title, sailed, tv_send_price, labe_items;
        private RatingBar ra_scores111;
        private RecyclerView myRecyclerView;
        private LinearLayout llmian, item;

        public ViewHolder(View itemView) {
            super(itemView);
            im_logo = itemView.findViewById(R.id.im_logo);
            tv_title = itemView.findViewById(R.id.tv_title);
            sailed = itemView.findViewById(R.id.tv_sailed);
            ra_scores111 = itemView.findViewById(R.id.ra_scores111);
            tv_send_price = itemView.findViewById(R.id.tv_send_price);
            myRecyclerView = itemView.findViewById(R.id.MyRecyclerView);
            llmian = itemView.findViewById(R.id.llmian);
            labe_items = itemView.findViewById(R.id.labe_items);
            item = itemView.findViewById(R.id.item);
        }
    }

    public interface backItem {
        void onBackItem(int position);
    }
}
