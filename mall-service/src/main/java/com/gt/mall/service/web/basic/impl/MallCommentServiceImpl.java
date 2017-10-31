package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.api.bean.session.Member;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.basic.MallCommentGiveService;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.utils.CommonUtil;
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
    private MallOrderDetailDAO          mallOrderDetailDAO;
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
		    if ( map.get( "is_upload_image" ).toString().equals( "1" ) ) {
			Map< String,Object > imageMap = new HashMap< String,Object >();
			imageMap.put( "assId", map.get( "id" ) );
			imageMap.put( "assType", 4 );
			List< MallImageAssociative > imageList = mallImageAssociativeDAO.selectImageByAssId( imageMap );
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
			List childList = mallCommentDAO.ownerResponseList( param );
			if ( childList != null && childList.size() > 0 ) {
			    map.put( "chilComment", childList.get( 0 ) );
			}
		    }
		}
		if ( shoplist != null && shoplist.size() > 0 ) {
		    for ( Map< String,Object > shopMap : shoplist ) {
			if ( map.get( "shop_id" ).toString().equals( shopMap.get( "id" ).toString() ) ) {
			    map.put( "sto_name", shopMap.get( "sto_name" ) );
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
    public boolean checkComment( Map< String,Object > params ) {
	if ( CommonUtil.isNotEmpty( params.get( "ids" ) ) ) {

	    String[] ids = JSONArray.parseArray( params.get( "ids" ).toString() ).toArray( new String[0] );
	    params.put( "ids", ids );
	}
	int count = mallCommentDAO.batchUpdateComment( params );
	if ( count > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean replatComment( Map< String,Object > params, int userId ) {
	if ( CommonUtil.isNotEmpty( params.get( "params" ) ) ) {
	    MallComment comment = (MallComment) JSONObject.toJavaObject( JSONObject.parseObject( params.get( "params" ).toString() ), MallComment.class );
	    comment.setCreateTime( new Date() );
	    comment.setUserId( userId );
	    comment.setUserType( 2 );
	    int count = mallCommentDAO.insert( comment );
	    if ( count > 0 ) {
		MallComment pComment = new MallComment();
		pComment.setId( comment.getRepPId() );
		pComment.setIsRep( "1" );
		mallCommentDAO.updateById( pComment );
		return true;
	    }
	}
	return false;
    }

    @Override
    public MallComment addAppraise( Map< String,Object > map, MallComment mallComment, HttpServletRequest request ) {
	logger.info( "进入添加评论方法" );

	mallComment.setCreateTime( new Date() );
	int count = mallCommentDAO.insert( mallComment );

	if ( count > 0 ) {
	    MallOrderDetail orderDetail = new MallOrderDetail();
	    orderDetail.setAppraiseStatus( 1 );
	    Wrapper wrapper = new EntityWrapper();
	    wrapper.where( "id={0} ", mallComment.getOrderDetailId() );
	    mallOrderDetailDAO.update( orderDetail, wrapper );//评论添加完成，修改订单详情待评价的的状态

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
		    mallImageAssociativeDAO.insert( ass );//添加图片
		}
	    }
	    mallCommentGiveService.commentGive( mallComment.getId(), request, mallComment.getUserId() );

	    return mallComment;
	}
	return null;
    }

    @Override
    public PageUtil myAppraise( Map< String,Object > param ) {
	param.put( "curPage", CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) ) );
	int pageSize = 10000;
	int rowCount = mallCommentDAO.countAppraise( param );//评论条数
	PageUtil page = new PageUtil( CommonUtil.toInteger( param.get( "curPage" ) ), pageSize, rowCount, "mMember/79B4DE7C/myAppraise.do" );
	param.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	param.put( "maxResult", pageSize );

	List< Map< String,Object > > list = mallCommentDAO.findByPage( param );//查询我的评论
	List< Map< String,Object > > list1 = new ArrayList< Map< String,Object > >();
	for ( int i = 0; i < list.size(); i++ ) {
	    Map< String,Object > p = list.get( i );
	    param.put( "appraise", p.get( "id" ) );//评论id
	    List hf = mallCommentDAO.ownerResponseList( param );//查询店家回复
	    for ( int j = 0; j < hf.size(); j++ ) {
		JSONObject h = JSONObject.parseObject( hf.get( j ).toString() );
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
		    List< MallImageAssociative > imageList = mallImageAssociativeDAO.selectImageByAssId( imageMap );
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

    @Override
    public MallComment selectComment( MallComment comment ) {
	return mallCommentDAO.selectByComment( comment );
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
		if ( set.getIsCommentCheck().toString().equals( "1" ) ) {
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
		/*Map gradeMap = memberService.findGradeType( CommonUtil.toInteger( map.get( "user_id" ) ) );//查询会员卡片名称
		if ( CommonUtil.isNotEmpty( gradeMap ) ) {
		    map.put( "gradeTypeName", gradeMap.get( "gtName" ) );
		}*/
		if ( map.get( "is_rep" ).toString().equals( "1" ) ) {
		    //查询回复内容
		    Map< String,Object > replyMap = new HashMap<>();
		    replyMap.put( "appraise", id );
		    List< Map< String,Object > > replayList = mallCommentDAO.ownerResponseList( replyMap );
		    if ( replayList != null && replayList.size() > 0 ) {
			map.put( "replyContent", replayList.get( 0 ).get( "content" ) );
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

}
