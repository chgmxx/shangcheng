package com.gt.mall.service.web.order.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.order.MallOrderTaskDAO;
import com.gt.mall.entity.order.MallOrderTask;
import com.gt.mall.service.web.order.MallOrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单任务表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2018-02-02
 */
@Service
public class MallOrderTaskServiceImpl extends BaseServiceImpl< MallOrderTaskDAO,MallOrderTask > implements MallOrderTaskService {

    @Autowired
    private MallOrderTaskService mallOrderTaskService;
    @Autowired
    private MallOrderTaskDAO     mallOrderTaskDAO;

    @Override
    public boolean saveOrUpdate( Integer type, Integer orderId, String orderNo, Integer orderRerurnId, Integer day ) {
        boolean flag = false;
        Wrapper< MallOrderTask > wrapper = new EntityWrapper<>();
        wrapper.and( "is_delete= 0 and task_type={0} and order_id={1}", type, orderId );
        MallOrderTask orderTask = mallOrderTaskService.selectOne( wrapper );
        if ( orderTask != null ) {
            orderTask.setReturnDay( day );
            flag = mallOrderTaskService.updateById( orderTask );
        } else {
            MallOrderTask task = new MallOrderTask();
            task.setTaskType( type );
            task.setOrderId( orderId );
            task.setOrderReturnId( orderRerurnId );
            task.setOrderNo( orderNo );
            task.setReturnDay( day );
            task.setCreateTime( new Date() );
            flag = mallOrderTaskService.insert( task );
        }
        return flag;
    }

    @Override
    public List< MallOrderTask > findByType( Map< String,Object > params ) {
        return mallOrderTaskDAO.findByType( params );
    }
}
