package com.example.expandablelistviewdemo.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.expandablelistviewdemo.R;
import com.example.expandablelistviewdemo.bean.Chapter;
import com.example.expandablelistviewdemo.bean.ChapterItem;

import java.util.List;

/**
 * create by wangzhen 2021/7/30
 */
public class ChapterAdapter extends BaseExpandableListAdapter {
    private List<Chapter> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;

    public ChapterAdapter(Context context, List<Chapter> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDatas = datas;
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_parent_chapter, parent, false);
            parentViewHolder = new ParentViewHolder();
            parentViewHolder.mTvName = convertView.findViewById(R.id.tv_name);
            parentViewHolder.mIvIndicator = convertView.findViewById(R.id.indicator_iv);
            convertView.setTag(parentViewHolder); // 保存
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        Chapter chapter = mDatas.get(groupPosition);
        parentViewHolder.mTvName.setText(chapter.getName());
        //是否展开
        parentViewHolder.mIvIndicator.setSelected(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_child_chapter, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mTvName = convertView.findViewById(R.id.tv_name);
            convertView.setTag(childViewHolder); // 保存
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        ChapterItem chapterItem = mDatas.get(groupPosition).getChildren().get(childPosition);
        childViewHolder.mTvName.setText(chapterItem.getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ParentViewHolder {
        TextView mTvName;
        ImageView mIvIndicator;
    }


    public static class ChildViewHolder {
         TextView mTvName;
    }

}
