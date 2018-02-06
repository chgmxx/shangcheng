package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductGroupDAO;
import com.gt.mall.entity.product.MallProductGroup;
import com.gt.mall.service.web.product.MallProductGroupService;
import com.gt.mall.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品分组中间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductGroupServiceImpl extends BaseServiceImpl< MallProductGroupDAO,MallProductGroup > implements MallProductGroupService {

    @Autowired
    private MallProductGroupDAO mallProductGroupDAO;



    @Override
    public void saveOrUpdate( Object obj, int proId ) {

        Wrapper< MallProductGroup > groupWrapper = new EntityWrapper<>();
        groupWrapper.where( " product_id = {0} and is_delete = 0", proId );
        List< MallProductGroup > defaultList = mallProductGroupDAO.selectList( groupWrapper );

        List< MallProductGroup > groupList = JSONArray.parseArray( obj.toString(), MallProductGroup.class );
        if ( groupList != null && groupList.size() > 0 ) {
            for ( MallProductGroup mallProductGroup : groupList ) {
                if ( defaultList != null ) {
                    for ( MallProductGroup group : defaultList ) {
                        if ( group.getGroupId().equals( mallProductGroup.getGroupId() ) ) {
                            mallProductGroup.setId( group.getId() );
                            defaultList.remove( group );
                            break;
                        }
                    }
                }
                if ( CommonUtil.isEmpty( mallProductGroup.getId() ) ) {
                    mallProductGroup.setProductId( proId );
                    mallProductGroupDAO.insert( mallProductGroup );
                } else {
                    mallProductGroupDAO.updateById( mallProductGroup );
                }

            }
        }

        if ( defaultList != null && defaultList.size() > 0 ) {
            for ( MallProductGroup group : defaultList ) {
                group.setIsDelete( 1 );
                mallProductGroupDAO.updateById( group );
            }
        }
    }
}
