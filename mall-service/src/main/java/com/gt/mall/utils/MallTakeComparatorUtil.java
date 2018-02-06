package com.gt.mall.utils;

import com.gt.mall.entity.basic.MallTakeTheirTime;

import java.io.Serializable;
import java.util.Comparator;

public class MallTakeComparatorUtil implements Comparator< MallTakeTheirTime >, Serializable {

    private static final long serialVersionUID = 4891828291875625638L;

    public MallTakeComparatorUtil() {
    }

    @Override
    public int compare( MallTakeTheirTime arg0, MallTakeTheirTime arg1 ) {
        String sort1 = arg0.getTimes();
        String sort2 = arg1.getTimes();
        return sort1.compareTo( sort2 );
    }

}
