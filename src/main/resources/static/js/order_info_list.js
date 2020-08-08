//要根据goods_id获取商品名称、商品单价、商品图片路径，商品属性，根据店铺id获取店铺名称
$(document).ready(function (){
    var goods_id = [];
    var order_id = [];
    var img = [];
    var img_path=[];
    var goods_namelist=[];
    var goods_name=[];
    var store_name=[];
    var store_id=[];
    var store_namelist=[];
    var goods_price=[];
    var goods_pricelist=[];
    var store=[];
    var storelist=[];
    $(".goods_id").each(function(){
        goods_id.push($(this).val());
    });
    $(".order_id").each(function(){
        order_id.push($(this).val());
    });
    $(".store_id").each(function(){
        store_id.push($(this).val());
    });

    $(".img").each(function(){
        img.push($(this));
    });
    $(".goods_name").each(function(){
        goods_name.push($(this));
    });
    $(".store_name").each(function(){
        store_name.push($(this));
    });
    $(".goods_price").each(function(){
        goods_price.push($(this));
    });
    $(".store").each(function(){
        store.push($(this));
    });
    for (var i=0;i<img.length;i++) {
        $.ajax({
            async: false,
            type : "get",
            url : "http://127.0.0.1:8080/get_goods_photo_path",
            data : {"goods_id" : goods_id[i]},
            success : function (data) {
                img_path.push(data);
            }
        });

        $.ajax({
            async: false,
            type : "get",
            url : "http://127.0.0.1:8080/get_goods_name",
            data : {"goods_id" : goods_id[i]},
            success : function (data) {
                goods_namelist.push(data);
            }
        });
        $.ajax({
            async: false,
            type : "get",
            url : "http://127.0.0.1:8080/get_store_name",
            data : {"store_id" : store_id[i]},
            success : function (data) {
                store_namelist.push(data);
            }
        });
        $.ajax({
            async: false,
            type : "get",
            url : "http://127.0.0.1:8080/get_goods_price",
            data : {"goods_id" : goods_id[i]},
            success : function (data) {
                goods_pricelist.push(data);
            }
        });


        img[i].attr("src",img_path[i])
        goods_name[i].text(goods_namelist[i])
        store_name[i].text(store_namelist[i])
        goods_price[i].text('￥'+goods_pricelist[i])
        store[i].attr('href',"show_store_goods?store_id="+store_id[i]+"&start=");
    }

    $(".goods").click(function (){
        var b_s_id = $(".b_s_id").val()
        location.href = "get_order_list_by_b_s_id?b_s_id=" + b_s_id+"&start=&query_value=";
    });

    var pay=[];
    var deletelist=[];
    var cancel=[];
    $(".pay").each(function(){
        pay.push($(this));
    });
    $(".delete").each(function () {
        deletelist.push($(this));
    });
    $(".cancel-order").each(function () {
        cancel.push($(this));
    })
        // update_order_status
        var b_s_id = $(".b_s_id").val()
        $.ajax({
            async: false,
            type : "get",
            url : "http://127.0.0.1:8080/getOrder_IdList",
            data : {"b_s_id" : b_s_id},
            success : function (data) {
                for (j in data){
                    pay[j].attr("href","update_order_status?order_id="+data[j])
                    deletelist[j].attr("href","delete_order?order_id="+data[j])
                    cancel[j].attr("href","cancelOrder?order_id="+data[j])
                }
            }
        })



    // 分页
    var b_s_id = $(".b_s_id").val()
    var query=$(".search-content").val();
    var start=$(".start").val();
    var totalPage=$(".totalPage").val();
    $(".order-search").click(function () {
        var query=$(".search-content").val();
        if (query===""){
            alert("请输入关键字后再进行搜索哦~")
        }else {
            location.href="get_order_list_by_b_s_id?b_s_id="+b_s_id+"&query_value="
                +query+"&start="+start;
        }

    })

    $(".first-page").click(function () {
        location.href="get_order_list_by_b_s_id?b_s_id="+b_s_id+"&query_value="
            +query+"&start=1";
    })

    $(".next-page").click(function () {
        start++;
        location.href="get_order_list_by_b_s_id?b_s_id="+b_s_id+"&query_value="
            +query+"&start="+start;

    })

    $(".pre-page").click(function () {
        start--;
        location.href="get_order_list_by_b_s_id?b_s_id="+b_s_id+"&query_value="
            +query+"&start="+start;
    })

    $(".last-page").click(function () {
        start=totalPage;
        location.href="get_order_list_by_b_s_id?b_s_id="+b_s_id+"&query_value="
            +query+"&start="+start;
    })
})