package com.example.harvic.blogrecyclerviewsec.snaphelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.harvic.blogrecyclerviewsec.R;

import java.util.ArrayList;

public class SnapHelperAdapter  extends Adapter {

    private Context mContext;
    private ArrayList<String> mDatas;
    private int[] mPics = {R.mipmap.item1,R.mipmap.item2,R.mipmap.item3,R.mipmap.item4,
            R.mipmap.item5,R.mipmap.item6};
    public SnapHelperAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new NormalHolder(inflater.inflate(R.layout.snap_helper_horizontal_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.mImg.setImageDrawable(mContext.getResources().getDrawable(mPics[position%mPics.length]));
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends ViewHolder {
        public ImageView mImg;
        public NormalHolder(View itemView) {
            super(itemView);

            mImg = (ImageView)itemView.findViewById(R.id.item_image);
        }

    }
}
