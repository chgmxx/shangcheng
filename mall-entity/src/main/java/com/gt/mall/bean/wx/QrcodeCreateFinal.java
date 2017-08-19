package com.gt.mall.bean.wx;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/***
 * 生成商家关注二维码实体类
 */
@Getter
@Setter
public class QrcodeCreateFinal implements Serializable {

    private static final long serialVersionUID = -2795076341646784080L;
    /**
     * 场景值
     */
    private String scene_id;

    /**
     * 公众号ID
     */
    private Integer publicId;

}
