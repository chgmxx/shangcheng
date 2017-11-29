//package com.gt.mall;
//
//import com.gt.entityBo.PaySuccessBo;
//import com.gt.mall.service.inter.member.MemberPayService;
//import com.gt.mall.service.inter.member.MemberService;
//import com.gt.mall.utils.CommonUtil;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * 测试
// * User : yangqian
// * Date : 2017/8/8 0008
// * Time : 10:23
// */
//public class MemberBeanControllerTest extends BasicTest {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private MemberPayService memberPayService;
//
//    @Test
//    public void tests() {
//	//	memberService.findCardAndShopIdsByMembeId(  1225352,"17,18,19,21");
//
//
//	PaySuccessBo successBo = new PaySuccessBo();
//	successBo.setMemberId( 1225352 );//会员id
//	successBo.setOrderCode( "SC1504783166352" );//订单号
//	successBo.setTotalMoney( CommonUtil.toDouble( 10000 ) );//订单原价
//	successBo.setDiscountMoney( CommonUtil.toDouble( 7750 ) );//折扣后的价格（不包含运费）
//	successBo.setPay( 7782.0 );
//	successBo.setPayType( 10 );////支付方式 查询看字典1198
//	successBo.setUcType( 104 );//消费类型 字典1197
//	successBo.setUseCoupon( true );//是否使用优惠券
//	successBo.setCouponType( 11 );//优惠券类型 0微信 1多粉优惠券
//	successBo.setCodes( "15017445820915712" );//使用优惠券code值 用来核销卡券
//	successBo.setUserFenbi( true );
//	successBo.setFenbiNum( 333.2 );
//	successBo.setUserJifen( true );
//	successBo.setJifenNum( 460 );
//	successBo.setDelay( 0 ); //会员赠送物品 0延迟送 1立即送  -1 不赠送物品
//	successBo.setDataSource( 0 );
//
//	memberPayService.paySuccess( successBo );
//
//    }
//}
//
//
