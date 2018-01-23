package com.gt.mall;

import com.gt.mall.service.quartz.MallQuartzNewService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/8/24 0024
 * Time : 10:08
 */
public class UserControllerTest extends BasicTest {
    @Autowired
    private MallQuartzNewService mallQuartzNewService;

    @Test
    public void user() {
	mallQuartzNewService.orderCallback();
    }

    @Test
    public void dict() {

    }
}
