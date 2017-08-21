package com.gt.mall.bean.member;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Getter
@Setter
public class MemberCard implements Serializable {

    private static final long serialVersionUID = -7387180299755373020L;
    private Integer mcId;
    private String  cardNo;
    private Integer ctId;
    private Integer gtId;
    private Integer publicId;
    private Date    receiveDate;
    private Integer source;
    private Integer isbinding;
    private Double  money;
    private Integer frequency;
    private Integer grId;
    private Date    expireDate;
    private Integer ucId;
    private String  nominateCode;
    private String  systemcode;
    private Integer isChecked;
    private Integer applyType;
    private Integer memberId;
    private Integer changeCardType;
    private Integer entityMemberId;
    private Integer cardStatus;
    private Integer busId;
    private Integer oldId;
    private Double  giveMoney;
    private Integer shopId;
    private Integer online;

}
