package com.juyijia.mm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.juyijia.mm.AppConfig;
import com.juyijia.mm.R;
import com.juyijia.mm.bean.frontConfig;

import java.util.List;

public class MainHomeShopingGridviewAdapter extends BaseAdapter {
    private Context context;
    List<frontConfig.DataBean.InfoBean> data;
    public MainHomeShopingGridviewAdapter(Context context, List<frontConfig.DataBean.InfoBean> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    context).inflate(R.layout.item_mainhomeshopinggridview, null);
            holder = new ViewHolder();
            holder.mTexTView = convertView.findViewById(R.id.mTexTView);
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        frontConfig.DataBean.InfoBean item = data.get(i);
        holder.mTexTView.setText(item.getModule());
        Glide.with(context).load(AppConfig.HOST1+ item.getIcon()).into(holder.imageView);
        return convertView;
    }

    class ViewHolder {
        TextView mTexTView;
        ImageView imageView;
    }

}
