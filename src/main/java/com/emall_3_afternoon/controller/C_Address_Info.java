package com.emall_3_afternoon.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.emall_3_afternoon.entity.Address_Info;
import com.emall_3_afternoon.entity.Buyer_Seller_Info;
import com.emall_3_afternoon.service.S_Address_Info;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
//@RestController
public class C_Address_Info {
    @Autowired
    private S_Address_Info s_address_info; //spring 自动装配一个service的类的对象

    //返回list数据给发送请求的客户端(h5,thymeleaf模板的网页,app--ajax)
    //根据用户（买家）的id
    @RequestMapping("get_address_list")
    public String getAddressList(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        //假定程序能走如这个请求，则session没有过期。如果过期，在过滤器/拦截器中已经判断，并返回到login界面
        int b_s_id = Integer.parseInt(session.getAttribute("b_s_id").toString());
        //从session中获取用户id，根据id去查找地址数据返回,现在没有。则写死先。以后写好拦截器在注释掉
//        int b_s_id = 1;
        List<Address_Info> address_infoList = null;
        address_infoList = s_address_info.getAddressList(b_s_id);
        model.addAttribute("address_list", address_infoList);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "address_info";
    }

    /*添加的业务逻辑
    1：前端输入的合法性校验
    2：进入拦截器判断session的有效性
    3: 进入控制器，接受数据，初始化后转给service层处理。
    4: service层的业务逻辑中：判断记录是否超过10条。
    （邹桥东说要后台判断一下。前端判断优先，后台判断效率低了，
    1:前端可以绕过.2:前端容易写死了。中间层相对灵活，但效率低下。
     先假定用后端验证。属于业务逻辑的一部分。刘子煜说：如果后台超过10条
    提醒删一条的同时要暂时保存当前的数据）--业务过于复杂。给程序员增加负担。好处：客户体验感好。
    5：要判断是否设置成默认：1：不设置。直接做单表插入。
    2:设置了。则应该先修改原来数据库默认记录为不默认。然后再插入新的记录。（需要事务设置）
    6：成功则返回到控制层。控制层调用重定向请求返回页面。
    */
    @RequestMapping("insert_address")
    public String insertAddress(HttpServletRequest request) {
        /*1:可以通过thymeleaf的自动绑定来实现对象输入，效率高，这样传入参数不是reqeust。请同学们解决
         * 2:可以通过ajax + json 在用一个插件把json自动转换成对象。*/
        //老师写个最笨的供同学们参考。
        Address_Info address_info = new Address_Info();
        //通过request初始化
        address_info.setAddress(request.getParameter("address"));
        address_info.setAddress_detail(request.getParameter("address_detail"));
        address_info.setPostcode(request.getParameter("postcode"));
        address_info.setTelephone(request.getParameter("telephone"));
        address_info.setAddresssee(request.getParameter("addresssee"));
        if (request.getParameter("default_status") == null)
            address_info.setDefault_status(0);
        else
            address_info.setDefault_status(1);
        //注释掉。临时写死了。以后有session和拦截器则自动打开
        HttpSession session = request.getSession();
        int b_s_id = Integer.parseInt(session.getAttribute("b_s_id").toString());
//        int b_s_id = Integer.parseInt(request.getSession().getAttribute("b_s_id").toString());
//        int b_s_id = 1;
        address_info.setB_s_id(b_s_id);
        int flag = 0;
        flag = s_address_info.insertAddress(address_info);
        if (flag == 1)
            return "redirect:get_address_list";//这样写没有解决参数传递的问题。请同学们解决。发给我。
        else
            return "error";//目前没有写error请求，真的调用肯定出错
    }

    /*
     * 删除的逻辑 1：传入删除的address_id，买家的b_s_id，判断要删除的是不是>1，=1 直接删除掉。
     * > 1 要考虑删除的default_status 是不是1，如果不是，直接删除。
     * 如果1，删除调后，默认设置成第一条default_status= 1*/
    @RequestMapping("delete_address")
    public String deleteAddress(HttpServletRequest request) {
        int address_id = Integer.parseInt(request.getParameter("address_id"));
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        int default_status = Integer.parseInt(request.getParameter("default_status"));
        int flag = s_address_info.deleteAddress(address_id, b_s_id, default_status);
        if (flag == 1)
            return "redirect:get_address_list";
        else
            return "error";
    }

    /*设置为默认的业务逻辑：简单做法，把原来的找出来设置为0，把新的设置为1*/
    @RequestMapping("set_default_status")
    public String setDefaultStatus(HttpServletRequest request) {
        int address_id = Integer.parseInt(request.getParameter("address_id"));
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        int flag = s_address_info.setDefaultStatus(address_id, b_s_id);
        if (flag == 1)
            return "redirect:get_address_list";
        else
            return "error";
    }

    @RequestMapping("set_default_status_in_order")
    public String setDefaultStatusInOrder(HttpServletRequest request) {
        int address_id = Integer.parseInt(request.getParameter("address_id"));
        int b_s_id = Integer.parseInt(request.getParameter("b_s_id"));
        int flag = s_address_info.setDefaultStatus(address_id, b_s_id);
        if (flag == 1)
            return "redirect:show_confirm_order_by_goods";
        else
            return "error";
    }

    /*
     * 修改的逻辑分2部分，也就是2个请求，第一个，把数据取出来显示在页面
     * 第二个把修改的数据传回控制器。update*/
    //1:显示要修改的数据.可以通过thymeleaf，也可以通过ajax。
    @RequestMapping("show_edit_address")
    public String showEditAddress(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        //假定程序能走如这个请求，则session没有过期。如果过期，在过滤器/拦截器中已经判断，并返回到login界面
        int b_s_id = Integer.parseInt(session.getAttribute("b_s_id").toString());
        //从session中获取用户id，根据id去查找地址数据返回,现在没有。则写死先。以后写好拦截器在注释掉
//        int b_s_id = 1;
        List<Address_Info> address_infoList = null;
        address_infoList = s_address_info.getAddressList(b_s_id);
        model.addAttribute("address_list", address_infoList);
        // 把要修改的数据回传到页面显示
        int address_id = Integer.parseInt(request.getParameter("address_id"));
        Address_Info address_info = s_address_info.getAddress(address_id);
        model.addAttribute("address_info", address_info);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "edit_address_info";
    }

    //2:回传数据在中间层进行update
    @RequestMapping("edit_address")//自动绑定到对象
    public String editAddress(Address_Info address_info, HttpServletRequest request) {
        System.out.println(address_info.getTelephone());
        //有一个瑕疵：default_status没有办法自动绑定。
        if (request.getParameter("default_status") == null)
            address_info.setDefault_status(0);
        else
            address_info.setDefault_status(1);
        int flag = s_address_info.editAddress(address_info);
        if (flag == 1)
            return "redirect:get_address_list";
        else
            return "error";
    }

//    @RequestMapping("edit_address_json")
//    @ResponseBody
//    public int editAddrssJson(@RequestBody Address_Info address_info) {
//        //简单对象。json直接传递过来这么做可以的，复杂对象。这样做不来。
//        return 0;
//    }
//
//    @RequestMapping("edit_address_json")
//    @ResponseBody
//    public int editAddressJson(HttpServletRequest request){
//        String sample = request.getParameter("sample");
//        Address_Info address_info = JSON.parseObject(sample, new TypeReference<Address_Info>(){});
//
//        return 0;
//    }
}
