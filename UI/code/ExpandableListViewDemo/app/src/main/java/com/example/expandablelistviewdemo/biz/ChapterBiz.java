package com.example.expandablelistviewdemo.biz;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.expandablelistviewdemo.MainActivity;
import com.example.expandablelistviewdemo.bean.Chapter;
import com.example.expandablelistviewdemo.bean.ChapterItem;
import com.example.expandablelistviewdemo.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 从网络加载数据
 * create by wangzhen 2021/7/30
 */
public class ChapterBiz {
    public void loadDatas(Context context, Callback callback, boolean userCache) {
        AsyncTask<Boolean, Void, List<Chapter>> asyncTask = new AsyncTask<Boolean, Void, List<Chapter>>() {
            private Exception ex;
            @Override
            protected List<Chapter> doInBackground(Boolean... booleans) {
                boolean isUseCache = booleans[0];
                List<Chapter> chapterList = new ArrayList<>();

                try {
                    if (isUseCache) {
                        // 从缓存加载
                    }
                    if (chapterList.isEmpty()) {
                        //从网络加载
                        List<Chapter> chapterListFromNet = loadFromNet(context);
                        //缓存

                        chapterList.addAll(chapterListFromNet);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                    this.ex = exception;
                }
                return chapterList;
            }

            @Override
            protected void onPostExecute(List<Chapter> chapterList) {
                if (ex != null) {//没有发生异常
                    if (callback != null) {
                        callback.onFailed(ex);
                        return;
                    }
                }
                if (callback != null) {
                    callback.onSuccess(chapterList);
                }
            }
        };
        asyncTask.execute(userCache);
    }

    // 从网络加载数据
    private List<Chapter> loadFromNet(Context context) {
        List<Chapter> chapterList = new ArrayList<>();
        String url = "https://www.imooc.com/api/expandablelistview";
        String content = HttpUtils.doGet(url);

        Log.d(MainActivity.TAG, "content = " + content);

        if (content != null) {
            chapterList = parseContent(content);

            Log.d(MainActivity.TAG, "parseContent finish chapterList = " + chapterList);
        }
        return chapterList;
    }

    private List<Chapter> parseContent(String content) {
        List<Chapter> chapterList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(content);
            int errorCode = root.optInt("errorCode");
            if (errorCode == 0) {
                JSONArray dataJsonArray = root.optJSONArray("data");
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    JSONObject chapterJsonObj = dataJsonArray.getJSONObject(i);
                    int id = chapterJsonObj.optInt("id");
                    String name = chapterJsonObj.optString("name");
                    Chapter chapter = new Chapter(id, name);
                    chapterList.add(chapter);
                    //子child
                    JSONArray childJsonArr = chapterJsonObj.optJSONArray("children");
                    if (childJsonArr != null) {
                        for (int j = 0; j < childJsonArr.length(); j++) {
                            JSONObject childJsonObject = childJsonArr.getJSONObject(j);
                            int cid = childJsonObject.optInt("id");
                            String cname = childJsonObject.optString("name");
                            ChapterItem chapterItem = new ChapterItem(cid, cname);
                            chapter.addChild(chapterItem);
                        }
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return chapterList;
    }

    public static interface Callback {
        void onSuccess(List<Chapter> chapterList);
        void onFailed(Exception exception);
    }
}
