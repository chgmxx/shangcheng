package com.gt.mall.dao.presale;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.presale.MallPresaleMessageRemind;

/**
 * <p>
 * 预售消息提醒 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleMessageRemindDAO extends BaseMapper< MallPresaleMessageRemind > {

    /**
     * 查询消息提醒信息
     *
     * @param message
     *
     * @return
     */
    MallPresaleMessageRemind selectByPresale( MallPresaleMessageRemind message );
}
