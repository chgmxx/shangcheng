package com.gt.mall.bean.wx.shop;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 门店图片
 */
@Getter
@Setter
public class ShopPhoto implements Serializable {

    private static final long serialVersionUID = 2791260615176145545L;

    private Integer id;

    private Integer publicId;

    private Integer shopId;

    private String localAddress;

    private String wxAddress;

    private String createtime;

    private String modifyTime;

    private Integer busId;

}