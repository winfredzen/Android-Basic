package com.example.recyclerviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wangzhen 2021/3/18
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private Context context;

    private List<String> dataSource;

    private RecyclerView recyclerView;

    private OnItemClickListener onItemClickListener;

    // 插入数据的位置
    private int addDataPosition = -1;

    public MyRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.dataSource = new ArrayList<>();
        this.recyclerView = recyclerView;
    }

    // 创建并返回viewholder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
    }

    // 通过viewholder来绑定数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(getIcon(position));
        holder.textView.setText(dataSource.get(position));

        if (recyclerView.getLayoutManager().getClass() == StaggeredGridLayoutManager.class) {
            // 为瀑布流布局，使用随机高度
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getRandomHeight());
            holder.textView.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.textView.setLayoutParams(params);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接口的回调方法
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        //改变itemview的背景颜色
        if (addDataPosition == position) {
            holder.itemView.setBackgroundColor(Color.RED);
        }

    }

    // 返回数据数量
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    private int getIcon(int postion) {
        switch (postion % 5) {
            case 0:
                return R.mipmap.a;
            case 1:
                return R.mipmap.b;
            case 2:
                return R.mipmap.c;
            case 3:
                return R.mipmap.d;
            case 4:
                return R.mipmap.e;
        }
        return 0;
    }

    // 返回不同的高度
    private int getRandomHeight() {
        return (int) (Math.random() * 1000);
    }

    // 添加一条数据
    public void addData(int position) {
        this.addDataPosition = position;
        dataSource.add(position, "插入的数据");
        notifyItemInserted(position);
        // 刷新itemview
        notifyItemRangeChanged(position, dataSource.size() - position);
    }

    // 删除一条数据
    public void removeData(int position) {
        this.addDataPosition= -1;
        dataSource.remove(position);
        notifyItemRemoved(position);
        // 刷新itemview
        notifyItemRangeChanged(position, dataSource.size() - position);
    }

    public List<String> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<String> dataSource) {
        this.dataSource = dataSource;
        // 刷新
        notifyDataSetChanged();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.iv);
            textView = itemView.findViewById(R.id.tv);


        }
    }

    /**
     * itemview点击事件回到接口
     */
    interface OnItemClickListener {
        void onItemClick(int postion);
    }

}
