package com.coolweather.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mcontext;
    private LayoutInflater mInflater;

    //数据源
    private List<String> mList;

    public RecyclerViewAdapter(Context context,List<String> list) {
        mcontext = context;
        this.mInflater = LayoutInflater.from(context);
        mList = list;

    }

    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_tv.setText(mList.get(position));

        //这里的监听是每一个item都有的
        holder.item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
                Toast.makeText(mcontext, "点击ITEM", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //总共有多少个条目
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_tv;

        public ViewHolder(View view) {
            super(view);
            item_tv = (TextView) view.findViewById(R.id.item_tv);
        }
    }
}