package com.gt.mall.service.web.product.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.product.MallProductTemplateDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallProductTemplate;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.product.MallProductTemplateService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品页模板表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-09-20
 */
@Service
public class MallProductTemplateServiceImpl extends BaseServiceImpl< MallProductTemplateDAO,MallProductTemplate > implements MallProductTemplateService {

    @Autowired
    private MallProductTemplateDAO productTemplateDAO;
    @Autowired
    private MallProductDAO         mallProductDAO;

    @Override
    public PageUtil findTemplateByPage( Map< String,Object > param ) {

	int curPage = CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) );
	param.put( "curPage", curPage );
	int pageSize = 10;
	int userId = CommonUtil.toInteger( param.get( "userId" ) );
	// 统计商品分组
	Wrapper< MallProductTemplate > pageWrapper = new EntityWrapper<>();
	pageWrapper.where( "is_delete = 0 and user_id={0}", userId );
	int count = productTemplateDAO.selectCount( pageWrapper );

	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallProduct/template/list.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	param.put( "firstNum", firstNum );// 起始页
	param.put( "maxNum", pageSize );// 每页显示分组的数量
	if ( count > 0 ) {// 判断商品分组是否有数据
	    List< MallProductTemplate > templateList = productTemplateDAO.selectTemplateByPage( param );
	    page.setSubList( templateList );
	}
	return page;
    }

    @Override
    public boolean batchDelTemplate( String[] id, Integer userId ) {
	boolean result = true;
	if ( id != null && id.length > 0 ) {
	    for ( String str : id ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    MallProductTemplate template = productTemplateDAO.selectById( CommonUtil.toInteger( str ) );
		    if ( template != null ) {
			//判断有无 商品调用
			Wrapper wrapper = new EntityWrapper();
			wrapper.where( "user_id = {0} and is_delete = 0 and template_id ={1}", userId, template.getId() );
			int count = mallProductDAO.selectCount( wrapper );
			if ( count > 0 ) {
			    result = false;
			    break;
			} else {
			    template.setIsDelete( 1 );
			    productTemplateDAO.updateById( template );
			}
		    } else {
			result = false;
		    }
		}
	    }
	}
	return result;
    }

}
