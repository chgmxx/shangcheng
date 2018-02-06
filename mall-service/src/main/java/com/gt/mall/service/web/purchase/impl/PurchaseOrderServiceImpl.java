package com.gt.mall.service.web.purchase.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.purchase.*;
import com.gt.mall.entity.purchase.*;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.service.web.purchase.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Service
public class PurchaseOrderServiceImpl extends BaseServiceImpl< PurchaseOrderDAO,PurchaseOrder > implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderDAO         purchaseOrderDAO;
    @Autowired
    private PurchaseOrderDetailsDAO  purchaseOrderDetailsDAO;
    @Autowired
    private PurchaseLanguageDAO      purchaseLanguageDAO;
    @Autowired
    private PurchaseContractOrderDAO purchaseContractOrderDAO;
    @Autowired
    private PurchaseTermDAO          purchaseTermDAO;
    @Autowired
    private PurchaseCarouselDAO      purchaseCarouselDAO;
    @Autowired
    private MallProductDAO           productDAO;

    /**
     * 分页查询数据
     *
     * @param parms
     *
     * @return
     */
    @Override
    public PageUtil findList( Map< String,Object > parms ) {
        try {
            int pageSize = 10;
            int count = 0;
            List< Map< String,Object > > map = new ArrayList< Map< String,Object > >();
            int curPage = CommonUtil.isEmpty( parms.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( parms.get( "curPage" ) );
            count = purchaseOrderDAO.findListCount( parms );
            PageUtil page = new PageUtil( curPage, pageSize, count, "" );
            //            page.setUrl("orderForm");
            parms.put( "pageFirst", ( page.getCurPage() - 1 ) * 10 );
            parms.put( "pageLast", 10 );
            if ( count > 0 ) {
                map = purchaseOrderDAO.findList( parms );
            }
            if ( map.size() > 0 ) {
                String orderIds = "";
                for ( int i = 0; i < map.size(); i++ ) {
                    if ( orderIds.equals( "" ) ) {
                        orderIds += map.get( i ).get( "id" );
                    } else {
                        orderIds += "," + map.get( i ).get( "id" );
                    }
                }
                List< Map< String,Object > > languageMap = purchaseLanguageDAO.findLanguangeNotRead( orderIds );
                if ( languageMap.size() > 0 ) {
                    for ( int a = 0; a < languageMap.size(); a++ ) {
                        for ( int i = 0; i < map.size(); i++ ) {
                            PurchaseLanguage language = (PurchaseLanguage) languageMap.get( a );
                            if ( language.getOrderId().toString().equals( map.get( i ).get( "id" ).toString() ) ) {
                                map.get( i ).put( "is_notRead", "0" );
                            }
                        }
                    }
                }
            }
            page.setSubList( map );
            return page;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存订单
     */
    @Override
    public Map< String,Object > saveOrder( PurchaseOrder order, List< PurchaseOrderDetails > orderDetailsList, List< PurchaseTerm > termList,
        List< PurchaseCarousel > carouselList ) {

        Map< String,Object > map = new HashMap< String,Object >();
        try {
            if ( order.getId() != null ) {
                //修改订单
                purchaseOrderDAO.updateById( order );
            } else {
                //新增订单
                order.setCreateDate( new Date() );
                order.setOrderStatus( "1" );
                order.setOrderNumber( "CG" + new Date().getTime() );
                purchaseOrderDAO.insert( order );
                //		for ( int i = 0; i < termList.size(); i++ ) {
                //		    termList.get( i ).setOrderId( order.getId() );
                //		}
                //		for ( int i = 0; i < carouselList.size(); i++ ) {
                //		    carouselList.get( i ).setOrderId( order.getId() );
                //		    carouselList.get( i ).setBusId( order.getBusId() );
                //		}
            }
            if ( CommonUtil.isNotEmpty( order.getId() ) ) {
                String detailIds = "";
                //修改订单详情的数据
                for ( int i = 0; i < orderDetailsList.size(); i++ ) {
                    orderDetailsList.get( i ).setProductName( CommonUtil.urlEncode( orderDetailsList.get( i ).getProductName() ) );
                    if ( orderDetailsList.get( i ).getId() == null ) {
                        orderDetailsList.get( i ).setOrderId( order.getId() );
                        purchaseOrderDetailsDAO.insert( orderDetailsList.get( i ) );
                    } else {
                        purchaseOrderDetailsDAO.updateById( orderDetailsList.get( i ) );
                    }
                    if ( detailIds.equals( "" ) ) {
                        detailIds += orderDetailsList.get( i ).getId();
                    } else {
                        detailIds += "," + orderDetailsList.get( i ).getId();
                    }
                }
                if ( !detailIds.equals( "" ) ) {
                    Map< String,Object > parms = new HashMap< String,Object >();
                    parms.put( "detailIds", detailIds );
                    parms.put( "orderId", order.getId() );
                    //删除该订单下的未保留分期信息
                    purchaseOrderDetailsDAO.deleteDetailsByOrderId( parms );
                }
                if ( order.getHaveContract().equals( "0" ) ) {
                    purchaseContractOrderDAO.deleteContractOrderData( order.getId() );
                    PurchaseContractOrder contractOrder = new PurchaseContractOrder();
                    contractOrder.setContractId( Integer.parseInt( order.getContractId() ) );
                    contractOrder.setOrderId( order.getId() );
                    purchaseContractOrderDAO.insert( contractOrder );
                }
                if ( order.getOrderType().equals( "1" ) ) {
                    String termIds = "";
                    //重新把新的分期信息录入
                    for ( int a = 0; a < termList.size(); a++ ) {
                        if ( CommonUtil.isNotEmpty( termList.get( a ).getId() ) ) {
                            purchaseTermDAO.updateById( termList.get( a ) );
                        } else {
                            termList.get( a ).setOrderId( order.getId() );
                            purchaseTermDAO.insert( termList.get( a ) );
                        }
                        if ( termIds.equals( "" ) ) {
                            termIds += termList.get( a ).getId();
                        } else {
                            termIds += "," + termList.get( a ).getId();
                        }
                    }
                    if ( !termIds.equals( "" ) ) {
                        Map< String,Object > parms = new HashMap< String,Object >();
                        parms.put( "termIds", termIds );
                        parms.put( "orderId", order.getId() );
                        //删除该订单下的未保留分期信息
                        purchaseTermDAO.deleteTermByOrderId( parms );
                    }
                }
                if ( carouselList.size() > 0 ) {
                    String carouselIds = "";
                    for ( int i = 0; i < carouselList.size(); i++ ) {
                        if ( CommonUtil.isNotEmpty( carouselList.get( i ).getId() ) ) {
                            purchaseCarouselDAO.updateById( carouselList.get( i ) );
                        } else {
                            carouselList.get( i ).setOrderId( order.getId() );
                            carouselList.get( i ).setBusId( order.getBusId() );
                            purchaseCarouselDAO.insert( carouselList.get( i ) );
                        }
                        if ( carouselIds.equals( "" ) ) {
                            carouselIds += carouselList.get( i ).getId();
                        } else {
                            carouselIds += "," + carouselList.get( i ).getId();
                        }
                    }
                    if ( !carouselIds.equals( "" ) ) {
                        Map< String,Object > parms = new HashMap< String,Object >();
                        parms.put( "carouselIds", carouselIds );
                        parms.put( "orderId", order.getId() );
                        purchaseCarouselDAO.deleteCarouselIds( parms );
                    }

                }
                map.put( "result", "true" );
            } else {
                map.put( "result", "false" );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            map.put( "result", "false" );
        }
        return map;
    }

    /**
     * 分页查询商品
     */
    public PageUtil productList( Map< String,Object > parms ) {
        try {
            int pageSize = 10;
            Integer count = 0;
            int curPage = CommonUtil.isEmpty( parms.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( parms.get( "curPage" ) );
            count = productDAO.countPurchaseProByShopId( parms );
            PageUtil page = new PageUtil( curPage, pageSize, count, "purchaseOrder/getProductByGroup.do" );
            parms.put( "firstNum", ( page.getCurPage() - 1 ) * 10 );
            parms.put( "maxNum", 10 );
            if ( count > 0 ) {
                List< Map< String,Object > > productList = productDAO.selectPurchaseProByShopId( parms );
                page.setSubList( productList );
            }
            return page;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }
}
