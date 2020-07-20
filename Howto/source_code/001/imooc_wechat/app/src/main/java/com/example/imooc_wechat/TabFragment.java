package com.example.imooc_wechat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.imooc_wechat.utils.L;

import org.w3c.dom.Text;

public class TabFragment extends Fragment {

    private static final String BUNDLE_KEY_TITLE = "key_title";

    private TextView mTvTitle;

    private String mTitle;


    public static TabFragment newInstance(String title){
        //传递参数
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TITLE, title);

        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle); //在界面的销毁和恢复过程中有很重要的作用
        return tabFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_KEY_TITLE, "");
        }

        L.d("onCreate, title = " + mTitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        L.d("onCreateView, title = " + mTitle);

        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvTitle = view.findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d("onDestroyView, title = " + mTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("onDestroy, title = " + mTitle);
    }

    public void changeTitle(String title) {
        if (!isAdded()) {//注意要判断
            return;
        }
        mTvTitle.setText(title);
    }
}
