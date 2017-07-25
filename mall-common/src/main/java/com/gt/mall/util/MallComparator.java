package com.gt.mall.util;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;

@Service
public class MallComparator implements Comparator<Map<String, Object>> {
	
	private String key;
	

	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}
	public MallComparator() {
	}

	public MallComparator(String key2) {
		this.key = key2;
	}
	@Override
	public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
		 double raill1 = CommonUtil.toDouble(arg0.get(this.key));
         double raill2 = CommonUtil.toDouble(arg1.get(this.key));
         //按照距离进行升序排列  
         if(raill1 > raill2){  
             return 1;  
         }  
         if(raill1 == raill2){  
             return 0;  
         }  
         return -1;  
	}

	
	
}
