package com.gt.mall;

import com.gt.mall.service.quartz.MallQuartzNewService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2018/1/25 0025
 * Time : 15:57
 */
public class TestController extends BasicTest {

    @Autowired
    private MallQuartzNewService mallQuartzNewService;

    @Test
    public void tests() {
	mallQuartzNewService.memberRefund();
    }
}
