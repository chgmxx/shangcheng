package com.gt.mall.config;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.annotation.CommAnno;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.util.SessionUtils;
import net.sf.json.JSONArray;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拦截器，记录移动端的访问记录数据、菜单
 * User : yangqian
 * Date : 2017/8/2 0002
 * Time : 20:21
 */
@Aspect
@Component
public class ControllerInterceptor {
    /**
     * 找到每个controller中拥有anno注解的方法,在执行目标函数之前执行
     * @param joinPoint
     * @param anno
     * @throws Throwable
     */
    @Before("within(com.gt.mall.controller..*) && @annotation(anno)")
    public void conntrollerAop(JoinPoint joinPoint, CommAnno anno)
		    throws Throwable {
	RequestAttributes ra = RequestContextHolder.getRequestAttributes();
	ServletRequestAttributes sra = (ServletRequestAttributes) ra;
	HttpServletRequest request =  sra.getRequest();
	HttpServletResponse response = null;
	Object[] paramObjs = joinPoint.getArgs();
	for (Object object : paramObjs) {
	    if(object instanceof HttpServletResponse)
	    {
		response = (HttpServletResponse) object;
	    }
	}
	try {

	    if(CommonUtil.isEmpty(response)){
		throw new Exception("方法中缺少参数：HttpServletResponse！");
	    }
	    if(CommonUtil.isEmpty(request)){
		throw new Exception("方法中缺少参数：HttpServletRequest！");
	    }
	    if(CommonUtil.isEmpty(anno.menu_url())){
		throw new Exception("菜单路径为空！");
	    }
	    BusUser user= SessionUtils.getLoginUser(request);//获取登录人信息
	    if(user != null){
		Map<String,Object> url = new HashMap<String,Object>();
		String key = "menus:menus"+user.getId();
		String menus = JedisUtil.get(key);//获取存到redis里面的菜单列表
		if(CommonUtil.isEmpty(menus)){
		    //todo 调用陈丹接口  menusSerice.EnsMenus
//		    menusSerice.EnsMenus(user);
		    key = "menus:menus"+user.getId();
		    menus = JedisUtil.get(key);//获取存到redis里面的菜单列表
		}
		JSONArray  menuslist = JSONArray.fromObject(menus);
		for(int i=0;i<menuslist.size();i++){
		    Map<String,Object> menusmap = (Map<String, Object>) menuslist.get(i);
		    JSONArray  zimenuslist = JSONArray.fromObject(menusmap.get("children"));
		    for(int j=0;j<zimenuslist.size();j++){
			Map<String,Object> zimenusmap = (Map<String, Object>) zimenuslist.get(j);
			Object menusurl = zimenusmap.get("url");
			if(!CommonUtil.isEmpty(menusurl)&&menusurl.equals(anno.menu_url())){
			    url = zimenusmap;
			}
		    }
		}
		WxPublicUsers wxPublicUsers = SessionUtils.getLoginPbUser(request);
		//num为0时，代表没绑定微信号，弹跳到微信绑定页面
		if(CommonUtil.isEmpty(wxPublicUsers)&&user.getPid()==0&&Integer.valueOf(url.get("iswx").toString())==0){
		    response.sendRedirect("/jsp/wx/wxapi.jsp");
		}else{
		    String parentclass_id =url.get("parentclass_id").toString();
		    Map map = queryBusUserIds(user,parentclass_id,menuslist,wxPublicUsers);
		    request.setAttribute("map", map);
		    request.setAttribute("url", url);
		}
	    }else{
		response.sendRedirect("/jsp/merchants/user/login.jsp");
	    }

	} catch (Exception e) {
	    response.sendRedirect("/jsp/error/error.jsp");
	    e.printStackTrace();
	}
    }


