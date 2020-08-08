package com.emall_3_afternoon.entity;

import java.util.ArrayList;
import java.util.List;

//相当于购物车里面的展示的store这一层。
public class Store_Item {
    private int store_id;
    private String store_name;
    private int link_man_id;
    private List<Goods_Item> goods_itemList= new ArrayList<Goods_Item>();

    public int findGood_Item(int goods_id){
        int flag = -1;
        int i = 0;
        for (Goods_Item item : this.goods_itemList) {
            if (item.getGoods_info().getGoods_id() == goods_id){
                flag = i;
                break;
            }
            i++;
        }
        return flag;
    }
    public void AddGoods_Item(Goods_Item goods_item){
        //Goods_Item goods_item = null;
        boolean flag = false;
        for (Goods_Item item : goods_itemList) {
            if (item.getGoods_info().getGoods_id() == goods_item.getGoods_info().getGoods_id()){
                //说明要添加的商品在购物车里面已经存在了。则应该修改数量和金额。
                item.setAccount(item.getAccount() + 1);
                item.setGoods_money(item.getGoods_money() + goods_item.getGoods_money());
                flag = true;
                break;
            }
        }
        if (flag != true){   //原来商品不存在，直接添加可以采用这一种。
            goods_itemList.add(goods_item);
        }
    }
    public void removeGoods_Item(int index){
        goods_itemList.remove(index);
    }
    public void removeGoods_Item(Goods_Item goods_item){
        goods_itemList.remove(goods_item);
    }
    public void removeGoods_ItemByGoods_id(int goods_id){
        //根据goods_id找到要删除的商品.
        for (Goods_Item goods_item: this.goods_itemList) {
            if (goods_item.getGoods_info().getGoods_id() == goods_id){
                //查找到要删除的商品之后.
                goods_itemList.remove(goods_item);
                break;
            }
        }
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getLink_man_id() {
        return link_man_id;
    }

    public void setLink_man_id(int link_man_id) {
        this.link_man_id = link_man_id;
    }

    public List<Goods_Item> getGoods_itemList() {
        return goods_itemList;
    }

    public void setGoods_itemList(List<Goods_Item> goods_itemList) {
        this.goods_itemList = goods_itemList;
    }
}
