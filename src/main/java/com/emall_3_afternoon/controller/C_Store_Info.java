package com.emall_3_afternoon.controller;

import com.emall_3_afternoon.entity.Buyer_Seller_Info;
import com.emall_3_afternoon.entity.Goods_Info;
import com.emall_3_afternoon.entity.Goods_Photo_Path_Info;
import com.emall_3_afternoon.entity.Store_Info;
import com.emall_3_afternoon.service.S_Goods_Info;
import com.emall_3_afternoon.service.S_Store_Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class C_Store_Info {
    @Autowired
    private S_Store_Info s_store_info;
    @Autowired
    private S_Goods_Info s_goods_info;

    @RequestMapping(value = "show_goods_list", method = RequestMethod.GET)
    public String showGoodsList(HttpServletRequest request, Model model,HttpSession session){
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        Store_Info store_info=s_goods_info.getAStore_Info(store_id);
        model.addAttribute("store_info", store_info);
        List<Goods_Info> goods_infoList = s_goods_info.getAllGoodsList(store_id);
        model.addAttribute("goods_info_list", goods_infoList);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "show_goods_list";
    }

    //店铺展示及搜索
    @RequestMapping(value = "show_store_goods", method = RequestMethod.GET)
    public String showStoreGoods(HttpServletRequest request, Model model, HttpSession session){
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        model.addAttribute("store_id", store_id);
        Store_Info store_info=s_goods_info.getAStore_Info(store_id);
        model.addAttribute("store_info",store_info);
        int start = 1;
        if (!request.getParameter("start").equals("")){
            System.out.println("!=null");
            start=Integer.parseInt(request.getParameter("start"));
        }
//        int page_size = Integer.parseInt(request.getParameter("page_size"));
        int page_size = 10;
        String query_value = request.getParameter("query_value");
        List<Goods_Info> goods_infoList = s_goods_info.getGoodsInfoListByStoreSearch(start, page_size, query_value,store_id);
        int count=s_goods_info.getCountGoodsInfoListByStoreSearch(start, page_size, query_value,store_id);
        model.addAttribute("goods_info_list", goods_infoList);
        for (Goods_Info goodsInfo : goods_infoList) {
            List<Goods_Photo_Path_Info> flag = s_goods_info.getAllGoods_Photo_Path(goodsInfo.getGoods_id());
            goodsInfo.setGoods_photo_path_infoList(flag);
        }
        model.addAttribute("goods_info", goods_infoList);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        model.addAttribute("query_value",query_value);
        model.addAttribute("start",start);
        model.addAttribute("page_size",page_size);
        model.addAttribute("count",count);
        Integer num = count%page_size==0? count / page_size: count / page_size+1;
        model.addAttribute("totalPage",num.intValue());
        return "show_store_goods";
    }

    @RequestMapping(value = "get_seller_store_id",method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getSeller_Store_id(HttpServletRequest request){
        int b_s_id=Integer.parseInt(request.getParameter("b_s_id"));
        List<Integer> store_infoList=s_store_info.getSeller_Store_id(b_s_id);
        return store_infoList;
    }

    @RequestMapping(value = "get_seller_store_info",method = RequestMethod.GET)
    @ResponseBody
    public List<Store_Info> getSeller_Store_Info(HttpServletRequest request){
        int b_s_id=Integer.parseInt(request.getParameter("b_s_id"));
        List<Store_Info> store_infoList=s_store_info.getSeller_Store_Info(b_s_id);
        return store_infoList;
    }

}
