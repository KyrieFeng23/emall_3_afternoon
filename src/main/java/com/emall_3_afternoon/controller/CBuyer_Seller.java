package com.emall_3_afternoon.controller;

import com.emall_3_afternoon.entity.Buyer_Seller_Info;
import com.emall_3_afternoon.entity.Goods_Info;
import com.emall_3_afternoon.entity.Goods_Photo_Path_Info;
import com.emall_3_afternoon.entity.Store_Info;
import com.emall_3_afternoon.service.SBuyer_Seller;
import com.emall_3_afternoon.service.S_Goods_Info;
import com.emall_3_afternoon.service.S_Store_Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class CBuyer_Seller {
    @Autowired
    private SBuyer_Seller s_buyer_seller;
    @Autowired
    private S_Goods_Info s_goods_info;
    @Autowired
    private S_Store_Info s_store_info;

    @RequestMapping("get_buyer_list")
    @ResponseBody
    public List<Buyer_Seller_Info> getBuyerList(HttpServletRequest request){
        int status=Integer.parseInt(request.getParameter("status"));
        return s_buyer_seller.getBuyerList(status);
    }

    //跳转到登录界面
    @RequestMapping(value = "to_login", method = RequestMethod.GET)
    public String toLogin() {
        System.out.println("login Get方法");
        return "login";
    }

    //登录，先写了通过nickname登录
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public String login(String username, String password, Model model, HttpSession session){
        Buyer_Seller_Info buyer_seller_info=s_buyer_seller.find_buyer_seller_by_name(username,password);
        if (buyer_seller_info != null) {
            //将对象添加到session
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date_time = df.format(new Date());//调用时间函数生成
            s_buyer_seller.update_last_login_time(date_time,buyer_seller_info.getB_s_id());
            session.setAttribute("BUYER_SELLER_INFO_SESSION", buyer_seller_info);
            session.setAttribute("b_s_id",buyer_seller_info.getB_s_id());
            //跳转到主界面
            System.out.println("user_login success");
            return "redirect:index";
        }
        model.addAttribute("msg", "用户名或密码错误，请重新输入！");
        //返回到登录界面
        return "login";
    }

    //跳转到首页，暂时先写到这个c层
    @RequestMapping(value = "index")
    public String index(Model model,HttpSession session){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        List<Goods_Info> goods_infoList=s_goods_info.getAllUpGoodsList();
        Goods_Info goods_info=new Goods_Info();
        List<Goods_Photo_Path_Info> add=goods_info.getGoods_photo_path_infoList();
        for (Goods_Info goodsInfo : goods_infoList) {
            List<Goods_Photo_Path_Info> flag = s_goods_info.getAllGoods_Photo_Path(goodsInfo.getGoods_id());
            goodsInfo.setGoods_photo_path_infoList(flag);
        }
        model.addAttribute("goods_list",goods_infoList);
        return "index";
    }

    //跳转到卖家中心，暂时先写到这个c层
    @RequestMapping(value = "index_seller")
    public String Index_Seller(Model model,HttpSession session){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        int b_s_id=buyer_seller_info.getB_s_id();
        List<Store_Info> store_infoList=s_store_info.getSeller_Store_Info(b_s_id);
        model.addAttribute("Store_Info_List",store_infoList);
        return "index_seller";
    }

    //退出登录
    @RequestMapping(value = "logout")
    public String logout(HttpSession session) {
        // 清除Session
        session.invalidate();
        System.out.println("logout退出登录");
        // 重定向到登录页面的跳转方法
        return "redirect:to_login";
    }

    //跳转到个人信息页面
    @RequestMapping(value = "get_personal_info")
    public String getPersonalInfo(Model model,HttpSession session){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "personal_info";
    }

    //跳转到我的资料页面
    @RequestMapping(value = "to_my_info")
    public String ToMyInfo(Model model,HttpSession session ){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "my_info";
    }

    @RequestMapping(value = "update_b_s_info",method = RequestMethod.GET)
    public String UpdateB_s_info(HttpServletRequest request,HttpSession session,Model model){
        String nickname=request.getParameter("nickname");
        String pwd=request.getParameter("pwd");
        String b_s_name=request.getParameter("b_s_name");
        int sex=Integer.parseInt(request.getParameter("sex"));
        String telephone=request.getParameter("telephone");
        String email=request.getParameter("email");
        String home=request.getParameter("home");
        String home_detail=request.getParameter("home_detail");
        int b_s_id=Integer.parseInt(request.getParameter("b_s_id"));
        int flag=s_buyer_seller.update_b_s_info(nickname,pwd,b_s_name,sex,telephone,email,home,home_detail,b_s_id);
        if (flag>0){
            Buyer_Seller_Info buyer_seller_info=s_buyer_seller.find_buyer_seller_by_b_s_id(b_s_id);
            if (buyer_seller_info != null) {
                session.setAttribute("BUYER_SELLER_INFO_SESSION", buyer_seller_info);
                model.addAttribute("B_S_INFO",buyer_seller_info);
            }
        }

        return "my_info";
    }
}
