package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.product.*;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.product.*;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.service.web.product.MallGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 商品分组 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallGroupServiceImpl extends BaseServiceImpl< MallGroupDAO,MallGroup > implements MallGroupService {

    @Autowired
    private MallGroupDAO mallGroupDAO;//分组dao

    @Autowired
    private MallProductDAO mallProductDAO;//商品dao

    @Autowired
    private MallImageAssociativeDAO mallImageAssociativeDAO;//图片dao

    @Autowired
    private MallSearchLabelDAO mallSearchLabelDAO;//分组推荐dao

    @Autowired
    private MallSearchKeywordDAO mallSearchKeywordDAO;//搜索keyDao

    @Autowired
    private MallProductGroupDAO mallProductGroupDAO;//商品分组中间表dao

    @Override
    public PageUtil findGroupByPage( Map< String,Object > param, List< Map< String,Object > > shoplist, int userId ) {
	int curPage = CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) );
	param.put( "curPage", curPage );
	int pageSize = 10;
	param.put( "shoplist", shoplist );
	// 统计商品分组
	int count = mallGroupDAO.selectGroupByCount( param );

	String url = "/mPro/group/group_index.do";
	if ( CommonUtil.isNotEmpty( param.get( "isLabel" ) ) ) {
	    if ( param.get( "isLabel" ).toString().equals( "1" ) ) {
		url = "/mPro/group/label_index.do";
	    }
	}
	PageUtil page = new PageUtil( curPage, pageSize, count, url );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	param.put( "firstNum", firstNum );// 起始页
	param.put( "maxNum", pageSize );// 每页显示分组的数量

	if ( count > 0 ) {// 判断商品分组是否有数据
	    if ( CommonUtil.isEmpty( param.get( "type" ) ) ) {
		param.put( "type", 0 );
	    }
	    List< Map< String,Object > > groupList = mallGroupDAO.selectGroupByPage( param );
	    if ( groupList != null && groupList.size() > 0 ) {
		for ( int i = 0; i < groupList.size(); i++ ) {
		    Map< String,Object > map = groupList.get( i );
		    int productNum = mallProductDAO.countProductByGroup( CommonUtil.toInteger( map.get( "shopId" ) ), CommonUtil.toInteger( map.get( "id" ) ),
				    userId );
		    map.put( "COUNT", productNum );
		}
	    }
	    page.setSubList( groupList );
	}
	return page;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public boolean saveOrUpdateGroup( MallGroup group, List< MallImageAssociative > imageList, int userId ) {
	boolean flag = true;
	int groupId = 0;
	// 判断分组id是否为空
	if ( CommonUtil.isEmpty( group.getId() ) ) {
	    group.setUserId( userId );
	    group.setCreateTime( new Date() );
	    // 添加商品分组
	    groupId = mallGroupDAO.insert( group );
	    // 批量添加商品分组关联表
	    if ( imageList != null && imageList.size() > 0 ) {
		for ( int i = 0; i < imageList.size(); i++ ) {
		    MallImageAssociative image = imageList.get( i );
		    image.setAssId( group.getId() );
		    mallImageAssociativeDAO.insert( image );
		}
	    }
	} else {
	    group.setEditTime( new Date() );
	    group.setEditUserId( userId );
	    groupId = mallGroupDAO.updateById( group );
	    if ( imageList != null && imageList.size() > 0 ) {
		for ( MallImageAssociative image : imageList ) {
		    if ( CommonUtil.isEmpty( image.getId() ) ) {
			image.setAssId( group.getId() );
			mallImageAssociativeDAO.insert( image );
		    } else {
			mallImageAssociativeDAO.updateById( image );
		    }
		}
	    }
	}
	group = mallGroupDAO.selectById( group.getId() );
	// 修改父类分组
	if ( group.getGroupPId() != null && group.getGroupPId() > 0 ) {
	    MallGroup parent = new MallGroup();
	    parent.setId( group.getGroupPId() );
	    parent.setIsChild( 1 );
	    mallGroupDAO.updateById( parent );
	}
	if ( groupId > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean deleteGroup( Integer id ) throws Exception {
	try {
	    MallGroup group = new MallGroup();
	    group.setId( id );
	    group.setIsDelete( 1 );
	    int count = mallGroupDAO.updateById( group );//逻辑删除商品分组

	    //查询父类的分组
	    Wrapper< MallGroup > groupWrapper = new EntityWrapper<>();
	    groupWrapper.where( "group_p_id = {0}", id );
	    List< MallGroup > groupList = mallGroupDAO.selectList( groupWrapper );
	    if ( groupList != null && groupList.size() > 0 ) {
		mallGroupDAO.updateByGroupId( groupList );// 删除父类商品分组
	    }
	    if ( count > 0 ) {
		return true;
	    }
	} catch ( Exception e ) {
	    throw new Exception( "删除商品分组失败：" + e.getMessage() );
	}
	return false;
    }

    @Override
    public MallGroup findGroupById( Integer id ) {
	return mallGroupDAO.selectById( id );
    }

    @Override
    public List< Map< String,Object > > findGroupByShopId( Map< String,Object > maps ) {
	return mallGroupDAO.selectGroupByPage( maps );
    }

    @Override
    public List< MallGroup > selectGroupByShopId( Integer shopId, Integer groupPId ) {
	Wrapper< MallGroup > groupWrapper = new EntityWrapper<>();
	groupWrapper.where( "is_delete = 0 and shop_id = {0}", shopId );
	if ( groupPId == 0 ) {
	    groupWrapper.where( "is_first_parents = 1" );
	}
	return mallGroupDAO.selectList( groupWrapper );
    }

    @Override
    public List< MallGroup > selectPGroupByShopId( Map< String,Object > params ) {
	Wrapper< MallGroup > groupWrapper = new EntityWrapper<>();
	groupWrapper.where( "is_delete=0 and is_first_parents=1 and shop_id = {0}", params.get( "shopId" ) ).orderBy( "sorc", true );
	return mallGroupDAO.selectList( groupWrapper );
    }

    @Override
    public List< Map< String,Object > > selectGroupByParent( Map< String,Object > param ) {
	Map< String,Object > proGroupMap = new HashMap< String,Object >();
	if ( CommonUtil.isNotEmpty( param.get( "group" ) ) ) {
	    String group = param.get( "group" ).toString();
	    String[] str = group.split( "," );
	    for ( String string : str ) {
		if ( CommonUtil.isNotEmpty( string ) ) {
		    proGroupMap.put( string, string );
		}
	    }
	}
	List< Map< String,Object > > groupList = new ArrayList< Map< String,Object > >();
	List< Map< String,Object > > list = mallGroupDAO.selectGroupByParent( param );
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > map : list ) {
		boolean flag = false;
		if ( CommonUtil.isNotEmpty( map.get( "groupPId" ) ) ) {
		    int groupPId = CommonUtil.toInteger( map.get( "groupPId" ) );
		    if ( groupPId > 0 ) {
			flag = true;
		    }
		}
		int select = 0;
		if ( CommonUtil.isNotEmpty( proGroupMap ) ) {
		    if ( CommonUtil.isNotEmpty( proGroupMap.get( map.get( "id" ).toString() ) ) ) {
			select = 1;
		    }
		}
		map.put( "select", select );
		if ( flag ) {
		    if ( groupList != null && groupList.size() > 0 ) {
			for ( Map< String,Object > map2 : groupList ) {
			    if ( map2.get( "id" ).toString().equals( map.get( "groupPId" ).toString() ) ) {
				List< Map > childList = new ArrayList<>();
				if ( CommonUtil.isNotEmpty( map2.get( "childGroupList" ) ) ) {
				    childList = JSONArray.parseArray( JSON.toJSONString( map2.get( "childGroupList" ) ), Map.class );
				}
				childList.add( map );
				map2.put( "childGroupList", childList );
				break;
			    }
			}
		    } else {
			groupList.add( map );
		    }
		} else {
		    groupList.add( map );
		}

	    }
	}
	return groupList;
    }

    @Override
    public boolean saveOrUpdateGroupLabel( List< MallSearchLabel > labelList, int userId ) {
	int count = 0;
	if ( labelList != null && labelList.size() > 0 ) {
	    for ( MallSearchLabel label : labelList ) {
		if ( CommonUtil.isNotEmpty( label ) ) {
		    label.setUserId( userId );
		    if ( CommonUtil.isEmpty( label.getId() ) ) {
			MallSearchLabel sLabel = mallSearchLabelDAO.selectOne( label );
			if ( CommonUtil.isNotEmpty( sLabel ) ) {
			    label.setId( sLabel.getId() );
			}
		    }
		    if ( CommonUtil.isNotEmpty( label.getId() ) ) {
			count = mallSearchLabelDAO.updateById( label );
		    } else {
			label.setCreateTime( new Date() );
			count = mallSearchLabelDAO.insert( label );
		    }
		}
	    }
	}
	if ( count > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public List< MallGroup > selectChildGroupByPId( Map< String,Object > params ) {
	Wrapper< MallGroup > groupWrapper = new EntityWrapper<>();
	String sql = "";
	if ( CommonUtil.isNotEmpty( params.get( "groupPId" ) ) ) {
	    sql += " and group_p_id = " + params.get( "groupPId" );
	}
	if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
	    sql += " and shop_id = " + params.get( "shopId" );
	}
	groupWrapper.where( "is_delete=0  " + sql );
	List< MallGroup > groupList = mallGroupDAO.selectList( groupWrapper );
	List< Integer > groupIds = new ArrayList<>();
	if ( groupList != null && groupList.size() > 0 ) {
	    for ( MallGroup mallGroup : groupList ) {
		if ( !groupIds.contains( mallGroup.getId() ) ) {
		    groupIds.add( mallGroup.getId() );
		}
	    }
	    Map< String,Object > imageParams = new HashMap<>();
	    imageParams.put( "isMainImages", 1 );
	    imageParams.put( "assType", 2 );
	    imageParams.put( "assIds", groupIds );
	    List< Map< String,Object > > imageList = mallImageAssociativeDAO.selectByAssIds( imageParams );
	    if ( imageList != null && imageList.size() > 0 ) {
		for ( MallGroup mallGroup : groupList ) {
		    for ( Map< String,Object > imageMap : imageList ) {
			if ( mallGroup.getId().toString().equals( imageMap.get( "ass_id" ).toString() ) ) {
			    mallGroup.setImageUrl( imageMap.get( "image_url" ).toString() );
			    break;
			}
		    }
		}
	    }

	}
	return groupList;
    }

    @Override
    public boolean clearSearchKeyWord( Map< String,Object > params ) {

	//	return mallSearchKeywordDAO.update(  );
	Wrapper< MallSearchKeyword > keywordWrapper = new EntityWrapper<>();
	String sql = " user_id=" + params.get( "userId" );
	if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
	    sql += " and shop_id = " + params.get( "shopId" );
	}
	keywordWrapper.where( sql );
	MallSearchKeyword keyword = new MallSearchKeyword();
	keyword.setIsDelete( 1 );
	int count = mallSearchKeywordDAO.update( keyword, keywordWrapper );
	if ( count > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public void copyProductGroupByProduct( Map< String,Object > params, MallProduct product ) {
	List< Map< String,Object > > productGroupList = mallProductGroupDAO.selectgroupsByProductId( params );
	if ( productGroupList != null && productGroupList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productGroupList ) {
		MallProductGroup productGroup = new MallProductGroup();
		productGroup.setProductId( product.getId() );

		int groupPId = 0;
		int groupId = 0;
		//判断分组是否存在
		if ( CommonUtil.isNotEmpty( map2.get( "groupPId" ) ) ) {//判断是否有父类 的分组
		    groupPId = CommonUtil.toInteger( map2.get( "groupPId" ) );
		    if ( groupPId > 0 ) {
			MallGroup group = mallGroupDAO.selectById( groupPId );
			groupPId = insertGroup( map2, product, group.getGroupName(), group.getIsChild(), group.getIsFirstParents(), 0 );
		    }
		}
		if ( CommonUtil.isNotEmpty( map2.get( "groupId" ) ) ) {//判断是否有分组
		    groupId = CommonUtil.toInteger( map2.get( "groupId" ) );
		    if ( groupId > 0 ) {
			groupId = insertGroup( map2, product, CommonUtil.toString( map2.get( "groupName" ) ), map2.get( "is_child" ), map2.get( "is_first_parents" ), groupPId );
		    }
		}
		productGroup.setGroupId( groupId );
		productGroup.setShopId( product.getShopId() );
		if ( groupPId > 0 ) {
		    productGroup.setGroupPId( groupPId );
		}
		productGroup.setSort( CommonUtil.toInteger( map2.get( "sort" ) ) );
		mallProductGroupDAO.insert( productGroup );
	    }
	}
    }

    @Override
    public Map< String,Object > selectGroupBySearchName( String searchName ) {
	//select id,group_p_id,id from t_mall_group where group_name like '%" + params.get( "proName" ) + "%'
	Wrapper wrapper = new EntityWrapper();
	wrapper.setSqlSelect( "id,group_p_id" );
	wrapper.like( "group_name", searchName );
	List< Map< String,Object > > groupList = mallGroupDAO.selectList( wrapper );
	if ( groupList != null && groupList.size() > 0 ) {
	    return groupList.get( 0 );
	}
	return null;
    }

    private int insertGroup( Map< String,Object > map2, MallProduct product, String groupName, Object isChild, Object isFirst, int pId ) {
	int groupId = 0;
	MallGroup group = new MallGroup();
	Wrapper< MallGroup > groupWrapper = new EntityWrapper<>();
	groupWrapper.where( "group_name = {0} and shop_id = {1}", groupName, product.getShopId() );

	List< MallGroup > groupList = mallGroupDAO.selectList( groupWrapper );
	if ( groupList != null && groupList.size() > 0 ) {
	    MallGroup mGroup = groupList.get( 0 );
	    groupId = mGroup.getId();
	} else {
	    group.setGroupName( groupName );
	    group.setGroupPId( pId );
	    group.setIsShowPage( 1 );
	    group.setShopId( product.getShopId() );
	    group.setIsChild( CommonUtil.toInteger( isChild ) );
	    group.setUserId( product.getUserId() );
	    group.setCreateTime( new Date() );
	    group.setIsFirstParents( CommonUtil.toInteger( isFirst ) );
	    int count = mallGroupDAO.insert( group );
	    if ( count > 0 ) {
		groupId = group.getId();
	    }
	}
	return groupId;
    }
}
