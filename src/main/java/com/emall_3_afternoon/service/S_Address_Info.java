package com.emall_3_afternoon.service;

import com.emall_3_afternoon.entity.Address_Info;
import com.emall_3_afternoon.mapper.Address_InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class S_Address_Info {
    @Autowired
    private Address_InfoMapper address_infoMapper;
    public List<Address_Info> getAddressList(int b_s_id){
        return address_infoMapper.getAddressList(b_s_id);
    }

    public Address_Info getDefaultAddress(int b_s_id){
        return address_infoMapper.getDefaultAddress(b_s_id);
    }
    //下面的@Transactional是一个spring级别的事务，保证要么都成功，要么都失败。
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public  int insertAddress(Address_Info address_info){
        //1:判断是否超过10条。超过直接返回-1。前端可以根据返回值-1显示插不下了。
        //如果成功则返回>0的数。
        //调用一个判断f返回条数的函数
        if (address_infoMapper.getAddressCount(address_info.getB_s_id()) >= 10)
            return -1;
        int flag = 0;
        if (address_info.getDefault_status() == 1) //说明需要先update在insert
            flag = address_infoMapper.updateDefaultStatus(address_info.getB_s_id());
        flag = address_infoMapper.insertAddress(address_info);
        return  flag;
    }

    /*
     * 删除的逻辑 1：传入删除的address_id，买家的b_s_id，判断要删除的是不是>1，=1 直接删除掉。
     * > 1 要考虑删除的default_status 是不是1，如果不是，直接删除。
     * 如果1，删除调后，默认设置成第一条default_status= 1
     * */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int deleteAddress(int address_id, int b_s_id, int default_status){
        int flag = address_infoMapper.deleteAddress(address_id);
        //判断要删除的买家地址数是不是>1，=1 直接删除掉，先删除在update。
        if (address_infoMapper.getAddressCount(b_s_id) > 1){
            //判断default_status是不是1
            if (default_status == 1){
                //找出第一条记录的数据对他进行update。
                flag = address_infoMapper.updateTopOneAddress(b_s_id);
            }
        }
        return  flag;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int setDefaultStatus(int address_id, int b_s_id){
        int flag = 0;
        //先设置为0，把以前的数据
        flag = address_infoMapper.updateDefaultStatus(b_s_id);
        //update需要修改的数据为1
        flag = address_infoMapper.setDefaultStatus(address_id);

        return  flag;
    }

    public Address_Info getAddress(int address_id){
        return  address_infoMapper.getAddress(address_id);
    }
    /*
     * 编辑的逻辑：default_status是不是影响到别的记录*/
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int editAddress(Address_Info address_info){
        //1:
        int flag = 0;

        return flag;
    }
}
