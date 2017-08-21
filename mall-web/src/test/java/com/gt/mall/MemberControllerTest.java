package com.gt.mall;

import com.gt.mall.service.inter.member.MemberService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试
 * User : yangqian
 * Date : 2017/8/8 0008
 * Time : 10:23
 */
public class MemberControllerTest extends BasicTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void tests() {
	memberService.findCardAndShopIdsByMembeId(  1225352,"17,18,19,21");

    }
}


