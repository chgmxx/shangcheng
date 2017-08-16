package com.gt.mall;

import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.inter.service.DictService;
import com.gt.mall.inter.service.MemberService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/8/8 0008
 * Time : 10:23
 */
public class BaseControllerTest extends BasicTest {

    @Autowired
    private MallStoreDAO  mallStoreDAO;
    @Autowired
    private MemberService memberService;
    @Autowired
    private DictService dictService;

    @Test
    public void tests() {
	/*Map< String,Object > params = new HashMap<>();
	params.put( "userId",42 );
	int rowCount = mallStoreDAO.countByPage( params );

	params.put( "firstResult", 1 );
	params.put( "maxResult", 10 );
	List< Map< String,Object > > list = mallStoreDAO.findByPage( params );*/

	/*List< Integer > list = memberService.findMemberListByIds( 124 );
	System.out.println( "list = " + list );*/

	List<Map > typeMap = dictService.getDict( "1073" );
	System.out.println("typeMap = " + typeMap);

    }
}
