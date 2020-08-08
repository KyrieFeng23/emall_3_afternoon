package com.emall_3_afternoon.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.emall_3_afternoon.entity.Buyer_Seller_Info;
import com.emall_3_afternoon.service.BuyerCart;
import com.emall_3_afternoon.entity.Store_Item;
import com.emall_3_afternoon.service.S_Goods_Info;
import com.emall_3_afternoon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CBuyerCart {
    @Autowired
    private S_Goods_Info s_goods_info;
    @Autowired
    private RedisUtil redis;//用来从redis里面获取value或者设置value。

    @RequestMapping(value = "show_cart", method = RequestMethod.GET)
    public String showCart(HttpServletRequest request, Model model){
        //根据传入的用户id.不考虑jd模式的cookie。从redis获取购物车信息，并显示出来。
        //1:获取用户的id或者name。。可以从session里面获取。
        HttpSession session = request.getSession();
        String b_s_id = session.getAttribute("b_s_id").toString();
        //写死了,也可以通过用户名作为key去存储。但是要保证用户名是唯一的。
//        String b_s_id = "1";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        //通过model传入到thymeleaf页面处理。
        model.addAttribute("cart_item_list", buyerCart.getStore_itemList());
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        //System.out.println(buyerCart.getStore_itemList());
//        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
//        String goods_photo_path=s_goods_info.getAGoods_Photo_Path(goods_id);
//        model.addAttribute("goods_photo_path",goods_photo_path);
        return "show_cart";
    }

    @RequestMapping(value = "get_cart_item_list", method = RequestMethod.GET)
    @ResponseBody
    public List<Store_Item> getCartItemList(HttpServletRequest request){
        //根据传入的用户id.不考虑jd模式的cookie。从redis获取购物车信息，并显示出来。
        //1:获取用户的id或者name。。可以从session里面获取。
        HttpSession session = request.getSession();
        String b_s_id = session.getAttribute("b_s_id").toString();
        //写死了,也可以通过用户名作为key去存储。但是要保证用户名是唯一的。
//        String b_s_id = "1";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        //System.out.println(buyerCart.getStore_itemList());
        return buyerCart.getStore_itemList();
    }

    @RequestMapping(value = "add_cart", method = RequestMethod.GET)
    public String addCart(HttpServletRequest request, RedirectAttributes attributes){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        int goods_sum = Integer.parseInt(request.getParameter("goods_sum"));
        float goods_money = Float.parseFloat(request.getParameter("goods_money"));
        //根据id，获取要插入到购物车的对象.这里不处理，全部交给购物车这个类去处理。控制器只负责初始化和调度
        //Goods_Info goods_info = s_goods_info.getAGoods_Info(goods_id);
        System.out.println("---------add_cart");
        System.out.println(goods_id);
        System.out.println(store_id);
        System.out.println(goods_sum);
        System.out.println(goods_money);
        //1:获取用户的id或者name。。可以从session里面获取。
        HttpSession session = request.getSession();
        String b_s_id = session.getAttribute("b_s_id").toString();
        //写死了,也可以通过用户名作为key去存储。但是要保证用户名是唯一的。
//        String b_s_id = "1";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        //添加商品进入购物车.
        String goods_photo_path=s_goods_info.getAGoods_Photo_Path(goods_id);
        buyerCart.addStore_Item(goods_id, store_id, goods_sum, goods_money,goods_photo_path);
        //要保存回redis.其实就是序列化
        String fromObject = JSON.toJSONString(buyerCart);
        redis.set(b_s_id, fromObject.toString());
        //保存成功之后，跳转到购物车。
        attributes.addAttribute("b_s_id", b_s_id);
//        attributes.addAttribute("goods_id", goods_id);

        return "redirect:show_cart";
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

    @RequestMapping(value = "add_cart_ajax", method = RequestMethod.POST)
    @ResponseBody
    public String addCartAjax(HttpServletRequest request){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        int goods_sum = Integer.parseInt(request.getParameter("goods_sum"));
        float goods_money = Float.parseFloat(request.getParameter("goods_money"));
        //根据id，获取要插入到购物车的对象.这里不处理，全部交给购物车这个类去处理。控制器只负责初始化和调度
        //Goods_Info goods_info = s_goods_info.getAGoods_Info(goods_id);

        //1:获取用户的id或者name。。可以从session里面获取。
        HttpSession session = request.getSession();
        String b_s_id = session.getAttribute("b_s_id").toString();
        //写死了,也可以通过用户名作为key去存储。但是要保证用户名是唯一的。
//        String b_s_id = "1";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        String goods_photo_path=s_goods_info.getAGoods_Photo_Path(goods_id);
        //添加商品进入购物车.
        buyerCart.addStore_Item(goods_id, store_id, goods_sum, goods_money,goods_photo_path);
        //要保存回redis.其实就是序列化
        String fromObject = JSON.toJSONString(buyerCart);
        redis.set(b_s_id, fromObject.toString());

        return "1";
    }

    @RequestMapping(value = "update_cart", method = RequestMethod.GET)
    //@ResponseBody
    public String updateCart(HttpServletRequest request,RedirectAttributes attributes){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        int goods_sum = Integer.parseInt(request.getParameter("goods_sum"));
        float goods_money = Float.parseFloat(request.getParameter("goods_money"));
        System.out.println("---------update");
        System.out.println(goods_id);
        System.out.println(store_id);
        System.out.println(goods_sum);
        System.out.println(goods_money);
        HttpSession session = request.getSession();

        String b_s_id = session.getAttribute("b_s_id").toString();
        //写死了,也可以通过用户名作为key去存储。但是要保证用户名是唯一的。
//        String b_s_id = "1";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        //修改购物车
        // 里面的商品的数量和金额.
        buyerCart.updateStore_Item(goods_id, store_id, goods_sum, goods_money);
        //要保存回redis.其实就是序列化
        String fromObject = JSON.toJSONString(buyerCart);
        redis.set(b_s_id, fromObject.toString());
        attributes.addAttribute("b_s_id", b_s_id);
        return "redirect:show_cart";
        //return "1";
    }

    @RequestMapping(value = "delete_goods_in_cart", method = RequestMethod.GET)
    //@ResponseBody
    public String deleteGoodsInCart(HttpServletRequest request,RedirectAttributes attributes){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        System.out.println("---------delete");
        System.out.println(goods_id);
        System.out.println(store_id);
        HttpSession session = request.getSession();
        String b_s_id = session.getAttribute("b_s_id").toString();
        //写死了,也可以通过用户名作为key去存储。但是要保证用户名是唯一的。
//        String b_s_id = "1";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        //删除只需要2个参数
        buyerCart.deleteStore_Item(goods_id, store_id);
        //要保存回redis.其实就是序列化
        String fromObject = JSON.toJSONString(buyerCart);
        redis.set(b_s_id, fromObject.toString());
        attributes.addAttribute("b_s_id", b_s_id);
        return "redirect:show_cart";
        //return "1";
    }

}
