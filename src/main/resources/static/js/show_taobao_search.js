$(document).ready(function (){
    var goods_id = [];
    var detail = [];
    var store_name = [];
    var store_id=[];
    var store_info=[];
    $(".goods_id").each(function(){
        goods_id.push($(this).val());
    });

    $(".detail").each(function(){
        detail.push($(this));
    });

    $(".store_name").each(function () {
        store_name.push($(this));
    })

    for (var i=0;i<goods_id.length;i++) {
        detail[i].attr("href", "show_a_goods_info_detail?goods_id=" + goods_id[i])
        $.ajax({
            async: false,
            type: "get",
            url: "http://127.0.0.1:8080/get_store_id",
            data: {"goods_id": goods_id[i]},
            success: function (data) {
                store_id.push(data);
            }
        });
        $.ajax({
            async: false,
            type : "get",
            url : "http://127.0.0.1:8080/get_store_name",
            data : {"store_id" : store_id[i]},
            success : function (data) {
                store_info.push(data);
            }
        });
        store_name[i].attr('href',"show_store_goods?store_id="+store_id[i]+"&start=")
        store_name[i].text(store_info[i])
    }

    // var query=$(".search-content").val();
    // var start=$(".start").val();
    // var totalPage=$(".totalPage").val();
    // $(".store").click(function () {
    //     var query=$(".search-content").val();
    //     if (query==""){
    //         alert("请输入关键字后再进行搜索哦~")
    //     }else {
    //         location.href="get_goods_info_list_by_store_search?store_id="+store_id+"&query_value="
    //             +query+"&start="+start+"&page_size=10";
    //     }
    //
    // })

    var query=$(".search-content").val();
    var start=$(".start").val();
    var totalPage=$(".totalPage").val();
    $(".first-page").click(function () {
        location.href="get_goods_info_list_by_taobao_search?query_value="
            +query+"&start=";
    })

    $(".next-page").click(function () {
        start++;
        location.href="get_goods_info_list_by_taobao_search?query_value="
            +query+"&start="+start;

    })

    $(".pre-page").click(function () {
        start--;
        location.href="get_goods_info_list_by_taobao_search?query_value="
            +query+"&start="+start;
    })

    $(".last-page").click(function () {
        start=totalPage;
        location.href="get_goods_info_list_by_taobao_search?query_value="
            +query+"&start="+start;
    })

    $(".search-but").click(function () {
        var query=$(".search-in").val();
        if (query==""){
            alert("请输入关键字后再进行搜索哦~")
        }else {
            location.href="get_goods_info_list_by_taobao_search?query_value="
                +query+"&start=";
        }
    })

})