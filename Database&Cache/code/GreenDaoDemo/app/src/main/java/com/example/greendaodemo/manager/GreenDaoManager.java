package com.example.greendaodemo.manager;

import android.content.Context;

import com.example.greendaodemo.MyApplication;
import com.example.greendaodemo.model.GoodsModel;
import com.example.greendaodemo.model.GoodsModelDao;
import com.example.greendaodemo.utils.DataUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * create by wangzhen 2021/7/29
 */
public class GreenDaoManager {
    private Context mContext;
    private GoodsModelDao mGoodsModelDao;

    public GreenDaoManager(Context context) {
        mContext = context;
        mGoodsModelDao = MyApplication.sDaoSession.getGoodsModelDao();
    }

    /**
     * 添加所有数据到数据库
     */
    public void insertGoods() {
        String json = DataUtils.getJson("goods.json", mContext);
        List<GoodsModel> list = DataUtils.getGoodsModels(json);

        mGoodsModelDao.insertInTx(list);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<GoodsModel> queryGoods() {
        QueryBuilder<GoodsModel> queryBuilder = mGoodsModelDao.queryBuilder();
        return queryBuilder.orderDesc(GoodsModelDao.Properties.GoodsId).list();
    }

    /**
     * 筛选水果
     * @return
     */
    public List<GoodsModel> queryFruits() {
        QueryBuilder<GoodsModel> queryBuilder = mGoodsModelDao.queryBuilder();
        queryBuilder = queryBuilder.where(GoodsModelDao.Properties.Type.eq("0")).orderDesc(GoodsModelDao.Properties.GoodsId);
        return queryBuilder.list();
    }

    /**
     * 筛选零食
     * @return
     */
    public List<GoodsModel> querySnacks() {
        QueryBuilder<GoodsModel> queryBuilder = mGoodsModelDao.queryBuilder();
        queryBuilder = queryBuilder.where(GoodsModelDao.Properties.Type.eq("1")).orderDesc(GoodsModelDao.Properties.GoodsId);
        return queryBuilder.list();
    }

    /**
     * 删除指定数据
     * @param goodsModel
     */
    public void deleteGoodsInfo(GoodsModel goodsModel) {
        mGoodsModelDao.delete(goodsModel);
    }

    /**
     * 更新数据
     * @param goodsModel
     */
    public void updateGoodsInfo(GoodsModel goodsModel) {
        mGoodsModelDao.update(goodsModel);
    }


}


