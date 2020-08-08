package com.emall_3_afternoon.controller;

import com.emall_3_afternoon.entity.Buyer_Seller_Info;
import com.emall_3_afternoon.entity.Field_Dictionary;
import com.emall_3_afternoon.entity.Goods_Property_Info;
import com.emall_3_afternoon.service.S_Field_Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class C_Field_Dictionary {
    @Autowired
    private S_Field_Dictionary s_field_dictionary;

    @RequestMapping("get_field_list")
    @ResponseBody
    public List<Field_Dictionary> getFieldList(HttpServletRequest request){
        String dictionary_code = request.getParameter("dictionary_code");
        System.out.println(dictionary_code);
        List<Field_Dictionary> fieldDictionaryList = s_field_dictionary.getFieldList(dictionary_code);

        return fieldDictionaryList;
    }

    @RequestMapping("select_field")
    public String selectField(HttpServletRequest request, HttpSession session, Model model){
        //其他需要加的业务在此添加
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "select_field";
    }



    //写的不好。应该放在字典类里面。同学们切记。
    @RequestMapping("get_field_value")
    @ResponseBody
    public String getFieldValue(HttpServletRequest request){
        String dictionary_code = request.getParameter("dictionary_code");
        return  s_field_dictionary.getField_Value(dictionary_code);
    }

    @RequestMapping("get_property_value")
    @ResponseBody
    public List<String> getPropertyValue(HttpServletRequest request){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        return  s_field_dictionary.getProperty_Value(goods_id);
    }


    @RequestMapping("get_goods_property_list")
    @ResponseBody
    public List<Goods_Property_Info> getGoodsProperty(HttpServletRequest request){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        return  s_field_dictionary.getGoodsProperty(goods_id);
    }
}
