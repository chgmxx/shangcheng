package com.gt.mall.web.service.html.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dao.html.MallHtmlFromDAO;
import com.gt.mall.entity.html.MallHtmlFrom;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.web.service.html.MallHtmlFromService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * h5商城里面的表单信息 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallHtmlFromServiceImpl extends BaseServiceImpl< MallHtmlFromDAO,MallHtmlFrom > implements MallHtmlFromService {

    private Logger log = Logger.getLogger( MallHtmlFromServiceImpl.class );

    @Autowired
    private MallHtmlFromDAO htmlFromDAO;

    @Override
    public Map< String,Object > htmlListfrom( HttpServletRequest request ) {
	Map< String,Object > map = new HashMap< String,Object >();
	BusUser obj = SessionUtils.getLoginUser( request );//获取登录信息
	Integer id = Integer.valueOf( request.getParameter( "id" ).toString() );//获取H5id
	map.put( "id", id );
	//pid==0 主账户,否则是子账户

	Integer pageNum = 1;
	Object pagenum = request.getParameter( "pageNum" );
	if ( pagenum != null ) {
	    pageNum = Integer.valueOf( pagenum.toString() );
	}
	Integer pagesize = 10;
	Integer firstnum = ( pageNum - 1 ) * pagesize;
	List< Map< String,Object > > list = htmlFromDAO.getHtmlFromByHtmlId( id, firstnum, pagesize );
	int total = htmlFromDAO.countHtmlFromByHtmlId( id );

	map.put( "list", list );
	int pagetotal = total / pagesize;
	int toy = total % pagesize;
	if ( toy != 0 ) {
	    pagetotal += 1;
	}
	map.put( "total", total );
	map.put( "pageNum", pageNum );
	map.put( "pagetotal", pagetotal );
	return map;
    }

    @Override
    public Map< String,Object > htmlfromview( HttpServletRequest request ) {
	Integer id = Integer.valueOf( request.getParameter( "id" ).toString() );//获取H5id
	return htmlFromDAO.htmlFromView( id );
    }
}
