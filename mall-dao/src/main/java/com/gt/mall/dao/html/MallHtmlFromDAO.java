package com.gt.mall.dao.html;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.html.MallHtmlFrom;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * h5商城里面的表单信息 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlFromDAO extends BaseMapper< MallHtmlFrom > {

    /**
     * 得到H5模板的表单列表
     *
     * @param htmlId   模板id
     * @param firstNum 页数
     * @param pageSize 记录数
     *
     * @return 表单列表
     */
    List< Map< String,Object > > getHtmlFromByHtmlId( @Param( "htmlId" ) Integer htmlId, @Param( "firstNum" ) Integer firstNum, @Param( "pageSize" ) Integer pageSize );

    /**
     * 统计5模板的表单数量
     *
     * @param html_id 模板id
     *
     * @return 数量
     */
    int countHtmlFromByHtmlId( @Param( "html_id" ) Integer html_id );

    /**
     * 表单详情
     *
     * @param id 表单id
     *
     * @return 表单信息
     */
    Map< String,Object > htmlFromView( @Param( "id" ) Integer id );
}