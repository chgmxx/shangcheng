package com.gt.mall.service.web.html.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.html.MallHtmlDAO;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.html.MallHtmlService;
import com.gt.mall.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城里面的H5 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallHtmlServiceImpl extends BaseServiceImpl< MallHtmlDAO,MallHtml > implements MallHtmlService {

    private Logger log = Logger.getLogger( MallHtmlServiceImpl.class );

    @Autowired
    private MallHtmlDAO    htmlDAO;
    @Autowired
    private BusUserService busUserService;

    @Override
    public Map< String,Object > htmlList( HttpServletRequest request ) {
	Map< String,Object > map = new HashMap< String,Object >();
	BusUser obj = MallSessionUtils.getLoginUser( request );//获取登录信息
	Integer id = obj.getId();//获取登录人id
	//pid==0 主账户,否则是子账户
	Integer pageNum = 1;
	Object pagenum = request.getParameter( "pageNum" );
	if ( pagenum != null ) {
	    pageNum = Integer.valueOf( pagenum.toString() );
	}
	Integer pagesize = 9;
	Integer firstnum = ( pageNum - 1 ) * pagesize;
	List< Map< String,Object > > list = htmlDAO.getHtmlByUserId( id, obj.getPid(), firstnum, pagesize );
	for ( Map< String,Object > map3 : list ) {
	    BusUser busUser = busUserService.selectById( CommonUtil.toInteger( map3.get( "bus_user_id" ) ) );
	    if ( busUser != null ) {
		map3.put( "name", busUser.getName() );
	    }
	}
	int total = htmlDAO.countHtmlByUserId( id, obj.getPid() );
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
    public PageUtil newHtmlList( HttpServletRequest request, Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = htmlDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断团购是否有数据
	    List< Map< String,Object > > list = htmlDAO.selectByPage( params );
	    for ( Map< String,Object > map3 : list ) {
		BusUser busUser = busUserService.selectById( CommonUtil.toInteger( map3.get( "bus_user_id" ) ) );
		if ( busUser != null ) {
		    map3.put( "name", busUser.getName() );
		}
	    }
	    page.setSubList( list );
	}

	return page;
    }

    @Override
    public void addorUpdateSave( MallHtml obj, BusUser user ) {
	obj.setPid( user.getPid() );
	obj.setBusUserId( user.getId() );
	obj.setCreattime( DateTimeKit.getDateTime() );
	if ( CommonUtil.isEmpty( obj.getId() ) || obj.getId() == 0 ) {
	    obj.setDataBg( "[{}]" );
	    obj.setDataJson( "[[]]" );
	    obj.setDataTransverse(
			    " [{'data':[[],[],[]],'bg':[{ 'background':'#fff',},{ 'background':'#fff',},{ 'background':'#fff',}],'attr':{'width': 380,'height': 500,'top': 62,'left': 0,},'dataTransverse_on':false},]" );

	    htmlDAO.insert( obj );
	    MallHtml obj1 = new MallHtml();
	    String url = PropertiesUtil.getHomeUrl() + "mallhtml/" + obj.getId() + "/79B4DE7C/phoneHtml.do";
	    String code = PropertiesUtil.getResourceUrl() + "/2/" + user.getName() + "/" + Constants.IMAGE_FOLDER_TYPE_20 + "/" + System.currentTimeMillis();
	    String codeurl = QRcodeKit.buildQRcode( url, code, 180, 180 );
	    codeurl = codeurl.replaceAll( "\\\\", "/" );
	    obj1.setCodeUrl( codeurl.split( "upload" )[1] );
	    obj1.setId( obj.getId() );
	    htmlDAO.updateById( obj1 );
	} else {
	    htmlDAO.updateById( obj );
	}
    }

    @Override
    public void htmlSave( MallHtml obj, BusUser user ) {
	obj.setPid( user.getPid() );
	obj.setBusUserId( user.getId() );
	obj.setCreattime( DateTimeKit.getDateTime() );
	htmlDAO.updateById( obj );
    }

    @Override
    public void updateimage( Integer id, String bakurl ) {
	MallHtml html = new MallHtml();
	html.setId( id );
	html.setBakurl( bakurl );
	htmlDAO.updateById( html );
    }

    //    @Override
    //    public int wxidSelect(Integer userid) {
    //        Integer id = 0;
    //
    //        String sql = "SELECT id FROM t_wx_public_users WHERE bus_user_id="+userid+" AND is_power=0";
    //        List<Map<String,Object>> list = daoUtil.queryForList(sql);
    //        if(list.size()>0){
    //            id = Integer.valueOf(list.get(0).get("id").toString());
    //        }
    //        return 0;
    //    }

    @Override
    public int htmltotal( Integer userid ) {
	return htmlDAO.countHtmlByUserId( userid, 0 );
    }

    @Override
    public Map< String,Object > modelList( HttpServletRequest request ) {
	Map< String,Object > map = new HashMap< String,Object >();
	Integer pageNum = 1;
	Object pagenum = request.getParameter( "pageNum" );
	if ( pagenum != null ) {
	    pageNum = Integer.valueOf( pagenum.toString() );
	}
	Integer pagesize = 8;
	Integer firstnum = ( pageNum - 1 ) * pagesize;
	List< Map< String,Object > > list = htmlDAO.getHtmlModelList( firstnum, pagesize );
	int total = htmlDAO.countHtmlModelList();
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
    public PageUtil newModelList( HttpServletRequest request, Map< String,Object > params ) {
	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );

	Wrapper< MallHtml > pageWrapper = new EntityWrapper<>();
	pageWrapper.where( "source_type = 1  and state=0" );
	int count = htmlDAO.selectCount( pageWrapper );

	PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );

	if ( count > 0 ) {
	    List< Map< String,Object > > list = htmlDAO.getHtmlModelList( firstNum, pageSize );
	    page.setSubList( list );
	}

	return page;
    }

    @Override
    public Integer SetmallHtml( Integer htmlid, BusUser user ) {
	MallHtml obj = htmlDAO.selectById( htmlid );//查询原数据
	obj.setId( null );
	obj.setSourceType( 2 );
	obj.setBusUserId( user.getId() );
	obj.setPid( user.getPid() );
	obj.setModelid( htmlid );
	obj.setCreattime( DateTimeKit.getDateTime() );
	htmlDAO.insert( obj );//新增数据
	MallHtml obj1 = new MallHtml();
	String url = PropertiesUtil.getHomeUrl() + "mallhtml/" + obj.getId() + "/79B4DE7C/phoneHtml.do";
	String code = PropertiesUtil.getResourceUrl() + "/2/" + user.getName() + "/" + Constants.IMAGE_FOLDER_TYPE_20 + "/" + System.currentTimeMillis();
	String codeurl = QRcodeKit.buildQRcode( url, code, 180, 180 );
	codeurl = codeurl.replaceAll( "\\\\", "/" );
	obj1.setCodeUrl( codeurl.split( "upload" )[1] );
	obj1.setId( obj.getId() );
	htmlDAO.updateById( obj1 );//修改二维码
	return obj.getId();
    }
}
