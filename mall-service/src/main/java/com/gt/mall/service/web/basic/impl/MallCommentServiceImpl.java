package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.result.phone.comment.PhoneCommentListCommentResult;
import com.gt.mall.result.phone.comment.PhoneCommentListResult;
import com.gt.mall.result.phone.comment.PhoneCommentProductResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.basic.MallCommentGiveService;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.order.MallOrderDetailService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.PageUtil;
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
    private MallCommentDAO              mallCommentDAO;
    @Autowired
    private MallCommentGiveService      mallCommentGiveService;
    @Autowired
    private MallImageAssociativeDAO     mallImageAssociativeDAO;
    @Autowired
    private MallOrderDetailService      mallOrderDetailService;
    @Autowired
    private MallOrderService            mallOrderService;
    @Autowired
    private MallPaySetService           mallPaySetService;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;
    @Autowired
    private MemberService               memberService;

    @Override
    public Map< String,Object > selectCommentCount( Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	//全部
	params.put( "feel", "" );
	int count = mallCommentDAO.selectCommentCount( params );
	result.put( "total", count );
	//好评
	params.put( "feel", "1" );
	int count1 = mallCommentDAO.selectCommentCount( params );
	result.put( "good", count1 );
	//中评
	params.put( "feel", "0" );
	int count2 = mallCommentDAO.selectCommentCount( params );
	result.put( "medium", count2 );
	//差评
	params.put( "feel", "-1" );
	int count3 = mallCommentDAO.selectCommentCount( params );
	result.put( "bad", count3 );

	return result;
    }

    @Override
    public PageUtil selectCommentPage( Map< String,Object > params, List< Map< String,Object > > shoplist ) {
	List< Map< String,Object > > productList = null;
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );

	int count = mallCommentDAO.selectCommentCount( params );
	PageUtil page = new PageUtil( curPage, pageSize, count, "comment/to_index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断商品是否有数据
	    productList = mallCommentDAO.selectCommentList( params );
	}
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map : productList ) {
		if ( CommonUtil.isNotEmpty( map.get( "is_upload_image" ) ) ) {
		    if ( map.get( "isUploadImage" ).toString().equals( "1" ) ) {
			Map< String,Object > imageMap = new HashMap<>();
			imageMap.put( "assId", map.get( "id" ) );
			imageMap.put( "assType", 4 );
			List< MallImageAssociative > imageList = mallImageAssociativeDAO.selectImageByAssId( imageMap );
			if ( imageList != null && imageList.size() > 0 ) {
			    map.put( "imageList", imageList );
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( map.get( "isRep" ) ) ) {
		    int isRep = CommonUtil.toInteger( map.get( "isRep" ) );
		    if ( isRep == 1 ) {
			Map< String,Object > param = new HashMap<>();
			param.put( "appraise", map.get( "id" ) );
			List childList = mallCommentDAO.ownerResponseList( param );
			if ( childList != null && childList.size() > 0 ) {
			    map.put( "chilComment", childList.get( 0 ) );
			}
		    }
		}
		if ( shoplist != null && shoplist.size() > 0 ) {
		    for ( Map< String,Object > shopMap : shoplist ) {
			if ( map.get( "shopId" ).toString().equals( shopMap.get( "id" ).toString() ) ) {
			    map.put( "stoName", shopMap.get( "sto_name" ) );
			    break;
			}
		    }
		}
	    }
	}
	page.setSubList( productList );
	return page;
    }

    @Override
    public MallComment addAppraise( String imageUrls, MallComment mallComment, HttpServletRequest request ) {

	mallComment.setCreateTime( new Date() );
	if ( CommonUtil.isNotEmpty( imageUrls ) ) {
	    mallComment.setIsUploadImage( 1 );
	} else {
	    mallComment.setIsUploadImage( 0 );
	}
	mallComment.setUserType( 1 );
	int count = mallCommentDAO.insert( mallComment );

	if ( count > 0 ) {
	    MallOrderDetail orderDetail = new MallOrderDetail();
	    orderDetail.setAppraiseStatus( 1 );
	    Wrapper< MallOrderDetail > wrapper = new EntityWrapper<>();
	    wrapper.where( "id={0} ", mallComment.getOrderDetailId() );
	    mallOrderDetailService.update( orderDetail, wrapper );//评论添加完成，修改订单详情待评价的的状态

	    if ( CommonUtil.isNotEmpty( imageUrls ) ) {
		List< String > imageList = JSONArray.parseArray( imageUrls, String.class );
		for ( int i = 0; i < imageList.size(); i++ ) {
		    MallImageAssociative ass = new MallImageAssociative();
		    ass.setAssType( 4 );
		    ass.setAssId( mallComment.getId() );
		    ass.setImageUrl( imageList.get( i ) );
		    if ( i == 0 ) {
			ass.setIsMainImages( 1 );
		    }
		    ass.setAssSort( i + 1 );
		    mallImageAssociativeDAO.insert( ass );//添加图片
		}
	    }
	    mallCommentGiveService.commentGive( mallComment.getId(), request, mallComment.getUserId() );

	    return mallComment;
	}
	return null;
    }

    @Override
    public Map< String,Object > getProductComment( int busId, int productId, String feel ) {
	int proId = CommonUtil.toInteger( productId );
	Map< String,Object > maps = new HashMap<>();
	//查询是否开启评论审核
	MallPaySet set = mallPaySetService.selectByUserId( busId );

	Map< String,Object > commentMap = new HashMap<>();
	commentMap.put( "productId", proId );
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getIsCommentCheck() ) ) {
		if ( "1".equals( set.getIsCommentCheck().toString() ) ) {
		    commentMap.put( "checkStatus", 1 );
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( feel ) ) {
	    commentMap.put( "feel", feel );
	}

	List< Map< String,Object > > countList = mallCommentDAO.selectCountFeel( commentMap );
	if ( countList != null && countList.size() > 0 ) {
	    Map< String,Object > countMap = new HashMap<>();
	    for ( Map< String,Object > map : countList ) {
		String key = "hao";
		if ( map.get( "feel" ).toString().equals( "0" ) ) {
		    key = "zhong";
		} else if ( map.get( "feel" ).toString().equals( "-1" ) ) {
		    key = "cha";
		}
		countMap.put( key, map.get( "count" ) );
	    }
	    maps.put( "countMap", countMap );
	}
	List< Map< String,Object > > productCommentList = new ArrayList<>();
	List< Map< String,Object > > commentList = mallCommentDAO.selectCommentByProId( commentMap );
	if ( commentList != null && commentList.size() > 0 ) {
	    for ( Map< String,Object > map : commentList ) {
		String id = map.get( "id" ).toString();
		if ( CommonUtil.isNotEmpty( map.get( "is_upload_image" ) ) ) {
		    if ( map.get( "is_upload_image" ).toString().equals( "1" ) ) {//评论人已经上传图片
			Map< String,Object > params2 = new HashMap<>();
			params2.put( "assType", 4 );
			params2.put( "assId", id );
			//查询评论图片
			List< MallImageAssociative > imageList = mallImageAssociativeService.selectByAssId( params2 );
			if ( imageList != null && imageList.size() > 0 ) {
			    map.put( "imageList", imageList );
			}
		    }
		}
		List< Map< String,Object > > list = new ArrayList<>();
		if ( CommonUtil.isNotEmpty( map.get( "product_specificas" ) ) ) {
		    StringBuilder spec = new StringBuilder();
		    String specificas = map.get( "product_specificas" ).toString();
		    List< MallProductSpecifica > specList = mallProductSpecificaService.selectByValueIds( proId, specificas.split( "," ) );
		    if ( specList != null && specList.size() > 0 ) {
			for ( MallProductSpecifica specifica : specList ) {
			    if ( CommonUtil.isNotEmpty( specifica ) ) {
				spec.append( " " ).append( specifica.getSpecificaValue() );
				Map< String,Object > specMap = new HashMap<>();
				specMap.put( "name", specifica.getSpecificaName() );
				specMap.put( "value", specifica.getSpecificaValue() );
				list.add( specMap );
			    }
			}
		    }
		    if ( CommonUtil.isNotEmpty( spec.toString() ) ) {
			map.put( "spec", spec.toString() );
		    }
		    if ( list != null && list.size() > 0 ) {
			map.put( "specList", list );
		    }
		}
		if ( map.get( "is_rep" ).toString().equals( "1" ) ) {
		    //查询回复内容
		    Map< String,Object > replyMap = new HashMap<>();
		    replyMap.put( "appraise", id );
		    List< MallComment > replayList = mallCommentDAO.ownerResponseList( replyMap );
		    if ( replayList != null && replayList.size() > 0 ) {
			map.put( "replyContent", replayList.get( 0 ).getContent() );
		    }
		}
		Member member1 = memberService.findMemberById( CommonUtil.toInteger( map.get( "user_id" ) ), null );
		if ( CommonUtil.isNotEmpty( member1 ) ) {
		    map.put( "nickname", member1.getNickname() );
		    map.put( "headimgurl", member1.getHeadimgurl() );
		}

		productCommentList.add( map );
	    }
	    maps.put( "commentList", productCommentList );
	}
	return maps;
    }

    @Override
    public PhoneCommentProductResult getCommentProduct( Integer orderDetailId ) {
	if ( CommonUtil.isEmpty( orderDetailId ) || orderDetailId == 0 ) {
	    return null;
	}
	PhoneCommentProductResult result = new PhoneCommentProductResult();
	MallOrderDetail detail = mallOrderDetailService.selectById( orderDetailId );
	if ( CommonUtil.isEmpty( detail ) ) {
	    return null;
	}
	MallOrder order = mallOrderService.selectById( detail.getOrderId() );
	result.setOrderId( order.getId() );
	result.setProductId( detail.getProductId() );
	result.setShopId( detail.getShopId() );
	result.setBusId( order.getBusUserId() );
	result.setProductName( detail.getDetProName() );
	result.setProductImageUrl( detail.getProductImageUrl() );
	if ( CommonUtil.isNotEmpty( detail.getProductSpeciname() ) ) {
	    result.setProductSpecifica( detail.getProductSpeciname().replaceAll( ",", "/" ) );
	}
	result.setProductNum( detail.getDetProNum() );
	return result;
    }

    @Override
    public MallComment selectComment( MallComment comment ) {
	return mallCommentDAO.selectByComment( comment );
    }

    @Override
    public PhoneCommentListResult myCommentList( Integer memberId, Integer busId, Integer curPage ) {
	PhoneCommentListResult result = new PhoneCommentListResult();
	int pageSize = 10;
	Map< String,Object > param = new HashMap<>();
	//获取会员集合
	List< Integer > memberList = memberService.findMemberListByIds( memberId );
	if ( memberList != null && memberList.size() > 0 ) {
	    param.put( "memberList", memberList );
	} else {
	    param.put( "memberId", memberId );
	}
	int rowCount = mallCommentDAO.countAppraise( param );//评论条数
	curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
	param.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	param.put( "maxResult", pageSize );
	List< Map< String,Object > > list = mallCommentDAO.findByPage( param );//查询我的评论
	List< PhoneCommentListCommentResult > commentList = new ArrayList<>();
	for ( Map< String,Object > map : list ) {
	    PhoneCommentListCommentResult commentResult = new PhoneCommentListCommentResult();
	    commentResult.setFeel( CommonUtil.toInteger( map.get( "feel" ) ) );
	    commentResult.setContent( CommonUtil.toString( map.get( "content" ) ) );
	    Date createTime = DateTimeKit.parseDate( map.get( "createTime" ).toString() );
	    commentResult.setCommentTime( DateTimeKit.format( createTime, DateTimeKit.DEFAULT_DATETIME_FORMAT_YYYYMMDD_MMSS ) );
	    if ( CommonUtil.isNotEmpty( map.get( "product_speciname" ) ) ) {
		commentResult.setProductSpecifica( CommonUtil.toString( map.get( "product_speciname" ) ) );
	    }
	    commentResult.setProductId( CommonUtil.toInteger( map.get( "product_id" ) ) );
	    commentResult.setShopId( CommonUtil.toInteger( map.get( "shop_id" ) ) );
	    commentResult.setProductName( CommonUtil.toString( map.get( "det_pro_name" ) ) );
	    commentResult.setProductImageUrl( CommonUtil.toString( map.get( "product_image_url" ) ) );
	    commentResult.setProductPrice( CommonUtil.toDouble( map.get( "total_price" ) ) );
	    if ( CommonUtil.isNotEmpty( map.get( "is_rep" ) ) && "1".equals( map.get( "is_rep" ).toString() ) ) {
		param.put( "appraise", map.get( "id" ) );//评论id
		List< MallComment > replyList = mallCommentDAO.ownerResponseList( param );//查询店家回复
		for ( int i = 0; i < replyList.size(); i++ ) {
		    MallComment reply = replyList.get( i );
		    if ( map.get( "id" ).toString().equals( reply.getRepPId().toString() ) ) {
			commentResult.setReplyContent( reply.getContent() );
			replyList.remove( i );
			break;
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( map.get( "is_upload_image" ) ) ) {
		if ( map.get( "is_upload_image" ).toString().equals( "1" ) ) {
		    //该评论已经上传了图片
		    List< MallImageAssociative > imageList = mallImageAssociativeService.selectImageByAssId( null, 4, CommonUtil.toInteger( map.get( "id" ) ) );
		    if ( imageList != null && imageList.size() > 0 ) {
			commentResult.setCommentImageList( imageList );
		    }
		}
	    }
	    commentList.add( commentResult );
	}
	result.setCommentResultList( commentList );
	result.setCurPage( page.getCurPage() );
	result.setPageCount( page.getPageCount() );
	return result;
    }

}
