package com.gt.mall.config.interceptor;

import com.gt.mall.annotation.AfterAnno;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 拦截器，记录移动端的访问记录数据、菜单
 * User : yangqian
 * Date : 2017/8/2 0002
 * Time : 20:21
 */
@Aspect
@Component
public class ControllerInterceptor {

    //用来记录移动端的访问记录数据
    @After( "within(com.gt.mall.controller..*) && @annotation(anno)" )
    public void conntrollerAfter( JoinPoint joinPoint, AfterAnno anno )
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
