package com.gt.mall.utils;

import com.gt.mall.entity.basic.MallTakeTheirTime;

import java.util.Comparator;

public class MallTakeComparatorUtil implements Comparator< MallTakeTheirTime > {

    public MallTakeComparatorUtil() {
    }

    @Override
    public int compare( MallTakeTheirTime arg0, MallTakeTheirTime arg1 ) {
	String sort1 = arg0.getTimes();
	String sort2 = arg1.getTimes();
	return sort1.compareTo(sort2);
    }

}
