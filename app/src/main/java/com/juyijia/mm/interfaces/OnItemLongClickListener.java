package com.juyijia.mm.interfaces;

/**
 * Created by cxf on 2017/8/9.
 * RecyclerView的Adapter点击事件
 */

public interface OnItemLongClickListener<T> {
    void onItemLongClick(T bean, int position);
}