    public Map queryBusUserIds(BusUser user, String parentclass_id,JSONArray  menuslist,WxPublicUsers wxPublicUsers) {
        //todo 调用陈丹接口
	/*int usersocket = 1;//有推送机制
	Integer pid = user.getPid();
	Map mapreturn = new HashMap();
	mapreturn.put("appid", 1);// appid存在，则返回1
	if(pid==0){
	    if(CommonUtil.isEmpty(wxPublicUsers)){
		mapreturn.put("appid", 0);// appidbu 存在，则返回0
	    }
	}
	String host = PropertiesUtil.getSocketUrl();
	mapreturn.put("bus_userid",user.getId());
	mapreturn.put("host",host);
	mapreturn.put("usersocket",usersocket);
	List<Map<String,Object>> amenu = new ArrayList<Map<String,Object>>();
	List<Map<String,Object>> twomenu = new ArrayList<Map<String,Object>>();
	for(int i=0;i<menuslist.size();i++){
	    Map<String,Object> menusmap = (Map<String, Object>) menuslist.get(i);
	    String parentclass_id2 = menusmap.get("menus_id").toString();
	    if(parentclass_id.equals(parentclass_id2)){
		JSONArray  zimenuslist = JSONArray.fromObject(menusmap.get("children"));
		twomenu = zimenuslist;
	    }
	    menusmap.remove("children");
	    amenu.add(menusmap);
	}
	for(int i=0;i<amenu.size();i++){
	    Map map = (Map) amenu.get(i);
	    String menus_ids = map.get("menus_id").toString();
	    if (menus_ids == parentclass_id || menus_ids.equals(parentclass_id)) {
		mapreturn.put("num", i);
	    }
	    if(menus_ids.equals("22")||menus_ids=="22"){
		mapreturn.put("iscanyinmenus", "0");
	    }
	}
	mapreturn.put("amenu", amenu);
	mapreturn.put("twomenu", twomenu);
	return mapreturn;*/
	return null;
    }

    public int queryMap(int id) {
	int a = 0;
	/*Map appidmap = busUserMapper.countMap(id);
	Object appid = appidmap == null ? null : appidmap.get("appid");
	if (appid != null && !appid.equals("")) {
	    a = 1;// appid存在，则返回1
	} else {
	    a = 0;// appid不存在，则返回0；
	}*/
	return a;
    }
    /**
     * 是组织菜单返回大于1，不是的话为0,(或者，该用户是否拥有该组织菜单)
     * @param menusid
     * @return
     */
    public int rolemenu(int menusid,List list){
	int num = 0;
		/*	List list = busUserMapper.rolemenu();*/
	for(int i=0;i<list.size();i++){
	    Map map = (Map) list.get(i);
	    String menu_idstring = map.get("menu_id").toString();
	    Integer menu_id = Integer.valueOf(menu_idstring);
	    if(menu_id==menusid){
		num+=1;
	    }
	}
	return num;
    }
    //用来记录移动端的访问记录数据
    @After("within(com.gt.mall.controller..*) && @annotation(anno)")
    public void conntrollerAfter(JoinPoint  joinPoint, AfterAnno anno)
		    throws Throwable {
	/*RequestAttributes ra = RequestContextHolder.getRequestAttributes();
	ServletRequestAttributes sra = (ServletRequestAttributes) ra;
	HttpServletRequest request =  sra.getRequest();
	HttpServletResponse response = null;
	Object[] paramObjs = joinPoint.getArgs();
	for (Object object : paramObjs) {
	    if(object instanceof HttpServletResponse)
	    {
		response = (HttpServletResponse) object;
	    }
	}
	try {

	    if( CommonUtil.isEmpty(response)){
		throw new Exception("方法中缺少参数：HttpServletResponse！");
	    }
	    if(CommonUtil.isEmpty(request)){
		throw new Exception("方法中缺少参数：HttpServletRequest！");
	    }
	    Object userid = request.getAttribute("userid");
	    if(CommonUtil.isEmpty(anno.style())||CommonUtil.isEmpty(request.getAttribute("userid"))){
		throw new Exception("属性未传值或者页面没赋值！");
	    }
	    //todo 调用陈丹接口   countUserModel.insertSelective
	    *//*CountUserModel obj = new CountUserModel();
	    obj.setUserId(Integer.valueOf(userid.toString()));
	    obj.setStyle(anno.style());
	    obj.setVisittime( DateTimeKit.getNow());
	    obj.setVisitip( IPKit.getIp(request));//获取手机端的移动IP
	    obj.setRemark(anno.remark());
	    countUserModel.insertSelective(obj);*//*

	} catch (Exception e) {
	    response.sendRedirect("/error/pvcount.jsp");
	    e.printStackTrace();
	}*/
    }
}
