package com.example.tablayoutdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Arrays;

/**
 * create by wangzhen 2021/10/9
 */
public class TabFragment extends Fragment {
    private String[] nameDatas = new String[]{"智能","红润","日系","自然","艺术黑白","甜美","蜜粉","清新","夏日阳光","唯美","蜜粉",};
    private RecyclerView recyclerView;
    private CommonAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new CommonAdapter<String>(getActivity(),R.layout.item, Arrays.asList(nameDatas)) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        recyclerView.setAdapter(adapter);
    }

}
