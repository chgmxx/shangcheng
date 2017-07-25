package com.gt.mall.web.service.html;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.entity.html.MallHtmlFrom;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 商城里面的H5 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlService extends BaseService<MallHtml> {

    /**
     * 根据id查询相对应的数据
     * @param id
     * @return
     */
//    public MallHtml selectByPrimaryKey(Integer id);

    /**
     * h5商城列表页
     *
     * @param request
     * @return
     */
    public Map<String, Object> htmlList(HttpServletRequest request);

    /**
     * 根据h5商城id获取相对应的部分数据
     * @param id
     * @return
     */
//    public Map<String,Object> queryMap(Integer id);

    /**
     * 保存H5商城信息
     *
     * @param obj
     * @param user
     */
    public void addorUpdateSave(MallHtml obj, BusUser user);

    /**
     * 删除数据
     * @param id
     */
//    public void delect(Integer id);

    /**
     * 修改H5商城信息
     *
     * @param obj
     * @param user
     */
    public void htmlSave(MallHtml obj, BusUser user);


    /**
     * 商城表单数据添加
     * @param obj
     */
//    public void savehtmlfrom(MallHtmlFrom obj);

    /**
     * 修改背景图
     *
     * @param id
     * @param bakurl
     */
    public void updateimage(Integer id, String bakurl);

    /**
     * 根据用户id 查询公众号id
     * @param userid
     * @return
     */
//    public int wxidSelect(Integer userid);

    /**
     * 根据主账户id 查询已经创建的h5商城的个数
     *
     * @param userid
     * @return
     */
    public int htmltotal(Integer userid);

    /**
     * h5商城模板列表页
     *
     * @param request
     * @return
     */
    public Map<String, Object> modelList(HttpServletRequest request);


    /**
     * 复制模板信息，添加新数据，并且返回新增数据的id（商家添加h5列表页）
     *
     * @param htmlid
     * @param user
     * @return
     */
    public Integer SetmallHtml(Integer htmlid, BusUser user);
}
