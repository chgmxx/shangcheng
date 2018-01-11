package com.gt.mall.bean.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 积分粉币实体类
 * User : yangqian
 * Date : 2017/10/25 0025
 * Time : 10:50
 */
@Getter
@Setter
public class JifenAndFenbBean {

    private Double jifenNum;//积分数量（会员拥有的）

    private Double jifenMoney;//积分金额（积分数量兑换的金额）

    private Double fenbiNum;//粉币数量（会员拥有的）

    private Double fenbiMoney;//粉币金额（粉币数量兑换的金额）

}
