package com.emall_3_afternoon.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.emall_3_afternoon.entity.*;
import com.emall_3_afternoon.mapper.Store_InfoMapper;
import com.emall_3_afternoon.service.BuyerCart;
import com.emall_3_afternoon.service.S_Address_Info;
import com.emall_3_afternoon.service.S_Goods_Info;
import com.emall_3_afternoon.service.S_Order;
import com.emall_3_afternoon.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class C_Order {
    @Autowired
    private S_Order s_order;
    @Autowired
    private S_Address_Info s_address_info; //spring 自动装配一个service的类的对象
    @Autowired
    private S_Goods_Info s_goods_info;

    @RequestMapping(value = "get_order_list_by_b_s_id", method = RequestMethod.GET)
    public String getOrderListByB_s_id(HttpServletRequest request, Model model,HttpSession session){
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        //写死了后面请同学们自己改好
//        int b_s_id = 1;
        List<Integer> order_idList=s_order.getOrder_IdList(b_s_id);
        model.addAttribute("order_id_list",order_idList);
        int start = 1;
        if (!request.getParameter("start").equals("")){
            System.out.println("!=null");
            start=Integer.parseInt(request.getParameter("start"));
        }
//        int page_size = Integer.parseInt(request.getParameter("page_size"));
        int page_size = 6;
        String query_value = request.getParameter("query_value");
        List<Order_Info> orderInfoList = s_order.getOrderInfoListByOrderSearch(start, page_size, query_value,b_s_id);
        int count=s_order.getCountGoodsInfoListByStoreSearch(start, page_size, query_value,b_s_id);
        model.addAttribute("order_info_list", orderInfoList);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        model.addAttribute("query_value",query_value);
        model.addAttribute("start",start);
        model.addAttribute("page_size",page_size);
        model.addAttribute("count",count);
        Integer num = count%page_size==0? count / page_size: count / page_size+1;
        model.addAttribute("totalPage",num.intValue());
        return "order_info_list";
    }

    @RequestMapping(value = "getOrder_IdList", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getOrder_IdList(HttpServletRequest request, Model model,HttpSession session){
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        List<Integer> order_idList=s_order.getOrder_IdList(b_s_id);
        return order_idList;
    }

    @RequestMapping(value = "goods_to_order", method = RequestMethod.GET)
    public String goodsToOrder(HttpServletRequest request, RedirectAttributes attributes,HttpSession session){
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        //        int b_s_id = 1;
        Order_Item_Info order_item_info = initOrder_Item_Info(request);
        Order_Info order_info = initOrder_Info(request,session);
        Logistics_Info logistics_info = initLogistics_Info(request);
        int flag = 0;
        flag += s_order.goodsToOrder(order_info, order_item_info, logistics_info);
        if (flag == 0)
            return "error";
        //以下二行代码用于重定向，并带参数传递
        attributes.addAttribute("b_s_id", b_s_id);
        attributes.addAttribute("start","");
        attributes.addAttribute("query_value","");
        return  "redirect:get_order_list_by_b_s_id";
    }

    @RequestMapping(value = "carts_to_order", method = RequestMethod.GET)
    public String cartsToOrder(HttpServletRequest request, RedirectAttributes attributes,HttpSession session){
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        //        int b_s_id = 1;
        Order_Item_Info order_item_info = initOrder_Item_Info(request);
        Order_Info order_info = initOrder_Info(request,session);
        Logistics_Info logistics_info = initLogistics_Info(request);
        int flag = 0;
        flag += s_order.cartsToOrder(order_info, order_item_info, logistics_info);
        if (flag == 0)
            return "error";
        //以下二行代码用于重定向，并带参数传递
        attributes.addAttribute("b_s_id", b_s_id);
        attributes.addAttribute("start","");
        attributes.addAttribute("query_value","");
        return  "redirect:get_order_list_by_b_s_id";
    }


    @RequestMapping(value = "show_confirm_order_by_goods", method = RequestMethod.GET)
    public String showConfirmOrderByGoods(HttpServletRequest request, Model model,HttpSession session){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        int b_s_id = Integer.parseInt(session.getAttribute("b_s_id").toString());
        //从session中获取用户id，根据id去查找地址数据返回,现在没有。则写死先。以后写好拦截器在注释掉
        //int b_s_id = 1;
        //店铺信息
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        Store_InfoMapper storeInfoMapper =(Store_InfoMapper) SpringUtil.applicationContext.getBean("store_InfoMapper");
        Store_Info store_info = storeInfoMapper.getAStoreInfo(store_id);
        model.addAttribute("store_info",store_info);
        //商品信息
        int goods_id=Integer.parseInt(request.getParameter("goods_id"));
        model.addAttribute("goods_id",goods_id);
        Goods_Info goods_info = s_goods_info.getAGoods_Info(goods_id);
        model.addAttribute("goods_info", goods_info);
        float goods_actual_price=Float.parseFloat(request.getParameter("goods_actual_price"));
        model.addAttribute("goods_actual_price",goods_actual_price);
        int goods_sum=Integer.parseInt(request.getParameter("goods_sum"));
        model.addAttribute("goods_sum",goods_sum);
        float goods_money=Float.parseFloat(request.getParameter("goods_money"));
        model.addAttribute("goods_money",goods_money);
        String  photo_path=request.getParameter("photo_path");
        model.addAttribute("photo_path",photo_path);

        //地址信息
        List<Address_Info> address_infoList = null;
        address_infoList = s_address_info.getAddressList(b_s_id);
        model.addAttribute("address_list", address_infoList);
        Address_Info default_address=s_address_info.getDefaultAddress(b_s_id);
        model.addAttribute("default_address",default_address);
        return "order_confirm";
    }

    @RequestMapping(value = "update_order_status")
    public String UpdateOrder_status(HttpServletRequest request,Model model,HttpSession session,RedirectAttributes attributes){
        int order_id=Integer.parseInt(request.getParameter("order_id"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String pay_time = df.format(new Date());//调用时间函数生成
        int flag=s_order.update_order_status(order_id,pay_time);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        attributes.addAttribute("b_s_id", buyer_seller_info.getB_s_id());
        attributes.addAttribute("start","");
        attributes.addAttribute("query_value","");
        return "redirect:get_order_list_by_b_s_id";
    }

    @RequestMapping(value = "delete_order")
    public String DeleteOrder(HttpServletRequest request,HttpSession session,Model model,RedirectAttributes attributes){
        int order_id=Integer.parseInt(request.getParameter("order_id"));
        s_order.delete_order(order_id);
        s_order.delete_orderItem(order_id);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        attributes.addAttribute("b_s_id", buyer_seller_info.getB_s_id());
        attributes.addAttribute("start","");
        attributes.addAttribute("query_value","");
        return "redirect:get_order_list_by_b_s_id";
    }

    @RequestMapping(value = "cancelOrder")
    public String cancelOrder(HttpServletRequest request,HttpSession session,Model model,RedirectAttributes attributes){
        int order_id=Integer.parseInt(request.getParameter("order_id"));
        s_order.cancelOrder(order_id);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        attributes.addAttribute("b_s_id", buyer_seller_info.getB_s_id());
        attributes.addAttribute("start","");
        attributes.addAttribute("query_value","");
        return "redirect:get_order_list_by_b_s_id";
    }

    @RequestMapping(value = "show_confirm_order_by_cart", method = RequestMethod.GET)
    public String showConfirmOrderByCart(HttpServletRequest request, Model model,HttpSession session){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        int b_s_id = Integer.parseInt(session.getAttribute("b_s_id").toString());
        //从session中获取用户id，根据id去查找地址数据返回,现在没有。则写死先。以后写好拦截器在注释掉
        //int b_s_id = 1;
        //商品信息
        int goods_id=Integer.parseInt(request.getParameter("checked0"));
        model.addAttribute("goods_id",goods_id);
        Goods_Info goods_info = s_goods_info.getAGoods_Info(goods_id);
        model.addAttribute("goods_info", goods_info);
        float goods_actual_price=goods_info.getGoods_actual_price();
        model.addAttribute("goods_actual_price",goods_actual_price);
        int goods_sum=Integer.parseInt(request.getParameter("summ"));
        model.addAttribute("goods_sum",goods_sum);
        float goods_money=goods_sum*goods_actual_price;
        model.addAttribute("goods_money",goods_money);
        String photo_path=s_goods_info.getAGoods_Photo_Path(goods_id);
        model.addAttribute("photo_path",photo_path);
        //店铺信息
        int store_id = s_goods_info.getStore_id(goods_id);
        Store_InfoMapper storeInfoMapper =(Store_InfoMapper) SpringUtil.applicationContext.getBean("store_InfoMapper");
        Store_Info store_info = storeInfoMapper.getAStoreInfo(store_id);
        model.addAttribute("store_info",store_info);
        //地址信息
        List<Address_Info> address_infoList = null;
        address_infoList = s_address_info.getAddressList(b_s_id);
        model.addAttribute("address_list", address_infoList);
        Address_Info default_address=s_address_info.getDefaultAddress(b_s_id);
        model.addAttribute("default_address",default_address);
        return "order_confirm_cart";
    }


    @RequestMapping(value = "cart_to_order", method = RequestMethod.GET)
    public String cartToOrder(HttpServletRequest request, RedirectAttributes attributes,HttpSession session){
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        //        int b_s_id = 1;
        Order_Item_Info order_item_info = initOrder_Item_Info(request);
        Order_Info order_info = initOrder_Info(request,session);
        Logistics_Info logistics_info = initLogistics_Info(request);
        int flag = 0;
        flag += s_order.goodsToOrder(order_info, order_item_info, logistics_info);
        if (flag == 0)
            return "error";
        //以下二行代码用于重定向，并带参数传递
        attributes.addAttribute("b_s_id", b_s_id);
        attributes.addAttribute("start","");
        attributes.addAttribute("query_value","");
        return  "redirect:get_order_list_by_b_s_id";
    }



    //生成购物车编号
    public String getAnOrderNumber(HttpSession session) {
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String order_no=date.format(new Date())+buyer_seller_info.getB_s_id()+buyer_seller_info.getSex();
        return order_no;
     }


    public Order_Info initOrder_Info(HttpServletRequest request,HttpSession session){
        Order_Info order_info = new Order_Info();
        order_info.setStore_id(Integer.parseInt(request.getParameter("store_id")));
        order_info.setOrder_money(Float.parseFloat(request.getParameter("order_money")));
//        String order_no = "200820092010201101";//可以调用第三方插件或者函数生成一个唯一性的订单号
        String order_no=this.getAnOrderNumber(session);
        order_info.setOrder_no(order_no);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String order_time = df.format(new Date());//调用时间函数生成
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        order_info.setB_s_id(b_s_id);
        order_info.setOrder_time(order_time);
        order_info.setOrder_status(1);
        order_info.setPay_way("");
        order_info.setPay_status(0);
        order_info.setPay_time(null);
        return  order_info;
    }

    public Order_Item_Info initOrder_Item_Info(HttpServletRequest request){
        Order_Item_Info order_item_info = new Order_Item_Info();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String order_item_time = df.format(new Date());//调用时间函数生成
//        String order_item_time = "2020-11-26";
        order_item_info.setOrder_item_time(order_item_time);
        order_item_info.setGoods_id(Integer.parseInt(request.getParameter("goods_id")));
        order_item_info.setGoods_sum(Integer.parseInt(request.getParameter("goods_sum")));
        order_item_info.setGoods_money(Float.parseFloat(request.getParameter("goods_money")));
        order_item_info.setGoods_property(request.getParameter("goods_property"));

        return order_item_info;
    }
    public Logistics_Info initLogistics_Info(HttpServletRequest request){
        Logistics_Info logistics_info = new Logistics_Info();
        logistics_info.setGoods_address("");//不应该从界面获取，而应该从卖家那边获取。在此设计中并没有什么意义.
        logistics_info.setLink_man(request.getParameter("link_man"));
        logistics_info.setLink_telephone(request.getParameter("link_telephone"));
        logistics_info.setAddress_1(request.getParameter("address_1"));
        logistics_info.setAddress_detail(request.getParameter("address_detail"));
        logistics_info.setLogistics_status(0);

        return logistics_info;
    }
}
