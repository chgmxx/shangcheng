package com.gt.mall.inter.service;

import java.util.List;
import java.util.Map;

/**
 * 字典接口
 * User : yangqian
 * Date : 2017/8/16 0016
 * Time : 10:29
 */
public interface DictService {

    /**
     * 查询字典 返回键值对
     * @param dictType 字典属性
     * @return 键值对
     */
    public List<Map> getDict(String dictType);

    /**
     * 查询字典 value值
     * @param dictType 属性
     * @param key key值
     * @return 字典value
     */
    public String getDictRuturnValue(String dictType,int key);
}
