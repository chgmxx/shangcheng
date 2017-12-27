package com.gt.mall;

import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxAppletService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.store.MallStoreService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试
 * User : yangqian
 * Date : 2017/8/8 0008
 * Time : 10:23
 */
public class BaseControllerTest extends BasicTest {

    @Autowired
    private MallStoreService mallStoreService;
    @Autowired
    private MemberService    memberService;
    @Autowired
    private DictService      dictService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Autowired
    private WxAppletService wxAppletService;

    @Autowired
    private BusUserService busUserService;

    @Autowired
    private WxShopService wxShopService;

    @Autowired
    private MallOrderService mallOrderService;

    @Test
    public void tests() {
	try {
	    mallOrderService.walletReturnOrder( "SC1514268652699" );
	} catch ( Exception e ) {
	    this.logger.error( "BaseControllerTest.tests方法异常：" + e.getMessage() );
	    e.printStackTrace();
	}

    }
}


