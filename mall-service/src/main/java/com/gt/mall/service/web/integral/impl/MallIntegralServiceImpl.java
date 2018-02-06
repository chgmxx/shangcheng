package com.gt.mall.service.web.integral.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.integral.MallIntegralDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.integral.MallIntegral;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.integral.PhoneAddIntegralDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.integral.MallIntegralService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.fenbiFlow.BusFlowInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 积分商品表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallIntegralServiceImpl extends BaseServiceImpl< MallIntegralDAO,MallIntegral > implements MallIntegralService {

    private Logger log = Logger.getLogger( MallIntegralServiceImpl.class );

    @Autowired
    private MallIntegralDAO             integralDAO;
    @Autowired
    private MallImageAssociativeDAO     imageAssociativeDAO;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallProductDAO              productDAO;
    @Autowired
    private MallProductService          productService;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MallOrderDAO                orderDAO;
    @Autowired
    private MallOrderService            orderService;
    @Autowired
    private MallOrderDetailDAO          orderDetailDAO;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private FenBiFlowService            fenBiFlowService;
    @Autowired
    private MemberAddressService        memberAddressService;

    @Override
    public PageUtil selectIntegralByUserId( Map< String,Object > params ) {
        List< Map< String,Object > > productList = new ArrayList< Map< String,Object > >();
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
        params.put( "curPage", curPage );

        // 统计售罄商品
        int count = integralDAO.selectCountIntegralByShopids( params );

        PageUtil page = new PageUtil( curPage, pageSize, count, "phoneIntegral/79B4DE7C/toIndex.do" );
        int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
        params.put( "firstNum", firstNum );// 起始页
        params.put( "maxNum", pageSize );// 每页显示商品的数量

        List< Map< String,Object > > list = integralDAO.selectIntegralByShopids( params );
        List< Integer > proIds = new ArrayList<>();
        if ( list != null && list.size() > 0 ) {
            for ( Map< String,Object > map : list ) {
                if ( CommonUtil.isNotEmpty( map.get( "product_id" ) ) && !proIds.contains( CommonUtil.toInteger( map.get( "product_id" ) ) ) ) {
                    proIds.add( CommonUtil.toInteger( map.get( "product_id" ) ) );
                }
                productList.add( map );
            }
        }
        if ( proIds != null && proIds.size() > 0 ) {
            List< MallImageAssociative > imageList = mallImageAssociativeService.selectImageByAssIds( 1, 1, proIds );
            if ( imageList != null && imageList.size() > 0 ) {
                productList = new ArrayList<>();
                for ( Map< String,Object > map : list ) {
                    String id = map.get( "id" ).toString();
                    for ( int i = 0; i < imageList.size(); i++ ) {
                        MallImageAssociative imageAssociative = imageList.get( i );
                        if ( CommonUtil.isNotEmpty( imageAssociative.getImageUrl() ) && imageAssociative.getAssId().toString().equals( id ) ) {
                            map.put( "image_url", imageAssociative.getImageUrl() );
                            imageList.remove( i );
                            break;
                        }
                    }
                    productList.add( map );
                }
            }
        }
        page.setSubList( productList );
        return page;
    }

    //    @Override  调用memberService.findCardrecordList
    //    public PageUtil selectIntegralDetail( Member member, Map< String,Object > params ) {
    //	int pageSize = 20;
    //        int curPage = CommonUtil.isEmpty(params.get("curPage")) ? 1 : CommonUtil.toInteger(params.get("curPage"));
    //        String countSql = "SELECT count(id) FROM t_member_cardrecord WHERE recordType = 2 AND cardId=" + member.getMcId();
    //        int count = daoUtil.queryForInt(countSql);

    //        PageUtil page = new PageUtil(curPage, pageSize, count, "phoneIntegral/79B4DE7C/toIndex.do");
    //        int firstNum = pageSize * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);

    //        String sql = "SELECT itemName,number,date_format(createDate, '%Y-%c-%d %h:%i:%s') as createDate FROM t_member_cardrecord " +
    //                "WHERE recordType = 2 AND cardId=" + member.getMcId() + " order by id desc limit " + firstNum + "," + pageSize;
    //        List<Map<String, Object>> recordList = daoUtil.queryForList(sql);
    //        page.setSubList(recordList);
    //        return page;
    //	return null;
    //    }

    @Override
    public Map< String,Object > selectProductDetail( Member member, Map< String,Object > params ) {
        Map< String,Object > resultMap = new HashMap< String,Object >();
        int productId = CommonUtil.toInteger( params.get( "id" ) );
        MallProduct product = productDAO.selectById( productId );// 根据商品id查询商品的基本信息
        resultMap.put( "product", product );
        //查询商品规格
        if ( product.getIsSpecifica().toString().equals( "1" ) ) {
            Map< String,Object > guige = productInventoryService.productSpecifications( product.getId(), null );//查询商品的默认规格
            List< Map< String,Object > > specificaList = productSpecificaService.getSpecificaByProductId( product.getId() );//获取商品规格值
            List< Map< String,Object > > guigePriceList = productInventoryService.guigePrice( product.getId() );//获取商品所有规格
            if ( guige == null || specificaList == null || specificaList.size() == 0 ) {
                product.setIsSpecifica( 0 );
            }
            resultMap.put( "guige", guige );
            resultMap.put( "specificaList", specificaList );
            resultMap.put( "guigePriceList", guigePriceList );
        }

        Map< String,Object > integralParams = new HashMap< String,Object >();
        integralParams.put( "productId", product.getId() );
        integralParams.put( "isUse", 1 );
        integralParams.put( "shopId", product.getShopId() );
        List< MallIntegral > integralList = integralDAO.selectByIntegral( integralParams );
        if ( integralList != null && integralList.size() > 0 ) {
            MallIntegral integral = integralList.get( 0 );
            resultMap.put( "integral", integral );
            if ( CommonUtil.isNotEmpty( integral ) ) {

                Date endTime = DateTimeKit.parse( integral.getEndTime(), "yyyy-MM-dd HH:mm:ss" );
                Date startTime = DateTimeKit.parse( integral.getStartTime(), "yyyy-MM-dd HH:mm:ss" );
                Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
                if ( nowTime.getTime() < startTime.getTime() ) {
                    resultMap.put( "isNoStart", 1 );
                }
                if ( nowTime.getTime() > endTime.getTime() ) {
                    resultMap.put( "isEnd", 1 );
                }

                integral.setEndTime( DateTimeKit.getDateTime( endTime, "yyyy-MM-dd" ) );
                integral.setStartTime( DateTimeKit.getDateTime( startTime, "yyyy-MM-dd" ) );
            }
        }

        params.put( "assId", productId );
        params.put( "assType", 1 );
        // 查询商品图片
        List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( params );
        resultMap.put( "imageList", imageList );

        if ( CommonUtil.isNotEmpty( member ) && member.getId() > 0 ) {
            params.put( "productId", productId );
            params.put( "busUserId", product.getUserId() );
            params.put( "buyerUserId", member.getId() );
            List< Map< String,Object > > orderList = orderDAO.selectIntegralOrder( params );
            int recordNum = orderList.size();
            resultMap.put( "recordNum", recordNum );
        }

        if ( CommonUtil.isNotEmpty( product.getFlowId() ) && product.getFlowId() > 0 ) {
            BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );
            if ( CommonUtil.isNotEmpty( flow ) ) {
                resultMap.put( "flow_desc", flow.getType() + "M流量" );
            }
        }
        int proViewNum = 0;
        if ( CommonUtil.isNotEmpty( product.getViewsNum() ) ) {
            proViewNum = CommonUtil.toInteger( product.getViewsNum() );
        }
        updateProNum( product.getId().toString(), proViewNum );
        return resultMap;
    }

    private void updateProNum( String proId, Integer proViewNum ) {

        String key = Constants.REDIS_KEY + "proViewNum";
        int viewNum = 0;
        String viewNums = "";
        if ( JedisUtil.hExists( key, proId ) ) {
            viewNums = JedisUtil.maoget( key, proId );
        }
        if ( viewNums == null || viewNums.equals( "" ) ) {
            if ( CommonUtil.isNotEmpty( proViewNum ) ) {
                viewNums = proViewNum.toString();
            }
        }
        if ( viewNums != null && !viewNums.equals( "" ) ) {
            viewNum = CommonUtil.toInteger( viewNums );
        }
        if ( viewNum + 1 < 1000000000 ) {
            JedisUtil.map( key, proId, ( viewNum + 1 ) + "" );
        }
    }

    @Override
    public Map< String,Object > recordIntegral( PhoneAddIntegralDTO integralDTO, Member member, Integer browser, HttpServletRequest request ) {
        DecimalFormat df = new DecimalFormat( "######0.00" );
        Map< String,Object > resultMap = new HashMap< String,Object >();
        int productId = integralDTO.getProductId();
        MallProduct product = productDAO.selectById( productId );
        int integralId = integralDTO.getIntegralId();
        MallIntegral integral = integralDAO.selectById( integralId );
        double price = CommonUtil.toDouble( integral.getMoney() );
        Integer num = integralDTO.getProductNum();
        if ( CommonUtil.isEmpty( num ) || num == 0 ) {
            throw new BusinessException( ResponseEnums.ERROR.getCode(), "购买积分商品的数量不能小于1件" );
        }
        double totalPrice = CommonUtil.toDouble( df.format( price * num ) );
        int orderPayWay = 4;
        String proSpecificas = integralDTO.getProductSpecificas();
        String flowPhone = integralDTO.getFlowPhone();
        int proTypeId = product.getProTypeId();

        int memType = memberService.isCardType( member.getId() );
        if ( orderPayWay == 4 ) {//积分支付
            Integer mIntergral = member.getIntegral();
            if ( mIntergral < totalPrice || mIntergral < 0 ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), "您的积分不够，不能用积分来兑换这件商品" );
            }
        }
        Map< String,Object > result = orderService.calculateInventory( productId + "", proSpecificas, num + "" );
        if ( result.get( "result" ).toString().equals( "false" ) ) {
            throw new BusinessException( ResponseEnums.ERROR.getCode(), result.get( "msg" ).toString() );
            //	    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
            //	    resultMap.put( "msg", result.get( "msg" ) );
            //	    return resultMap;
        }
        if ( proTypeId == 0 && CommonUtil.isEmpty( integralDTO.getReceiveId() ) ) {
            resultMap.put( "code", 1 );
            resultMap.put( "proTypeId", proTypeId );
            return resultMap;
        }
    /*if ( CommonUtil.isNotEmpty( flowPhone ) ) {//流量充值，判断手机号码
	    Map< String,String > map = MobileLocationUtil.getMobileLocation( flowPhone );
	    if ( map.get( "code" ).equals( "-1" ) ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), map.get( "msg" ) );
	    } else if ( map.get( "code" ).equals( "1" ) && product.getFlowId() > 0 ) {
		BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );
		if ( map.get( "supplier" ).equals( "中国联通" ) && flow.getType() == 10 ) {
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "充值失败,联通号码至少30MB" );
		}
	    }
	}*/

        //	int code = -1;
        MallOrder order = new MallOrder();
        order.setOrderNo( "SC" + System.currentTimeMillis() );
        order.setOrderMoney( BigDecimal.valueOf( totalPrice ) );
        order.setOrderOldMoney( BigDecimal.valueOf( totalPrice ) );
        order.setOrderPayWay( orderPayWay );
        order.setBuyerUserId( member.getId() );
        if ( CommonUtil.isNotEmpty( member.getPublicId() ) ) {
            order.setSellerUserId( member.getPublicId() );
        }
        if ( CommonUtil.isNotEmpty( member.getBusid() ) ) {
            order.setBusUserId( member.getBusid() );
        }
        order.setShopId( product.getShopId() );
        order.setOrderStatus( 1 );
        order.setCreateTime( new Date() );
        //order.setPayTime(new Date());
        order.setOrderType( 2 );
        order.setMemCardType( memType );
        if ( CommonUtil.isNotEmpty( flowPhone ) ) {
            order.setFlowPhone( flowPhone );
            order.setFlowRechargeStatus( 0 );
        }
        if ( CommonUtil.isNotEmpty( integralDTO.getReceiveId() ) && integralDTO.getReceiveId() > 0 ) {
            MemberAddress memberAddress = memberAddressService.addreSelectId( integralDTO.getReceiveId() );
            if ( CommonUtil.isNotEmpty( memberAddress ) ) {
                String address = CommonUtil.toString( memberAddress.getProvincename() ) + CommonUtil.toString( memberAddress.getCityname() );
                if ( CommonUtil.isNotEmpty( memberAddress.getAreaname() ) ) {
                    address += CommonUtil.toString( memberAddress.getAreaname() );
                }
                address += CommonUtil.toString( memberAddress.getMemAddress() );
                if ( CommonUtil.isNotEmpty( memberAddress.getMemZipCode() ) ) {
                    address += CommonUtil.toInteger( memberAddress.getMemZipCode() );
                }
                order.setReceiveName( memberAddress.getMemName() );
                order.setReceivePhone( memberAddress.getMemPhone() );
                order.setReceiveAddress( address );
                order.setReceiveId( integralDTO.getReceiveId() );
            }
        }
        if ( CommonUtil.isNotEmpty( browser ) ) {
            order.setBuyerUserType( browser );
        }
        order.setMemberName( member.getNickname() );
        int count = orderDAO.insert( order );

        if ( count > 0 ) {
            MallOrderDetail detail = new MallOrderDetail();
            detail.setOrderId( order.getId() );
            detail.setProductId( productId );
            detail.setShopId( product.getShopId() );
            Map< String,Object > params = new HashMap<>();
            params.put( "assId", productId );
            params.put( "isMainImages", 1 );
            params.put( "assType", 1 );
            List< MallImageAssociative > imagesList = imageAssociativeDAO.selectImageByAssId( params );//获取商品图片
            if ( imagesList != null && imagesList.size() > 0 ) {
                detail.setProductImageUrl( imagesList.get( 0 ).getImageUrl() );
            }
            if ( CommonUtil.isNotEmpty( proSpecificas ) ) {
                detail.setProductSpecificas( CommonUtil.toString( proSpecificas ) );
                Map< String,Object > invMap = productService.getProInvIdBySpecId( proSpecificas.toString(), productId );
                detail.setProductSpeciname( invMap.get( "specifica_values" ).toString() );
                if ( CommonUtil.isNotEmpty( invMap.get( "specifica_img_url" ) ) ) {
                    detail.setProductImageUrl( invMap.get( "specifica_img_url" ).toString() );
                }
            }

            detail.setDetProNum( CommonUtil.toInteger( num ) );
            detail.setDetProPrice( BigDecimal.valueOf( price ) );
            detail.setDetProName( product.getProName() );
            if ( CommonUtil.isNotEmpty( product.getProCode() ) ) {
                detail.setDetProCode( product.getProCode() );
            }
            detail.setDetPrivivilege( BigDecimal.valueOf( price ) );
            //	    detail.setReturnDay( product.getReturnDay() );
            if ( CommonUtil.isNotEmpty( product.getIsReturn() ) ) {
                if ( product.getIsReturn().toString().equals( "1" ) ) {
                    detail.setReturnDay( 7 );//完成订单后在有效天数内退款
                }
            }
            detail.setCreateTime( new Date() );
            detail.setProTypeId( CommonUtil.toInteger( product.getProTypeId() ) );
            detail.setTotalPrice( totalPrice );
            if ( CommonUtil.isNotEmpty( product.getCardType() ) ) {
                detail.setCardReceiveId( product.getCardType() );
            }
            if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
                detail.setFlowId( product.getFlowId() );
            }
            if ( CommonUtil.isNotEmpty( product.getFlowRecordId() ) ) {
                detail.setFlowRecordId( product.getFlowRecordId() );
            }
            count = orderDetailDAO.insert( detail );
            if ( count > 0 ) {
                params.put( "status", 2 );
                params.put( "out_trade_no", order.getOrderNo() );
                if ( detail.getProTypeId() == 3 ) {
                    //会员卡券包查询页面
                    resultMap.put( "url", PropertiesUtil.getWxmpDomain() + "/duofenCardPhoneController/79B4DE7C/memberCardList.do?memberId=" + member.getId() );
                }
                orderService.paySuccessModified( params, member );//修改库存和订单状态
                //		code = 1;
            }
        }

        //	resultMap.put( "code", code );

        return resultMap;
    }

    @Override
    public PageUtil selectIntegralByPage( Map< String,Object > params, int userId, List< Map< String,Object > > shoplist ) {
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
        params.put( "curPage", curPage );
        int count = integralDAO.selectByCount( params );

        PageUtil page = new PageUtil( curPage, pageSize, count, "/mallIntegral/index.do" );
        int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
        params.put( "firstNum", firstNum );// 起始页
        params.put( "maxNum", pageSize );// 每页显示商品的数量

        if ( count > 0 ) {// 判断团购是否有数据
            List< Map< String,Object > > integralList = integralDAO.selectByPage( params );
            if ( integralList != null && integralList.size() > 0 ) {
                for ( Map< String,Object > integral : integralList ) {
                    int shopId = CommonUtil.toInteger( integral.get( "shop_id" ) );
                    for ( Map< String,Object > shopMap : shoplist ) {
                        int shop_id = CommonUtil.toInteger( shopMap.get( "id" ) );
                        if ( shop_id == shopId ) {
                            integral.put( "shopName", shopMap.get( "sto_name" ) );
                            break;
                        }
                    }
                }
            }
            page.setSubList( integralList );
        }
        return page;
    }

    @Override
    public Map< String,Object > selectByIds( int id ) {
        return integralDAO.selectByIds( id );
    }

    @Override
    public Map< String,Object > saveIntegral( int busUserId, Map< String,Object > params ) {
        int count = 0;
        Map< String,Object > resultMap = new HashMap< String,Object >();
        if ( CommonUtil.isNotEmpty( params.get( "integral" ) ) ) {
            MallIntegral mallIntegral = (MallIntegral) JSONObject.toJavaObject( JSONObject.parseObject( params.get( "integral" ).toString() ), MallIntegral.class );
            if ( CommonUtil.isEmpty( mallIntegral.getShopId() ) ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), "店铺不能为空" );
            }
            if ( CommonUtil.isEmpty( mallIntegral.getProductId() ) ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), "商品不能为空" );
            }
            if ( CommonUtil.isEmpty( mallIntegral.getMoney() ) ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), "积分不能为空" );
            }
            if ( CommonUtil.isEmpty( mallIntegral.getStartTime() ) || CommonUtil.isEmpty( mallIntegral.getEndTime() ) ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), "开始或结束时间不能为空" );
            }
            MallIntegral integral = null;
            if ( CommonUtil.isNotEmpty( mallIntegral.getProductId() ) ) {
                Map< String,Object > map = new HashMap< String,Object >();
                map.put( "productId", mallIntegral.getProductId() );
                map.put( "userId", busUserId );
                List< MallIntegral > list = integralDAO.selectByIntegral( map );
                if ( list != null && list.size() > 0 ) {
                    integral = list.get( 0 );
                }
            }
            if ( CommonUtil.isNotEmpty( mallIntegral.getId() ) ) {
                if ( CommonUtil.isNotEmpty( integral ) ) {
                    if ( !integral.getId().toString().equals( mallIntegral.getId().toString() ) ) {
                        resultMap.put( "msg", "您选择的商品已经设置了积分，请重新选择" );
                        resultMap.put( "flag", false );
                        return resultMap;
                    }
                }
                count = integralDAO.updateById( mallIntegral );
            } else {
                if ( CommonUtil.isNotEmpty( integral ) ) {
                    if ( CommonUtil.isNotEmpty( integral.getId() ) ) {
                        resultMap.put( "msg", "您选择的商品已经设置了积分，请重新选择" );
                        resultMap.put( "flag", false );
                        return resultMap;
                    }
                }
                mallIntegral.setUserId( busUserId );
                mallIntegral.setCreateTime( new Date() );
                count = integralDAO.insert( mallIntegral );
            }
        }
        if ( count > 0 ) {
            resultMap.put( "flag", true );
        } else {
            resultMap.put( "flag", false );
        }
        return resultMap;
    }

    @Override
    public boolean removeIntegral( Map< String,Object > params ) {
        int type = CommonUtil.toInteger( params.get( "type" ) );
        int id = CommonUtil.toInteger( params.get( "id" ) );
        MallIntegral integral = new MallIntegral();
        if ( type == -1 ) {//删除积分商品
            integral.setIsDelete( 1 );
        } else if ( type == -2 ) {//使失效积分商品
            integral.setIsUse( -1 );
        } else if ( type == 1 ) {//启用失效积分商品
            integral.setIsUse( 1 );
        }
        integral.setId( id );
        int count = integralDAO.updateById( integral );
        if ( count > 0 ) {
            return true;
        }
        return false;
    }
}
