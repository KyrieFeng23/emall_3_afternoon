package com.emall_3_afternoon.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.emall_3_afternoon.entity.*;
import com.emall_3_afternoon.service.BuyerCart;
import com.emall_3_afternoon.service.S_Field_Dictionary;
import com.emall_3_afternoon.service.S_Goods_Info;
import com.emall_3_afternoon.util.RedisUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Controller
public class C_Goods_Info {
    @Autowired
    private RedisUtil redis;//用来从redis里面获取value或者设置value。
    @Autowired
    private S_Field_Dictionary s_field_dictionary;
    @Autowired
    private S_Goods_Info s_goods_info;

//    @RequestMapping("show_goods_list")
//    public String showGoodsList(HttpServletRequest request, Model model){
//        int store_id = Integer.parseInt(request.getParameter("store_id"));
//        List<Goods_Info> goods_infoList = null;
//        //传入分页的信息
//        int page_size = 10;
//        int start = 1;
//        String field = "";
//        String field_value = "";
//        goods_infoList = s_goods_info.getGoodsList(field, field_value, start, page_size);
//        model.addAttribute("store_id",store_id);
//        model.addAttribute("goods_info_list",goods_infoList);
//        return "show_goods_list";
//    }



    @RequestMapping(value = "show_edit_goods_info", method = RequestMethod.GET)
    public String showEditGoodsInfo(HttpServletRequest request, Model model,HttpSession session){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        Goods_Info goods_info = s_goods_info.getA_EditGoods_Info(goods_id);
        String field_value = s_field_dictionary.getField_Value(goods_info.getDictionary_code());
        model.addAttribute("field_value", field_value);
        model.addAttribute("goods_info", goods_info);
        model.addAttribute("dictionary_code", goods_info.getDictionary_code());
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "show_edit_goods_info";//template里面对应肯定有这个名字的页面
    }


    //发布宝贝第二步，添加具体数据
    @RequestMapping(value = "show_input_goods",method = RequestMethod.GET)
    public String showInputGoods_Info(HttpServletRequest request, Model model,HttpSession session){
        int store_id=Integer.parseInt(request.getParameter("store_id"));
        model.addAttribute("store_id",store_id);
        String dictionary_code = request.getParameter("three");
        //System.out.println(dictionary_code);
        model.addAttribute("dictionary_code", dictionary_code);
        String field_value = s_field_dictionary.getField_Value(dictionary_code);
        model.addAttribute("field_value", field_value);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "input_goods";
    }




    @RequestMapping(value = "show_a_goods_info_detail", method = RequestMethod.GET)
    public String showAGoods_InfoDetail(HttpServletRequest request, Model model, HttpSession session){
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        Goods_Info goods_info = s_goods_info.getAGoods_Info(goods_id);
        int store_id = s_goods_info.getStore_id(goods_id);
        Store_Info store_info=s_goods_info.getAStore_Info(store_id);
        model.addAttribute("goods_info", goods_info);
        model.addAttribute("store_info",store_info);
        List<Goods_Photo_Path_Info> goods_photo_path_list=s_goods_info.getAllGoods_Photo_Path(goods_id);
        model.addAttribute("Photo_Path_List",goods_photo_path_list);
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        return "show_a_goods_info_detail";
    }

