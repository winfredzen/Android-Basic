package com.example.loadimagedemo;


import androidx.annotation.NonNull;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

@GlideExtension
public class MyGlideExtension {
    private MyGlideExtension() {

    }

    /**
     * 全局统一的配置
     * @param options
     */
    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> injectOptions(BaseRequestOptions<?> options) {
       return options.placeholder(R.mipmap.loading)
                .error(R.mipmap.loader_error)
                .circleCrop();
    }
}
