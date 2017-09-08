package com.gt.mall.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MemberAddress implements Serializable {

    private static final long serialVersionUID = 3184995296305348418L;

    private Integer id;

    private String memName;

    private String memPhone;

    private String memAddress;

    private String memLongitude;

    private String memLatitude;

    private Integer dfMemberId;

    private Integer memDefault;

    private String memHouseMember;

    private String memZipCode;

    private Integer memProvince;

    private Integer memCity;

    private Integer memArea;

    private String provincename;

    private String cityname;

    private String areaname;

}