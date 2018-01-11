package com.gt.mall.param.phone.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@Data
public class PhoneOrderUserDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;

    /**
     * 公众号id
     */
    private Integer publicId;

    /**
     * 商家id
     */
    private Integer busId;

    /**
     * 会员类型
     */
    private Integer memberCtId;

}
