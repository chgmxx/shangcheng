package com.gt.mall.web.service.basic.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.web.service.basic.MallCommentGiveService;
import com.gt.mall.web.service.basic.MallCommentService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 商城评论 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCommentServiceImpl extends BaseServiceImpl< MallCommentDAO,MallComment > implements MallCommentService {

    private Logger logger = Logger.getLogger( MallCommentServiceImpl.class );

    @Autowired
    private MallCommentDAO          commentDAO;
    @Autowired
    private MallCommentGiveService  commentGiveService;
    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;
    @Autowired
    private MallOrderDetailDAO      orderDetailDAO;

    @Override
    public PageUtil selectCommentPage( Map< String,Object > params ) {
	List< Map< String,Object > > productList = null;
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );

	int count = commentDAO.selectCommentCount( params );
	PageUtil page = new PageUtil( curPage, pageSize, count, "comment/to_index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断商品是否有数据
	    productList = commentDAO.selectCommentList( params );
	}
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map : productList ) {
		if ( CommonUtil.isNotEmpty( map.get( "is_upload_image" ) ) ) {
		    if ( map.get( "is_upload_image" ).toString().equals( "1" ) ) {
			Map< String,Object > imageMap = new HashMap< String,Object >();
			imageMap.put( "assId", map.get( "id" ) );
			imageMap.put( "assType", 4 );
			List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( imageMap );
			if ( imageList != null && imageList.size() > 0 ) {
			    map.put( "imageList", imageList );
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( map.get( "is_rep" ) ) ) {
		    int isRep = CommonUtil.toInteger( map.get( "is_rep" ) );
		    if ( isRep == 1 ) {
			Map< String,Object > param = new HashMap< String,Object >();
			param.put( "appraise", map.get( "id" ) );
			List childList = commentDAO.ownerResponseList( param );
			if ( childList != null && childList.size() > 0 ) {
			    map.put( "chilComment", childList.get( 0 ) );
			}
		    }
		}
	    }
	}
	page.setSubList( productList );
	return page;
    }

    @Override
    public boolean checkComment( Map< String,Object > params ) {
	if ( CommonUtil.isNotEmpty( params.get( "ids" ) ) ) {
	    String[] ids = (String[]) JSONArray.toArray( JSONArray.fromObject( params.get( "ids" ) ), String.class );
	    params.put( "ids", ids );
	}
	int count = commentDAO.batchUpdateComment( params );
	if ( count > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean replatComment( Map< String,Object > params, int userId ) {
	if ( CommonUtil.isNotEmpty( params.get( "params" ) ) ) {
	    MallComment comment = (MallComment) JSONObject.toBean( JSONObject.fromObject( params.get( "params" ) ), MallComment.class );
	    comment.setCreateTime( new Date() );
	    comment.setUserId( userId );
	    comment.setUserType( 2 );
	    int count = commentDAO.insert( comment );
	    if ( count > 0 ) {
		MallComment pComment = new MallComment();
		pComment.setId( comment.getRepPId() );
		pComment.setIsRep( "1" );
		commentDAO.updateById( pComment );
		return true;
	    }
	}
	return false;
    }

    @Override
    public MallComment addAppraise( Map< String,Object > map, MallComment mallComment, HttpServletRequest request ) {
	logger.info( "进入添加评论方法" );

	mallComment.setCreateTime( new Date() );
	int count = commentDAO.insert( mallComment );

	if ( count > 0 ) {
	    MallOrderDetail orderDetail = new MallOrderDetail();
	    orderDetail.setAppraiseStatus( 1 );
	    Wrapper wrapper = new EntityWrapper();
	    wrapper.where( "id={0} ", mallComment.getOrderDetailId() );
	    orderDetailDAO.update( orderDetail, wrapper );//评论添加完成，修改订单详情待评价的的状态

	    if ( CommonUtil.isNotEmpty( map.get( "imageUrls" ) ) ) {
		String imagePath = map.get( "imageUrls" ).toString();
		String[] imageUrl = imagePath.split( ";" );
		for ( int i = 0; i < imageUrl.length; i++ ) {
		    MallImageAssociative ass = new MallImageAssociative();
		    ass.setAssType( 4 );
		    ass.setAssId( mallComment.getId() );
		    ass.setImageUrl( imageUrl[i] );
		    if ( i == 0 ) {
			ass.setIsMainImages( 1 );
		    }
		    ass.setAssSort( i + 1 );
		    imageAssociativeDAO.insert( ass );//添加图片
		}
	    }
	    commentGiveService.commentGive( mallComment.getId(), request, mallComment.getUserId() );

	    return mallComment;
	}
	return null;
    }

    @Override
    public PageUtil myAppraise( Map< String,Object > param ) {
	param.put( "curPage", CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) ) );
	int pageSize = 10000;
	int rowCount = commentDAO.countAppraise( param );//评论条数
	PageUtil page = new PageUtil( CommonUtil.toInteger( param.get( "curPage" ) ), pageSize, rowCount, "mMember/79B4DE7C/myAppraise.do" );
	param.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	param.put( "maxResult", pageSize );

	List< Map< String,Object > > list = commentDAO.findByPage( param );//查询我的评论
	List< Map< String,Object > > list1 = new ArrayList< Map< String,Object > >();
	for ( int i = 0; i < list.size(); i++ ) {
	    Map< String,Object > p = list.get( i );
	    param.put( "appraise", p.get( "id" ) );//评论id
	    List hf = commentDAO.ownerResponseList( param );//查询店家回复
	    for ( int j = 0; j < hf.size(); j++ ) {
		JSONObject h = JSONObject.fromObject( hf.get( j ) );
		if ( p.get( "id" ).equals( h.get( "rep_p_id" ) ) ) {
		    p.put( "resContent", h );

		}
	    }
	    if ( CommonUtil.isNotEmpty( p.get( "is_upload_image" ) ) ) {
		if ( p.get( "is_upload_image" ).toString().equals( "1" ) ) {
		    //该评论已经上传了图片
		    Map< String,Object > imageMap = new HashMap< String,Object >();
		    imageMap.put( "assId", p.get( "id" ) );
		    imageMap.put( "assType", 4 );
		    List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( imageMap );
		    if ( imageList != null && imageList.size() > 0 ) {
			p.put( "imageList", imageList );
		    }
		}
	    }
	    list1.add( p );
	}
	page.setSubList( list1 );
	return page;
    }

    //    public Integer addUploadImage( MallImageAssociative iass ) {
    //	return imageAssociativeDAO.insert( iass );
    //    }

    @Override
    public MallComment selectComment( MallComment comment ) {
	return commentDAO.selectByComment( comment );
    }
}
