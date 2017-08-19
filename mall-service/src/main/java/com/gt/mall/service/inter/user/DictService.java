package com.gt.mall.service.inter.user;

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
     *
     * @param dictType 字典属性
     *
     * @return 键值对
     */
    public List< Map > getDict( String dictType );

    /**
     * 查询字典 value值
     *
     * @param dictType 属性
     * @param key      key值
     *
     * @return 字典value
     */
    public String getDictRuturnValue( String dictType, int key );

    /**
     * 根据用户和字典模块属性获取创建模块的数量
     *
     * @param userId     商家id
     * @param modelStyle 模块属性
     * @param dictstyle  字典属性
     *
     * @return 数量
     */
    int getDiBserNum( int userId, int modelStyle, String dictstyle );
}
