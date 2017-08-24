package com.gt.mall;

import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
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
    private BusUserService busUserService;

    @Autowired
    private DictService dictService;


    @Test
    public void user(){
        System.out.println("user = " + busUserService.selectById( 42 ));

        System.out.println("isAdmin = " + busUserService.getIsAdmin( 22091 ));

        System.out.println("isJxc = " + busUserService.getIsErpCount( 8,42 ));

        System.out.println("MainBusId = " + busUserService.getMainBusId( 22091 ));

        System.out.println("VoiceUrl = " + busUserService.getVoiceUrl( "77" ));
    }

    @Test
    public void dict(){

    }
}
