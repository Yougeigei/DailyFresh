package cn.hnist.service;

import cn.hnist.pojo.Address;

public interface AddressService {

    /**
     * 添加用户地址信息
     */
    boolean addAddress(Address addr);

    /**
     * 根据用户id获取用户默认收货地址
     */
    Address findDefaultAddress(Integer id);
}
