package com.gt.mall.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */
@Getter
@Setter
public class BusUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id 主键
     */
    private Integer id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 用户密码（用于UC登陆）
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号码
     */
    private String phone;

    private String gender;

    private String registerIp;

    private String recentIp;

    private Date ctime;

    private Date mtime;

    private String status;

    private Integer level;

    private Integer city_id;

    private Date startTime;

    private Date endTime;

    private Integer days;

    private String levelDesc;

    private Integer years;

    private Double fansCurrency;

    private long flow;

    private Integer industryid;

    private Integer pid;

    private Integer smsCount;

    private Integer istechnique;

    private Integer advert;

    private String busmoney_level;

    private String regionids;

    private Integer isagent;

    private Integer agentid;

    private String realname;

    private Integer login_source;

    private Boolean is_binding;

    private Date unbundling_time;

    private String fixed_phone;

    private String customer_id;

    private String merchant_name;

}