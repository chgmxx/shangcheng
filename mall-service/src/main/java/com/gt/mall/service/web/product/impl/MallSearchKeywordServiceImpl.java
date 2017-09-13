package com.gt.mall.service.web.product.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallSearchKeywordDAO;
import com.gt.mall.entity.product.MallSearchKeyword;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城搜索表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSearchKeywordServiceImpl extends BaseServiceImpl< MallSearchKeywordDAO,MallSearchKeyword > implements MallSearchKeywordService {

    @Autowired
    private MallSearchKeywordDAO mallSearchKeywordDAO;

    @Override
    public MallSearchKeyword selectBykeyword( int shopId, String keyword, int userId ) {
	Wrapper< MallSearchKeyword > keywordWrapper = new EntityWrapper<>();
	keywordWrapper.where( "shop_id={0} and keyword ={1} and user_id={2}", shopId, keyword, userId );
	List< MallSearchKeyword > keywordList = mallSearchKeywordDAO.selectList( keywordWrapper );
	if ( keywordList != null && keywordList.size() > 0 ) {
	    return keywordList.get( 0 );
	}
	return null;
    }

    @Override
    public List< MallSearchKeyword > selectByUser( Map< String,Object > map ) {
	Wrapper< MallSearchKeyword > keywordWrapper = new EntityWrapper<>();
	keywordWrapper.where( "shop_id={0} and user_id={1} and is_delete = 0", map.get( "shopId" ), map.get( "userId" ) );
	return mallSearchKeywordDAO.selectList( keywordWrapper );
    }

    @Override
    public int clearSearchKeyWord( Map< String,Object > params ) {
	Wrapper wrapper = new EntityWrapper();
	wrapper.where( "user_id={0}", params.get( "userId" ) );
	if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
	    wrapper.andNew( "shop_id ={0}", params.get( "shopId" ) );
	}
	MallSearchKeyword keyword = new MallSearchKeyword();
	keyword.setIsDelete( 1 );
	return mallSearchKeywordDAO.update( keyword, wrapper );
    }

    @Override
    public void insertSeachKeyWord(int memberId,int shopId,Object searchName) {

	if ( CommonUtil.isNotEmpty( searchName)) {
	    //保存到搜索关键字表
	    MallSearchKeyword keyword = selectBykeyword( shopId, searchName.toString(), memberId );
	    if ( CommonUtil.isEmpty( keyword ) ) {
		keyword = new MallSearchKeyword();
		keyword.setKeyword( searchName.toString() );
		keyword.setSearchNum( 1 );
		keyword.setShopId( shopId );
		keyword.setUserId( memberId );
		keyword.setEditTime( new Date() );
		keyword.setCreateTime( new Date() );
		mallSearchKeywordDAO.insert( keyword );
	    } else {
		keyword.setEditTime( new Date() );
		keyword.setSearchNum( keyword.getSearchNum() + 1 );
		keyword.setIsDelete( 0 );
		mallSearchKeywordDAO.updateById( keyword );
	    }
	}
    }
}
