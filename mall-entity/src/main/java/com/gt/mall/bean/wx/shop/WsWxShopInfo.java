package com.gt.mall.bean.wx.shop;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class WsWxShopInfo implements Serializable {
    private static final long serialVersionUID = -627108022981124113L;
    /**
     * id
     */
    private Integer id;
    /**
     * 多粉内部ID
     */
    private String  sid;

    /**
     * 微信的门店ID
     */
    private String poiid;

    /**
     * 门店名
     */
    private String businessName;

    /**
     * 分店名
     */
    private String branchName;

    /**
     * 门店类型主类型
     */

    private String categories;

    /**
     * 二级类目
     */
    private String twoCategories;

    /**
     * 三级类目
     */
    private String threeCategories;

    /**
     * 电话(固定电话需加区号；区号、分机号均用“-”连接)
     */
    private String telephone;

    /**
     * 人均价格(大于零的整数，须如实填写，默认单位为人民币)
     */
    private Integer avgPrice;

    /**
     * 开始营业时间
     */
    private String startTime;

    /**
     * 关门时间
     */
    private String endTime;

    /**
     * 特色服务，如免费wifi，免费停车，送货上门等商户能提供的特色功能或服务
     */
    private String special;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 推荐品
     */
    private String recommend;

    /**
     * 门店所在的省份
     */
    private String province;

    /**
     * 门店所在的城市
     */
    private String city;

    /**
     * 门店所在地区
     */
    private String district;

    /**
     * 门店所在的详细街道地址（不要填写省市信息）
     */
    private String address;

    /**
     * 坐标类型(1 为火星坐标（目前只能选1）)
     */
    private Integer offsetType;

    /**
     * 门店所在地理位置的经度
     */
    private String longitude;

    /**
     * 门店所在地理位置的纬度
     */
    private String latitude;

    /**
     * 门店是否可用状态(门店是否可用状态。0:创建但未送审1 表示系统错误、2 表示审核中、3 审核通过、4 审核驳回。当该字段为1、2、4 状态时，poi_id 为空)
     */
    private Integer availableState;

    /**
     * 扩展字段是否正在更新中。1 表示扩展字段正在更新中，尚未生效，不允许再次更新； 0 表示扩展字段没有在更新中或更新已生效，可以再次更新
     */
    private Integer updateStatus;

    /**
     * 0：表示还没将门店信息发送到公众号审核，可修改全部内容；1：表示已发送给微信公众号审核  2 已审核通过 3审核通过后重新审核服务信息 4 送审被失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 单位信息
     */
    private String detail;

    /**
     * 商家ID
     */
    private Integer busId;

    /**
     * 是否是总店
     */
    private Integer mainShop;

}