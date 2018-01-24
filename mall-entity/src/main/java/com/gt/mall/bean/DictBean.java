package com.gt.mall.bean;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

import java.io.Serializable;

/**
 * 字典
 * User : yangqian
 * Date : 2017/11/6 0006
 * Time : 10:52
 */
@Getter
@Setter
public class DictBean implements Serializable {

    private static final long serialVersionUID = -3463779241417759317L;

    private String item_key;//key
    private String  item_value; //值

    private String    value;//名称
    private String[]  style;//风格
    private JSONArray childList;

}
