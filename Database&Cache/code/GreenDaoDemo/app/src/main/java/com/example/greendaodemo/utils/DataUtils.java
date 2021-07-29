package com.example.greendaodemo.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.greendaodemo.model.GoodsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    /**
     * 获取数据
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<GoodsModel> getGoodsModels (String json){
        List<GoodsModel> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0 ; i < jsonArray.length() ; i ++) {
                JSONObject object = jsonArray.getJSONObject(i); //goods.json最后多个分号，可能会为null，严格来说要判断
                GoodsModel goodsModel = new GoodsModel();
                goodsModel.setGoodsId(object.getInt("goodsId"));
                goodsModel.setIcon(object.getString("icon"));
                goodsModel.setInfo(object.getString("info"));
                goodsModel.setName(object.getString("name"));
                goodsModel.setType(object.getString("type"));
                result.add(goodsModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
