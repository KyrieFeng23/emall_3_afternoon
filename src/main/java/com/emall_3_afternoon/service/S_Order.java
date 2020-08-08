package com.emall_3_afternoon.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.emall_3_afternoon.entity.*;
import com.emall_3_afternoon.mapper.Address_InfoMapper;
import com.emall_3_afternoon.mapper.Logistics_InfoMapper;
import com.emall_3_afternoon.mapper.Order_InfoMapper;
import com.emall_3_afternoon.mapper.Order_Item_InfoMapper;
import com.emall_3_afternoon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class S_Order {
    @Autowired
    private RedisUtil redis;//用来从redis里面获取value或者设置value。

    @Autowired
    private Order_InfoMapper order_infoMapper;
    @Autowired
    private Order_Item_InfoMapper order_itemMapper;
    @Autowired
    private Logistics_InfoMapper logistics_infoMapper;
    @Autowired
    private Address_InfoMapper address_infoMapper;
    public List<Order_Info> getOrder_InfoList(int b_s_id){
        return order_infoMapper.getOrder_InfoList(b_s_id);
    }

    public List<Integer> getOrder_IdList(int b_s_id){
        return order_infoMapper.getOrder_IdList(b_s_id);
    }

    public int getOrder_id_by_Order_no(String order_no){
        return order_infoMapper.getOrder_id_by_Order_no(order_no);
    }

    //插入数据到订单。一种是直接从商品到订单。一种是从购物车到订单
    //1:商品到订单。这是写的不好的典型案例，应该按2的标准设计实现
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int goodsToOrder(int goods_id, int store_id, int goods_sum, float goods_money, int b_s_id,
                            int address_id){
        int flag = 0;
        System.out.println("不好的goodstoorder");
        //设置订单总表
        Order_Info order_info = new Order_Info();
        order_info.setB_s_id(b_s_id);
        order_info.setOrder_money(goods_money);
        order_info.setStore_id(store_id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date_time = df.format(new Date());//调用时间函数生成
//        String date_time = "2020-05-14";
        order_info.setOrder_time(date_time);

        //设置订单明细表
        Order_Item_Info order_item_info = new Order_Item_Info();
        order_item_info.setGoods_id(goods_id);
        order_item_info.setGoods_sum(goods_sum);
        order_item_info.setGoods_money(goods_money);
        order_item_info.setOrder_item_time(date_time);
        //设置物流信息
        Address_Info address_info = address_infoMapper.getAddress(address_id);
        Logistics_Info logistics_info = new Logistics_Info();
        logistics_info.setLink_man(address_info.getAddresssee());
        logistics_info.setLink_telephone(address_info.getTelephone());
        logistics_info.setAddress_1(address_info.getAddress());
        logistics_info.setAddress_detail(address_info.getAddress_detail());
        logistics_info.setLogistics_status(0);


        //分别insert order order_item 和logistics 3张表的数据
        flag += order_infoMapper.insertOrder_Info(order_info);
        //order_item 和logistics都需要order_info里面的order_id主键作为外键
        order_item_info.setOrder_id(order_info.getOrder_id());
        flag += order_itemMapper.insertOrder_Item_Info(order_item_info);
        //order_item 和logistics都需要order_info里面的order_id主键作为外键
        logistics_info.setOrder_id(order_info.getOrder_id());
        flag += logistics_infoMapper.insertLogistics_Info(logistics_info);

        return  flag;
    }

    //2:商品插入到订单，接口参数尽可能少，尽可能一个函数(或者方法)只做一件事，初始化这其他地方做。或者
    //通过调用函数来完成初始化.
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int goodsToOrder(Order_Info order_info, Order_Item_Info order_item_info,
                            Logistics_Info logistics_info){
        int flag = 0;
        System.out.println("好的goodstoorder");
        //1插入订单总表
        flag += order_infoMapper.insertOrder_Info(order_info);
        String order_no=order_info.getOrder_no();
        int order_id=this.getOrder_id_by_Order_no(order_no);
        order_info.setOrder_id(order_id);
        order_id = order_info.getOrder_id();//因为order_id是自增长的，所以只有插入之后才能取到
        System.out.println("order_id:"+order_id);
        //2:插入订单明细
        order_item_info.setOrder_id(order_id);
        flag += order_itemMapper.insertOrder_Item_Info(order_item_info);
        //3:插入物流总表。物流明细由物流点自动添加进去
        logistics_info.setOrder_id(order_id);
        flag += logistics_infoMapper.insertLogistics_Info(logistics_info);

        return  flag;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int cartsToOrder(Order_Info order_info, Order_Item_Info order_item_info,
                            Logistics_Info logistics_info){
        int flag = 0;
        System.out.println("好的goodstoorder");
        //1插入订单总表
        flag += order_infoMapper.insertOrder_Info(order_info);
        String order_no=order_info.getOrder_no();
        int order_id=this.getOrder_id_by_Order_no(order_no);
        order_info.setOrder_id(order_id);
        order_id = order_info.getOrder_id();//因为order_id是自增长的，所以只有插入之后才能取到
        System.out.println("order_id:"+order_id);
        //2:插入订单明细
        order_item_info.setOrder_id(order_id);
        flag += order_itemMapper.insertOrder_Item_Info(order_item_info);
        //3:插入物流总表。物流明细由物流点自动添加进去
        logistics_info.setOrder_id(order_id);
        flag += logistics_infoMapper.insertLogistics_Info(logistics_info);
        String b_s_id = order_info.getB_s_id() + "";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
            //通过goods_id循环删除购物车里面的数据
            int store_id = order_info.getStore_id();
                int goods_id = order_item_info.getGoods_id();
                //删除只需要2个参数
                buyerCart.deleteStore_Item(goods_id, store_id);
        //序列化购物车对象到redis,要保存回redis.其实就是序列化
        String fromObject = JSON.toJSONString(buyerCart);
        redis.set(b_s_id, fromObject.toString());
        return  flag;
    }

    //3:购物车导入到订单
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    int cartToOrder(List<Order_Info> order_infoList, Logistics_Info logistics_info){
        int flag = 0;
        String b_s_id = order_infoList.get(0).getB_s_id() + "";
        //先进行第一重循环，把order_info循环插入
        for (Order_Info order_info :  order_infoList) {
            flag += order_infoMapper.insertOrder_Info(order_info);
            int order_id = order_info.getOrder_id();
            //循环插入订单明细，有多少条插入多少条。order_info.getOrder_item_infoList()获取要插入的明细数据
            for (Order_Item_Info order_item_info : order_info.getOrder_item_infoList()) {
                order_item_info.setOrder_id(order_id);
                flag = order_itemMapper.insertOrder_Item_Info(order_item_info);
            }
            //最后插入物流总表信息。譬如寄给谁 地址，电话等信息
            logistics_info.setOrder_id(order_id);
            flag += logistics_infoMapper.insertLogistics_Info(logistics_info);
        }
        //清除购物车选中的信息。//循环删除对应购物车里面goods_id的信息。
        //反序列化购物车对象。
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        for (Order_Info order_info :  order_infoList) {
            //通过goods_id循环删除购物车里面的数据
            int store_id = order_info.getStore_id();
            for (Order_Item_Info order_item_info : order_info.getOrder_item_infoList()) {
                int goods_id = order_item_info.getGoods_id();
                //删除只需要2个参数
                buyerCart.deleteStore_Item(goods_id, store_id);
            }
        }
        //序列化购物车对象到redis,要保存回redis.其实就是序列化
        String fromObject = JSON.toJSONString(buyerCart);
        redis.set(b_s_id, fromObject.toString());

        return  flag;
    }


    //搜订单用：start表示第几页。如果是负数。直接设置为第一页
    public List<Order_Info> getOrderInfoListByOrderSearch(int start, int page_size, String query_value, int b_s_id){
        List<Order_Info> orderInfoList = null;
        if (start <= 0)
            start = 1;
        //1页的位置：（1-1）*10 = 0;第二页 (2-1)*10 = 10 (3-1)*10 = 20 如果是上一页是负数。-3.-4，-5；
        //处理过之后，把页的位置转换成数据库里面数据的位置。
        int pos = (start - 1) * page_size;
        orderInfoList = order_infoMapper.getOrderInfoListByOrderSearch(pos, page_size, query_value,b_s_id);

        return  orderInfoList;
    }
    //订单返回个数
    //start表示第几页。如果是负数。直接设置为第一页
    public int getCountGoodsInfoListByStoreSearch(int start, int page_size, String query_value, int b_s_id){
        if (start <= 0)
            start = 1;
        //1页的位置：（1-1）*10 = 0;第二页 (2-1)*10 = 10 (3-1)*10 = 20 如果是上一页是负数。-3.-4，-5；
        //处理过之后，把页的位置转换成数据库里面数据的位置。
        int pos = (start - 1) * page_size;
        int count = order_infoMapper.getCountOrderInfoListByOrderSearch(pos, page_size, query_value,b_s_id);

        return  count;
    }


    public int update_order_status(int order_id,String pay_time){
        return order_infoMapper.update_order_status(order_id,pay_time);
    }
    public int delete_order(int order_id){
        return order_infoMapper.delete_order(order_id);
    }
    public int delete_orderItem(int order_id){
        return order_infoMapper.delete_orderItem(order_id);
    }

    public int cancelOrder(int order_id){
        return order_infoMapper.cancelOrder(order_id);
    }



    public BuyerCart getABuyerCart(String b_s_id){
        String buyer_cart_value = redis.get(b_s_id);
        BuyerCart buyerCart = null;
        //从redis里面获取购物车,获取购物车也有2种情况，1：获取到了。2：获取不到.
        //如何去获取。在redis里面根据keyname:用户名,keyvalue:存储购物车信息。
        //从redis获取完之后。对keyvalue进行反序列化生成对象。
        if (buyer_cart_value == "" || buyer_cart_value.equals(null) || buyer_cart_value == null)
            buyerCart = new BuyerCart();
        else {//if表示没有值，else表示有值，所以对他进行反序列化。
            buyerCart = JSON.parseObject(buyer_cart_value,
                    new TypeReference<BuyerCart>(){});
        }
        return buyerCart;
    }
}
