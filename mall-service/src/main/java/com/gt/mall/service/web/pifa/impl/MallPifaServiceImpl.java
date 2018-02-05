package com.gt.mall.service.web.pifa.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.api.bean.session.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.pifa.MallPifaApplyDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.pifa.MallPifaPriceDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.entity.pifa.MallPifaPrice;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.param.phone.order.PhoneToOrderPifatSpecificaDTO;
import com.gt.mall.param.phone.order.PhoneOrderPifaSpecDTO;
import com.gt.mall.result.phone.product.PhonePifaProductDetailResult;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.pifa.MallPifaPriceService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.utils.PageUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 商品批发表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPifaServiceImpl extends BaseServiceImpl< MallPifaDAO,MallPifa > implements MallPifaService {

    private static Logger logger = LoggerFactory.getLogger( MallPifaServiceImpl.class );

    @Autowired
    private MallPifaApplyDAO mallPifaApplyDAO;

    @Autowired
    private MallPifaDAO mallPifaDAO;

    @Autowired
    private MallPifaPriceDAO mallPifaPriceDAO;

    @Autowired
    private MallPifaPriceService mallPifaPriceService;

    @Autowired
    private MallSearchKeywordService mallSearchKeywordService;

    @Autowired
    private MallPageService mallPageService;

    @Autowired
    private MallPifaApplyService mallPifaApplyService;

    @Autowired
    private MallPaySetService mallPaySetService;

    @Autowired
    private MallProductService mallProductService;

    @Override
    @Transactional( rollbackFor = Exception.class )
    public int addWholesaler( MallPifaApply pifaApply ) {
        int count = mallPifaApplyDAO.insert( pifaApply );
        return count;
    }

    @Override
    public MallPifaApply selectByPifaApply( MallPifaApply pifaApply ) {
        MallPifaApply mallPifaApply = mallPifaApplyDAO.selectByPifaApply( pifaApply );
        return mallPifaApply;
    }

    @Override
    public int updateSetWholesaler( Map< String,Object > map ) {
        int count = mallPifaApplyDAO.updateSetWholesaler( map );
        return count;
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public PageUtil wholesalerList( Map< String,Object > params ) {
        PageUtil page = null;
        params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1
            : CommonUtil.toInteger( params.get( "curPage" ) ) );
        int pageSize = 10;
        int rowCount = mallPifaApplyDAO.count( params );//查询批发商总条数
        page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ),
            pageSize, rowCount, "mallWholesalers/wholesaleList.do" );
        params.put( "firstResult", pageSize
            * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
        params.put( "maxResult", pageSize );
        //查询批发商列表
        List< Map< String,Object > > list = mallPifaApplyDAO.wholesalerList( params );
        List< Map< String,Object > > list1 = new ArrayList<>();
        for ( Map< String,Object > pifaMap : list ) {
            //微信昵称转换
            String nickname = CommonUtil.blob2String( pifaMap.get( "nickname" ) );
            //	    JSONObject obj = JSONObject.fromObject( pifaMap );
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
            JSONObject jsonObj = JSONObject.fromObject( pifaMap.get( "create_time" ) );
            JSONObject jsonObj1 = JSONObject.fromObject( pifaMap.get( "check_time" ) );
            //申请时间转换
            if ( jsonObj.containsKey( "time" ) ) {
                long time = Long.valueOf( ( jsonObj.get( "time" ).toString() ) );
                pifaMap.put( "create_time", sdf.format( new Date( time ) ) );
            }
            //审核时间转换
            if ( jsonObj1.containsKey( "time" ) ) {
                long time = Long.valueOf( ( jsonObj1.get( "time" ).toString() ) );
                pifaMap.put( "check_time", sdf.format( new Date( time ) ) );
            }
            pifaMap.put( "nickname", nickname );

            String key = Constants.REDIS_KEY + "syncOrderCount";
            String member_id = pifaMap.get( "member_id" ).toString();
            if ( JedisUtil.hExists( key, member_id ) ) {
                String str = JedisUtil.maoget( key, member_id );
                if ( CommonUtil.isNotEmpty( str ) ) {
                    JSONObject orderObj = JSONObject.fromObject( str );
                    if ( CommonUtil.isNotEmpty( orderObj.get( "num" ) ) ) {
                        pifaMap.put( "num", orderObj.get( "num" ) );
                    }
                    if ( CommonUtil.isNotEmpty( orderObj.get( "proPrice" ) ) ) {
                        pifaMap.put( "money", orderObj.get( "proPrice" ) );
                    }
                }
            }
            if ( pifaMap.containsKey( "telephone" ) && CommonUtil.isNotEmpty( pifaMap.get( "telephone" ) ) ) {
                String[] telephone = pifaMap.get( "telephone" ).toString().split( "," );
                if ( telephone.length == 2 ) {
                    pifaMap.put( "telephone", "+" + telephone[0] + " " + telephone[1] );
                }
            }

            list1.add( pifaMap );
        }
        page.setSubList( list1 );
        return page;
    }

    @Override
    public int updateStatus( Map< String,Object > params ) {
        return mallPifaApplyDAO.updateStatus( params );
    }

    @Override
    public PageUtil pifaProductList( Map< String,Object > params, List< Map< String,Object > > shoplist ) {
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1
            : CommonUtil.toInteger( params.get( "curPage" ) );
        params.put( "curPage", curPage );
        int count = mallPifaDAO.selectByCount( params );

        PageUtil page = new PageUtil( curPage, pageSize, count, "mallWholesalers/index.do" );
        int firstNum = pageSize
            * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
        params.put( "firstNum", firstNum );// 起始页
        params.put( "maxNum", pageSize );// 每页显示商品的数量

        if ( count > 0 ) {// 判断批发是否有数据
            List< MallPifa > pifaList = mallPifaDAO.selectByPage( params );
            if ( pifaList != null && pifaList.size() > 0 ) {
                for ( MallPifa pifa : pifaList ) {
                    //循环门店
                    for ( Map< String,Object > storeMap : shoplist ) {
                        int shopId = CommonUtil.toInteger( storeMap.get( "id" ) );
                        if ( shopId == pifa.getShopId() ) {
                            pifa.setShopName( storeMap.get( "sto_name" ).toString() );
                            break;
                        }
                    }
                }
            }
            page.setSubList( pifaList );
        }

        return page;
    }

    /**
     * 通过批发id查询批发信息
     */
    @Override
    public Map< String,Object > selectPifaById( Integer id ) {
        Map< String,Object > map = mallPifaDAO.selectByPifaId( id );
        int pifaId = CommonUtil.toInteger( map.get( "id" ) );
        List< MallPifaPrice > priceList = mallPifaPriceDAO.selectPriceByGroupId( pifaId );
        map.put( "priceList", priceList );
        return map;
    }

    /**
     * 编辑批发
     *
     * @Title: editFreight
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int editPifa( Map< String,Object > pifaMap, int userId ) {
        int num = 0;
        boolean flag = false;
        int code = -1;
        int status = 0;
        if ( CommonUtil.isNotEmpty( pifaMap.get( "pifa" ) ) ) {
            MallPifa pifa = com.alibaba.fastjson.JSONObject.parseObject( pifaMap.get( "pifa" ).toString(), MallPifa.class );
            // 判断选择的商品是否已经存在未开始和进行中的批发中
            List< MallPifa > buyList = mallPifaDAO.selectStartPifaByProId( pifa );
            if ( buyList == null || buyList.size() == 0 ) {
                pifa.setUserId( userId );
                if ( CommonUtil.isNotEmpty( pifa.getId() ) ) {
                    // 判断本商品是否正在批发中
                    MallPifa buy = mallPifaDAO.selecPifaByIds( pifa.getId() );
                    if ( buy.getStatus() == 1 && buy.getJoinId() > 0 ) {// 正在进行批发的商品不能修改
                        code = -2;
                        status = buy.getStatus();
                    } else {
                        MallPifa buyOld = mallPifaDAO.selectById( pifa.getId() );
                        if ( !buyOld.getProductId().equals( pifa.getProductId() ) ) {// 用户更换了商品
                            flag = true;
                        }
                        num = mallPifaDAO.updateById( pifa );
                    }
                } else {
                    pifa.setCreateTime( new Date() );
                    num = mallPifaDAO.insert( pifa );
                }
                if ( CommonUtil.isNotEmpty( pifa.getId() ) ) {
                    if ( status != 1 ) {
                        mallPifaPriceService.editPifaPrice( pifaMap, pifa.getId(), flag );
                    }
                }
            } else {
                code = 0;
            }
            if ( num > 0 ) {
                code = 1;
            }
        }
        return code;
    }

    /**
     * 删除批发
     *
     * @Title: deleteFreight
     */
    @Override
    public boolean deletePifa( MallPifa pifa ) {
        int num = mallPifaDAO.updateById( pifa );
        if ( num > 0 ) {
            return true;
        }
        return false;
    }

    /**
     * 根据店铺id查询批发信息
     */
    @Override
    public List< Map< String,Object > > selectgbPifaByShopId(
        Map< String,Object > maps ) {
        List< Map< String,Object > > productList = mallPifaDAO
            .selectgbProductByShopId( maps );
        return productList;
    }

    /**
     * 获取店铺下所有的批发
     */
    @Override
    public PageUtil getPifaAll( Member member, Map< String,Object > maps ) {

        int shopid = 0;
        if ( CommonUtil.isNotEmpty( maps.get( "shopId" ) ) ) {
            shopid = CommonUtil.toInteger( maps.get( "shopId" ) );
        }
        //新增搜索关键词
        mallSearchKeywordService.insertSeachKeyWord( member.getId(), shopid, maps.get( "proName" ) );

        int pageSize = 10;
        int curPage = CommonUtil.isEmpty( maps.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( maps.get( "curPage" ) );
        int rowCount = mallPifaDAO.selectCountgbProductByShopId( maps );
        PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
        maps.put( "firstNum", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
        maps.put( "maxNum", pageSize );
        maps.put( "status", 1 );

        maps.put( "status", 1 );
        List< Map< String,Object > > productList = mallPifaDAO.selectgbProductByShopId( maps );

        if ( productList != null && productList.size() > 0 ) {
            String specImgIds = "";
            List< Integer > proIds = new ArrayList<>();
            for ( int i = 0; i < productList.size(); i++ ) {
                Map< String,Object > map1 = productList.get( i );

                if ( CommonUtil.isNotEmpty( map1.get( "specifica_img_id" ) ) ) {
                    if ( !map1.get( "specifica_img_id" ).toString().equals( "0" ) ) {
                        if ( CommonUtil.isNotEmpty( specImgIds ) ) {
                            specImgIds += ",";
                        }
                        specImgIds += map1.get( "specifica_img_id" ).toString();
                    }
                }
                if ( CommonUtil.isNotEmpty( map1.get( "id" ) ) ) {
                    proIds.add( CommonUtil.toInteger( map1.get( "id" ) ) );
                }
            }
            String[] split = null;
            if ( CommonUtil.isNotEmpty( specImgIds ) ) {
                split = specImgIds.split( "," );
            }
            productList = mallPageService.getProductImages( productList, proIds );
        }
        List< Map< String,Object > > list = getHomeParams( productList );
        page.setSubList( list );
        return page;
    }

    public List< Map< String,Object > > getHomeParams( List< Map< String,Object > > productList ) {
        List< Map< String,Object > > list = new ArrayList<>();// 存放店铺下的商品
        if ( productList != null && productList.size() > 0 ) {
            for ( Map< String,Object > map2 : productList ) {
                String is_specifica = map2.get( "is_specifica" ).toString();// 是否有规格，1有规格，有规格取规格里面的值
                if ( is_specifica.equals( "1" ) ) {
                    //					map2.put("old_price", map2.get("inv_price"));
                    map2.put( "price", map2.get( "inv_price" ) );
                    String specifica_img_id = map2.get( "specifica_img_id" ).toString();
                    if ( specifica_img_id.equals( "0" ) ) {
                        map2.put( "image_url", map2.get( "image_url" ) );
                    } else {
                        map2.put( "image_url", map2.get( "specifica_img_url" ) );
                    }
                } else {
                    map2.put( "price", map2.get( "pro_price" ) );
                    //					map2.put("old_price", map2.get("pro_price"));
                    map2.put( "image_url", map2.get( "image_url" ) );
                }
                map2.put( "old_price", map2.get( "pro_cost_price" ) );
                String is_member_discount = map2.get( "is_member_discount" )
                    .toString();// 商品是否参加折扣,1参加折扣
                if ( is_member_discount.equals( "1" ) ) {
                    /*map2.put("price", Math.ceil((Double.parseDouble(map2.get(
							"price").toString()) * discount) * 100) / 100);*/
                }
                if ( CommonUtil.isNotEmpty( map2.get( "price" ) ) ) {
                    map2.put( "price", Math.ceil( ( Double.parseDouble( map2.get(
                        "price" ).toString() ) ) * 100 ) / 100 );
                }
                Date endTime = DateTimeKit.parse(
                    map2.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
                Date startTime = DateTimeKit.parse(
                    map2.get( "startTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
                Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(),
                    "yyyy-MM-dd HH:mm:ss" );
                Date date = new Date();
                map2.put( "times", ( endTime.getTime() - nowTime.getTime() ) / 1000 );
                int isEnd = 1;
                if ( startTime.getTime() < date.getTime() && endTime.getTime() <= date.getTime() ) {
                    isEnd = -1;
                }
                map2.put( "isEnd", isEnd );
                int status = -1;
                if ( startTime.getTime() > date.getTime() && endTime.getTime() > date.getTime() ) {
                    status = 0;
                    map2.put( "startTimes", ( startTime.getTime() - nowTime.getTime() ) / 1000 );
                } else if ( startTime.getTime() <= date.getTime() && date.getTime() < endTime.getTime() ) {
                    status = 1;
                }
                map2.put( "status", status );
                list.add( map2 );
            }
        }
        return list;
    }

    /**
     * 根据商品id查询秒杀信息和秒杀价格
     *
     * @return
     */
    @Override
    public MallPifa getPifaByProId( Integer proId, Integer shopId, int activityId ) {
        MallPifa pifa = new MallPifa();
        pifa.setProductId( proId );
        pifa.setShopId( shopId );
        if ( activityId > 0 ) {
            pifa.setId( activityId );
        }
        pifa = mallPifaDAO.selectBuyByProductId( pifa );
        if ( pifa != null && CommonUtil.isNotEmpty( pifa.getId() ) ) {
            Date endTime = DateTimeKit.parse( pifa.getPfEndTime().toString(),
                "yyyy-MM-dd HH:mm:ss" );
            Date startTime = DateTimeKit.parse( pifa.getPfStartTime().toString(),
                "yyyy-MM-dd HH:mm:ss" );
            Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(),
                "yyyy-MM-dd HH:mm:ss" );
            pifa.setTimes( ( endTime.getTime() - nowTime.getTime() ) / 1000 );
            if ( pifa.getStatus() == null || pifa.getStatus() == 0 ) {
                pifa.setStartTimes( ( startTime.getTime() - nowTime.getTime() ) / 1000 );
            }

            List< MallPifaPrice > priceList = mallPifaPriceService.selectPriceByGroupId( pifa.getId() );
            pifa.setPriceList( priceList );
            return pifa;
        }
        return null;
    }

    @Override
    public MallPifa selectBuyByProductId( Integer proId, Integer shopId, int activityId ) {
        MallPifa pifa = new MallPifa();
        pifa.setProductId( proId );
        pifa.setShopId( shopId );
        if ( activityId > 0 ) {
            pifa.setId( activityId );
        }
        return mallPifaDAO.selectBuyByProductId( pifa );
    }

    @Override
    public int updateWholesaleApplay( MallPifaApply applay ) {
        return mallPifaApplyDAO.updateById( applay );
    }

    @Override
    public double getPifaPriceByProIds( boolean isPifa, int productId ) {
        if ( isPifa ) {
            Map< String,Object > params = new HashMap<>();
            params.put( "pfType", 1 );
            params.put( "product_id", productId );
            //查询商品是否已经加入批发
            List< MallPifa > pifaList = mallPifaDAO.selectStartPiFaByProductId( params );
            if ( pifaList != null && pifaList.size() > 0 && isPifa ) {
                MallPifa pifa = pifaList.get( 0 );
                if ( CommonUtil.isNotEmpty( pifa.getPfPrice() ) ) {
                    return CommonUtil.toDouble( pifa.getPfPrice() );
                }
            }
        }
        return -1;
    }

    /**
     * 搜索店铺下所有的批发商品
     */
    @Override
    public PageUtil searchPifaAll( PhoneSearchProductDTO searchProductDTO, Member member ) {
        int pageSize = 10;
        int curPage = CommonUtil.isEmpty( searchProductDTO.getCurPage() ) ? 1 : searchProductDTO.getCurPage();
        int rowCount = mallPifaDAO.selectCountGoingPifaProduct( searchProductDTO );
        PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
        searchProductDTO.setFirstNum( pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
        searchProductDTO.setMaxNum( pageSize );

        List< Map< String,Object > > productList = mallPifaDAO.selectGoingPifaProduct( searchProductDTO );

        page.setSubList( mallPageService.getSearchProductParam( productList, 1, searchProductDTO ) );
        return page;
    }

    @Override
    public PhoneProductDetailResult getPifaProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result, Member member, MallPaySet mallPaySet ) {
	/*if ( activityId == 0 ) {
	    return result;
	}*/
        PhonePifaProductDetailResult pifaResult = new PhonePifaProductDetailResult();
        boolean isOpenPifa = false;
        if ( CommonUtil.isNotEmpty( mallPaySet ) ) {
            if ( CommonUtil.isNotEmpty( mallPaySet.getIsPf() ) ) {//是否开启批发
                if ( mallPaySet.getIsPf().toString().equals( "1" ) ) {
                    isOpenPifa = true;
                    if ( CommonUtil.isNotEmpty( mallPaySet.getPfSet() ) ) {
                        com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject( mallPaySet.getPfSet() );
                        pifaResult.setPfSetObj( obj );
                    }
                }
            }
        }
        if ( !isOpenPifa ) {
            return result;
        }
        //通过商品id查询批发信息
        MallPifa pifa = getPifaByProId( proId, shopId, activityId );
        if ( pifa == null ) {
            return result;
        }
        if ( pifa.getStatus() == -1 || pifa.getStatus() == -2 ) {
            return result;
        }
        result.setPfPrice( CommonUtil.toDouble( pifa.getPfPrice() ) );
        result.setActivityTimes( pifa.getTimes() );
        result.setActivityStatus( pifa.getStatus() );
        int pfStatus = mallPifaApplyService.getPifaApplay( member, mallPaySet );
        pifaResult.setPfType( pifa.getPfType() );
        pifaResult.setPfStatus( pfStatus );//批发状态
        result.setActivityId( pifa.getId() );
        double groupPrice = CommonUtil.toDouble( pifa.getPfPrice() );
        List< Integer > invIdList = new ArrayList<>();
        if ( CommonUtil.isNotEmpty( pifa.getPriceList() ) ) {
            for ( MallPifaPrice price : pifa.getPriceList() ) {
                if ( price.getIsJoinGroup() == 1 ) {
                    if ( result.getInvId() == 0 ) {
                        groupPrice = CommonUtil.toDouble( price.getSeckillPrice() );
                        result.setInvId( price.getInvenId() );
                    }
                    if ( result.getInvId() > 0 ) {
                        invIdList.add( price.getInvenId() );
                        if ( result.getInvId() == price.getInvenId() ) {
                            result.setPfPrice( CommonUtil.toDouble( price.getSeckillPrice() ) );
                        }
                    }
                }
            }
        }
        String errorMsg = "";
        if ( pifa.getStatus() == 0 ) {
            errorMsg = "该商品批发还未开始，请耐心等待";
        } else if ( pifa.getStatus() == -1 ) {
            errorMsg = "该商品批发已结束，暂不能批发";
        }
        String msg = CommonUtil.getPifaErrorMsg( pfStatus );
        if ( CommonUtil.isNotEmpty( msg ) ) {
            errorMsg = msg;
        }
        pifaResult.setPfErrorMsg( errorMsg );
        result.setPifaResult( pifaResult );
        result.setInvIdList( invIdList );
        result.setPfPrice( groupPrice );
        return result;
    }

    @Override
    public boolean pifaProductCanBuy( int pifaId, int invId, int productNum, int memberId, int memberBuyNum, MallPaySet mallPaySet ) {
        if ( CommonUtil.isEmpty( pifaId ) || pifaId == 0 ) {
            return false;
        }
        boolean isOpenPifa = false;
        if ( CommonUtil.isNotEmpty( mallPaySet ) ) {
            if ( CommonUtil.isNotEmpty( mallPaySet.getIsPf() ) ) {//是否开启批发
                if ( mallPaySet.getIsPf().toString().equals( "1" ) ) {
                    isOpenPifa = true;
                }
            }
        }
        if ( !isOpenPifa ) {
            throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "商家还未开启批发活动，不能购买批发" );
        }
        //通过商品id查询批发信息
        MallPifa pifa = new MallPifa();
        pifa.setId( pifaId );
        pifa = mallPifaDAO.selectBuyByProductId( pifa );
        if ( CommonUtil.isEmpty( pifa ) ) {
            throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的批发商品被删除或已失效" );
        }
        if ( pifa.getStatus() == 0 ) {
            throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的批发商品活动还未开始" );
        } else if ( pifa.getStatus() == -1 ) {
            throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的批发商品活动已结束" );
        }
        if ( invId > 0 ) {
            List< MallPifaPrice > priceList = mallPifaPriceService.selectPriceByInvId( pifaId, invId );
            if ( priceList != null && priceList.size() > 0 ) {
                MallPifaPrice buyPrice = priceList.get( 0 );
                if ( buyPrice.getIsJoinGroup() == 0 ) {
                    throw new BusinessException( ResponseEnums.INV_NO_JOIN_ERROR.getCode(), "该规格未参加批发活动" );
                }
            } else {
                throw new BusinessException( ResponseEnums.INV_NO_JOIN_ERROR.getCode(), "该规格未参加批发活动" );
            }
        }
        return false;
    }

    @Override
    public Map< String,Object > getPifaSet( int busId ) {
        //通过商品id查询批发信息
        MallPaySet set = mallPaySetService.selectByUserId( busId );
        double hpMoney = 0;
        int hpNum = 1;//混批件数
        int spHand = 1;
        if ( CommonUtil.isNotEmpty( set ) ) {
            if ( CommonUtil.isNotEmpty( set.getIsPf() ) ) {//是否开启批发
                if ( CommonUtil.isNotEmpty( set.getPfSet() ) ) {
                    JSONObject obj = JSONObject.fromObject( set.getPfSet() );
                    if ( CommonUtil.isNotEmpty( obj.get( "isHpMoney" ) ) ) {
                        if ( obj.get( "isHpMoney" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( obj.get( "hpMoney" ) ) ) {
                            hpMoney = CommonUtil.toDouble( obj.get( "hpMoney" ) );
                        }
                    }
                    if ( CommonUtil.isNotEmpty( obj.get( "isHpNum" ) ) ) {
                        if ( obj.get( "isHpNum" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( obj.get( "hpNum" ) ) ) {
                            hpNum = CommonUtil.toInteger( obj.get( "hpNum" ) );
                        }
                    }
                    if ( CommonUtil.isNotEmpty( obj.get( "isSpHand" ) ) ) {
                        if ( obj.get( "isSpHand" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( obj.get( "spHand" ) ) ) {
                            spHand = CommonUtil.toInteger( obj.get( "spHand" ) );
                        }
                    }
                }
            }
        }
        Map< String,Object > pfMap = new HashMap<>();
        pfMap.put( "hpMoney", hpMoney );
        pfMap.put( "hpNum", hpNum );
        pfMap.put( "spHand", spHand );
        return pfMap;
    }

    public List< PhoneOrderPifaSpecDTO > getPifaPrice( int proId, int shopId, int activityId, List< PhoneToOrderPifatSpecificaDTO > specificaIdsList ) {
        List< MallPifaPrice > priceList = mallPifaPriceService.selectPriceByGroupId( activityId );
        List< PhoneOrderPifaSpecDTO > pfSpecResultList = new ArrayList<>();

        if ( CommonUtil.isNotEmpty( priceList ) && priceList.size() > 0 ) {

            for ( MallPifaPrice price : priceList ) {
                if ( price.getIsJoinGroup() == 1 ) {
                    PhoneOrderPifaSpecDTO pfSpecResult = new PhoneOrderPifaSpecDTO();
                    if ( specificaIdsList != null && specificaIdsList.size() > 0 ) {
                        for ( PhoneToOrderPifatSpecificaDTO specificaDTO : specificaIdsList ) {
                            if ( specificaDTO.getSpecificaValueIds().equals( price.getSpecificaIds() ) ) {

                                Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( price.getSpecificaIds(), proId );
                                if ( CommonUtil.isNotEmpty( invMap ) ) {
                                    pfSpecResult.setSpecificaValues( CommonUtil.toString( invMap.get( "specifica_values" ) ) );
                                    pfSpecResult.setSpecificaName( CommonUtil.toString( invMap.get( "specificaName" ) ) );
                                }
                                pfSpecResult.setSpecificaIds( price.getSpecificaIds() );
                                pfSpecResult.setPfPrice( CommonUtil.toDouble( price.getSeckillPrice() ) );
                                pfSpecResult.setTotalNum( specificaDTO.getProductNum() );
                                pfSpecResultList.add( pfSpecResult );
                            }
                        }
                    }
                }
            }

        }
        return pfSpecResultList;
    }
}
