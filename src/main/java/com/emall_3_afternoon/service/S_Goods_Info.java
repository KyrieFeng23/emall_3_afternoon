package com.emall_3_afternoon.service;

import com.emall_3_afternoon.entity.Goods_Info;
import com.emall_3_afternoon.entity.Goods_Photo_Path_Info;
import com.emall_3_afternoon.entity.Goods_Property_Info;
import com.emall_3_afternoon.entity.Store_Info;
import com.emall_3_afternoon.mapper.Goods_InfoMapper;
import com.emall_3_afternoon.mapper.Goods_Photo_Path_InfoMapper;
import com.emall_3_afternoon.mapper.Goods_Property_InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class S_Goods_Info {
    @Autowired
    private Goods_InfoMapper goods_infoMapper;
    @Autowired
    private Goods_Photo_Path_InfoMapper goods_photo_path_infoMapper;
    @Autowired
    private Goods_Property_InfoMapper goods_property_infoMapper;

//    public List<Goods_Info> getGoodsList(String field, String field_value, int start, int page_size){
//        return goods_infoMapper.getGoodsList(field, field_value, start, page_size);
//    }

    public List<Goods_Info> getGoodsList(int store_id) {
        return goods_infoMapper.getGoodsList(store_id);
    }
    public List<Goods_Info> getAllGoodsList(int store_id) {
        return goods_infoMapper.getAllGoodsList(store_id);
    }
    public List<Goods_Info> getAllUpGoodsList() {
        return goods_infoMapper.getAllUpGoodsList();
    }


    public Goods_Info getA_EditGoods_Info(int goods_id){
        return goods_infoMapper.getA_EditGoods_Info(goods_id);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int insertGoods_Info(Goods_Info goods_info,
                                List<Goods_Photo_Path_Info> goods_photo_path_infoList,
                                List<Goods_Property_Info> goods_property_infoList){
        int flag = 0;
        int goods_id = -1;
        //插入goods_info 一条数据
        flag = goods_infoMapper.insertGoods_Info(goods_info);
        goods_id = goods_info.getGoods_id();
        //插入图片路径， 多条数据
        for (Goods_Photo_Path_Info goods_photo_path_info : goods_photo_path_infoList){
            goods_photo_path_info.setGoods_id(goods_id);
            flag = goods_photo_path_infoMapper.insertGoods_Photo_Path_Info(goods_photo_path_info);
        }
        //插入属性值 多条
        for (Goods_Property_Info goods_property_info : goods_property_infoList){
            goods_property_info.setGoods_id(goods_id);
            flag = goods_property_infoMapper.insertGoods_Property(goods_property_info);
        }

        return  flag;
    }

    //上架
    public int upStatus(int goods_id, int goods_status) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("日期："+df.format(new Date()));// new Date()为获取当前系统时间
        String begin_time = df.format(new Date());//需要代码实现日期
        int flag = 0;
        if (goods_status == 1)
            flag = goods_infoMapper.upStatus(goods_id, begin_time);

        return flag;
    }

    //下架
    public int downStatus(int goods_id, int goods_status) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("日期："+df.format(new Date()));// new Date()为获取当前系统时间
        String end_time = df.format(new Date());//需要代码实现日期
        int flag = 0;
        if (goods_status == 0)
            flag = goods_infoMapper.downStatus(goods_id, end_time);

        return flag;
    }

    //删除
    public int deleteGoods_Info(int goods_id){
        int flag=0;
        flag=goods_infoMapper.deleteGoods_Info(goods_id)+goods_property_infoMapper.deleteGoods_Property_Info(goods_id);
        return flag;
    }

    //修改的逻辑 ：1：对goods_info表的数据进行真的修改。
    //2:对path表进行先删除，在插入，
    //3:对property表进行先删除，在插入。也可以每条做到进行update。
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int updateGoods_Info(Goods_Info goods_info,
                                List<Goods_Photo_Path_Info> goods_photo_path_infoList,
                                List<Goods_Property_Info> goods_property_infoList) {
        int flag = 0;
        //插入商品信息 表名：goods_info
        flag += goods_infoMapper.updateGoods_Info(goods_info);
        int goods_id = goods_info.getGoods_id(); //获取修改后的对象的主键id
        //property采用了第一种方法：先把原来的旧数据删除，再插入商品属性信息 多条 表名：goods_property_info
        flag += goods_property_infoMapper.deleteGoods_Property_Info(goods_id);
        for (Goods_Property_Info goods_property_info : goods_property_infoList) {
            goods_property_info.setGoods_id(goods_id);
            flag += goods_property_infoMapper.insertGoods_Property(goods_property_info);
        }
        //先删除path的数据，再插入多条到商品图片表。表名：goods_photo_path_info
        flag += goods_photo_path_infoMapper.deleteGoods_Photo_Path_Info(goods_id);
        for (Goods_Photo_Path_Info goods_photo_path_info : goods_photo_path_infoList) {
            goods_photo_path_info.setGoods_id(goods_id);
            flag += goods_photo_path_infoMapper.insertGoods_Photo_Path_Info(goods_photo_path_info);
        }

        return flag;
    }

    //start表示第几页。如果是负数。直接设置为第一页
    public List<Goods_Info> getGoodsInfoListByPage(int start, int page_size, String query_value){
        List<Goods_Info> goods_infoList = null;
        if (start <= 0)
            start = 1;
        //1页的位置：（1-1）*10 = 0;第二页 (2-1)*10 = 10 (3-1)*10 = 20 如果是上一页是负数。-3.-4，-5；
        //处理过之后，把页的位置转换成数据库里面数据的位置。
        int pos = (start - 1) * page_size;
        goods_infoList = goods_infoMapper.getGoodsInfoListByPage(pos, page_size, query_value);

        return  goods_infoList;
    }

    //搜店铺用：start表示第几页。如果是负数。直接设置为第一页
    public List<Goods_Info> getGoodsInfoListByStoreSearch(int start, int page_size, String query_value,int store_id){
        List<Goods_Info> goods_infoList = null;
        if (start <= 0)
            start = 1;
        //1页的位置：（1-1）*10 = 0;第二页 (2-1)*10 = 10 (3-1)*10 = 20 如果是上一页是负数。-3.-4，-5；
        //处理过之后，把页的位置转换成数据库里面数据的位置。
        int pos = (start - 1) * page_size;
        goods_infoList = goods_infoMapper.getGoodsInfoListByStoreSearch(pos, page_size, query_value,store_id);

        return  goods_infoList;
    }

    //店铺返回个数
    //start表示第几页。如果是负数。直接设置为第一页
    public int getCountGoodsInfoListByStoreSearch(int start, int page_size, String query_value, int store_id){
        if (start <= 0)
            start = 1;
        //1页的位置：（1-1）*10 = 0;第二页 (2-1)*10 = 10 (3-1)*10 = 20 如果是上一页是负数。-3.-4，-5；
        //处理过之后，把页的位置转换成数据库里面数据的位置。
        int pos = (start - 1) * page_size;
        int count = goods_infoMapper.getCountGoodsInfoListByStoreSearch(pos, page_size, query_value,store_id);

        return  count;
    }

    //搜淘宝使用：start表示第几页。如果是负数。直接设置为第一页
    public List<Goods_Info> getGoodsInfoListByTaoBaoSearch(int start, int page_size, String query_value){
        List<Goods_Info> goods_infoList = null;
        if (start <= 0)
            start = 1;
        //1页的位置：（1-1）*10 = 0;第二页 (2-1)*10 = 10 (3-1)*10 = 20 如果是上一页是负数。-3.-4，-5；
        //处理过之后，把页的位置转换成数据库里面数据的位置。
        int pos = (start - 1) * page_size;
        goods_infoList = goods_infoMapper.getGoodsInfoListByTaoBaoSearch(pos, page_size, query_value);

        return  goods_infoList;
    }

    //淘宝返回个数
    //start表示第几页。如果是负数。直接设置为第一页
    public int getCountGoodsInfoListByTaoBaoSearch(int start, int page_size, String query_value){
        if (start <= 0)
            start = 1;
        //1页的位置：（1-1）*10 = 0;第二页 (2-1)*10 = 10 (3-1)*10 = 20 如果是上一页是负数。-3.-4，-5；
        //处理过之后，把页的位置转换成数据库里面数据的位置。
        int pos = (start - 1) * page_size;
        int count = goods_infoMapper.getCountGoodsInfoListByTaoBaoSearch(pos, page_size, query_value);

        return  count;
    }


    public Goods_Info getAGoods_Info(int goods_id){
        return goods_infoMapper.getAGoods_Info(goods_id);
    }
    public int getStore_id(int goods_id){
        return goods_infoMapper.getStore_id(goods_id);
    }
    public Store_Info getAStore_Info(int store_id){
        return goods_infoMapper.getAStore_Info(store_id);
    }

    public String getGoodsName(int goods_id){
        return goods_infoMapper.getGoodsName(goods_id);
    }
    public String getStoreName(int store_id){
        return goods_infoMapper.getStoreName(store_id);
    }
    public Float getGoodsPrice(int goods_id){
        return goods_infoMapper.getGoodsPrice(goods_id);
    }
    public String get_goods_property(int order_id){
        return goods_infoMapper.get_goods_property(order_id);
    }

    public String getAGoods_Photo_Path(int goods_id){
        return goods_photo_path_infoMapper.get_A_Goods_Photo_Path(goods_id);
    }

    public List<Goods_Photo_Path_Info> getAllGoods_Photo_Path(int goods_id){
        List<Goods_Photo_Path_Info> goods_photo_path_info=goods_photo_path_infoMapper.get_All_Goods_Photo_Path(goods_id);
        List<Goods_Photo_Path_Info> added=new ArrayList<Goods_Photo_Path_Info>();
        for (int i=0;i<goods_photo_path_info.size();i++){
            added.add(goods_photo_path_info.get(i));
        }
        return added;
    }

    public List<Goods_Info> getCollectGoods(int b_s_id){
        List<Integer> goods_idList= goods_infoMapper.getCollectGoods(b_s_id);
        List<Goods_Info> goods_infoList=new ArrayList<>();
        for (int i=0;i<goods_idList.size();i++){
            goods_infoList.add(this.getAGoods_Info(goods_idList.get(i)));
        }
        return goods_infoList;
    }

    public int countcollect(int b_s_id){
        return goods_infoMapper.countcollect(b_s_id);
    }


    public int addtocollect(List<Goods_Info> goods_infoList,int b_s_id){
        int flag=0;
        for (Goods_Info goods_info:goods_infoList) {
            int goods_id=goods_info.getGoods_id();
            flag += goods_infoMapper.addtocollect(goods_id, b_s_id);
        }
        return flag;
    }

}
