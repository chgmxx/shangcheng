package com.gt.mall.param.phone.freight;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 运费
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneFreightDTO", description = "运费" )
@Data
public class PhoneFreightDTO implements Serializable {

    private static final long serialVersionUID = -1424109806117433065L;

    /**
     * 省份id
     */
    private Integer provinceId;

    /**
     * 粉丝到门店的距离
     */
    private Double juli;

    /**
     * 是否是到店 1到店
     */
    private Integer toshop;

}