    @RequestMapping(value = "image_upload", method = RequestMethod.POST)
    public @ResponseBody
    String upload(MultipartFile file, HttpServletRequest request){
        try {
            //String pathName = System.getProperty("user.dir")
            //        + "\\src\\main\\resources\\static\\upload\\";
            String pathName = request.getServletContext().getRealPath("/upload/");
            pathName += file.getOriginalFilename();
            //System.out.println(pathName);
            FileUtils.writeByteArrayToFile(new File(pathName), file.getBytes());
            return  "/upload/" + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "add_to_collect")
    public String AddToCollect(HttpServletRequest request,Model model,HttpSession session){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        int goods_length=Integer.parseInt(request.getParameter("goods_length"));
        List<Goods_Info> goods_infoList=new ArrayList<>();
        String b_s_id=buyer_seller_info.getB_s_id()+"";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        for (int i=0;i<goods_length;i++){
            int goods_id=Integer.parseInt(request.getParameter("goods_id"+i));
            goods_infoList.add(s_goods_info.getAGoods_Info(goods_id));
            //通过goods_id循环删除购物车里面的数据
            int store_id = s_goods_info.getStore_id(goods_id);
            //删除只需要2个参数
            buyerCart.deleteStore_Item(goods_id, store_id);
        }
        s_goods_info.addtocollect(goods_infoList,buyer_seller_info.getB_s_id());

        //序列化购物车对象到redis,要保存回redis.其实就是序列化
        String fromObject = JSON.toJSONString(buyerCart);
        redis.set(b_s_id, fromObject.toString());

        return "redirect:to_collect";
    }

    @RequestMapping(value = "clean_goods")
    public String CleanGoods(HttpServletRequest request,Model model,HttpSession session,RedirectAttributes attributes){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        int goods_length=Integer.parseInt(request.getParameter("goods_length"));
        String b_s_id=buyer_seller_info.getB_s_id()+"";
        BuyerCart buyerCart = this.getABuyerCart(b_s_id);
        int flag=0;
        for (int i=0;i<goods_length;i++){
            int goods_id=Integer.parseInt(request.getParameter("goods_id"+i));
            Goods_Info goods_info=s_goods_info.getAGoods_Info(goods_id);
            //通过goods_id循环删除购物车里面的数据
            if (goods_info.getGoods_status()==1) {
                flag++;
                int store_id = s_goods_info.getStore_id(goods_id);
                //删除只需要2个参数
                buyerCart.deleteStore_Item(goods_id, store_id);
            }
        }
        if (flag!=0) {
            //序列化购物车对象到redis,要保存回redis.其实就是序列化
            String fromObject = JSON.toJSONString(buyerCart);
            redis.set(b_s_id, fromObject.toString());
        }
        attributes.addAttribute("b_s_id", b_s_id);
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


    //收藏
    @RequestMapping(value = "to_collect")
    public String ToCollect(HttpSession session,HttpServletRequest request,Model model){
        Buyer_Seller_Info buyer_seller_info=(Buyer_Seller_Info)session.getAttribute("BUYER_SELLER_INFO_SESSION");
        model.addAttribute("B_S_INFO",buyer_seller_info);
        List<Goods_Info> goods_infoList=new ArrayList<>();
        int b_s_id=buyer_seller_info.getB_s_id();
        goods_infoList=s_goods_info.getCollectGoods(b_s_id);
        for (Goods_Info goodsInfo : goods_infoList) {
            List<Goods_Photo_Path_Info> flag = s_goods_info.getAllGoods_Photo_Path(goodsInfo.getGoods_id());
            goodsInfo.setGoods_photo_path_infoList(flag);
        }
        model.addAttribute("goods_infoList",goods_infoList);
        int count=s_goods_info.countcollect(b_s_id);
        model.addAttribute("count",count);
        return "goods_collect";
    }


    //上架
    @RequestMapping(value = "up_status", method = RequestMethod.GET)
    public String upStatus(HttpServletRequest request, RedirectAttributes attributes){
        int flag = 0;
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        int goods_status = Integer.parseInt(request.getParameter("goods_status"));
        flag = s_goods_info.upStatus(goods_id, goods_status);
        if (flag == 0)
            return "error";
        else{
            int store_id = Integer.parseInt(request.getParameter("store_id"));
            attributes.addAttribute("store_id", store_id);
            return "redirect:show_goods_list";
        }
    }

    //下架
    @RequestMapping(value = "down_status", method = RequestMethod.GET)
    public String downStatus(HttpServletRequest request, RedirectAttributes attributes){
        int flag = 0;
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        int goods_status = Integer.parseInt(request.getParameter("goods_status"));
        flag = s_goods_info.downStatus(goods_id, goods_status);
        if (flag == 0)
            return "error";
        else{
            int store_id = Integer.parseInt(request.getParameter("store_id"));
            attributes.addAttribute("store_id", store_id);
            return "redirect:show_goods_list";
        }
    }

    //删除
    @RequestMapping(value = "delete_goods")
    public String deleteGoods(HttpServletRequest request,RedirectAttributes attributes){
        int flag = 0;
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        flag = s_goods_info.deleteGoods_Info(goods_id);
        if (flag == 0)
            return "error";
        else{
            int store_id = Integer.parseInt(request.getParameter("store_id"));
            attributes.addAttribute("store_id", store_id);
            return "redirect:show_goods_list";
        }
    }

    @RequestMapping(value = "insert_goods_info", method = RequestMethod.POST)
    public String insertGoods_Info(HttpServletRequest request, Goods_Info goods_info, RedirectAttributes attributes){
        goods_info.setGoods_actual_price(goods_info.getGoods_price());
        int store_id=Integer.parseInt(request.getParameter("store_id"));
        goods_info.setStore_id(store_id);
        //init 图片路径,写的不好的地方，不想改了。
        List<Goods_Photo_Path_Info> goods_photo_path_infoList = new ArrayList<Goods_Photo_Path_Info>();
        String image_one = request.getParameter("image_one");
        if (image_one.equals(null) || image_one.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_one);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }
        String image_two = request.getParameter("image_two");
        if (image_two.equals(null) || image_two.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_two);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }
        String image_three = request.getParameter("image_three");
        if (image_three.equals(null) || image_three.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_three);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }
        String image_four = request.getParameter("image_four");
        if (image_four.equals(null) || image_four.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_four);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }
        String image_five = request.getParameter("image_five");
        //request.getSession().getAttribute('a') == null; 如果改为equals则得不到想要的答案。
        //而image_five == null 则得不到想要的答案。和String对象比较有关系。
        if (image_five.equals(null) || image_five.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_five);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }
        //init 对应属性 图像应该参考属性来写。这样废码就少了。
        int field_value_length = Integer.parseInt(request.getParameter("field_value_length"));
        List<Goods_Property_Info> goods_property_infoList = new ArrayList<Goods_Property_Info>();
        for (int i = 0; i < field_value_length; i++){
            String field_name = request.getParameter("hidden_field" + i);
            String field_value = request.getParameter("field" + i);
            Goods_Property_Info goods_property_info = new Goods_Property_Info();
            goods_property_info.setProperty_name(field_name);
            goods_property_info.setProperty_value(field_value);
            goods_property_infoList.add(goods_property_info);
        }
        //test
        /*for (Goods_Property_Info goods_property_info : goods_property_infoList) {
            System.out.println(goods_property_info.getProperty_name() + "---" + goods_property_info.getProperty_value());
        }
        for (Goods_Photo_Path_Info goods_photo_path_info : goods_photo_path_infoList){
            System.out.println(goods_photo_path_info.getPath_name());
        }*/
        int flag = 0;
        flag = s_goods_info.insertGoods_Info(goods_info, goods_photo_path_infoList, goods_property_infoList);
        if (flag == 0)
            return "error";
        else{
            attributes.addAttribute("store_id", store_id);
            return "redirect:show_goods_list";
        }
    }

    @RequestMapping(value = "get_goods_photo_path",method = RequestMethod.GET)
    @ResponseBody
    public String getGoodsPhotoPath(HttpServletRequest request,Model model) {
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        String goods_photo_path=s_goods_info.getAGoods_Photo_Path(goods_id);
//        model.addAttribute("goods_photo_path",goods_photo_path);
        return goods_photo_path;
    }

    @RequestMapping(value = "get_goods_name",method = RequestMethod.GET)
    @ResponseBody
    public String getGoodsName(HttpServletRequest request) {
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        String goods_name=s_goods_info.getGoodsName(goods_id);
        return goods_name;
    }

    @RequestMapping(value = "get_store_id",method = RequestMethod.GET)
    @ResponseBody
    public int getStoreId(HttpServletRequest request) {
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        int store_id=s_goods_info.getStore_id(goods_id);
        return store_id;
    }

    @RequestMapping(value = "get_store_name",method = RequestMethod.GET)
    @ResponseBody
    public String getStoreName(HttpServletRequest request) {
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        String store_name=s_goods_info.getStoreName(store_id);
        return store_name;
    }

    @RequestMapping(value = "get_goods_price",method = RequestMethod.GET)
    @ResponseBody
    public Float getGoodsPrice(HttpServletRequest request) {
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        float goods_price=s_goods_info.getGoodsPrice(goods_id);
        return goods_price;
    }

    @RequestMapping(value = "get_goods_property",method = RequestMethod.GET)
    @ResponseBody
    public String  getGoodsProperty(HttpServletRequest request) {
        int order_id = Integer.parseInt(request.getParameter("order_id"));
        String  goods_property=s_goods_info.get_goods_property(order_id);
        return goods_property;
    }

    @RequestMapping(value = "get_all_goods_photo_path",method = RequestMethod.GET)
    @ResponseBody
    public List<Goods_Photo_Path_Info> getAllGoodsPhotoPath(HttpServletRequest request,Model model) {
        int goods_id = Integer.parseInt(request.getParameter("goods_id"));
        List<Goods_Photo_Path_Info> goods_photo_path_all=s_goods_info.getAllGoods_Photo_Path(goods_id);
        Goods_Info goods_info=new Goods_Info();
        goods_info.setGoods_photo_path_infoList(goods_photo_path_all);
//        model.addAttribute("goods_photo_path",goods_photo_path);
        return goods_photo_path_all;
    }

    @RequestMapping(value = "get_all_up_goods")
    public List<Goods_Info> getAllUpGoodsList(HttpServletRequest request){
        List<Goods_Info> goods_infoList=s_goods_info.getAllUpGoodsList();
        return goods_infoList;
    }

    //简单查询。根据商品名称搜索。可以用在首页的搜索框
    @RequestMapping("get_goods_info_list_bypage")
    public String getGoodsInfoListByPage(HttpServletRequest request, Model model){
        int start = Integer.parseInt(request.getParameter("start"));
        int page_size = Integer.parseInt(request.getParameter("page_size"));
        String query_value = request.getParameter("query_value");
        List<Goods_Info> goods_infoList = s_goods_info.getGoodsInfoListByPage(start, page_size, query_value);
        model.addAttribute("goods_info_list", goods_infoList);

        return "get_goods_info_list_bypage";
    }


    //店铺内根据商品名称搜索商品
    @RequestMapping("get_goods_info_list_by_store_search")
    public String getGoodsInfoListByStoreSearch(HttpServletRequest request, Model model,HttpSession session){
        int store_id = Integer.parseInt(request.getParameter("store_id"));
        model.addAttribute("store_id", store_id);
        Store_Info store_info=s_goods_info.getAStore_Info(store_id);
        model.addAttribute("store_info",store_info);
        int start = 1;
        if (!request.getParameter("start").equals("")){
            System.out.println("!=null");
            start=Integer.parseInt(request.getParameter("start"));
        }
        //个人觉得还是把每页显示的个数写死在控制层比较好，根据方法来决定页面展示的多少，而且只需要改一个位置
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


    //搜淘宝-全局搜索
    @RequestMapping("get_goods_info_list_by_taobao_search")
    public String getGoodsInfoListByTaoBaoSearch(HttpServletRequest request, Model model,HttpSession session){
//        int store_id = Integer.parseInt(request.getParameter("store_id"));
//        model.addAttribute("store_id", store_id);
//        Store_Info store_info=s_goods_info.getAStore_Info(store_id);
//        model.addAttribute("store_info",store_info);
        int start = 1;
        if (!request.getParameter("start").equals("")){
            System.out.println("!=null");
            start=Integer.parseInt(request.getParameter("start"));
        }
        //个人觉得还是把每页显示的个数写死在控制层比较好，根据方法来决定页面展示的多少，而且只需要改一个位置
//        int page_size = Integer.parseInt(request.getParameter("page_size"));
        int page_size = 10;
        String query_value = request.getParameter("query_value");
        List<Goods_Info> goods_infoList = s_goods_info.getGoodsInfoListByTaoBaoSearch(start, page_size, query_value);
        int count=s_goods_info.getCountGoodsInfoListByTaoBaoSearch(start, page_size, query_value);
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
        return "show_taobao_search";
    }


    @RequestMapping(value = "update_goods", method = RequestMethod.POST)
    public String updateGoods(HttpServletRequest request, Goods_Info goods_info, RedirectAttributes attributes){
        //主要是3部分事情。1：insert goods_info表一条记录 2：插入photo_path表多条记录 3：插入property表多条记录
        //对上述对象进行初始化，然后转s_goods_info对应的方法执行。
        //1:goods_info通过Goods_Info goods_info与form表单insert_goods对应的input name进行自动绑定，在手动初始化一部分代码
        goods_info.setGoods_actual_price(goods_info.getGoods_price());

        goods_info.setStore_id(goods_info.getStore_id());

        //2:初始化图片
        List<Goods_Photo_Path_Info> goods_photo_path_infoList = new ArrayList<Goods_Photo_Path_Info>();
        String image_one = request.getParameter("image_one");
        if (image_one.equals(null) || image_one.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_one);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }

        String image_two = request.getParameter("image_two");
        if (image_two.equals(null) || image_two.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_two);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }

        String image_three = request.getParameter("image_three");
        if (image_three.equals(null) || image_three.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_three);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }

        String image_four = request.getParameter("image_four");
        if (image_four.equals(null) || image_four.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_four);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }

        String image_five = request.getParameter("image_five");
        //request.getSession().getAttribute('a') == null; 如果改为equals则得不到想要的答案。
        //而image_five == null 则得不到想要的答案。和String对象比较有关系。
        if (image_five.equals(null) || image_five.trim() != "") {
            Goods_Photo_Path_Info goods_photo_path_info = new Goods_Photo_Path_Info();
            goods_photo_path_info.setPath_name(image_five);
            goods_photo_path_infoList.add(goods_photo_path_info);
        }

        //3:初始化属性
        //init 对应属性 图像应该参考属性来写。这样废码就少了。
        int field_value_length = Integer.parseInt(request.getParameter("field_value_length"));
        List<Goods_Property_Info> goods_property_infoList = new ArrayList<Goods_Property_Info>();
        int i = 0;
        for (i = 0; i < field_value_length; i++){
            String field_name = request.getParameter("hidden_field" + i);
            String field_value = request.getParameter("field" + i);
            Goods_Property_Info goods_property_info = new Goods_Property_Info();
            goods_property_info.setProperty_name(field_name);
            goods_property_info.setProperty_value(field_value);
            goods_property_infoList.add(goods_property_info);
        }
        //test
        /*for (Goods_Property_Info goods_property_info : goods_property_infoList) {
            System.out.println(goods_property_info.getProperty_name() + "---" + goods_property_info.getProperty_value());
        }
        for (Goods_Photo_Path_Info goods_photo_path_info : goods_photo_path_infoList){
            System.out.println(goods_photo_path_info.getPath_name());
        }*/
        int flag = 0;
        flag = s_goods_info.updateGoods_Info(goods_info, goods_photo_path_infoList, goods_property_infoList);
        if (flag == 0)
            return "error";
        else{
            attributes.addAttribute("store_id", 1);
            return "redirect:show_goods_list";//== url://show_goods_list?store_id=1
        }
    }
}
