package com.gt.mall.dao.html;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.html.MallHtml;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城里面的H5 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlDAO extends BaseMapper<MallHtml> {

    /**
     * 得到该用户 商家中的H5模板列表
     * @param user_id 用户Id
     * @param pid 创建人的父类id
     * @param firstNum 页数
     * @param pageSize 记录数
     * @return 模板列表
     */
    List<Map<String, Object>> getHtmlByUserId(@Param( "user_id" ) Integer user_id, @Param( "pid" ) Integer pid, @Param( "firstNum" ) Integer firstNum, @Param( "pageSize" ) Integer pageSize);

    /**
     * 统计该用户 商家中的H5模板
     * @param user_id 用户id
     * @param pid 创建人的父类id
     * @return 数量
     */
    int countHtmlByUserId(@Param( "user_id" ) Integer user_id, @Param( "pid" ) Integer pid);


    /**
     * 后台中的H5模板列表
     *
     * @param firstNum 页数
     * @param pageSize 记录数
     * @return 模板列表
     */
    List<Map<String, Object>> getHtmlModelList(@Param( "firstNum" ) Integer firstNum, @Param( "pageSize" ) Integer pageSize);

    /**
     * 统计后台中的H5模板
     *
     * @return 数量 
     */
    int countHtmlModelList();


}