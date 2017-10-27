package com.gt.mall.bean.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 积分粉币兑换规则实体类
 * User : yangqian
 * Date : 2017/10/25 0025
 * Time : 10:50
 */
@Getter
@Setter
public class JifenAndFenbiRule {

    private Double fenbiRatio;//粉币兑换比例 16.66

    private Double fenbiStartMoney;//粉币起兑金额

    private Double jifenRatio;//积分兑换比例 10个积分

    private Double jifenStartMoney;//积分起兑金额



}
