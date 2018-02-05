package com.gt.mall.utils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public class MallComparatorUtil implements Comparator< Map< String,Object > >, Serializable {

    private static final long serialVersionUID = 6097332167797692035L;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public MallComparatorUtil( String key2 ) {
        this.key = key2;
    }

    @Override
    public int compare( Map< String,Object > arg0, Map< String,Object > arg1 ) {
        double raill1 = CommonUtil.toDouble( arg0.get( this.key ) );
        double raill2 = CommonUtil.toDouble( arg1.get( this.key ) );
        //按照距离进行升序排列
        //	if ( raill1 > raill2 ) {
        //	    return 1;
        //	}
        //	if ( raill1 == raill2 ) {
        //	    return 0;
        //	}
        //	return -1;
        return Double.compare( raill1, raill2);
    }

}
